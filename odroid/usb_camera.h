/**********************************************************************
 * Copyright 2013 by team696.com.
 */
#ifndef USB_CAMERA_H
#define USB_CAMERA_H

#include <errno.h>
#include <fcntl.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <unistd.h>
#include <exception>
#include <assert.h>
#include <linux/videodev2.h>

#include "any_frame_queue.h"

class Usb_Cam_Err: public std::exception {
protected:
    static const int ERRMSG_MAX = 100;
    char errmsg[ERRMSG_MAX];

public:
    Usb_Cam_Err()
    { }

    Usb_Cam_Err(const char* errmsg_arg)
    {
        strncpy(errmsg, errmsg_arg, ERRMSG_MAX);
    }

    virtual const char* what() const throw()
    {
        return errmsg;
    }
};


class Usb_Cam_Err_Unexpected_Empty_Free_List: public Usb_Cam_Err {
public:
    Usb_Cam_Err_Unexpected_Empty_Free_List()
    : Usb_Cam_Err("Usb_Cam_Err_Unexpected_Empty_Free_List")
    { }
};


class Usb_Cam_Err_Cant_Open_Device: public Usb_Cam_Err {
public:
    Usb_Cam_Err_Cant_Open_Device(const char* device_name, int err_num)
    {
        if (err_num < sys_nerr) {
            snprintf(errmsg, ERRMSG_MAX,
                     "Usb_Cam_Err_Cant_Open_Device: device_name= %s; %s",
                     device_name, sys_errlist[err_num]);
        } else {
            snprintf(errmsg, ERRMSG_MAX,
                     "Usb_Cam_Err_Cant_Open_Device: device_name= %s errno= %d",
                     device_name, err_num);
        }
        errmsg[ERRMSG_MAX-1] = '0';
    }
};


/*
class Usb_Cam_Err_Bad_Format_Id: public Usb_Cam_Err {
public:
    Usb_Cam_Err_Bad_Format_Id(const char* device_name, int format_id)
    {
        snprintf(errmsg, ERRMSG_MAX,
                 "Usb_Cam_Err_Bad_Format_Id: device_name= %s format_id= %d",
                 device_name, format_id);
        errmsg[ERRMSG_MAX-1] = '0';
    }
};
*/


class Usb_Cam_Err_Ioctl: public Usb_Cam_Err {
public:
    static void request_name(int request, size_t name_bytes, char name[]);

    Usb_Cam_Err_Ioctl(const char* dev_name, int request, int err_num)
    {
        char req_name[ERRMSG_MAX];
        request_name(request, ERRMSG_MAX, req_name);
        if (err_num < sys_nerr) {
            snprintf(errmsg, ERRMSG_MAX,
                     "Usb_Cam_Err_Ioctl %s: %s; %s",
                     dev_name, req_name, sys_errlist[err_num]);
        } else {
            snprintf(errmsg, ERRMSG_MAX,
                     "Usb_Cam_Err_Ioctl %s: %s errno= %d",
                     dev_name, req_name, err_num);
        }
        errmsg[ERRMSG_MAX-1] = '0';
    }
};


class Usb_Camera;

class Usb_Frame {
    friend class Usb_Camera;
private:
    struct v4l2_buffer* vbuf_ptr;
    uint8_t* img_data;
    int rows;
    int cols;
public:
    Usb_Frame()
    : vbuf_ptr(NULL),
      img_data(NULL),
      rows(0),
      cols(0)
    { }

    struct timeval get_timestamp() const
    {
        return vbuf_ptr->timestamp;
    }

    int get_frame_num() const
    {
        return vbuf_ptr->sequence;
    }

    int get_rows() const
    {
        return rows;
    }

    int get_cols() const
    {
        return cols;
    }

    unsigned char* get_img_data() const
    {
        return img_data;
    }
};
    
    
class Usb_Camera : public Any_Frame_Queue {
    static const int MAX_BUFS = 5;
    static const int MAX_FMTS = 5;
    int fd;                            /// handle for the USB camera device
private:
    int buf_count;                     /// size of frame and vbuf arrays
    int buf_bytes;                     /// size of each image buffer
    int rows;
    int cols;
    Usb_Frame frame[MAX_BUFS];         /// space for the images
    struct v4l2_buffer vbuf[MAX_BUFS]; /// space for the video buffers
    Usb_Frame* free_head_ptr;          /// points to next available Usb_Frame
    char dev_name[FILENAME_MAX];
    int fmt_count;
    int fmt_current;
    struct v4l2_fmtdesc fmt_desc[MAX_FMTS];
    Any_Frame_Queue* frame_queue_ptr;

    /*******************************************************************//*
     * @brief Make a call to system ioctl(2) with error checking.
     *
     * This call defines the entire interface to the V4L2 device.  See
     * http://linuxtv.org/downloads/v4l-dvb-apis/ for documentation.
     *
     * @param [in] request     A V4L2 ioctl command.  These are defined in
     *                         linux/videodev2.h, and have names of the
     *                         form VIDIOC_XXXXX.
     * @param [in,out] arg_ptr A pointer to the argument to this command.  The
     *                         size of the argument and whether it is an input
     *                         or output is determined by request.
     */
    void yioctl(int request, void *arg_ptr) const
    {
        int r;
        do {
            r = ioctl(fd, request, arg_ptr);
        } while (r < 0 && errno == EINTR);
        if (r < 0) throw Usb_Cam_Err_Ioctl(dev_name, request, errno);
    }


    /*******************************************************************//*
     * @brief Initialize memory map.
     *
     * Allocate space for the specified number of video buffers.
     *
     * @param [in] buf_count  The number of buffers to use in processing.
     *                        If this number exceeds MAX_BUFS, or exceeds the
     *                        capacity of the device, a smaller number will
     *                        be used.  For the actual number in use call
     *                        get_buf_count().
     *                        
     */
    void init_mmap(int buf_count);


    /*******************************************************************//*
     * @brief Return a v4l2_buffer initialized to all zeroes.
     */
    static struct v4l2_buffer zero_v4l2_buffer()
    {
        struct v4l2_buffer zeros = { 0 };
        return zeros;
    }

public:

    /*******************************************************************//*
     * @brief Constructor.
     */
    Usb_Camera();


    /*******************************************************************//*
     * @brief Destructor.
     */
    ~Usb_Camera();


    /*******************************************************************//*
     * @brief Initialize the camera.
     *
     * Open the camera device.
     * Allocate space for the specified number of video buffers.
     *
     * @param [in] device_name The name of the device to open on the filesystem,
     *                         for example, "/dev/video10".
     * @param [in] format_id   Specifies the desired image format.
     * @param [in] rows        Specifies the number of rows in the image.
     * @param [in] cols        Specifies the number of columns in the image.
     * @param [in] buf_count   The number of buffers to use in processing.
     *                         If this number exceeds MAX_BUFS, or exceeds the
     *                         capacity of the device, a smaller number will
     *                         be used.  For the actual number in use call
     *                         get_buf_count().
     */
    void init(const char* device_name,
              int format_id = 0,
              int rows = 480,
              int cols = 640,
              int buf_count = 1);

    
    /*******************************************************************//*
     * @brief Undoes init(); disconnects from any camera device.
     */
    void deinit();

    const char* get_device_name()
    {
        return dev_name;
    }

    int get_current_format_id() const
    {
        return fmt_current;
    }

    /*******************************************************************//*
     * @brief Get a description of the specified image format.
     *
     * @param [in] format_id  Identifies the image format of interest.
     *                        Legal values are in the range 0..n-1, where n is
     *                        the number of supported formats.  If format_id is
     *                        not in this range, the current image format is
     *                        used (and return value is set to -1).
     * @param [out] desc_ptr  Returns a pointer to const memory containing a
     *                        description of the specified format.
     * @return If format_id specifies a supported format, format_id is return;
     *         else -1 is returned, and the returned *desc_ptr describes the
     *         current image format.
     */
    int get_format(int format_id,
                   const struct v4l2_fmtdesc*& desc_ptr) const;

    static const char* format_str(const struct v4l2_fmtdesc& fmt_desc,
                                  size_t bytes,
                                  char str[]);

    void set_format_and_frame_size(int format_id, int arg_rows, int arg_cols);

    int get_supported_frame_sizes(int format_id,
                                  size_t size,
                                  struct v4l2_frmsizeenum frm_size[]) const;

    /*******************************************************************//*
     * @brief Return the number of video buffers in use by the driver.
     */
    int get_buf_count() const { return buf_count; };


    /*******************************************************************//*
     * @brief Start the video stream.
     *
     * This must be called before the first call to frame_capture().
     */
    void stream_start()
    {
        uint32_t type = vbuf[0].type;
        yioctl(VIDIOC_STREAMON, &type);
    }


    /*******************************************************************//*
     * @brief Stop the video stream.
     */
    void stream_stop()
    {
        uint32_t type = vbuf[0].type;
        yioctl(VIDIOC_STREAMOFF, &type);
    }

    /*******************************************************************//*
     * @brief Capture the next frame and return it.
     *
     * @return The frame from whence an image may be derived.  A return value
     *         of NULL indicates that the request timed out before a frame
     *         became available.  Other errors are possible, but these result
     *         in exceptions being thrown.
     */
    virtual Usb_Frame* pop(int& count);


    /*******************************************************************//*
     * @brief Release the given frame so it may be refilled by a future
     *        call to frame_capture().
     *
     * If no released frames are available when frame_capture() is called
     * an exception will be raised.  It is the caller's responsibility to
     * make sure frames are released promptly to avoid this eventuality.
     *
     * @param [in] frame_ptr  Points to the frame to release.
     */
    virtual int push(Usb_Frame* frame_ptr);

    int get_rows() const
    {
        return rows;
    }

    int get_cols() const
    {
        return cols;
    }
};
#endif
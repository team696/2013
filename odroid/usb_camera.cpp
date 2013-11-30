/**********************************************************************
 * Copyright 2013 by team696.com.
 */

//code originated at http://jayrambhia.com/blog/capture-v4l2/


#include <errno.h>
#include <fcntl.h>
#include <linux/videodev2.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <unistd.h>
#include <exception>
#include "usb_camera.h"
#include "frame_queue.h"

void Usb_Cam_Err_Ioctl::request_name(int request,
                                     size_t name_bytes,
                                     char name[]) 
{ 
    switch (request) { 
    case VIDIOC_QUERYCAP:
        strncpy(name, "VIDIOC_QUERYCAP", name_bytes);
        break; 
    case VIDIOC_RESERVED:
        strncpy(name, "VIDIOC_RESERVED", name_bytes);
        break; 
    case VIDIOC_ENUM_FMT:
        strncpy(name, "VIDIOC_ENUM_FMT", name_bytes);
        break; 
    case VIDIOC_G_FMT:
        strncpy(name, "VIDIOC_G_FMT", name_bytes);
        break; 
    case VIDIOC_S_FMT:
        strncpy(name, "VIDIOC_S_FMT", name_bytes);
        break; 
    case VIDIOC_REQBUFS:
        strncpy(name, "VIDIOC_REQBUFS", name_bytes);
        break; 
    case VIDIOC_QUERYBUF:
        strncpy(name, "VIDIOC_QUERYBUF", name_bytes);
        break; 
    case VIDIOC_G_FBUF:
        strncpy(name, "VIDIOC_G_FBUF", name_bytes);
        break; 
    case VIDIOC_OVERLAY:
        strncpy(name, "VIDIOC_OVERLAY", name_bytes);
        break; 
    case VIDIOC_QBUF:
        strncpy(name, "VIDIOC_QBUF", name_bytes);
        break; 
    case VIDIOC_DQBUF:
        strncpy(name, "VIDIOC_DQBUF", name_bytes);
        break; 
    case VIDIOC_STREAMON:
        strncpy(name, "VIDIOC_STREAMON", name_bytes);
        break; 
    case VIDIOC_STREAMOFF:
        strncpy(name, "VIDIOC_STREAMOFF", name_bytes);
        break; 
    case VIDIOC_G_PARM:
        strncpy(name, "VIDIOC_G_PARM", name_bytes);
        break; 
    case VIDIOC_S_PARM:
        strncpy(name, "VIDIOC_S_PARM", name_bytes);
        break; 
    case VIDIOC_G_STD:
        strncpy(name, "VIDIOC_G_STD", name_bytes);
        break; 
    case VIDIOC_S_STD:
        strncpy(name, "VIDIOC_S_STD", name_bytes);
        break; 
    case VIDIOC_ENUMSTD:
        strncpy(name, "VIDIOC_ENUMSTD", name_bytes);
        break; 
    case VIDIOC_ENUMINPUT:
        strncpy(name, "VIDIOC_ENUMINPUT", name_bytes);
        break; 
    case VIDIOC_G_CTRL:
        strncpy(name, "VIDIOC_G_CTRL", name_bytes);
        break; 
    case VIDIOC_S_CTRL:
        strncpy(name, "VIDIOC_S_CTRL", name_bytes);
        break; 
    case VIDIOC_G_TUNER:
        strncpy(name, "VIDIOC_G_TUNER", name_bytes);
        break; 
    case VIDIOC_S_TUNER:
        strncpy(name, "VIDIOC_S_TUNER", name_bytes);
        break; 
    case VIDIOC_G_AUDIO:
        strncpy(name, "VIDIOC_G_AUDIO", name_bytes);
        break; 
    case VIDIOC_S_AUDIO:
        strncpy(name, "VIDIOC_S_AUDIO", name_bytes);
        break; 
    case VIDIOC_QUERYCTRL:
        strncpy(name, "VIDIOC_QUERYCTRL", name_bytes);
        break; 
    case VIDIOC_QUERYMENU:
        strncpy(name, "VIDIOC_QUERYMENU", name_bytes);
        break; 
    case VIDIOC_G_INPUT:
        strncpy(name, "VIDIOC_G_INPUT", name_bytes);
        break; 
    case VIDIOC_S_INPUT:
        strncpy(name, "VIDIOC_S_INPUT", name_bytes);
        break; 
    case VIDIOC_G_OUTPUT:
        strncpy(name, "VIDIOC_G_OUTPUT", name_bytes);
        break; 
    case VIDIOC_S_OUTPUT:
        strncpy(name, "VIDIOC_S_OUTPUT", name_bytes);
        break; 
    case VIDIOC_ENUMOUTPUT:
        strncpy(name, "VIDIOC_ENUMOUTPUT", name_bytes);
        break; 
    case VIDIOC_G_AUDOUT:
        strncpy(name, "VIDIOC_G_AUDOUT", name_bytes);
        break; 
    case VIDIOC_S_AUDOUT:
        strncpy(name, "VIDIOC_S_AUDOUT", name_bytes);
        break; 
    case VIDIOC_G_MODULATOR:
        strncpy(name, "VIDIOC_G_MODULATOR", name_bytes);
        break; 
    case VIDIOC_S_MODULATOR:
        strncpy(name, "VIDIOC_S_MODULATOR", name_bytes);
        break; 
    case VIDIOC_G_FREQUENCY:
        strncpy(name, "VIDIOC_G_FREQUENCY", name_bytes);
        break; 
    case VIDIOC_S_FREQUENCY:
        strncpy(name, "VIDIOC_S_FREQUENCY", name_bytes);
        break; 
    case VIDIOC_CROPCAP:
        strncpy(name, "VIDIOC_CROPCAP", name_bytes);
        break; 
    case VIDIOC_G_CROP:
        strncpy(name, "VIDIOC_G_CROP", name_bytes);
        break; 
    case VIDIOC_S_CROP:
        strncpy(name, "VIDIOC_S_CROP", name_bytes);
        break; 
    case VIDIOC_G_JPEGCOMP:
        strncpy(name, "VIDIOC_G_JPEGCOMP", name_bytes);
        break; 
    case VIDIOC_S_JPEGCOMP:
        strncpy(name, "VIDIOC_S_JPEGCOMP", name_bytes);
        break; 
    case VIDIOC_QUERYSTD:
        strncpy(name, "VIDIOC_QUERYSTD", name_bytes);
        break; 
    case VIDIOC_TRY_FMT:
        strncpy(name, "VIDIOC_TRY_FMT", name_bytes);
        break; 
    case VIDIOC_ENUMAUDIO:
        strncpy(name, "VIDIOC_ENUMAUDIO", name_bytes);
        break; 
    case VIDIOC_ENUMAUDOUT:
        strncpy(name, "VIDIOC_ENUMAUDOUT", name_bytes);
        break; 
    case VIDIOC_G_PRIORITY:
        strncpy(name, "VIDIOC_G_PRIORITY", name_bytes);
        break; 
    case VIDIOC_S_PRIORITY:
        strncpy(name, "VIDIOC_S_PRIORITY", name_bytes);
        break; 
    case VIDIOC_G_SLICED_VBI_CAP:
        strncpy(name, "VIDIOC_G_SLICED_VBI_CAP", name_bytes);
        break; 
    case VIDIOC_LOG_STATUS:
        strncpy(name, "VIDIOC_LOG_STATUS", name_bytes);
        break; 
    case VIDIOC_G_EXT_CTRLS:
        strncpy(name, "VIDIOC_G_EXT_CTRLS", name_bytes);
        break; 
    case VIDIOC_S_EXT_CTRLS:
        strncpy(name, "VIDIOC_S_EXT_CTRLS", name_bytes);
        break; 
    case VIDIOC_TRY_EXT_CTRLS:
        strncpy(name, "VIDIOC_TRY_EXT_CTRLS", name_bytes);
        break; 
    case VIDIOC_ENUM_FRAMESIZES:
        strncpy(name, "VIDIOC_ENUM_FRAMESIZES", name_bytes);
        break; 
    case VIDIOC_ENUM_FRAMEINTERVALS:
        strncpy(name, "VIDIOC_ENUM_FRAMEINTERVALS", name_bytes);
        break; 
    case VIDIOC_G_ENC_INDEX:
        strncpy(name, "VIDIOC_G_ENC_INDEX", name_bytes);
        break; 
    case VIDIOC_ENCODER_CMD:
        strncpy(name, "VIDIOC_ENCODER_CMD", name_bytes);
        break; 
    case VIDIOC_TRY_ENCODER_CMD:
        strncpy(name, "VIDIOC_TRY_ENCODER_CMD", name_bytes);
        break; 
    default:
        snprintf(name, name_bytes, "request=, %d", request);
        break;
    }
    name[name_bytes-1] = '\0';
}

void Usb_Camera::init_mmap(int buf_count_arg)
{
    struct v4l2_requestbuffers req = {0};
    if (buf_count_arg > MAX_BUFS) buf_count_arg = MAX_BUFS;
    req.count = buf_count_arg;
    req.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    req.memory = V4L2_MEMORY_MMAP;
 
    yioctl(VIDIOC_REQBUFS, &req);
    this->buf_count = req.count;
    frame_queue_ptr = new Frame_Queue(this->buf_count, false, false);
 
    for (int i = 0; i < this->buf_count; ++i) {
        vbuf[i] = zero_v4l2_buffer();
        vbuf[i].type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
        vbuf[i].memory = V4L2_MEMORY_MMAP;
        vbuf[i].index = i;
        yioctl(VIDIOC_QUERYBUF, &vbuf[i]);
     
        this->buf_bytes = vbuf[i].length;
        frame[i].img_data = (unsigned char*)mmap(NULL, buf_bytes,
                                                 PROT_READ | PROT_WRITE,
                                                 MAP_SHARED, fd,
                                                 vbuf[i].m.offset);
        if (frame[i].img_data == MAP_FAILED) {
            this->buf_count = i;
            break;
        }
        printf("Length: %d\nAddress: %p\n", buf_bytes, frame[i].img_data);
        printf("Image Length: %d\n", vbuf[i].bytesused);
        frame[i].vbuf_ptr = &vbuf[i];
        push(&frame[i]);
    }
}

int Usb_Camera::push(Usb_Frame* frame_ptr)
{
    // Add the vbuf onto the video queue.

    yioctl(VIDIOC_QBUF, frame_ptr->vbuf_ptr);

#if 0
    // Add this frame onto the free list.

    frame_ptr->next_free_ptr = free_head_ptr;
    free_head_ptr = frame_ptr;
    int count = 0;
#else
    int count = frame_queue_ptr->push(frame_ptr);
#endif
    return count;
}

// on timeout, returns NULL
// other errors raise exceptions
Usb_Frame* Usb_Camera::pop(int& count)
{
    count = 0;
    assert(this->fd >= 0);
 
    // Wait for capture to occur.

    fd_set fds;
    FD_ZERO(&fds);
    FD_SET(fd, &fds);
    struct timeval tv = {0};
    tv.tv_sec = 2;
    int r = select(fd+1, &fds, NULL, NULL, &tv);
    if (r < 0) return NULL;  // timeout

#if 0
    // Get a frame off the free list.

    Usb_Frame* frame_ptr = free_head_ptr;
    if (frame_ptr == NULL) throw Usb_Cam_Err_Unexpected_Empty_Free_List();
    free_head_ptr = free_head_ptr->next_free_ptr;
#else
    Usb_Frame* frame_ptr = frame_queue_ptr->pop(count);
    if (frame_ptr == NULL) throw Usb_Cam_Err_Unexpected_Empty_Free_List();
#endif
 
    // Dequeue the vbuf.

    yioctl(VIDIOC_DQBUF, frame_ptr->vbuf_ptr);

    frame_ptr->rows = this->rows;
    frame_ptr->cols = this->cols;
    return frame_ptr;
}
  

void Usb_Camera::set_format_and_frame_size(int format_id,
                                           int arg_rows,
                                           int arg_cols)
{
    const struct v4l2_fmtdesc* fmt_desc_ptr;
    format_id = get_format(format_id, fmt_desc_ptr);

    struct v4l2_format fmt = {(v4l2_buf_type)0};
    fmt.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;

    fmt.fmt.pix.width = arg_cols;
    fmt.fmt.pix.height = arg_rows;
    fmt.fmt.pix.pixelformat = fmt_desc_ptr->pixelformat;
    fmt.fmt.pix.field = V4L2_FIELD_NONE;

    yioctl(VIDIOC_S_FMT, &fmt);

    this->cols = fmt.fmt.pix.width;
    this->rows = fmt.fmt.pix.height;
    if (format_id > 0) this->fmt_current = format_id;

    char fourcc[5] = {0};
    strncpy(fourcc, (char *)&fmt.fmt.pix.pixelformat, 4);
    printf("Selected Camera Mode:\n"
           "  Width: %d\n"
           "  Height: %d\n"
           "  PixFmt: %s\n"
           "  Field: %d\n",
           fmt.fmt.pix.width,
           fmt.fmt.pix.height,
           fourcc,
           fmt.fmt.pix.field);
}


// format_id == -1 for current format
// always return a legal desc_ptr
// return value is foramt_id or -1 if format_id is illegal; in this case
// desc_ptr points to the current format
int Usb_Camera::get_format(int format_id,
                           const struct v4l2_fmtdesc*& desc_ptr) const
{
    int return_format_id = format_id;
    if (format_id < 0 || format_id >= this->fmt_count) {
        format_id = this->fmt_current;
        return_format_id = -1;
    }
    desc_ptr = &fmt_desc[format_id];
    return return_format_id;
}


const char* Usb_Camera::format_str(const struct v4l2_fmtdesc& fmt_desc,
                                   size_t bytes,
                                   char str[])
{
    char fourcc[5] = {0};
    char c, e;
    strncpy(fourcc, (char *)&fmt_desc.pixelformat, 4);
    c = fmt_desc.flags & 1? 'C' : ' ';
    e = fmt_desc.flags & 2? 'E' : ' ';
    snprintf(str, bytes, "%d: %s %c%c %s",
             fmt_desc.index, fourcc, c, e, fmt_desc.description);
    str[bytes - 1] = '\0';
    return str;
}


int Usb_Camera::get_supported_frame_sizes(
                                int format_id,
                                size_t size,
                                struct v4l2_frmsizeenum frm_size[]) const
{
    const struct v4l2_fmtdesc* fmt_desc_ptr;
    format_id = get_format(format_id, fmt_desc_ptr);
    unsigned int i = 0;
    while (1) {
        if ((size_t)i >= size) break;
        frm_size[i].pixel_format = fmt_desc_ptr->pixelformat;
        frm_size[i].index = i;
        try {
            yioctl(VIDIOC_ENUM_FRAMESIZES, &frm_size[i]);
        } catch (...) {
            break;
        }
        ++i;
    }
    return i;
}


Usb_Camera::Usb_Camera()
: fd(-1),
  frame_queue_ptr(NULL)
{ }

Usb_Camera::~Usb_Camera()
{
    deinit();
}

void Usb_Camera::deinit()
{
    if (this->fd < 0) return;
    for (int i = 0; i < buf_count; ++i) {
        munmap(frame[i].img_data, buf_bytes);
    }
    close(this->fd);
    this->fd = -1;
    delete frame_queue_ptr;
}


void Usb_Camera::init(const char* device_name,
                      int format_id,
                      int arg_rows,
                      int arg_cols,
                      int arg_buf_count)
{
    deinit();
    this->fd = open(device_name, O_RDWR);
    if (this->fd == -1) {
        throw Usb_Cam_Err_Cant_Open_Device(device_name, errno);
    }
    strncpy(this->dev_name, device_name, FILENAME_MAX);
    this->fmt_current = 0;

    // Ennumerate supported image formats to this->fmt_desc array.

    unsigned int i = 0;
    while (1) {
        if ((int)i >= MAX_FMTS) break;
        this->fmt_desc[i].type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
        this->fmt_desc[i].index = i;
        try {
            yioctl(VIDIOC_ENUM_FMT, &this->fmt_desc[i]);
        } catch (...) {
            break;
        }
        ++i;
    }
    this->fmt_count = i;

    //set_format_and_frame_size(2, 480, 640);
    set_format_and_frame_size(format_id, arg_rows, arg_cols);
    init_mmap(arg_buf_count);
}

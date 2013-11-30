/**********************************************************************
 * Copyright 2013 by team696.com.
 */
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "usb_camera.h"
#include "cam_thread.h"

pthread_mutex_t disp_mutex = PTHREAD_MUTEX_INITIALIZER;

const int CAM_COUNT = 2;
Usb_Camera cam[CAM_COUNT];

int main()
{
    pthread_t thread_id[CAM_COUNT];

    cam[0].init("/dev/video10", 2, 480, 640, 5);
    //cam[1].init("/dev/video11", 2, 480, 640, 5);
    cam[1].init("/dev/video11", 2, 240, 320, 5);

    for (int i = 0; i < CAM_COUNT; ++i) {
        printf("%s FORMATS\n--------------------\n", cam[i].get_device_name());
        fflush(stdout);
        int format_id_in = 0;
        while (1) {
            const struct v4l2_fmtdesc* fmt_desc_ptr;
            int format_id_out = cam[i].get_format(format_id_in, fmt_desc_ptr);
            if (format_id_out != format_id_in) break;
            const int STR_BYTES = 32;
            char str[STR_BYTES];
            printf("  %s\n", Usb_Camera::format_str(*fmt_desc_ptr, STR_BYTES, str));
            fflush(stdout);
            ++format_id_in;
        }
    }

    for (int i = 0; i < CAM_COUNT; ++i) {
        int rc = pthread_create(&thread_id[i], NULL, cam_thread,
                                (void*)&cam[i]);
        if (rc != 0) {
            printf("can't pthread_create, error_code= %d\n", rc);
            exit(-1);
        }
    }
    pthread_exit(NULL);
}

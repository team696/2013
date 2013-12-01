/**********************************************************************
 * Copyright 2013 by team696.com.
 */
#ifndef ANY_FRAME_QUEUE_H
#define ANY_FRAME_QUEUE_H

class Usb_Frame;

/**********************************************************************
 * @brief Base class for all Frame_Queue types.
 */
class Any_Frame_Queue {
public:
    virtual int push(Usb_Frame* frame_ptr) = 0;
    virtual Usb_Frame* pop(int& count) = 0;
};

#endif

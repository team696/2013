/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */

package com.team696;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
/**
 *
 * @author ThaHypnotoad
 * CircuitBreakerEncoder
 */
public class HighSpeedEncoder extends Encoder{
    Timer timer = new Timer();
    double curTime = 0.0;
    double lastTime = 0.0;
    double speed = 0.0;
    double lastDist = 0.0;
    double curDist = 0.0;
    
    public HighSpeedEncoder(int channelA, int channelB, boolean reverseDirection,EncodingType encodingType){
        super(channelA, channelB, reverseDirection, encodingType);
        timer.reset();
        timer.start();
    }
    
    public double getHighSpeed(){
        return speed;
    }
    public void update(){
        
        lastTime = curTime;
        curTime = timer.get();
        
        lastDist = curDist;
        curDist = super.getDistance();
        System.out.println(curTime-lastTime);
        speed = (curDist-lastDist)/(curTime-lastTime);    //speed is measured in distance per second
    }
    
    public void reset(){
        super.reset();
        timer.reset();
    }
    
}

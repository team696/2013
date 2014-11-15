/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team696;

/**
 *
 * @author ThaHypnotoad
 * 
 * A pure Integration controller with an overshoot clause for quick and accurate spinning up to speed.
 * 
 */
public class TBHController {
    double targetRPM;
    double output;
    double maxRPM;
    double TBH;
    double lastError;
    double error;
    double curRPM;
    double gain;
    
    public TBHController(){
    targetRPM = 0.0;
    output = 0.0;
    maxRPM = 9001;
    TBH = 0.0;
    lastError = 10.0;
    error = 0.0;
    curRPM = 0.0;
    gain = 1E-5;
    }
    
    public TBHController(double _maxRPM, double _gain){
    targetRPM = 0.0;
    output = 0.0;
    maxRPM = _maxRPM;
    TBH = 0.0;
    lastError = 10.0;
    error = 0.0;
    curRPM = 0.0;
    gain = _gain;
}
    
    public void setGain(double _gain){
        gain = _gain;
    }
    public int setRPM(double _targetRPM){
        if(Math.abs(_targetRPM -targetRPM)<10) return -1; //-1 indicates error
        if(_targetRPM<0) return -1;
    
        TBH = (2*_targetRPM/maxRPM)-1;
        lastError = (_targetRPM>targetRPM) ? 1: -1;
        targetRPM = _targetRPM;
        System.out.print("setting RPM to:  " +targetRPM);
        return 0;
    }
    
    public int update(double _RPM){
        curRPM = _RPM;
        lastError = error;
        error = targetRPM - curRPM;
        output += gain*error;
        output = Util.constrain(output, -1, 1);
        if(lastError>0 != error>0){
            System.out.print("overshoot!");
            output = 0.5 *(output+TBH);
            TBH = output;
        }
       System.out.println(error + "   " + gain + "  " + output);
       
       return 0;
    }
    public double getOutput(){
        return output;
    }
}

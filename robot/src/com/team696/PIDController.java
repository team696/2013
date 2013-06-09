/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team696;

/**
 *
 * @author CMR
 */
public class PIDController {
    private double setPoint;
    private double position;
    private double cumulativeError;
    private double previousError;
    private double error;
    private double kProportional;
    private double kIntegral;
    private double kDerivative;
    private double output;

    public PIDController(double Kp, double Ki, double Kd){
        kProportional = Kp;
        kIntegral = Ki;
        kDerivative = Kd;
        setPoint = 0.0;
        position = 0.0;
        cumulativeError = 0.0;
        previousError = 0.0;
        error = 0.0;
        output = 0.0;
    }
    
    
    public void setConstants(double Kp, double Ki, double Kd){
        kProportional = Kp;
        kIntegral = Ki;
        kDerivative = Kd;
    }
    public void update(double input, double pos){
        setPoint = input;
        position = pos;
        getError();
        PID();
        previousError = error;
        
    }
    public double getOutput(){   
    System.out.println(error);
    return(output);
    }
    
    private void PID() 
    {
        output = (P()*kProportional) + (I()*kIntegral) + (D()*kDerivative);
    }
    private double P()
    {
        return(error);
    }
    private double I()
    {
        cumulativeError += error;
        cumulativeError = Util.constrain(cumulativeError, -5, 5);
        return(cumulativeError);
    }
    private double D()
    {
        return(error - previousError);
    }
    private void getError(){
        error = setPoint - position;
    }
}

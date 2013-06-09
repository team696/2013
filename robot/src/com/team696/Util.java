/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team696;

/**
 *
 * @author YoungJae
 */
public class Util {
    public static double deadZone(double val, double LowVal, double HighVal) {//y
        if ((val > LowVal)&&(val < HighVal)) { 
            return ((LowVal+HighVal)/2);
        }
        return (val);
    }
    public static double map(double val, double lowIn, double highIn, double lowOut, double highOut) {//y
        return lerp(lowOut, highOut, norm(lowIn, highIn, val));
    }

    public static double norm(double low, double high, double input) {//y
        return ((input - low) / (high - low));
    }

    public static double lerp(double low, double high, double percent) {//y
        return (low + percent * (high - low));
    }
    public static double distBetween(double first, double second) {//y
        if (first < 0) {
            first *= -1;
        }
        if (second < 0) {
            second *= -1;
        }
        if (second < first) {
            return (first - second);
        }
        return (second - first);
    }
    
    public static double abs(double input){
        if(input < 0){
            input = -input;
        }
        return input;
    }
    
    public static double rocker(double val, double targVal) {//y

        if (val < 1) {
            return -targVal;
        } else if (val > 2) {
            return (targVal);
        } else {
            return (0);
        }
    }

    public static double constrain(double val, double lVal, double hVal) {//y
        if (val < lVal) {
            return lVal;
        } else if (hVal < val) {
            return hVal;
        }
        return val;
    }
    public static double smooth(double valWanted, double lastVal, double division){//y
        return(((valWanted-lastVal)/division)+lastVal);
    }
}

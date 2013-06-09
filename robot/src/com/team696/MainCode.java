/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.team696;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class MainCode extends IterativeRobot {

    DriverStationEnhancedIO cypress = DriverStation.getInstance().getEnhancedIO();
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    PIDController flyWheelPID = new PIDController(5.0, 0.07, 1.0);
    PIDController visionPID = new PIDController(0.7, 0.03, 0.0);
    PIDController ultrasonicController = new PIDController(1.0, 0.0, 0.0);
    DriverStationLCD lcd = DriverStationLCD.getInstance();
    RobotDrive drive = new RobotDrive(8, 4, 1, 3); //motors
    Talon flywheelMotorTalon1 = new Talon(5);
    Talon flywheelMotorTalon2 = new Talon(2);
    Talon ConveyorTalon = new Talon(6);
    Talon kickUpTalon = new Talon(9);
    Talon pickUpRollersTalon = new Talon(7);
    SmartDashboard dash = new SmartDashboard();
    DigitalInput encoderA = new DigitalInput(2);
    DigitalInput encoderB = new DigitalInput(3);
    Solenoid shooterPos = new Solenoid(3);
    Solenoid frisbeeFeed = new Solenoid(4);
    Solenoid gearBoxWinchLeft = new Solenoid(5);
    Solenoid gearBoxDriveLeft = new Solenoid(6);
    Solenoid gearBoxDriveRight = new Solenoid(7);
    Solenoid gearBoxWinchRight = new Solenoid(8);
    Solenoid firstClimb = new Solenoid(2);
    //Solenoid firstClimb = new Solenoid(2); //wtf here need alot\/
    Relay secondThirdRelease = new Relay(1);
    // AUTO CONSTANTS DO NOT TOUCH
    //FirstAuto&&Third
    double upDelay1 = 1.7;
    double retractDelay1 = 0.3;
    double extendDelay1 = 0.6;
    double flySpeed1 = -1.0;
    double firstTurnSpeed1 = 0.7;
    double firstTurnDelay1 = 0.45;
    double forwardSpeed1 = 0.6;
    double forwardDelay1 = 3;
    double secondTurnDelay1 = 0.4;
    double secondTurnSpeed1 = 0.7;
    double conveyorUpDelay1 = 3;
    double backSpeed1 = 1;
    double backDelay1 = 0.7;
    double autoAimDelay1 = 3.5;
    double lastShootDelay1 = 0.1;
    //SecondAuto CONSTANTS WORKED IN INLAND EMPIRE LAST QUAL. MATCH.
    double upDelay2 = 1.6;
    double retractDelay2 = 0.25;
    double extendDelay2 = 0.6;
    double flySpeed2 = -1.0;
    double firstTurnSpeed2 = 0.7;
    double firstTurnDelay2 = 0.45;
    double forwardSpeed2 = 0.7;
    double forwardDelay2 = 4.5;
    double secondTurnDelay2 = 0.4;
    double secondTurnSpeed2 = 0.7;
    double conveyorUpDelay2 = 2.3;
    double backSpeed2 = 1;
    double backDelay2 = 0.95;
    double autoAimDelay2 = 2.5;
    double lastShootDelay2 = 0.1;
    //Third Auto
    double upDelay3 = 1.7;
    double retractDelay3 = 0.4;
    double extendDelay3 = 0.9;
    double flySpeed3 = -1.0;
    double firstTurnSpeed3 = 0.6;
    double firstTurnDelay3 = 0.3;
    double forwardSpeed3 = 0.6;
    double forwardDelay3 = 3;
    double secondTurnDelay3 = 0.8;
    double secondTurnSpeed3 = 0.7;
    double conveyorUpDelay3 = 3;
    double backSpeed3 = 0.9;
    double backDelay3 = 1.3;
    double autoAimDelay3 = 3.5;
    double lastShootDelay3 = 0.1;
    
    //OTHER STUFF
    Timer flywheelTimer = new Timer();
    Timer shooterTimer = new Timer();
    int loadPos = 0;
    int autoShotsFired = 0;
    Compressor compressor = new Compressor(1, 2); // compressor
    double oldX = 0.0;
    double throttle = 0.0;
    double turnRate = 0.0;
    double frontRollerSpeed = 0.0;
    double conveyorMotorVal = 0.0;
    double kickUp = 0.0;
    double leftDrive = 0.0;
    double rightDrive = 0.0;
    double normalizedCenterX = 0.5;
    //PID variables begin here//
    double Kp = 0.0;
    double Ki = 0.0;
    double Kd = 0.0;
    //PID variables end here//
    //flyWheelValues begin//
    double pastTimer = 0.0;
    double pastCount = 0.0;
    double flyWheelSetSpeed = 0.0;
    double flyWheelPower = 0.0;
    double flyWheelSpeed = 0.0;
    double oldRightDriveVal = 0.0;
    double oldLeftDriveVal = 0.0;
    double pastFlyWheelPower = 0.0;
    double alpha = 0.5;
    //flyWheelValues end//
    double rightDriveVal = 0.0;
    double leftDriveVal = 0.0;
    boolean doneAiming = false;
    boolean kickUpEnable = false;
    boolean runOnce = true;
    boolean loadEnable = false;
    boolean frontRollers = false;
    boolean shooterEnable = false;
    boolean autoAimEnable = false;
    boolean shooterPosUp = false;
    boolean firstClimbEnable = false;
    boolean secondClimbEnable;
    boolean thirdClimbEnable;
    boolean frisbeeLoad;
    boolean hoodUp = true;
    boolean PTOPosition = false;
    boolean flyWheelIncreaseOld = false;
    boolean flyWheelIncrease = false;
    boolean flyWheelDecreaseOld = false;
    boolean flyWheelDecrease = false;
    boolean[] oldButtons = new boolean[13];
    boolean[] oldCypressButtons = new boolean[15];
    boolean loading = false;
    boolean fastTurn = false;
    double levelTwoWinch = 0.0;
    double levelThreeWinch = 0.0;
    Encoder flyWheelEncoder = new Encoder(4, 5, false, CounterBase.EncodingType.k4X); //see if this still works
    Joystick controller = new Joystick(1);
    AnalogChannel distanceSensor = new AnalogChannel(1);
    double sumTime = 0;
    double allowedDistance = 108.0;
    int imageSize = 640;//size of image returned by camera(use for image turn tracking)
    int numShot = 0;
    double pastFlywheelCount;
    double autonomousMode = 0;
    double pastFlyweelTimer;
    Timer autoTimer = new Timer();    //add these to variables and objects
    int autonomousStage = 0;
    boolean runAuto = true;

    public void robotInit() {
        runOnce = true;

        shooterTimer.start();
        drive.tankDrive(0.0, 0.0);
        flywheelMotorTalon1.set(0.0);
        flywheelMotorTalon2.set(0.0);
        //getController();
        getCypress();
        compressor.start();
        flywheelTimer.start();
        //flyWheelEncoder.start();
        flywheelMotorTalon2.set(0.0);
        frisbeeFeed.set(false);
        flyWheelEncoder.start();
        //getCypress();
        //firstClimb.set(false);
        //secondThirdRelease.set(Relay.Value.kOff);
        autoTimer.start();                //add this to robotInit()
        autoTimer.reset();
        sumTime = autoTimer.get();
        lcd.println(DriverStationLCD.Line.kUser1, 1, "Skynet Online");
        lcd.updateLCD();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {//y                          //actual autonomous code
        getCypress();

        if (autonomousMode == 1) {
            if (runAuto) {
                numShot = 0;
                pickUpRollersTalon.set(0.5);
                flywheelMotorTalon1.set(flySpeed1);
                flywheelMotorTalon2.set(flySpeed1);
                frisbeeFeed.set(false);
                shooterPos.set(true);
                autoTimer.delay(upDelay1);
                while (numShot < 3) {
                    frisbeeFeed.set(true);
                    autoTimer.delay(retractDelay1);
                    frisbeeFeed.set(false);
                    autoTimer.delay(extendDelay1);
                    numShot++;
                }
                numShot = 0;
                autoTimer.delay(lastShootDelay1);
                flywheelMotorTalon1.set(0.0);
                flywheelMotorTalon2.set(0.0);
                shooterPos.set(false);
                
                runAuto = false;
            } else {
                drive.tankDrive(0.0, 0.0);
            }




        } else if (autonomousMode == 2) {
            if (runAuto) {
                numShot = 0;
                pickUpRollersTalon.set(0.5);
                flywheelMotorTalon1.set(flySpeed2);
                flywheelMotorTalon2.set(flySpeed2);
                frisbeeFeed.set(false);
                shooterPos.set(true);
                autoTimer.delay(upDelay2);
                while (numShot < 3) {
                    frisbeeFeed.set(true);
                    autoTimer.delay(retractDelay2);
                    frisbeeFeed.set(false);
                    autoTimer.delay(extendDelay2);
                    numShot++;
                }
                numShot = 0;
                autoTimer.delay(lastShootDelay2);
                flywheelMotorTalon1.set(0.0);
                flywheelMotorTalon2.set(0.0);
                shooterPos.set(false);
                kickUpTalon.set(1);
                pickUpRollersTalon.set(-0.45);
                ConveyorTalon.set(-0.95);
                autoTimer.reset();
                while (autoTimer.get() < 0.5) {
                    drive.tankDrive(-forwardSpeed2, -forwardSpeed2*0.9);
                }
                while (autoTimer.get() < forwardDelay2) {
                    drive.tankDrive(-forwardSpeed2, -forwardSpeed2*0.925);
                }
                autoTimer.reset();
                /*while(autoTimer.get()<0.1){
                    drive.tankDrive(1,0.9);
                    
                }*/
                autoTimer.reset();
                
                drive.tankDrive(0, 0);
                autoTimer.delay(conveyorUpDelay2);


                pickUpRollersTalon.set(1.0);
                autoTimer.reset();
                while (autoTimer.get() < backDelay2) {
                    drive.tankDrive(backSpeed2, backSpeed2*0.875);
                }
                autoTimer.reset();
                /*while (autoTimer.get() < 0.1) {
                    drive.tankDrive(-1, -0.9);
                }*/
                drive.tankDrive(0.0, 0.0);
                kickUpTalon.set(0.0);
                pickUpRollersTalon.set(0.0);
                ConveyorTalon.set(0.0);

                flywheelMotorTalon1.set(flySpeed2);
                flywheelMotorTalon2.set(flySpeed2);
                frisbeeFeed.set(false);
                shooterPos.set(true);
                autoTimer.delay(upDelay2);
                while (numShot < 4) {
                    frisbeeFeed.set(true);
                    autoTimer.delay(retractDelay2);
                    frisbeeFeed.set(false);
                    autoTimer.delay(extendDelay2);
                    numShot++;
                }
                numShot = 0;
                runAuto = false;
            } else {
                drive.tankDrive(0.0, 0.0);
            }

        } else if (autonomousMode == 3) {
            if (runAuto) {
                flywheelMotorTalon1.set(flySpeed3);
                flywheelMotorTalon2.set(flySpeed3);
                pickUpRollersTalon.set(0.5);
                shooterPos.set(true);
                frisbeeFeed.set(false);
                autoTimer.delay(upDelay3);
                while (numShot < 3) {
                    frisbeeFeed.set(true);
                    autoTimer.delay(retractDelay3);
                    frisbeeFeed.set(false);
                    autoTimer.delay(extendDelay3);
                    numShot++;
                }
                autoTimer.delay(lastShootDelay3);
                flywheelMotorTalon1.set(0.0);
                flywheelMotorTalon2.set(0.0);
                shooterPos.set(false);
                
                
                autoTimer.reset();
                while(autoTimer.get()<backDelay3){
                    drive.tankDrive(backSpeed3, backSpeed3);
                }
                autoTimer.reset();
                while(autoTimer.get()<secondTurnDelay3){
                    drive.tankDrive(secondTurnSpeed3, -secondTurnSpeed3);
                }
                runAuto = false;
            } else {
                drive.tankDrive(0.0, 0.0);
            }
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        runAuto = true;
        runOnce = true;
        try {
            cypress.getDigital(1);
            getCypress();
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            //getController();
        }


        //climb
        firstClimb.set(firstClimbEnable);
        /*if ((secondClimbEnable) && (thirdClimbEnable)) {
         secondThirdRelease.setDirection(Relay.Direction.kBoth);
         } else if (secondClimbEnable) {
         secondThirdRelease.setDirection(Relay.Direction.kForward);
         } else if (thirdClimbEnable) {
         secondThirdRelease.setDirection(Relay.Direction.kReverse);
         } else {
         secondThirdRelease.set(Relay.Value.kOff);
         }*/

        //standard shooter data
        if (shooterEnable) {
            flywheelMotorTalon1.set(-flyWheelSetSpeed);
            flywheelMotorTalon2.set(-flyWheelSetSpeed);
        } else {
            flywheelMotorTalon1.set(0.0);
            flywheelMotorTalon2.set(0.0);
        }

        //PTO Data
        gearBoxWinchLeft.set(false);
        gearBoxDriveRight.set(true); // fix this- move the dependencies to the ptoclimb vars
        gearBoxDriveLeft.set(true);
        gearBoxWinchRight.set(false);

        shooterPos.set(shooterPosUp);

        kickUpTalon.set(-frontRollerSpeed);
        pickUpRollersTalon.set(frontRollerSpeed / 2.2);
        ConveyorTalon.set(Util.constrain(conveyorMotorVal, -1, 0));

        if (autoAimEnable) {
            autoAim();
        } else if (!autoAimEnable){
            if (fastTurn) {

                rightDriveVal = Util.signOf(throttle) * turnRate * -4;
                leftDriveVal = Util.signOf(throttle) * turnRate * 4;
                
            } else {
                if (throttle >= 0) {
                    rightDriveVal = (0.9*throttle) + ((-1 * (turnRate * (1.2 - throttle))) / 1.5);//1.5 for practice all div factor
                    leftDriveVal = throttle + ((turnRate * (1.2 - throttle)) / 1.5);
                    } else {
                    rightDriveVal = (0.9*throttle) + (((turnRate * (1.2 + (throttle)))) / 1.5);
                    leftDriveVal = throttle + ((-1 * (turnRate * (1.2 + (throttle)))) / 1.5);
                }
            }
            drive.tankDrive(-leftDriveVal, -rightDriveVal);
        }
        /*else{
         leftDrive = levelTwoWinch;
         rightDrive = levelThreeWinch;
         drive.tankDrive(leftDrive, rightDrive);
         }*/

        //frisbee loading
        if ((!loading) && frisbeeLoad && (shooterPosUp)) {//prevent things from happening if pressed with not enough deleay

            frisbeeFeed.set(true);
            loadPos = 1;
            loading = true;

        } else if ((loadPos >= 15) && loading) {
            frisbeeFeed.set(false);
            loadPos = 0;
            loading = false;

        } else if (loading) {
            loadPos++;

        }


        try {
            for (int i = 1; i < oldCypressButtons.length; i++) {
                oldCypressButtons[i] = cypress.getDigital(i);
            }
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            for (int i = 1; i < 13; i++) {
                oldButtons[i] = controller.getRawButton(i);
            }
        }


    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
    
    private double getDistance() {
        return (102.4) * distanceSensor.getVoltage();
    }

    private void autoAim() {
//        normalizedCenterX = dash.getNumber("CenterX", 160) / 320;
//        visionPID.setConstants(1.0, 0.0, 0.0);
//        visionPID.update(0.5, normalizedCenterX);
//        
//        if(visionPID.getOutput() <0.51 & visionPID.getOutput() >0.49){
//        lcd.println(DriverStationLCD.Line.kUser1, 1, "vision centered");
//        }
//        drive.arcadeDrive(0.0, visionPID.getOutput());

        normalizedCenterX = dash.getNumber("CenterX", 0) / 320;
        if (normalizedCenterX > 0.57) {
            leftDriveVal = (Util.slowDown(0.485, normalizedCenterX, 4.75)) - 0.575;
            rightDriveVal = -1 * leftDriveVal;
            doneAiming = false;
        } else if (normalizedCenterX < 0.43) {
            leftDriveVal = Util.slowDown(0.515, normalizedCenterX, 4.75) + 0.575;
            rightDriveVal = -1 * leftDriveVal;
            doneAiming = false;
        } else {
            leftDriveVal = 0.0;
            rightDriveVal = 0.0;
            if (Util.absDif(normalizedCenterX, oldX) < 0.00000000001) {
                doneAiming = true;

            }
            //System.out.println(doneAiming);
            oldX = normalizedCenterX;
        }
        System.out.println(autoAimEnable + "   " + leftDriveVal + "   " + rightDriveVal + "    " + normalizedCenterX);
        drive.tankDrive(leftDriveVal, rightDriveVal);
    }

    public void getController() {
        throttle = controller.getRawAxis(2);
        //turnRate = controller.getRawAxis(4);
        if (controller.getRawButton(2) && !oldButtons[2] && (flyWheelSetSpeed >= 0.1)) {
            flyWheelSetSpeed += 0.1;
        }
        if (controller.getRawButton(3) && !oldButtons[3] && (flyWheelSetSpeed <= 1)) {
            flyWheelSetSpeed += 0.1;
        }

        //conveyor: 6 and 8 control speed up/down, 5 controlls overall enable

        conveyorMotorVal = (controller.getRawButton(5)) ? 0.85 : 0;

        //front roller speed on off with 5 and 7 (default 0)
        frontRollerSpeed = controller.getRawButton(5) ? 1 : (controller.getRawButton(7) ? -1 : 0);

        frisbeeLoad = ((controller.getRawButton(1) && !oldButtons[1]));
        firstClimbEnable = controller.getRawButton(12);
        autoAimEnable = controller.getRawButton(10);

        //secondClimbEnable = controller.getRawButton(6);
        //thirdClimbEnable = controller.getRawButton(7);


        //shooterposition controlled by button 4 - sets to opposite.
        shooterPosUp = (controller.getRawButton(4) && (!oldButtons[4])) ? !shooterPosUp : shooterPosUp;
        //secondClimb = controller.getRawButton(6);
        //thirdClimb = controller.getRawButton(7);
        if (controller.getRawButton(9) && !oldButtons[9]) {
            PTOPosition = !PTOPosition;
        }
        //shooterEnable = controller.getRawButton(11);
        firstClimbEnable = controller.getRawButton(12);
    }

    public void getCypress() {//y
        try {
            autonomousMode = 2 + Util.rocker(cypress.getAnalogIn(4), 1.0);            // x axis of joystick = 3;

        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            throttle = 0;
            System.out.println("Auto Choose error");
        }
        try {
            throttle = Util.deadZone(Util.map(cypress.getAnalogIn(1), 0, 3.3, 1, -1), -0.15, 0.02);//-0.1 lower band if no work

        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            throttle = 0;
            System.out.println("Left Joystick error");
        }
        try {
            turnRate = Util.constrain(Util.deadZone(Util.map(cypress.getAnalogIn(8), 0.7, 2.6, 1, -1), -0.07, 0.07), -1, 1);


        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            turnRate = 0;
            System.out.println("Right Joystick error");
        }
        try {
            fastTurn = !cypress.getDigital(12);

        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            fastTurn = false;
            System.out.println("Right Joystick error");
        }
        try {
            flyWheelSetSpeed = Util.map(cypress.getAnalogIn(2), 0, 3.3, 1, 0);

        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            flyWheelSetSpeed = 0;
            System.out.println("Shoot Speed slider error");
        }
        try {
            conveyorMotorVal = -Util.rocker(cypress.getAnalogIn(6), -0.95);
            frontRollerSpeed = -Util.rocker(cypress.getAnalogIn(6), -1);

        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            conveyorMotorVal = 0;
            frontRollerSpeed = 0;
            System.out.println("Conveyor rocker error");
        }
        try {
            shooterEnable = !cypress.getDigital(7);
            shooterPosUp = !cypress.getDigital(7);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            shooterEnable = false;
            shooterPosUp = false;
            System.out.println("Shoot button error");
        }
        try {
            autoAimEnable = !cypress.getDigital(5);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            autoAimEnable = false;
            System.out.println("autoAim button error");
        }
        try {
            frisbeeLoad = cypress.getDigital(10);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            frisbeeLoad = false;
            System.out.println("loadEnable error");
        }
        /*try {
         autonomousMode = (int)cypress.getAnalogIn(8);
         } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
         autonomousMode = 3;
         System.out.println("autonomousMode error");
         }*/
        try {
            firstClimbEnable = !cypress.getDigital(2);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            firstClimbEnable = false;
            System.out.println("ClimbInit Error");
        }
        try {
            secondClimbEnable = !cypress.getDigital(6);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            firstClimbEnable = false;
            System.out.println("ClimbInit Error: Skynet is Not Pleased.");
        }
        try {
            thirdClimbEnable = !cypress.getDigital(4);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            firstClimbEnable = false;
            System.out.println("ClimbInit Error");
        }
        try {
            levelTwoWinch = cypress.getDigital(11) ? 0.0 : 1.0;
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            //levelTwoWinch = 1.0;
            System.out.println("ClimbInit Error");
        }
        try {
            levelThreeWinch = cypress.getDigital(1) ? 0.0 : 1.0;
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            //levelThreeWinch = 0.0;
            System.out.println("ClimbInit Error");
        }
        PTOPosition = (secondClimbEnable || thirdClimbEnable);
    }
}



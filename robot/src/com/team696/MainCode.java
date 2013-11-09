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
import edu.wpi.first.wpilibj.Gyro;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class MainCode extends IterativeRobot {

    DriverStationEnhancedIO cypress = DriverStation.getInstance().getEnhancedIO();
    Gyro gyro = new Gyro(2);
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    DriverStationLCD lcd = DriverStationLCD.getInstance();
    RobotDrive drive = new RobotDrive(8, 4, 1, 3); //motors
    Talon flywheelMotorTalon1 = new Talon(5);
    Talon flywheelMotorTalon2 = new Talon(2);
    Talon ConveyorTalon = new Talon(6);
    Talon kickUpTalon = new Talon(9);
    Talon pickUpRollersTalon = new Talon(7);
    SmartDashboard dash = new SmartDashboard();
    Solenoid shooterPos = new Solenoid(3);
    Solenoid frisbeeFeed = new Solenoid(4);
    Solenoid gearBoxWinchLeft = new Solenoid(5);
    Solenoid gearBoxDriveLeft = new Solenoid(6);
    Solenoid gearBoxDriveRight = new Solenoid(7);
    Solenoid gearBoxWinchRight = new Solenoid(8);
    Solenoid firstClimb = new Solenoid(2);
    Relay secondThirdRelease = new Relay(1);
    Encoder leftEncoder = new Encoder(6, 7);//A on 6, B on 7
    Encoder rightEncoder = new Encoder(8, 9);//A on 8, B on 9
    /*Encoder leftDriveEncoder = new Encoder(6, 7, true, CounterBase.EncodingType.k4X);
    Encoder rightCriveEncoder = new Encoder(8, 9, false, CounterBase.EncodingType.k4X);*/
    Timer shooterTimer = new Timer();
    Timer fireTimer = new Timer();
    Timer autonomousTimer = new Timer();

    PIDController speedController = new PIDController(0.3, 0.0, 0.2);
    PIDController turnController = new PIDController(6.0, 0.2, 1.0);
    double cumAngle = 0.0;
    Compressor compressor = new Compressor(1, 2); // compressor
    double oldX = 0.0;
    double throttle = 0.0;
    double turnRate = 0.0;
    double frontRollerSpeed = 0.0;
    double conveyorMotorVal = 0.0;
    double kickUp = 0.0;
    double leftDrive = 0.0;
    double rightDrive = 0.0;
    double dr_clouse_added_this_for_testing_github = 999.9;
    //flyWheelValues begin//

    double flyWheelSetSpeed = 0.0;

    //flyWheelValues end//
    double rightDriveVal = 0.0;
    double leftDriveVal = 0.0;
    double rightDriveSetVal = 0.0;
    double leftDriveSetVal = 0.0;
    boolean doneAiming = false;
    boolean kickUpEnable = false;
    boolean runOnce = true;
    
    
    boolean shooterEnable = false;
    boolean shooterPosUp = false;
    boolean firstClimbEnable = false;
    boolean secondClimbEnable;
    boolean thirdClimbEnable;
    boolean PTOPosition = false;
    boolean[] oldButtons = new boolean[13];
    boolean[] oldCypressButtons = new boolean[15];
    boolean fastTurn = false;
    boolean fire = false;
    boolean oldFire = false;
    double levelTwoWinch = 0.0;
    double levelThreeWinch = 0.0;
    Encoder flyWheelEncoder = new Encoder(4, 5, false, CounterBase.EncodingType.k4X); //see if this still works
    //Joystick controller = new Joystick(1);
    Joystick controller2 = new Joystick(1);
    double allowedDistance = 108.0;
    int imageSize = 640;//size of image returned by camera(use for image turn tracking)
    
    
    double autonomousMode = 2;
    
    Timer autoTimer = new Timer();    //add these to variables and objects
    int autonomousStage = 0;
    boolean runAuto = true;


    double speed = 0.0;
    double turnSpeed = 0.0;
    double turnSetPower = 0.0;
    double driveSetPower = 0.0;
    double autoPosition = 0.0;
    double autoPosSetPower = 0.0;
    //AUTO VARS
    double shooterUpTime = 1.6;
    double autoPos = 1;
    double shootTime = 0.6;
    double oldRightDist = 0.0;
    double oldLeftDist = 0.0;
    double secondFireStart = 0;
    boolean firstRun = true;
    boolean secondFireFirstRun = true;

    public void robotInit() {
        System.out.println("Hello");
        autonomousTimer.start();
        leftEncoder.start();
        
        rightEncoder.start();

        /*leftEncoder.start();
        righTDriveEncoder.start();
        leftDriveEncoder.setDistancePerPulse(1 / 1000.0);//0.09162978572970230278849376534565);
        rightDriveEncoder.setDistancePerPulse(1 / 1000.0);//0.09162978572970230278849376534565);*/

        runOnce = true;
        fireTimer.start();
        shooterTimer.start();
        drive.tankDrive(0.0, 0.0);
        flywheelMotorTalon1.set(0.0);
        flywheelMotorTalon2.set(0.0);
        getController();
        //getCypress();
        compressor.start();
        //leftEncoder.setDistancePerPulse(1 / 1000.0);
        //rightEncoder.setDistancePerPulse(1 / 1000.0);
                        
        //flyWheelEncoder.start();
        flywheelMotorTalon2.set(0.0);
        frisbeeFeed.set(false);
        flyWheelEncoder.start();
        //firstClimb.set(false);
        //secondThirdRelease.set(Relay.Value.kOff);
        autoTimer.start();                //add this to robotInit()
        autoTimer.reset();

        lcd.println(DriverStationLCD.Line.kUser1, 1, "Ratchet Online");
        lcd.updateLCD();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {//y                          //actual autonomous code
        getCypress();
        
        //System.out.println(rightEncoder.getDistance()+"    "+leftEncoder.getDistance());
        
        if (autonomousMode == 1) {
            if (firstRun) {
                rightEncoder.setDistancePerPulse(0.09162978572970230278849376534565);
                leftEncoder.setDistancePerPulse(0.09162978572970230278849376534565);
                leftEncoder.setReverseDirection(false);
                rightEncoder.setReverseDirection(false);
                autoTimer.start();
                fireTimer.start();
                rightEncoder.reset();
                leftEncoder.reset();
                
            }
            //System.out.println(fireTimer.get());
            if (autoPos == 1) {
                firstRun = false;
                drive.tankDrive(0, 0);
                shooterPos.set(true);
                flywheelMotorTalon1.set(-0.875);
                flywheelMotorTalon2.set(-0.875);

                if ((autoTimer.get() > shooterUpTime) && (autoTimer.get() < shooterUpTime + shootTime)) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("First");
                } else if ((autoTimer.get() > shooterUpTime + shootTime) && (autoTimer.get() < shooterUpTime + (shootTime * 2))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Second");
                } else if ((autoTimer.get() > shooterUpTime + (shootTime * 2)) && (autoTimer.get() < shooterUpTime + (shootTime * 3))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Third");
                } else if (autoTimer.get() > shooterUpTime + (shootTime * 3)) {
                    shooterPos.set(false);
                    flywheelMotorTalon1.set(0);
                    flywheelMotorTalon2.set(0);
                    autoTimer.reset();
                    autoPos = 2;
                }
            } else if (autoPos == 2) {
                System.out.println("Backing Up" + -rightEncoder.getDistance() + "  " + oldRightDist + "  " + leftEncoder.getDistance() + "  " + oldLeftDist);
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) + 5, 0, 20), leftEncoder, rightEncoder);
                if ((((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) > 19)) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 3;
                }
            } else if (autoPos == 3) {
                System.out.println("First Turn");
                turnRightWheels(drive, 11, rightEncoder, oldRightDist);
                System.out.println(-(rightEncoder.getDistance()));
                if (-rightEncoder.getDistance() > 10) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 4;

                }
            } else if (autoPos == 4) {

                System.out.println("Backing Up" + -rightEncoder.getDistance() + "  " + oldRightDist + "  " + leftEncoder.getDistance() + "  " + oldLeftDist);
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) + 9, 0, 50), leftEncoder, rightEncoder);
                if ((((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) > 49)) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 5;
                }
            } else if (autoPos == 5) {
                System.out.println("Turning Left   " + leftEncoder.getDistance());
                turnLeftWheels(drive, 50, leftEncoder, oldLeftDist);
                if (leftEncoder.getDistance() > 49.5) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 6;
                }
            } else if (autoPos == 6) {
                System.out.println("Forward");
                kickUpTalon.set(-1);
                pickUpRollersTalon.set(-1);
                ConveyorTalon.set(Util.constrain(-0.95, -1, 0));
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) - 9, -50, 0), leftEncoder, rightEncoder);
                if (((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) < -49.5) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 7;
                }

            } else if (autoPos == 7) {
                System.out.println("Forward 2   " + ((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2));
                kickUpTalon.set(-1);
                pickUpRollersTalon.set(-1);//div by 2.2
                ConveyorTalon.set(Util.constrain(-0.95, -1, 0));
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) - 3, -40, 0), leftEncoder, rightEncoder);
                if (((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) < -39) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 8;
                }

            } else if (autoPos == 8) {
                System.out.println("Turn Right");
                turnRightWheels(drive, 35, rightEncoder, oldRightDist);
                System.out.println(-(rightEncoder.getDistance()));
                if (-rightEncoder.getDistance() > 34) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 9;
                }

            } else if (autoPos == 9) {
                System.out.println("Forward");
                kickUpTalon.set(-1);
                pickUpRollersTalon.set(-1);
                ConveyorTalon.set(-0.95);
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) - 9, -50, 0), leftEncoder, rightEncoder);
                if (((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) < -49.5) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 10;
                }

            } else if (autoPos == 10) {
                if (secondFireFirstRun) {
                    autoTimer.reset();
                    secondFireFirstRun = false;
                }
                if ((autoTimer.get() > shooterUpTime) && (autoTimer.get() < shooterUpTime + shootTime)) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("First");
                } else if ((autoTimer.get() > shooterUpTime + shootTime) && (autoTimer.get() < shooterUpTime + (shootTime * 2))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Second");
                } else if ((autoTimer.get() > shooterUpTime + (shootTime * 2)) && (autoTimer.get() < shooterUpTime + (shootTime * 3))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Third");
                } else if (autoTimer.get() > shooterUpTime + (shootTime * 3)) {
                    shooterPos.set(false);
                    flywheelMotorTalon1.set(0);
                    flywheelMotorTalon2.set(0);
                    autoTimer.reset();
                    autoPos = 11;
                }
            } else if (autoPos == 11) {
                drive.tankDrive(0, 0);
            }

            oldRightDist = -rightEncoder.getDistance();
            oldLeftDist = leftEncoder.getDistance();

        } else if (autonomousMode == 2) {
            cumAngle = gyro.getAngle()/90;
            //lcd.println(DriverStationLCD.Line.kMain6, 0, );
            switch (autonomousStage) {                           //state machine using switch statements
                case 0:
                    if (true){//Util.absDif((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2, 0.15) < 0.05) {
                        leftEncoder.setReverseDirection(true);
                        rightEncoder.setReverseDirection(false);
                        
                        leftEncoder.setDistancePerPulse(1 / 1000.0);
                        rightEncoder.setDistancePerPulse(1 / 1000.0);
                        
                        flywheelMotorTalon1.set(-0.85);
                        flywheelMotorTalon2.set(-0.85);
                        shooterPos.set(true);
                        drive.arcadeDrive(0.0, 0.0);
                        autonomousStage = 1;
                        autonomousTimer.reset();
                        leftEncoder.reset();
                        rightEncoder.reset();

                    } else {

                        //autonomousStage = 1;
                        leftEncoder.setReverseDirection(true);
                        rightEncoder.setReverseDirection(false);
                        
                        leftEncoder.setDistancePerPulse(1 / 1000.0);
                        rightEncoder.setDistancePerPulse(1 / 1000.0);
                        
                        speed = (leftEncoder.getRate() + rightEncoder.getRate()) / 2;

                        turnController.update(0.0, cumAngle);

                        speedController.update(0.3, speed);

                        turnSetPower = turnController.getOutput();
                        driveSetPower += speedController.getOutput();

                        if (turnSetPower >= 1.0) {
                            turnSetPower = 1.0;
                        } else if (turnSetPower <= -1.0) {
                            turnSetPower = -1.0;
                        }
                        if (driveSetPower >= 1.0) {
                            driveSetPower = 1.0;
                        } else if (driveSetPower <= -1.0) {
                            driveSetPower = -1.0;
                        }

                        drive.arcadeDrive(-driveSetPower, turnSetPower - 0.1);

                        ConveyorTalon.set(-0.85);
                        pickUpRollersTalon.set(1.0);
                        kickUpTalon.set(1.0);
                        shooterPos.set(true);
                        flywheelMotorTalon1.set(-1.0);
                        flywheelMotorTalon2.set(-1.0);

                    }
                    break;
                case 1:
                    if (autonomousTimer.get() > 1.5) {
                        autonomousStage = 2;
                        autonomousTimer.reset();
                    }
                    pickUpRollersTalon.set(-1.0);
                    drive.tankDrive(0.0, 0.0);
                    break;

                case 2:                                         //shoot stuff
                    if (autonomousTimer.get() > 2.4) {             //if we have fired three times, go on to next stage
                        shooterPos.set(false);                  //bring shoooter down
                        flywheelMotorTalon1.set(0.0);
                        flywheelMotorTalon2.set(0.0);
                        frisbeeFeed.set(false);

                        autonomousStage = 3;
                        autonomousTimer.reset();
                    } else {
                        speed = (leftEncoder.getRate() + rightEncoder.getRate()) / 2;

                        turnController.update(0.0, cumAngle);

                        speedController.update(0.0, speed);

                        turnSetPower = turnController.getOutput();
                        driveSetPower += speedController.getOutput();

                        if (turnSetPower >= 1.0) {
                            turnSetPower = 1.0;
                        } else if (turnSetPower <= -1.0) {
                            turnSetPower = -1.0;
                        }
                        if (driveSetPower >= 1.0) {
                            driveSetPower = 1.0;
                        } else if (driveSetPower <= -1.0) {
                            driveSetPower = -1.0;
                        }

                        drive.arcadeDrive(-driveSetPower, turnSetPower );
                        if(autonomousTimer.get() >0.3){
                        flywheelMotorTalon1.set(-0.9);          //start the flywheel
                        flywheelMotorTalon2.set(-0.9);
                        }
                        else{
                            flywheelMotorTalon1.set(-0.7);          //start the flywheel
                            flywheelMotorTalon2.set(-0.7);
                        }
                        pickUpRollersTalon.set(-1.0);
                        shooterPos.set(true);                   ///bring shooter up
                        if (autonomousTimer.get() % 0.8 > 0.4) {    //use modulus to alternate between in and out for feeding piston.
                            frisbeeFeed.set(false);
                        } else {
                            frisbeeFeed.set(true);
                        }
                    }
                    break;
                case 3:
                    if (Util.absDif((leftEncoder.getDistance() + rightEncoder.getDistance()) / 1.0, 2.0) < 0.1) {
                        drive.arcadeDrive(0.0, 0.0);
                        autonomousStage = 4;
                        autonomousTimer.reset();
                    } else {


                        speed = (leftEncoder.getRate() + rightEncoder.getRate()) / 2;

                        turnController.update(0.0, cumAngle);

                        speedController.update(0.3, speed);

                        turnSetPower = turnController.getOutput();
                        driveSetPower += speedController.getOutput();

                        if (turnSetPower >= 1.0) {
                            turnSetPower = 1.0;
                        } else if (turnSetPower <= -1.0) {
                            turnSetPower = -1.0;
                        }
                        if (driveSetPower >= 1.0) {
                            driveSetPower = 1.0;
                        } else if (driveSetPower <= -1.0) {
                            driveSetPower = -1.0;
                        }

                        drive.arcadeDrive(-driveSetPower, turnSetPower);

                        ConveyorTalon.set(-0.85);
                        pickUpRollersTalon.set(-1.0);
                        kickUpTalon.set(1.0);
                        shooterPos.set(false);
                        flywheelMotorTalon1.set(0.0);
                        flywheelMotorTalon2.set(0.0);

                    }
                    break;
                case 4:
                    if (autonomousTimer.get() > 1.8) {
                        autonomousStage = 5;
                        autonomousTimer.reset();
                    }
                    break;
                case 5:
                    if (Util.absDif((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.2, 0) < 0.1) {
                        drive.arcadeDrive(0.0, 0.0); 

                        flywheelMotorTalon1.set(-1.0);
                        flywheelMotorTalon2.set(-1.0);

                        ConveyorTalon.set(0.0);
                        pickUpRollersTalon.set(0.0);
                        kickUpTalon.set(0.0);
                        autonomousStage = 6;
                        autonomousTimer.reset();
                    } else {

                        flywheelMotorTalon1.set(0.0);
                        flywheelMotorTalon2.set(0.0);

                        speed = (leftEncoder.getRate() + rightEncoder.getRate()) / 2;

                        turnController.update(0.0, cumAngle );

                        speedController.update(-0.4, speed);

                        turnSetPower = turnController.getOutput();
                        driveSetPower += speedController.getOutput();

                        if (turnSetPower >= 1.0) {
                            turnSetPower = 1.0;
                        } else if (turnSetPower <= -1.0) {
                            turnSetPower = -1.0;
                        }
                        if (driveSetPower >= 1.0) {
                            driveSetPower = 1.0;
                        } else if (driveSetPower <= -1.0) {
                            driveSetPower = -1.0;
                        }

                        drive.arcadeDrive(-driveSetPower, turnSetPower);
                        System.out.println(driveSetPower);

                        ConveyorTalon.set(-0.85);
                        pickUpRollersTalon.set(-1.0);
                        kickUpTalon.set(1.0);
                    }
                    break;
                case 6:
                    if (autonomousTimer.get() > 1.6) {           //bring shooter up. shooter wheel up to speed.
                        drive.arcadeDrive(0.0, 0.0);
                        flywheelMotorTalon1.set(-1.0);
                        flywheelMotorTalon2.set(-1.0);

                        autonomousStage = 7;
                        autonomousTimer.reset();
                    } else {
                        speed = (leftEncoder.getRate() + rightEncoder.getRate()) / 2;

                        turnController.update(0.0, cumAngle);

                        speedController.update(0.0, speed);

                        turnSetPower = turnController.getOutput();
                        driveSetPower += speedController.getOutput();

                        if (turnSetPower >= 1.0) {
                            turnSetPower = 1.0;
                        } else if (turnSetPower <= -1.0) {
                            turnSetPower = -1.0;
                        }
                        if (driveSetPower >= 1.0) {
                            driveSetPower = 1.0;
                        } else if (driveSetPower <= -1.0) {
                            driveSetPower = -1.0;
                        }

                        drive.arcadeDrive(-driveSetPower, turnSetPower-0.1);
                        shooterPos.set(true);
                        flywheelMotorTalon1.set(-0.9);
                        flywheelMotorTalon2.set(-0.9);
                    }
                    break;
                case 7:
                    if (autonomousTimer.get() > 3.2) {             //if we have fired four times, go on to next stage
                        flywheelMotorTalon1.set(0.0);
                        flywheelMotorTalon2.set(0.0);
                        frisbeeFeed.set(false);
                        autonomousStage = 8;
                        autonomousTimer.reset();
                    } else {
                        speed = (leftEncoder.getRate() + rightEncoder.getRate()) / 2;

                        turnController.update(0.0, cumAngle);

                        speedController.update(0.0, speed);

                        turnSetPower = turnController.getOutput();
                        driveSetPower += speedController.getOutput();

                        if (turnSetPower >= 1.0) {
                            turnSetPower = 1.0;
                        } else if (turnSetPower <= -1.0) {
                            turnSetPower = -1.0;
                        }
                        if (driveSetPower >= 1.0) {
                            driveSetPower = 1.0;
                        } else if (driveSetPower <= -1.0) {
                            driveSetPower = -1.0;
                        }

                        drive.arcadeDrive(-driveSetPower, turnSetPower - 0.05);

                        shooterPos.set(true);
                        flywheelMotorTalon1.set(-1.0);          //start the flywheel
                        flywheelMotorTalon2.set(-1.0);
                        if (autonomousTimer.get() % 0.8 > 0.4) {    //use modulus to alternate between in and out for feeding piston.
                            frisbeeFeed.set(false);
                        } else {
                            frisbeeFeed.set(true);
                        }
                    }
                    break;
                case 9:
                    drive.arcadeDrive(-1.0, 0.0);
                    shooterPos.set(false);
                    autonomousStage = 10;

                default:
                    drive.arcadeDrive(0.0, 0.0);
                    flywheelMotorTalon1.set(0.0);
                    flywheelMotorTalon2.set(0.0);
                    shooterPos.set(true);
                    frisbeeFeed.set(false);
                    System.out.println("Yo case is messed up dawg!");
                    break;
            }
        } else if (autonomousMode == 3) {
            if (firstRun) {
                rightEncoder.setDistancePerPulse(0.09162978572970230278849376534565);
                leftEncoder.setDistancePerPulse(0.09162978572970230278849376534565);
                leftEncoder.setReverseDirection(false);
                rightEncoder.setReverseDirection(false);
                autoTimer.start();
                fireTimer.start();
                rightEncoder.reset();
                leftEncoder.reset();
                
            }
            //System.out.println(fireTimer.get());
            if (autoPos == 1) {
                firstRun = false;
                pickUpRollersTalon.set(1);
                drive.tankDrive(0, 0);
                shooterPos.set(true);
                flywheelMotorTalon1.set(-0.875);
                flywheelMotorTalon2.set(-0.875);

                if ((autoTimer.get() > shooterUpTime) && (autoTimer.get() < shooterUpTime + shootTime)) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("First");
                } else if ((autoTimer.get() > shooterUpTime + shootTime) && (autoTimer.get() < shooterUpTime + (shootTime * 2))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Second");
                } else if ((autoTimer.get() > shooterUpTime + (shootTime * 2)) && (autoTimer.get() < shooterUpTime + (shootTime * 3))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Third");
                } else if (autoTimer.get() > shooterUpTime + (shootTime * 3)) {
                    shooterPos.set(false);
                    flywheelMotorTalon1.set(0);
                    flywheelMotorTalon2.set(0);
                    autoTimer.reset();
                    autoPos = 2;
                }
            } else if (autoPos == 2) {
                System.out.println("Backing Up" + -rightEncoder.getDistance() + "  " + oldRightDist + "  " + leftEncoder.getDistance() + "  " + oldLeftDist);
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) + 5, 0, 32), leftEncoder, rightEncoder);
                if ((((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) > 31.5)) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 3;
                }
            } else if (autoPos == 3) {
                System.out.println("First Turn");
                turnRightWheels(drive, 16, rightEncoder, oldRightDist);
                System.out.println(-(rightEncoder.getDistance()));
                if (-rightEncoder.getDistance() > 15) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 4;

                }
            } else if (autoPos == 4) {

                System.out.println("Backing Up" + -rightEncoder.getDistance() + "  " + oldRightDist + "  " + leftEncoder.getDistance() + "  " + oldLeftDist);
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) + 9, 0, 50), leftEncoder, rightEncoder);
                if ((((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) > 49)) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 5;
                }
            } else if (autoPos == 5) {
                System.out.println("Turning Left   " + leftEncoder.getDistance());
                turnLeftWheels(drive, 55, leftEncoder, oldLeftDist);
                if (leftEncoder.getDistance() > 54.5) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 6;
                }
            } else if (autoPos == 6) {
                System.out.println("Forward");
                kickUpTalon.set(1);
                pickUpRollersTalon.set(-1);
                ConveyorTalon.set(Util.constrain(-0.95, -1, 0));
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) - 7.5, -50, 0), leftEncoder, rightEncoder);
                if (((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) < -49.5) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 7;
                }

            } else if (autoPos == 7) {
                System.out.println("Forward 2   " + ((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2));
                kickUpTalon.set(1);
                pickUpRollersTalon.set(-1);//div by 2.2
                ConveyorTalon.set(Util.constrain(-0.95, -1, 0));
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) - 2.5, -40, 0), leftEncoder, rightEncoder);
                if (((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) < -39) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 11;
                }

            }/* else if (autoPos == 8) {
                System.out.println("Back");
                straight(drive, Util.constrain(((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) + 5, 100, 0), leftEncoder, rightEncoder);
                if (((-rightEncoder.getDistance() + leftEncoder.getDistance()) / 2) > 99) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 9;
                }

            } else if (autoPos == 9) {
                System.out.println("Turn Right");
                turnRightWheels(drive, 35, rightEncoder, oldRightDist);
                System.out.println(-(rightEncoder.getDistance()));
                if (-rightEncoder.getDistance() > 34) {
                    rightEncoder.reset();
                    leftEncoder.reset();
                    autoTimer.reset();
                    autoPos = 10;
                }

            } else if (autoPos == 10) {
                if (secondFireFirstRun) {
                    autoTimer.reset();
                    secondFireFirstRun = false;
                }
                if ((autoTimer.get() > shooterUpTime) && (autoTimer.get() < shooterUpTime + shootTime)) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("First");
                } else if ((autoTimer.get() > shooterUpTime + shootTime) && (autoTimer.get() < shooterUpTime + (shootTime * 2))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Second");
                } else if ((autoTimer.get() > shooterUpTime + (shootTime * 2)) && (autoTimer.get() < shooterUpTime + (shootTime * 3))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Third");
                } else if (autoTimer.get() > shooterUpTime + (shootTime * 3)) {
                    shooterPos.set(false);
                    flywheelMotorTalon1.set(0);
                    flywheelMotorTalon2.set(0);
                    autoTimer.reset();
                    autoPos = 11;
                }
            }*/ else if (autoPos == 11) {
                drive.tankDrive(0, 0);
            }

            oldRightDist = -rightEncoder.getDistance();
            oldLeftDist = leftEncoder.getDistance();
        } else if (autonomousMode == 4) {
            if (firstRun) {
                rightEncoder.setDistancePerPulse(0.09162978572970230278849376534565);
                leftEncoder.setDistancePerPulse(0.09162978572970230278849376534565);
                leftEncoder.setReverseDirection(false);
                rightEncoder.setReverseDirection(false);
                autoTimer.start();
                fireTimer.start();
                rightEncoder.reset();
                leftEncoder.reset();
                
            }
            //System.out.println(fireTimer.get());
            if (autoPos == 1) {
                firstRun = false;
                drive.tankDrive(0, 0);
                shooterPos.set(true);
                flywheelMotorTalon1.set(-0.875);
                flywheelMotorTalon2.set(-0.875);

                if ((autoTimer.get() > shooterUpTime) && (autoTimer.get() < shooterUpTime + shootTime)) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("First");
                } else if ((autoTimer.get() > shooterUpTime + shootTime) && (autoTimer.get() < shooterUpTime + (shootTime * 2))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Second");
                } else if ((autoTimer.get() > shooterUpTime + (shootTime * 2)) && (autoTimer.get() < shooterUpTime + (shootTime * 3))) {
                    fire(frisbeeFeed, fireTimer, shootTime);
                    System.out.println("Third");
                } else if (autoTimer.get() > shooterUpTime + (shootTime * 3)) {
                    shooterPos.set(false);
                    flywheelMotorTalon1.set(0);
                    flywheelMotorTalon2.set(0);
                    autoTimer.reset();
                    autoPos = 2;
                }
            }else if(autoPos == 2){
              drive.tankDrive(0,0);  
            
            }
        }
    }

    public void teleopPeriodic() {
        //System.out.println(rightEncoder.getDistance()+"    "+leftEncoder.getDistance());
        autonomousStage = 0;
        autonomousTimer.reset();
        leftEncoder.reset();
        rightEncoder.reset();
        firstRun = true;
        autoPos = 0;
        rightEncoder.reset();
        leftEncoder.reset();


        getCypress();
        //getController();
        firstClimb.set(firstClimbEnable);

        if (shooterEnable) {
            flywheelMotorTalon1.set(-flyWheelSetSpeed);
            flywheelMotorTalon2.set(-flyWheelSetSpeed);
        } else {
            flywheelMotorTalon1.set(0.0);
            flywheelMotorTalon2.set(0.0);
        }

        //PTO Data
        gearBoxWinchLeft.set(true);
        gearBoxDriveRight.set(false); // fix this- move the dependencies to the ptoclimb vars
        gearBoxDriveLeft.set(false);
        gearBoxWinchRight.set(true);

        shooterPos.set(shooterPosUp);

        kickUpTalon.set(-frontRollerSpeed);
        pickUpRollersTalon.set(frontRollerSpeed);//div by 2.2
        ConveyorTalon.set(Util.constrain(conveyorMotorVal, -1, 0));

if (fastTurn) {

            rightDriveVal = Util.signOf(throttle) * turnRate * -4;
            leftDriveVal = Util.signOf(throttle) * turnRate * 4;

        } else {
            if (throttle >= 0) {
                rightDriveVal = (throttle) + ((-1 * (turnRate * (1.15 - throttle))) / 1.3);
                leftDriveVal = throttle + ((turnRate * (1.15 - throttle)) / 1.3);
            } else {//switch left and right driveval around if want like a car
                leftDriveVal = (throttle) + (((turnRate * (1.15 + (throttle)))) / 1.3);
                rightDriveVal = (throttle) + ((-1 * (turnRate * (1.15 + (throttle)))) / 1.3);
            }
        }
        rightDriveSetVal = Util.smooth(rightDriveVal, rightDriveSetVal, 2);
        leftDriveSetVal = Util.smooth(leftDriveVal, leftDriveSetVal, 2);
        drive.tankDrive(-leftDriveSetVal, -rightDriveSetVal);
        if ((fire)&&(shooterPos.get())) {

            if (fireTimer.get() < 0.4) {
                frisbeeFeed.set(true);
            }
            if (fireTimer.get() > 0.4) {
                frisbeeFeed.set(false);
            }
            if (fireTimer.get() > shootTime) {
                fireTimer.reset();
            }

        } else {
            if ((oldFire) && frisbeeFeed.get()) {
                fireTimer.reset();
                frisbeeFeed.set(true);
            } else if (fireTimer.get() > 0.2) {
                frisbeeFeed.set(false);
            }
        }


        try {
            for (int i = 1; i < oldCypressButtons.length; i++) {
                oldCypressButtons[i] = cypress.getDigital(i);
            }
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            for (int i = 1; i < 13; i++) {
                oldButtons[i] = controller2.getRawButton(i);
            }
        }

        oldFire = fire;
    }

    public void testPeriodic() {
    }

    public void getController() {
        leftDrive = controller2.getRawAxis(2);
        rightDrive = controller2.getRawAxis(4);
        if (controller2.getRawButton(2) && !oldButtons[2] && (flyWheelSetSpeed >= 0.1)) {
            flyWheelSetSpeed -= 0.1;
        }
        if (controller2.getRawButton(3) && !oldButtons[3] && (flyWheelSetSpeed <= 1)) {
            flyWheelSetSpeed += 0.1;
        }
        //conveyor: 6 and 8 control speed up/down, 5 controlls overall enable

        conveyorMotorVal = (controller2.getRawButton(5)) ? -1 : 0;//-0.85

        //front roller speed on off with 5 and 7 (default 0)
        frontRollerSpeed = controller2.getRawButton(5) ? -1 : (controller2.getRawButton(7) ? 1 : 0);

        firstClimbEnable = (controller2.getRawButton(10) && (!oldButtons[10])) ? !firstClimbEnable : firstClimbEnable;

        shooterPosUp = (controller2.getRawButton(4) && (!oldButtons[4])) ? !shooterPosUp : shooterPosUp;
        shooterEnable = (controller2.getRawButton(4) && (!oldButtons[4])) ? !shooterEnable : shooterEnable;
        
        fire = controller2.getRawButton(6);
        
        }

    public void fire(Solenoid piston, Timer fireTimer, double totalTime) {
        if (fireTimer.get() < 0.2) {
            frisbeeFeed.set(true);
        }
        if (fireTimer.get() > 0.2) {
            frisbeeFeed.set(false);
        }
        if (fireTimer.get() > totalTime) {
            fireTimer.reset();
        }
        System.out.println(fireTimer.get());
    }

    public void straight(RobotDrive drive, double setDist, Encoder left, Encoder right) {

        double rightDist = -right.getDistance();
        double leftDist = left.getDistance();
        double driveError = rightDist - leftDist;
        double slowDownDist = 15;
        double oldRightEncoder = 0;
        double oldLeftEncoder = 0;
        double rightBoost = 1;
        double leftBoost = 1;
        //drive.tankDrive(Util.constrain((Util.slowDown(setDist, left.getDistance(),2)/5),-1,1),Util.constrain((Util.slowDown(setDist, -right.getDistance(), 2)/5),-1,1));
        //System.out.println(right.getDistance()+"   "+Util.seperatedConstrain((Util.slowDown(setDist, left.getDistance(),2)/5),-1.0,1.0,-0.05,0.05,0)+"   "+left.getDistance()+"   "+Util.seperatedConstrain((Util.slowDown(setDist, -right.getDistance(),2)/5),-1.0,1.0,-0.5,0.5,0));
        //System.out.println((Util.seperatedConstrain((Util.slowDown(setDist+(driveError), leftDist,2)/slowDownDist),-1.0,1.0,-0.1,0.1,0)*leftBoost)+"   "+((Util.seperatedConstrain((Util.slowDown(setDist-(driveError), rightDist,2)/slowDownDist),-1.0,1.0,-0.1,0.1,0))*rightBoost));
        if ((Util.abs((oldRightEncoder - rightDist)) > 0.00005) && (rightDist < 0.1)) {
            rightBoost = 1;
        } else {
            rightBoost = 1;
        }
        if ((Util.abs((oldLeftEncoder - leftDist)) > 0.00005) && (leftDist < 0.1)) {
            leftBoost = 1;
        } else {
            leftBoost = 1;
        }
        //System.out.println(leftDist+"   "+(rightDist));
        //drive.tankDrive((Util.constrain((Util.slowDown(setDist+(driveError/5), leftDist,2)/slowDownDist),-1.0,1.0)*leftBoost),((Util.constrain((Util.slowDown(setDist-(driveError/5), rightDist,2)/slowDownDist),-1.0,1.0))*rightBoost));
        drive.tankDrive(Util.seperatedConstrain((Util.slowDown(setDist + (driveError / 2), leftDist, 2) / 2), -0.65, 0.65, -0.1, 0.1, 0), Util.seperatedConstrain((Util.slowDown(setDist - (driveError / 5), -right.getDistance(), 2) / 5), -0.65, 0.65, -0.1, 0.1, 0));
        oldRightEncoder = rightDist;
        oldLeftEncoder = leftDist;

    }

    public void turnRightWheels(RobotDrive drive, double setDist, Encoder right, double oldRightEncoder) {
        double rightDist = -right.getDistance();
        double driveError = rightDist - setDist;
        double slowDownDist = 10;

        double rightBoost = 1;
        //System.out.println((Util.absDif(oldRightEncoder, rightDist))+"  "+rightDist+"   "+ oldRightEncoder);
        if ((Util.absDif(oldRightEncoder, rightDist) < 0.0005) && (driveError < 3)) {
            rightBoost = 5;
            //System.out.println("Boosting");
        } else {
            rightBoost = 1;
        }
        //System.out.println((rightDist));
        drive.tankDrive(0, Util.seperatedConstrain((Util.slowDown(setDist - (driveError), -right.getDistance(), 2) / slowDownDist) * rightBoost, -1, 1, -0.2, 0.2, 0));
        //System.out.println(Util.constrain((Util.slowDown(setDist-(driveError), -right.getDistance(), 2)/slowDownDist),-0.8,0.8));
        //drive.tankDrive(Util.constrain((Util.slowDown(setDist+(driveError/5), left.getDistance(),2)/5),-1,1), Util.constrain((Util.slowDown(setDist-(driveError/5), -right.getDistance(), 2)/5),-1,1));



    }

    public void turnLeftWheels(RobotDrive drive, double setDist, Encoder left, double oldLeftEncoder) {

        double leftDist = left.getDistance();
        double driveError = setDist - leftDist;
        double slowDownDist = 10;
        //System.out.println((Util.absDif(oldLeftEncoder, leftDist))+"  "+leftDist+"   "+ oldLeftEncoder);
        double leftBoost = 1;
        if ((Util.abs((oldLeftEncoder - leftDist)) < 0.0005) && (driveError < 3)) {
            leftBoost = 7;
            //System.out.println("Boosting");
        } else {
            leftBoost = 1;
        }
        //System.out.println(Util.slowDown(setDist+(driveError/2), leftDist,2)+"   "+leftDist+"   "+driveError);
        drive.tankDrive(Util.seperatedConstrain((Util.slowDown(setDist + (driveError / 2), leftDist, 2) / slowDownDist) * leftBoost, -1, 1, -0.2, 0.2, 0), 0);
        //drive.tankDrive(Util.constrain((Util.slowDown(setDist+(driveError/5), left.getDistance(),2)/5),-1,1), Util.constrain((Util.slowDown(setDist-(driveError/5), -right.getDistance(), 2)/5),-1,1));
        //oldLeftEncoder = leftDist;
    }

    public void getCypress() {
        try {
            autonomousMode = 2 + Util.rocker(cypress.getAnalogIn(4), 1.0);            // x axis of joystick = 3;
            autonomousMode = 2;
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            throttle = 0;
            System.out.println("Auto Choose error");
        }
        try {
            throttle = Util.smoothDeadZone(Util.map(cypress.getAnalogIn(1), 0, 3.3, 1, -1), -0.15, 0.02, -1, 1, 0);//-0.1 lower band if no work
            //throttle = Util.map(cypress.getAnalogIn(1), 0, 3.3, 1, -1);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            throttle = 0;
            System.out.println("Left Joystick error");
        }
        try {
            turnRate = Util.constrain(Util.smoothDeadZone(Util.map(cypress.getAnalogIn(8), 0.7, 2.6, 1, -1), -0.07, 0.07, -1, 1, 0), -1, 1);


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
            flyWheelSetSpeed = Util.constrain(Util.map(cypress.getAnalogIn(2), 0, 3.2, 1, 0), 0, 1);

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
            fire = cypress.getDigital(10);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            fire = false;
            System.out.println("loadEnable error");
        }

        try {
            firstClimbEnable = !cypress.getDigital(2);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            firstClimbEnable = false;
            System.out.println("ClimbInit Error");
        }
        try {
            secondClimbEnable = !cypress.getDigital(6);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            secondClimbEnable = false;
            System.out.println("ClimbInit Error: Skynet is Not Pleased.");
        }
        try {
            thirdClimbEnable = !cypress.getDigital(4);
        } catch (DriverStationEnhancedIO.EnhancedIOException ex) {
            thirdClimbEnable = false;
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

    private void autoAim() {
        /*normalizedCenterX = dash.getNumber("CenterX", 0) / 320;
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
         oldX = normalizedCenterX;
         }
         drive.tankDrive(leftDriveVal, rightDriveVal);*/
    }
}

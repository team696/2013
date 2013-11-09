package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    Encoder leftEncoder = new Encoder(6, 7);//A on 6, B on 7
    Encoder rightEncoder = new Encoder(8, 9);//A on 8, B on 9
    Gyro gyro1 = new Gyro(2);
    RobotDrive drive = new RobotDrive(8, 4, 1, 3); //motors
    Joystick controller = new Joystick(1);
    
    
    double throttle = 0.0;
    double turn = 0.0;
    double angle = 0.0;
    double fixPos = 0.0;
    boolean reset = false;
    public void robotInit() {
        leftEncoder.start();
        rightEncoder.start();
        rightEncoder.setDistancePerPulse(0.09162978572970230278849376534565);
        leftEncoder.setDistancePerPulse(0.09162978572970230278849376534565);
        leftEncoder.setReverseDirection(true);
        rightEncoder.setReverseDirection(false);      
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        getController();
        System.out.println((int)gyro1.getAngle() +"   "+ (int)(100*(throttle-(gyro1.getAngle()*0.003)))+"  "+(int)(100*(throttle+(gyro1.getAngle()*0.003))));
        fixPos = 0.05-(2*throttle/100);
        drive.tankDrive(-(throttle-(gyro1.getAngle()*fixPos)),-(throttle+(gyro1.getAngle()*fixPos)));
        gyro1.reset();
        if(reset){
            gyro1.reset();
        }
        
    }
    public void getController() {
        throttle = -controller.getRawAxis(2);
        turn = controller.getRawAxis(3)/10;
        angle = Util.constrain(angle+(turn/50),-1,1);
        reset = controller.getRawButton(2);
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    public void straight(double dist, Gyro gyro, Encoder right, Encoder left){
        
    }
    
    
}

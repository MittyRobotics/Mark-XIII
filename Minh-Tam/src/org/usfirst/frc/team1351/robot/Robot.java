
package org.usfirst.frc.team1351.robot;


import edu.wpi.first.wpilibj.SampleRobot;	
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    Joystick leftStick;
    Joystick rightStick;
    CANTalon leftTalon1;
    CANTalon leftTalon2;
    CANTalon rightTalon1;
    CANTalon rightTalon2;

    public Robot() {
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        rightTalon1 = new CANTalon(0);
        rightTalon2 = new CANTalon(1);
        leftTalon1 = new CANTalon(2);
        leftTalon2 = new CANTalon(3);
    	leftTalon1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	leftTalon2.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	rightTalon1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	rightTalon2.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        leftTalon1.setSafetyEnabled(true);
        leftTalon2.setSafetyEnabled(true);
        rightTalon1.setSafetyEnabled(true);
        rightTalon2.setSafetyEnabled(true);
    }
    
    public void robotInit() {
    	
    }
    
    public void move (double forward)
    {
    	double lStick = 0.6;
    	double rStick = 0.6;
    	while (leftTalon1.getEncPosition()<forward*0.6)
    	{
    		leftTalon1.set(lStick);			//move robot forward
    		leftTalon2.set(lStick);	
    		rightTalon1.set(-rStick);		
    		rightTalon2.set(-rStick);
    		Timer.delay(0.01);
    	}    	    	
		Timer.delay(0.05);
		leftTalon1.set(-lStick);			//stops robot
		leftTalon2.set(-lStick);	
		rightTalon1.set(rStick);		
		rightTalon2.set(rStick);
		Timer.delay(0.1);
		leftTalon1.set(0);	       	//stops motor
		leftTalon2.set(0);	
		rightTalon1.set(0);
		rightTalon2.set(0);
    }
    
    public void turn (double angle)
    {
    	double neg;
    	if (angle>0)
    	{
    		neg = -1;
    	} else {
    		neg =1;
    	}
    	double lStick = 0.6;
    	double rStick = 0.6;
    	leftTalon1.set(neg*lStick);			//move robot
		leftTalon2.set(neg*lStick);	
		rightTalon1.set(neg*rStick);		//move robot
		rightTalon2.set(neg*rStick);
		
		Timer.delay(angle/90);
		leftTalon1.set(0);			//move robot
		leftTalon2.set(0);	
		rightTalon1.set(0);		//move robot
		rightTalon2.set(0);	
    }
    public void autonomous() {
    	move (5);
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {

        double deadzone = 0.05;					//creates deadzone

        while (isOperatorControl() && isEnabled()) {
            double lStick = leftStick.getY();
            double rStick = rightStick.getY();
        	if (lStick>-deadzone && lStick<deadzone)
        	{
        		leftTalon1.set(0);				//deadzone
        		leftTalon2.set(0);
        	} else {
        		leftTalon1.set(lStick);			//move robot
        		leftTalon2.set(lStick);
        	}
        	if (rStick>-deadzone && rStick<deadzone)
        	{
        		rightTalon1.set(0);				//deadzone
        		rightTalon2.set(0);
        	} else {
        		rightTalon1.set(-rStick);		//move robot
        		rightTalon2.set(-rStick);
        	}
            Timer.delay(0.005);		// wait for a motor update time
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
}

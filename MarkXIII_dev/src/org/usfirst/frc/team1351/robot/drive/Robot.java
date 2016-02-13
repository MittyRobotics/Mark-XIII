package org.usfirst.frc.team1351.robot.drive;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.hal.CanTalonJNI;

public class Robot extends SampleRobot
{
	// RobotDrive myRobot;
	xboxController xboxController;
	CANTalon leftDrive1;
	CANTalon leftDrive2;
	CANTalon rightDrive1;
	CANTalon rightDrive2;

	public Robot()
	{
		xboxController = new xboxController(0);
		leftDrive1 = new CANTalon(0);
		leftDrive2 = new CANTalon(1);
		rightDrive1 = new CANTalon(2);
		rightDrive2 = new CANTalon(3);
		Timer.delay(0.1);
		
		//inits 
		leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
		leftDrive2.changeControlMode(TalonControlMode.Follower);
		leftDrive2.set(leftDrive1.getDeviceID());
		
		rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		rightDrive2.changeControlMode(TalonControlMode.Follower);
		rightDrive2.set(rightDrive1.getDeviceID());
	}

	
	public void autonomous()
	{
		System.out.println("Please stop, this isn't how this works.");
	}

	void squaredXboxInput()
	{
		double maxSpeed = 0.75;
		double leftDrive1 = xboxController.leftYAxis(); 
		double rightDrive1 = xboxController.rightYAxis(); 
		
		if(leftDrive1 <= rightDrive1 + .1 && leftDrive1 >= rightDrive1 - .1 ) {
			leftDrive1 = (leftDrive1 + rightDrive1) / 2.0; 
		}
		
		leftDrive1 = (Math.abs(leftDrive1) > 0.10) ? leftDrive1 : 0; 
		rightDrive1 = (Math.abs(rightDrive1) > 0.10) ? rightDrive1 : 0; 
		
		
		this.leftDrive1.set(-1 * maxSpeed * leftDrive1);
		this.rightDrive1.set(maxSpeed * rightDrive1);
	}

	public void operatorControl()
	{
		while (isEnabled() && isOperatorControl())
		{
			squaredXboxInput();
		}

		/*
		 * double leftStick = 0; double rightStick = 0; while (isOperatorControl() && isEnabled()) { // Ternary Operators be swaggy //
		 * Essentially, if the absolute value of the input is below 0.05, set the motors to 0, else set properly leftStick =
		 * (Math.abs(xboxController.getRawAxis(1)) > 0.20) ? xboxController.getRawAxis(1) : 0.0; rightStick =
		 * (Math.abs(xboxController.getRawAxis(5)) > 0.20) ? xboxController.getRawAxis(5) : 0.0;
		 * 
		 * // if (leftStick - rightStick <= 0.10) // { // leftStick = (leftStick + rightStick) / 2; // rightStick = leftStick; // } /*
		 * xboxController.setRumble(RumbleType.kLeftRumble, (float) leftStick); xboxController.setRumble(RumbleType.kRightRumble, (float)
		 * rightStick);
		 * 
		 * leftDrive.set(leftStick); // Gets Y-Axis on the left stick rightDrive.set(-1 * rightStick); // Gets Y-Axis on the right stick
		 * 
		 * }
		 */
	}

	public void test()
	{
	}
}
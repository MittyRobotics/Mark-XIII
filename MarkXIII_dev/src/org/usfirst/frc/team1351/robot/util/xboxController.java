package org.usfirst.frc.team1351.robot.util;

import edu.wpi.first.wpilibj.Joystick;

public class xboxController
{
	Joystick xbox;

	public xboxController(int _id)
	{
		xbox = new Joystick(_id);
	}

	double leftXAxis()
	{
		return xbox.getRawAxis(0);
	}

	double leftYAxis()
	{
		return xbox.getRawAxis(1);
	}

	double rightXAxis()
	{
		return xbox.getRawAxis(4);
	}

	double rightYAxis()
	{
		return xbox.getRawAxis(5);
	}

	// Triggers
	double getLeftTrigger()
	{
		return xbox.getRawAxis(2);
	}

	double getRightTrigger()
	{
		return xbox.getRawAxis(3);
	}

	// TODO: Test these, untested and really dunno if they work
	double directionalPadXAxis()
	{
		return xbox.getRawAxis(6);
	}

	double directionalPadYAxis()
	{
		return xbox.getRawAxis(7);
	}

	// Buttons
	boolean getAButton()
	{
		return xbox.getRawButton(0);
	}

	boolean getBButton()
	{
		return xbox.getRawButton(1);
	}

	boolean getXButton()
	{
		return xbox.getRawButton(2);
	}

	boolean getYButton()
	{
		return xbox.getRawButton(3);
	}

	boolean getLeftBumper()
	{
		return xbox.getRawButton(4);
	}

	boolean getRightBumper()
	{
		return xbox.getRawButton(5);
	}

	boolean getBackButton()
	{
		return xbox.getRawButton(6);
	}

	boolean getStartButton()
	{
		return xbox.getRawButton(7);
	}
}

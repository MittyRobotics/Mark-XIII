package org.usfirst.frc.team1351.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

public class XboxController
{
	private Joystick xbox;

	public XboxController(int _id)
	{
		xbox = new Joystick(_id);
		if (!(xbox.getIsXbox()))
		{
			System.out.println("WARNING: joystick is not xbox controller");
		}
		
	}

	public double getLeftX()
	{
		return xbox.getRawAxis(0);
	}

	public double getLeftY()
	{
		return xbox.getRawAxis(1);
	}

	public double getRightX()
	{
		return xbox.getRawAxis(4);
	}

	public double getRightY()
	{
		return xbox.getRawAxis(5);
	}

	// Triggers
	public double getLeftTrigger()
	{
		return xbox.getRawAxis(2);
	}

	public double getRightTrigger()
	{
		return xbox.getRawAxis(3);
	}

	// TODO: Test these, untested and really dunno if they work
	public double getPadX()
	{
		return xbox.getRawAxis(6);
	}

	public double getPadY()
	{
		return xbox.getRawAxis(7);
	}

	// Buttons
	public boolean getButtonA()
	{
		return xbox.getRawButton(1);
	}

	public boolean getButtonB()
	{
		return xbox.getRawButton(2);
	}

	public boolean getButtonX()
	{
		return xbox.getRawButton(3);
	}

	public boolean getButtonY()
	{
		return xbox.getRawButton(4);
	}

	public boolean getLeftBumper()
	{
		return xbox.getRawButton(5);
	}

	public boolean getRightBumper()
	{
		return xbox.getRawButton(6);
	}

	public boolean getBackButton()
	{
		return xbox.getRawButton(7);
	}

	public boolean getStartButton()
	{
		return xbox.getRawButton(8);
	}
	
	/*
	 * Weak vibration motor for the xbox controller
	 */
	public void vibrateLight(float value) {
		xbox.setRumble(RumbleType.kLeftRumble, value);
	}
	
	/*
	 * Strong vibration motor for the xbox controller. 
	 */
	public void vibrateStrong(float value) {
		xbox.setRumble(RumbleType.kRightRumble, value);
	}
	
	/*
	 * Stops both rumble motors, strong and weak 
	 */
	public void stopRumble() {
		xbox.setRumble(RumbleType.kLeftRumble, 0);
		xbox.setRumble(RumbleType.kRightRumble, 0);
	}
}

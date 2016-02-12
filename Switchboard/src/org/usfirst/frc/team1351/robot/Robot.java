package org.usfirst.frc.team1351.robot;

import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot
{
	
	public Robot()
	{

	}

	public void robotInit()
	{
		System.out.println("robotInit() finished");
	}
	
	public void disabled()
	{
		System.out.println("Robot Disabled!");
	}

	public void autonomous()
	{
		while (isAutonomous() && isEnabled())
		{
			
		}
	}

	public void operatorControl()
	{
		System.out.println("Enabling teleop!");
		
		StateMachine.getInstance().start();
		
		while (isOperatorControl() && isEnabled())
		{
			Timer.delay(0.1); // wait for a motor update time
		}

		try
		{
			StateMachine.getInstance().stop();
			StateMachine.getInstance().stateThread.join();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public void test()
	{
		System.out.println("Enabling test!");
		
		while (isTest() && isEnabled())
		{
			
		}
	}
}

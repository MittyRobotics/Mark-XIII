package org.usfirst.frc.team1351.robot.drive;

import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKORuntimeException;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;

public class TKODrive implements Runnable
{
	private static TKODrive m_Instance = null;
	public TKOThread driveThread = null;

	public static synchronized TKODrive getInstance()
	{
		if (TKODrive.m_Instance == null)
		{
			m_Instance = new TKODrive();
			m_Instance.driveThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	protected TKODrive()
	{
	}

	public void start()
	{
		System.out.println("Starting drive task");
		if (!driveThread.isAlive() && m_Instance != null)
		{
			driveThread = new TKOThread(m_Instance);
			driveThread.setPriority(Definitions.getPriority("drive"));
		}
		if (!driveThread.isThreadRunning())
			driveThread.setThreadRunning(true);

		System.out.println("Started drive task");
	}

	public void stop()
	{
		System.out.println("Stopping drive task");
		if (driveThread.isThreadRunning())
			driveThread.setThreadRunning(false);
		System.out.println("Stopped drive task");
	}

	// Tank drive using the two sticks of the xbox controller
	public void squaredDrive()
	{
		double maxSpeed = 0.75;

		try
		{
			double leftY = TKOHardware.getXboxController().getLeftY();
			double rightY = TKOHardware.getXboxController().getRightY();

			if (leftY <= (rightY + .1) && leftY >= (rightY - .1))
			{
				leftY = (leftY + rightY) / 2.0;
			}

			leftY = (Math.abs(leftY) > 0.10) ? leftY : 0;
			rightY = (Math.abs(rightY) > 0.10) ? rightY : 0;

			// TODO What the hell is this?
			TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
			TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);

			setLeftRightMotorOutputsPercentVBus(-1 * maxSpeed * leftY, maxSpeed * rightY);
		}
		catch (TKOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void squaredXbox() {
		try
		{
			//Get the squared inputs from xBox controller  
			double leftMove = Math.pow(TKOHardware.getXboxController().getLeftY(), 2); 
			double rightMove = Math.pow(TKOHardware.getXboxController().getRightY(), 2);  
			//Just gets the sign - positive or negative
			double leftSign = Math.abs(leftMove) / leftMove;
			double rightSign = Math.abs(rightMove) / rightMove; 
			
			//Averages the value so it will move more smoothly hopefully 
			if(leftMove < rightMove + 0.05 && leftMove > rightMove - 0.05) {
				leftMove = (leftMove + rightMove) / 2; 
				rightMove = leftMove; 
			}
			
			leftSign = leftSign * 0.5 * (1.0 - TKOHardware.getXboxController().getLeftTrigger()); 
			rightSign = rightSign * 0.5 * (1.0 - TKOHardware.getXboxController().getRightTrigger()); 
			
			//TODO Really Fix this stuff, this isn't normal.... plz. 
			TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
			TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
			
			setLeftRightMotorOutputsPercentVBus(leftMove * leftSign, rightMove * rightSign);
		}
		catch (TKOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	// Arcade drive using the two sticks of the xbox controller
	public void arcadeDrive()
	{
		boolean squaredInputs = true;
		try
		{
			double moveValue = TKOHardware.getXboxController().getLeftY();
			if (TKOHardware.getXboxController().getRightBumper())
				moveValue = TKOHardware.getXboxController().getLeftY() * 0.6;

			double rotateValue = TKOHardware.getXboxController().getRightX() * 0.8;
			if (TKOHardware.getXboxController().getRightBumper())
				rotateValue = TKOHardware.getXboxController().getRightX() * 0.6;

			if (squaredInputs) // keep sign
			{
				moveValue = Math.abs(moveValue) * moveValue;
				rotateValue = Math.abs(rotateValue) * rotateValue;
			}

			double max = Math.max(moveValue, rotateValue);
			double diff = moveValue - rotateValue;
			double leftMotorSpeed, rightMotorSpeed;

			if (moveValue > 0.0)
			{
				if (rotateValue > 0.0)
				{
					leftMotorSpeed = diff;
					rightMotorSpeed = max;
				}
				else
				{
					leftMotorSpeed = -diff;
					rightMotorSpeed = -max;
				}
			}
			else
			{
				if (rotateValue > 0.0)
				{
					leftMotorSpeed = max;
					rightMotorSpeed = -diff;
				}
				else
				{
					leftMotorSpeed = -max;
					rightMotorSpeed = -diff;
				}
			}
			TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
			TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
			setLeftRightMotorOutputsPercentVBus(leftMotorSpeed, rightMotorSpeed);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void setLeftRightMotorOutputsPercentVBus(double left, double right)
	{
		try
		{
			if (TKOHardware.getLeftDrive().getControlMode() == CANTalon.TalonControlMode.PercentVbus)
				TKOHardware.getLeftDrive().set(Definitions.DRIVE_MULTIPLIER_LEFT * left);
			else
				throw new TKORuntimeException("ERROR: Tried running tank drive when not PercentVbus");
			if (TKOHardware.getRightDrive().getControlMode() == CANTalon.TalonControlMode.PercentVbus)
				TKOHardware.getRightDrive().set(Definitions.DRIVE_MULTIPLIER_RIGHT * right);
			else
				throw new TKORuntimeException("ERROR: Tried running tank drive when not PercentVbus");
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	boolean creepAtomRunning = false;

	public void setCreepAtomRunning(boolean setting)
	{
		creepAtomRunning = setting;
	}

	@Override
	public void run()
	{
		try
		{
			// boolean calibRan = false;
			while (driveThread.isThreadRunning())
			{
				if (!creepAtomRunning)
				{
					// TODO why the hell does squared drive set the mode???? Mebbe fix?
					squaredXbox();
				}
				synchronized (driveThread)
				{
					driveThread.wait(5);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

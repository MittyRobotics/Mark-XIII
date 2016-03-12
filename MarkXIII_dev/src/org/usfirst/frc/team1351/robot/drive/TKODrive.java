package org.usfirst.frc.team1351.robot.drive;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKORuntimeException;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;

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

		init();
	}

	public synchronized void init()
	{
		try
		{
			TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
			TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public void stop()
	{
		System.out.println("Stopping drive task");
		if (driveThread.isThreadRunning())
			driveThread.setThreadRunning(false);
		System.out.println("Stopped drive task");
	}

	public void squaredXbox()
	{
		try
		{
			// Get the squared inputs from xBox controller - actually x^4 
			double leftMove = Math.pow(TKOHardware.getXboxController().getLeftY(), 4);
			double rightMove = Math.pow(TKOHardware.getXboxController().getRightY(), 4);

			// Averages the value so it will move more smoothly hopefully
			if (leftMove < rightMove + 0.05 && leftMove > rightMove - 0.05)
			{
				leftMove = (leftMove + rightMove) / 2;
				rightMove = leftMove;
			}

			// Just gets the sign - positive or negative
			double leftSign = Math.abs(TKOHardware.getXboxController().getLeftY()) / TKOHardware.getXboxController().getLeftY();
			double rightSign = Math.abs(TKOHardware.getXboxController().getRightY()) / TKOHardware.getXboxController().getRightY();
			leftSign = leftSign * (1.0 - (0.5 * TKOHardware.getXboxController().getLeftTrigger()));
			rightSign = rightSign * (1.0 - (0.5 * TKOHardware.getXboxController().getRightTrigger()));

			if(reverse) {
				leftSign *= -1; 
				rightSign *= -1; 
			}
			setLeftRightMotorOutputsPercentVBus(leftMove * leftSign, rightMove * rightSign);
		}
		catch (TKOException e)
		{
			// TODO Auto-generated catch block
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

	private boolean creep = false;
	public void isCreep(boolean b)
	{
		creep = b;
	}

	private boolean reverse = false; 
	@Override
	public void run()
	{
		try
		{
			while (driveThread.isThreadRunning())
			{
				if (!creep)
				{
					squaredXbox();
				}
				synchronized (driveThread)
				{
					driveThread.wait(5);
				}
				if(TKOHardware.getLeftDrive().getOutputCurrent() > Definitions.CURRENT_SAFETY_THRESHOLD || TKOHardware.getRightDrive().getOutputCurrent() > Definitions.CURRENT_SAFETY_THRESHOLD) {
					TKOHardware.getXboxController().vibrateStrong(1.f);
				}
				if(TKOHardware.getXboxController().getButtonA()) 
					reverse = !reverse; 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

package org.usfirst.frc.team1351.robot.drive;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKORuntimeException;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * TODO see why only one side turns, at the very least we need to be able to drive in a straight line
 * 
 */
public class TKODrive implements Runnable
{
	private static TKODrive m_Instance = null;
	public TKOThread driveThread = null;
	private boolean brakeToggle = false;

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
		SmartDashboard.putBoolean("Brake mode enabled: ", brakeToggle);
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
			double leftMove = Math.pow(TKOHardware.getXboxController().getLeftY(), 2);
			double rightMove = Math.pow(TKOHardware.getXboxController().getRightY(), 2);

			// // Averages the value so it will move more smoothly hopefully
			// if (leftMove < rightMove + 0.05 && leftMove > rightMove - 0.05)
			// {
			// leftMove = (leftMove + rightMove) / 2;
			// rightMove = leftMove;
			// }

			// Just gets the sign - positive or negative
			double leftSign = Math.abs(TKOHardware.getXboxController().getLeftY()) / TKOHardware.getXboxController().getLeftY();
			double rightSign = Math.abs(TKOHardware.getXboxController().getRightY()) / TKOHardware.getXboxController().getRightY();
			// leftSign = leftSign * (1.0 - (0.5 * TKOHardware.getXboxController().getLeftTrigger()));
			// rightSign = rightSign * (1.0 - (0.5 * TKOHardware.getXboxController().getRightTrigger()));

			if (reverse)
			{
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

	private void shimmy()
	{
		try
		{
			TKOHardware.getDSolenoid(0).set(Definitions.SHIFTER_HIGH);
			TKOHardware.getLeftDrive().enableBrakeMode(true);
			TKOHardware.getRightDrive().enableBrakeMode(true);
			boolean b = true;
			Timer t = new Timer();
			while (TKOHardware.getXboxController().getButtonA())
			{
				t.start();
				if (b)
				{
					while (t.get() < 0.25)
						setLeftRightMotorOutputsPercentVBus(-.25, .25);
				}
				else
				{
					while (t.get() < 0.25)
						setLeftRightMotorOutputsPercentVBus(.25, -.25);
				}
				b = !b;
				t.stop();
				t.reset();
			}

			TKOHardware.getLeftDrive().set(0.);
			TKOHardware.getRightDrive().set(0.);
			TKOHardware.getLeftDrive().enableBrakeMode(Definitions.DRIVE_BRAKE_MODE[0]);
			TKOHardware.getRightDrive().enableBrakeMode(Definitions.DRIVE_BRAKE_MODE[2]);
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
					squaredXbox();

				if (TKOHardware.getLeftDrive().getOutputCurrent() > Definitions.CURRENT_SAFETY_THRESHOLD
						|| TKOHardware.getRightDrive().getOutputCurrent() > Definitions.CURRENT_SAFETY_THRESHOLD)
				{
					TKOHardware.getXboxController().vibrateStrong(1.f);
				}
				else
				{
					TKOHardware.getXboxController().stopRumble();
				}
				
//				if (TKOHardware.getXboxController().getStartButton())
//					reverse = !reverse; //GOTO Hell
				
				if (TKOHardware.getXboxController().getButtonX())
				{
					brakeToggle = !brakeToggle;
					if (brakeToggle)
					{
						TKOHardware.getLeftDrive().enableBrakeMode(true);
						TKOHardware.getRightDrive().enableBrakeMode(true);
					}
					else if (!brakeToggle)
					{
						TKOHardware.getLeftDrive().enableBrakeMode(false);
						TKOHardware.getRightDrive().enableBrakeMode(false);
					}
				}
				if (TKOHardware.getXboxController().getButtonA())
					shimmy();
//				SmartDashboard.putNumber("Right Drive Current", TKOHardware.getRightDrive().getOutputCurrent());
//				SmartDashboard.putNumber("Right Drive Voltage", TKOHardware.getRightDrive().getOutputVoltage());
//				SmartDashboard.putNumber("Left Drive Current", TKOHardware.getLeftDrive().getOutputCurrent());
//				SmartDashboard.putNumber("Left Drive Voltage", TKOHardware.getLeftDrive().getOutputVoltage());

				
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

package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TKOConveyor implements Runnable
{
	public TKOThread conveyorThread = null;
	private static TKOConveyor m_Instance = null;
	private boolean testEnabled = true;
	double speed = 1400.;
	double incrementer = 200.;
	double rpm = 0.;
	long timeout = 0;

	protected TKOConveyor()
	{
		reset();
		SmartDashboard.putNumber("Speed: ", speed);
		SmartDashboard.putNumber("Incrementer: ", incrementer);
		SmartDashboard.putNumber("Current RPM: ", rpm);
	}

	public static synchronized TKOConveyor getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOConveyor();
			m_Instance.conveyorThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start()
	{
		if (!conveyorThread.isAlive() && m_Instance != null)
		{
			conveyorThread = new TKOThread(m_Instance);
			conveyorThread.setPriority(Definitions.getPriority("conveyor"));
		}
		if (!conveyorThread.isThreadRunning())
		{
			conveyorThread.setThreadRunning(true);
		}
	}

	public void stop()
	{
		if (conveyorThread.isThreadRunning())
		{
			conveyorThread.setThreadRunning(false);
		}
	}

	public synchronized void reset()
	{
		try
		{
			TKOHardware.changeTalonMode(TKOHardware.getConveyorTalon(0), CANTalon.TalonControlMode.PercentVbus);
			TKOHardware.changeTalonMode(TKOHardware.getConveyorTalon(1), CANTalon.TalonControlMode.PercentVbus);
			// TKOHardware.getConveyorTalon(1).set(TKOHardware.getConveyorTalon(0).getDeviceID());
			TKOHardware.changeTalonMode(TKOHardware.getConveyorTalon(2), CANTalon.TalonControlMode.PercentVbus);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void setManual(boolean b)
	{
		testEnabled = b;
	}

	@Override
	public void run()
	{
		try
		{
			while (conveyorThread.isThreadRunning())
			{
//				rollerControl(); 

				if (testEnabled)
				{
					speed = Definitions.REVOLUTIONS_TO_TICKS * SmartDashboard.getNumber("Speed: ");
					incrementer = SmartDashboard.getNumber("Incrementer: ");
					
					if (TKOHardware.getJoystick(2).getRawButton(4) && TKOHardware.getSwitch(0).get())
					{
						TKOHardware.getConveyorTalon(2).set(-0.4);
						timeout = System.currentTimeMillis();
					}
					else if (System.currentTimeMillis() - timeout <= 200)
					{
						System.out.println("Running conveyor backward");
						TKOHardware.getConveyorTalon(2).set(0.4);
					}
					else
					{
						System.out.println("Stopped conveyor");
						TKOHardware.getConveyorTalon(2).set(0.0);
					}
					
//					if (TKOHardware.getJoystick(2).getRawButton(4))
//						startConveyorForward();
//					else if (TKOHardware.getJoystick(2).getRawButton(5))
//						startConveyorBackward();
//					else
//						stopConveyor();

					if (TKOHardware.getJoystick(2).getTrigger() && TKOHardware.getSwitch(0).get())
						TKOShooter.getInstance().spinUp(speed, incrementer);
					else
						TKOShooter.getInstance().spinDown();

					SmartDashboard.putNumber("Current RPM: ", TKOHardware.getFlyTalon(0).getSpeed() * Definitions.TICKS_TO_REVOLUTIONS);
					TKOShooter.getInstance().logShooterData();
				}

				synchronized (conveyorThread)
				{
					conveyorThread.wait(50);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void rollerControl()
	{
		try
		{
			if (TKOHardware.getJoystick(2).getRawButton(4))
			{
				TKOHardware.getConveyorTalon(0).set(-0.5);
				TKOHardware.getConveyorTalon(1).set(0.5);
			}
			else if (TKOHardware.getJoystick(2).getRawButton(5))
			{
				TKOHardware.getConveyorTalon(0).set(0.5);
				TKOHardware.getConveyorTalon(1).set(-0.5);
			}
			else
			{
				TKOHardware.getConveyorTalon(0).set(0.);
				TKOHardware.getConveyorTalon(1).set(0.);
			}
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void startConveyorForward()
	{
		try
		{
			TKOHardware.getConveyorTalon(2).enableControl();
			TKOHardware.getConveyorTalon(2).set(-0.4);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void startConveyorBackward()
	{
		try
		{
			TKOHardware.getConveyorTalon(2).enableControl();
			TKOHardware.getConveyorTalon(2).set(0.4);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void stopConveyor()
	{
		try
		{
			TKOHardware.getConveyorTalon(2).set(0.0);
			TKOHardware.getConveyorTalon(2).disableControl();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}

package org.usfirst.frc.team1351.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TKOGyroThread implements Runnable
{
	public TKOThread gyroThread = null;
	private static TKOGyroThread instance = null;

	private static SPIGyro gyro;

	protected TKOGyroThread()
	{
	}

	public void setGyro(SPIGyro _gyro)
	{
		gyro = _gyro;
	}

	public static synchronized TKOGyroThread getInstance()
	{
		if (instance == null)
		{
			instance = new TKOGyroThread();
			instance.gyroThread = new TKOThread(instance);
		}
		return instance;
	}

	public void start()
	{
		if (!gyroThread.isAlive() && instance != null)
		{
			gyroThread = new TKOThread(instance);
		}
		if (!gyroThread.isThreadRunning())
		{
			gyroThread.setThreadRunning(true);
		}

	}

	public void stop()
	{
		if (gyroThread.isThreadRunning())
		{
			gyroThread.setThreadRunning(false);
		}
	}

	public void run()
	{
		try
		{
			// int counter = 0;
			// gyro.reset();
			// System.out.print("Calibrating...");
			// gyro.calibration();
			while (gyroThread.isThreadRunning())
			{
				//
				// if (counter > 500) { counter = 0; }
				//
				// if(counter == 0) { gyro.calibration(); }
				//
//				System.out.printf("Angle %f \n", gyro.getAngle());
				SmartDashboard.putNumber("SPI Gyro Angle: ", gyro.getAngle());
				// counter++;

			}
		}
		catch (Exception E)
		{
			E.printStackTrace();
		}
	}

}

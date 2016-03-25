package org.usfirst.frc.team1351.robot.util;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SPIGyro extends GyroBase
{
	// private Timer timer;

	private ADXRS450_Gyro gyro;

	// for calibration() and getAngle()
	// private double drift = 0;
	private double prevAngle = 0;
	private double currAngle = 0;
	private double change;
	private double deviation; // also drift

	public SPIGyro()
	{
		gyro = new ADXRS450_Gyro(Definitions.GYRO_SPI_PORT);
		// timer = new Timer();
		gyro.reset();
		gyro.calibrate();
		prevAngle = gyro.getAngle();
	}

	public void reset()
	{
		// SmartDashboard.putBoolean("Gyro Reset: ", true);
		TKOLogger.getInstance().addMessage("SPIGyro Restarting!");
		gyro.reset();
		gyro.calibrate();

	}

	public double getAngle()
	{

		// drift += (Math.abs(change) > 0.01) ? 0 : (change);
		// SmartDashboard.putNumber("Uncorrected Angle", gyro.getAngle());
		return gyro.getAngle() - calibration();
	}

	/*
	 * private double prevAngle = 0; private double currAngle = 0; private double change; private double deviation; //also drift
	 */

	public double calibration()
	{
		currAngle = gyro.getAngle();
		change = currAngle - prevAngle;

		deviation += (Math.abs(change) > 0.01) ? 0 : change;
		prevAngle = gyro.getAngle();

		return deviation;

	}

	public double deviation()
	{
		return deviation;
	}

	public void free()
	{
		gyro.free();
	}

	@Override
	public void calibrate()
	{
		gyro.calibrate();
	}

	@Override
	public double getRate()
	{
		return gyro.getRate();
	}
}

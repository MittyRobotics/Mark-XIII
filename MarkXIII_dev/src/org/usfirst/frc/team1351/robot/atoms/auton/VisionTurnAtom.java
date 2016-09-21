package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.util.SPIGyro;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.vision.TKOVision;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionTurnAtom extends Atom
{
	PIDController pid;
	SPIGyro gyro;
	double angle, incrementer, threshold;
	double p, i, d;

	public VisionTurnAtom()
	{
		threshold = 5;
		incrementer = SmartDashboard.getNumber("Turn Incrementer: ");
		p = SmartDashboard.getNumber("Turn P: ");
		i = SmartDashboard.getNumber("Turn I: ") / 1000.;
		d = SmartDashboard.getNumber("Turn D: ");
		angle = 0.;
	}

	public void init()
	{
		try
		{
			TKOHardware.autonInit(p, i, d);
			TKOHardware.configDriveTalons(p, i, d, TalonControlMode.PercentVbus);
			gyro = TKOHardware.getGyro();
			gyro.reset();
			pid = new PIDController(p, i, d, gyro, TKOHardware.getLeftDrive());
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
		pid.reset();
		pid.setOutputRange(-1, 1);
		pid.setContinuous();
		pid.setAbsoluteTolerance(1);

		TKOVision.getInstance().start();
	}

	@Override
	public void execute()
	{
		System.out.println("Executing vision turn atom");

		angle = TKOVision.getInstance().turnAngle();

		try
		{
			pid.enable();
			if (angle >= 0)
			{
				while (DriverStation.getInstance().isEnabled() && pid.getSetpoint() < angle)
				{
					pid.setSetpoint(pid.getSetpoint() + incrementer);
					TKOHardware.getRightDrive().set(TKOHardware.getLeftDrive().get());
					System.out.println("LEFT GET: " + TKOHardware.getLeftDrive().get() + "\t RIGHT GET: "
							+ TKOHardware.getRightDrive().get() + "\t Setpoint: " + pid.getSetpoint());
					TKOLogger.getInstance().addMessage(
							"LEFT GET: " + TKOHardware.getLeftDrive().get() + "\t RIGHT GET: " + TKOHardware.getRightDrive().get()
									+ "\t Setpoint: " + pid.getSetpoint());
					Timer.delay(0.001);
				}
			}
			else if (angle < 0)
			{
				while (DriverStation.getInstance().isEnabled() && pid.getSetpoint() > angle)
				{
					pid.setSetpoint(pid.getSetpoint() - incrementer);
					TKOHardware.getRightDrive().set(TKOHardware.getLeftDrive().get());
					System.out.println("LEFT GET: " + TKOHardware.getLeftDrive().get() + "\t RIGHT GET: "
							+ TKOHardware.getRightDrive().get() + "\t Setpoint: " + pid.getSetpoint());
					TKOLogger.getInstance().addMessage(
							"LEFT GET: " + TKOHardware.getLeftDrive().get() + "\t RIGHT GET: " + TKOHardware.getRightDrive().get()
									+ "\t Setpoint: " + pid.getSetpoint());
					Timer.delay(0.001);
				}
			}

			pid.setSetpoint(angle);

			Timer t = new Timer();
			t.reset();
			t.start();
			while (DriverStation.getInstance().isEnabled())
			{
				if (Math.abs(pid.getError()) > threshold)
				{
					t.reset();
					pid.setSetpoint(angle);
				}
				if (t.get() > .25)
				{
					System.out.println("Timeout in Gyro turn atom");
					break;
				}
				TKOHardware.getRightDrive().changeControlMode(TalonControlMode.Follower);
				TKOHardware.getRightDrive().set(TKOHardware.getLeftDrive().getDeviceID());
				System.out.println("Target Angle: " + pid.getSetpoint() + "\t PID Error: " + pid.getError() + "\t Current angle: "
						+ gyro.getAngle());
				TKOLogger.getInstance().addMessage(
						"Target Angle: " + pid.getSetpoint() + "\t PID Error: " + pid.getError() + "\t Current angle: " + gyro.getAngle());
				Timer.delay(0.001);
			}
			t.stop();
			TKOHardware.getLeftDrive().set(0);
			TKOHardware.getRightDrive().set(0);
			Timer.delay(0.1);
		}
		catch (TKOException e1)
		{
			e1.printStackTrace();
		}
		pid.disable();
		System.out.println("Done executing vision turn atom");

		TKOVision.getInstance().stop();
	}
}

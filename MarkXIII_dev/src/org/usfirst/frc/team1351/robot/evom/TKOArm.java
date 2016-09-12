package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.drive.TKODrive;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TKOArm
{
	private static TKOArm m_Instance = null;

	double driveSetpoint = 0;
	double p, i, d, distance, incrementer, threshold;
	private boolean doneBreaching = true;

	public TKOArm()
	{

	}

	public static synchronized TKOArm getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOArm();
		}
		return m_Instance;
	}

	//getting across the portcullis
	public void breachPortcullis()
	{
		//only do this if breach is false
		if (doneBreaching == false)
			return;
		doneBreaching = false;
		TKODrive.getInstance().isCreep(true);
		//move the arm down
		moveArmDown();
		//creep is happening = moving slowly
		TKOCreep();
		//arm goes up
		moveArmUp();
		//creep stops
		TKODrive.getInstance().isCreep(false);
		TKODrive.getInstance().init();
		//breaching is done
		doneBreaching = true;
	}

	//going across cheval de frise
	public void breachCheval()
	{
		//only run if breaching is false
		if (doneBreaching == false)
			return;
		doneBreaching = false;
		TKODrive.getInstance().isCreep(true);
		//arm up
		moveArmUp();
		//move in a creep
		TKOCreep();
		//arm down
		moveArmDown();
		//creep again
		TKOCreep();
		//arm up
		moveArmUp();
		//stop creeping
		TKODrive.getInstance().isCreep(false);
		TKODrive.getInstance().init();
		doneBreaching = true;
	}
	
	public synchronized void moveArmUp()
	{
		try
		{
			TKOHardware.getDSolenoid(1).set(Value.kReverse);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void moveArmDown()
	{
		try
		{
			TKOHardware.getDSolenoid(1).set(Value.kForward);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void TKOCreep()
	{
		// 3.183 revs needed (~40")
		// TODO Test this, ensure it works and robot doesn't get stuck

		p = SmartDashboard.getNumber("Drive P: ");
		i = SmartDashboard.getNumber("Drive I: ");
		d = SmartDashboard.getNumber("Drive D: ");
		
		System.out.println("Starting Creep Stuff");
		try
		{
			// arms on the back
			distance = TKOHardware.getLeftDrive().getPosition() - 795.75; // This helps account for the current position on the encoders
			// TODO determine whether or not to zero the encoders before doing this - would it affect anything? Would make this easier
			incrementer = Definitions.DRIVE_ATOM_INCREMENTER;
			threshold = 75; // we can be within approx. half an inch
			TKOHardware.autonInit(p, i, d);
			
			//while driver station is enabled and left drive is greater than the distance
			while (DriverStation.getInstance().isEnabled() && TKOHardware.getLeftDrive().getSetpoint() > distance)
			{
				//make the left drive setpoint subtracted by incrementer
				TKOHardware.getLeftDrive().set(TKOHardware.getLeftDrive().getSetpoint() - incrementer);
				//make right drive setpoint subtracted by incrementer
				TKOHardware.getRightDrive().set(TKOHardware.getRightDrive().getSetpoint() - incrementer);

				//get left and right encoder positions
				System.out.println("Encoder Left: " + TKOHardware.getLeftDrive().getPosition() + "\t Encoder Right: "
						+ TKOHardware.getRightDrive().getPosition() + "\t Left Setpoint: " + TKOHardware.getLeftDrive().getSetpoint());
				TKOLogger.getInstance().addMessage(
						"Encoder Left: " + TKOHardware.getLeftDrive().getPosition() + "\t Encoder Right: "
								+ TKOHardware.getRightDrive().getPosition() + "\t Left Setpoint: "
								+ TKOHardware.getLeftDrive().getSetpoint());
				Timer.delay(0.001);
			}
			TKOHardware.getLeftDrive().set(distance);
			TKOHardware.getRightDrive().set(distance);
			while (Math.abs(TKOHardware.getLeftDrive().getPosition()) < (Math.abs(distance) - threshold))
			{
				Timer.delay(0.001);
			}
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}

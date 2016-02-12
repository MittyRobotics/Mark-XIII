package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.drive.TKODrive;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;
import org.usfirst.frc.team1351.robot.util.ThreadExample;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TKOArm
{
	private TKOArm m_Instance = null;
	
	double driveSetpoint = 0;
	double p, i, d, distance, incrementer, threshold;
	
	public TKOArm()
	{

	}

	public synchronized TKOArm getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOArm();
		}
		return m_Instance;
	}
	
	public void breachPortcullis()
	{
		armMove(false); //Moves up, then creeps and moves back down 
		TKOCreep();
		armMove(true);
		TKODrive.getInstance().init();
	}

	public void breachCheval()
	{
		armMove(false); //Arm goes up, creeps forward, arm drops, and robot creeps forwards to have it stand on the drawbridge 
		TKOCreep();
		armMove(true);
		TKOCreep();
		TKODrive.getInstance().init();
	}
	
	public synchronized void armMove(boolean armUp)
	//If armUp is true, moves up. Else, moves down 
	{
		try
		{
			//TODO Add in some kind of check using the camera to throw hard and soft errors... Kinda important lol 
			if (armUp == false)
			{
				TKOHardware.getDSolenoid(1).set(Value.kReverse);
			}
			else
			{
				TKOHardware.getDSolenoid(1).set(Value.kForward);
			}
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
		TKODrive.getInstance().setCreepAtomRunning(true);

		p = SmartDashboard.getNumber("Drive P: ");
		i = SmartDashboard.getNumber("Drive I: ");
		d = SmartDashboard.getNumber("Drive D: ");
		// arms on the back
		distance = TKOHardware.getLeftDrive().getPosition() - 795.75; //This helps account for the current position on the encoders
		//TODO determine whether or not to zero the encoders before doing this - would it affect anything? Would make this easier 
		incrementer = Definitions.DRIVE_ATOM_INCREMENTER;
		threshold = 75; // we can be within approx. half an inch
		TKOHardware.autonInit(p, i, d);
		System.out.println("Starting Creep Stuff");
		try
		{
			while (DriverStation.getInstance().isEnabled() && TKOHardware.getLeftDrive().getSetpoint() > distance)
			{
				TKOHardware.getLeftDrive().set(TKOHardware.getLeftDrive().getSetpoint() - incrementer);
				TKOHardware.getRightDrive().set(TKOHardware.getRightDrive().getSetpoint() - incrementer);

				System.out.println("Encoder Left: " + TKOHardware.getLeftDrive().getPosition() + "\t Encoder Right: "
						+ TKOHardware.getRightDrive().getPosition() + "\t Left Setpoint: " + TKOHardware.getLeftDrive().getSetpoint());
				TKOLogger.getInstance().addMessage("Encoder Left: " + TKOHardware.getLeftDrive().getPosition() + "\t Encoder Right: "
						+ TKOHardware.getRightDrive().getPosition() + "\t Left Setpoint: " + TKOHardware.getLeftDrive().getSetpoint());
				Timer.delay(0.001);
			}
			TKOHardware.getLeftDrive().set(distance); 
			TKOHardware.getRightDrive().set(distance); 
			while(Math.abs(TKOHardware.getLeftDrive().getPosition()) < (Math.abs(distance) - threshold) ) {
				Timer.delay(0.001);
			}
			
			TKODrive.getInstance().setCreepAtomRunning(false);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
	
}

// Last edited by Ben Kim
// on 01/17/2015

package org.usfirst.frc.team1351.robot;

import org.usfirst.frc.team1351.robot.atoms.Molecule;
import org.usfirst.frc.team1351.robot.atoms.auton.DriveAtom;
import org.usfirst.frc.team1351.robot.atoms.auton.GyroTurnAtom;
import org.usfirst.frc.team1351.robot.drive.TKODrive;
import org.usfirst.frc.team1351.robot.evom.TKOPneumatics;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOLEDArduino;
import org.usfirst.frc.team1351.robot.util.TKOTalonSafety;
import org.usfirst.frc.team1351.robot.evom.TKOShooter;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Questions:
 * low goal?
 * idling wheel?
 * sensors for conveyor?
 */

/** Projects:
 * router working - Parks
 * state machine - Ben, Ishan
 * lift molecule - Tiina, Aditi
 * climbing molecule - Peri, Louis
 */

/** TODO
 * get new router working
 * button to reverse drive controls
 * spin rollers while climbing
 * brake mode: wheels can't spin while climbing
 * auto climb
 * need to (auto) drive while lifting portcullis
 * 
 * intake 2 rollers
 * one roller for conveyor, one roller for pre-shoot
 */

public class Robot extends SampleRobot
{
	SendableChooser autonChooser;

	public Robot()
	{
		// don't put stuff here, use robotInit();
	}

	public void robotInit()
	{
		System.out.println("-----WELCOME TO MarkXIII 2016-----");
		System.out.println("-----SYSTEM BOOT: " + Timer.getFPGATimestamp() + "-----");
		TKOHardware.initObjects();

		autonChooser = new SendableChooser();
		autonChooser.addDefault("Drive", new Integer(0));
		autonChooser.addObject("Drive, Turn", new Integer(1));
		
//		try
//		{
//			SmartDashboard.putBoolean("Top switch", TKOHardware.getLiftTop());
//			SmartDashboard.putBoolean("Bottom switch", TKOHardware.getLiftBottom());
//		} catch (TKOException e)
//		{
//			e.printStackTrace();
//		}

		System.out.println("robotInit() finished");
	}

	public void disabled()
	{
		System.out.println("ROBOT DISABLED!");
	}

	public void autonomous()
	{
		System.out.println("Enabling autonomous!");

		TKOLogger.getInstance().start();
		// TKODataReporting.getInstance().start();
		// TKOTalonSafety.getInstance().start();
		// TKOLEDArduino.getInstance().start();
		TKOPneumatics.getInstance().start();
		// TKOPneumatics.getInstance().reset(); //TODO
		
		Molecule molecule = new Molecule();
		molecule.clear();
		
		double distance = SmartDashboard.getNumber("Drive distance: ");
		double angle = SmartDashboard.getNumber("Turn angle: ");
		
		if (autonChooser.getSelected().equals(0))
		{
			molecule.add(new DriveAtom(distance));
		}
		else if (autonChooser.getSelected().equals(1))
		{
			molecule.add(new DriveAtom(distance));
			molecule.add(new GyroTurnAtom(angle));
		}
		else
		{
			System.out.println("Molecule empty why this");
		}

		System.out.println("Running molecule");
		molecule.initAndRun();
		System.out.println("Finished running molecule");

		try
		{
			TKOPneumatics.getInstance().stop();
			TKOPneumatics.getInstance().pneuThread.join();
			// TKODataReporting.getInstance().stop();
			// TKODataReporting.getInstance().dataReportThread.join();
			TKOLogger.getInstance().stop();
			TKOLogger.getInstance().loggerThread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void operatorControl()
	{
		System.out.println("Enabling teleop!");
		TKOLogger.getInstance().start();
		TKODrive.getInstance().start();
		TKOPneumatics.getInstance().start();
		// TKODataReporting.getInstance().start();
		TKOTalonSafety.getInstance().start();
		TKOLEDArduino.getInstance().start();
		
		while (isOperatorControl() && isEnabled())
		{
			try
			{
				TKOHardware.arduinoWrite(1);
//				SmartDashboard.putNumber("CRATE DISTANCE: ", TKOHardware.getCrateDistance());
//				SmartDashboard.putBoolean("Top switch", TKOHardware.getLiftTop());
//				SmartDashboard.putBoolean("Bottom switch", TKOHardware.getLiftBottom());
			} catch (TKOException e)
			{
				e.printStackTrace();
			}
			Timer.delay(0.1); // wait for a motor update time
		}

		try
		{
			TKOLEDArduino.getInstance().stop();
			TKOLEDArduino.getInstance().ledArduinoThread.join();
			TKOTalonSafety.getInstance().stop();
			TKOTalonSafety.getInstance().safetyCheckerThread.join();
			// TKODataReporting.getInstance().stop();
			// TKODataReporting.getInstance().dataReportThread.join();
			TKOPneumatics.getInstance().stop();
			TKOPneumatics.getInstance().pneuThread.join();
			TKODrive.getInstance().stop();
			TKODrive.getInstance().driveThread.join();
			TKOLogger.getInstance().stop();
			TKOLogger.getInstance().loggerThread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void test()
	{
		System.out.println("Enabling test!");
		
		while (isEnabled() && isTest())
		{
			
		}
	}
}
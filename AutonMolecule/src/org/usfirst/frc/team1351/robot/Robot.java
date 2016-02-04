
package org.usfirst.frc.team1351.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;

import org.usfirst.frc.team1351.robot.DriveAtom;
import org.usfirst.frc.team1351.robot.GyroTurnAtom;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {

    SendableChooser autonChooser;

    public Robot() {

    }
    
    public void robotInit() {
        autonChooser = new SendableChooser();
        autonChooser.addDefault("Don't pick this one", new Integer(0));
        autonChooser.addObject("LowBar", new Integer(1));
        autonChooser.addObject("Position 2", new Integer(2));
        autonChooser.addObject("Position 3", new Integer(3));
        autonChooser.addObject("Position 4", new Integer(4));
        autonChooser.addObject("Position 5", new Integer(5));
    }

    public void autonomous() {
    	
    	String autoSelected = (String) autonChooser.getSelected();
//		String autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    	
    	switch(autoSelected) {
    	default:
            Molecule molecule = new Molecule();
            molecule.clear();
            if (autonChooser.getSelected().equals(0))
    		{
    			System.out.println("You weren't supposed to pick this one :/");
    		}
    		else if (autonChooser.getSelected().equals(1))
    		{
                molecule.add(new DriveAtom(108.44));
                molecule.add(new GyroTurnAtom(60));
                molecule.add(new DriveAtom(139.37));
    		}
    		else if (autonChooser.getSelected().equals(2))
    		{
    			molecule.add(new DriveAtom(137.66));
    			molecule.add(new GyroTurnAtom(60));
    			molecule.add(new DriveAtom(81.7));
    		}
    		else if(autonChooser.getSelected().equals(3))
    		{
    			molecule.add(new DriveAtom(48));
    			molecule.add(new GyroTurnAtom(90));
    			molecule.add(new DriveAtom(4.5));
    			molecule.add(new GyroTurnAtom(-90));
    			molecule.add(new DriveAtom(117.5));
    		}
    		else if(autonChooser.getSelected().equals(4))
    		{
    			molecule.add(new DriveAtom(48));
    			molecule.add(new GyroTurnAtom(-90));
    			molecule.add(new DriveAtom(5.5));
    			molecule.add(new GyroTurnAtom(90));
    			molecule.add(new DriveAtom(117.5));
    		}
    		else if(autonChooser.getSelected().equals(5))
    		{
    			molecule.add(new DriveAtom(48));
    			molecule.add(new GyroTurnAtom(90));
    			molecule.add(new DriveAtom(12));
    			molecule.add(new GyroTurnAtom(-90));
    			molecule.add(new DriveAtom(86.5));
    			molecule.add(new GyroTurnAtom(60));
    			molecule.add(new DriveAtom(115));
    		}
    		else
    		{
    			System.out.println("The molecule is empty :/");
    		}
            molecule.initAndRun();
            break;
    	}
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
//        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
//            myRobot.arcadeDrive(stick); // drive with arcade style (use right stick)
            Timer.delay(0.005);		// wait for a motor update time
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
}


package org.usfirst.frc.team1351.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1351.robot.DriveAtom;
import org.usfirst.frc.team1351.robot.GyroTurnAtom;

import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {

    SendableChooser autonChooser;

    public Robot() {

    }
    
    public void robotInit() {
        autonChooser = new SendableChooser();
        autonChooser.addDefault("Low Bar", new Integer(0));
        autonChooser.addObject("Position 2", new Integer(1));
        autonChooser.addObject("Position 3", new Integer(2));
        autonChooser.addObject("Position 4", new Integer(3));
        autonChooser.addObject("Position 5", new Integer(4));
    }

    public void autonomous() {
    	
    	String autoSelected = (String) autonChooser.getSelected();
//		String autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    	
    	switch(autoSelected) {
    	default:
            Molecule molecule = new Molecule(); // creates a new molecule
            molecule.clear(); // verifies that the molecule is empty
            int autonSelection = (int) SmartDashboard.getNumber("Choose a position");
            autonChooser.equals(autonSelection);
    		if (autonChooser.getSelected().equals(0)) // instructions for getting from the low bar to the left batter
    		{
                molecule.add(new DriveAtom(108.44));
                molecule.add(new GyroTurnAtom(60));
                molecule.add(new DriveAtom(139.37));
    		}
    		else if (autonChooser.getSelected().equals(1)) // instructions for getting from position #2 to the left batter
    		{
    			molecule.add(new DriveAtom(137.66));
    			molecule.add(new GyroTurnAtom(60));
    			molecule.add(new DriveAtom(81.7));
    		}
    		else if(autonChooser.getSelected().equals(2)) // instructions for getting from position #3 to the middle batter
    		{
    			molecule.add(new DriveAtom(48));
    			molecule.add(new GyroTurnAtom(90));
    			molecule.add(new DriveAtom(4.5));
    			molecule.add(new GyroTurnAtom(-90));
    			molecule.add(new DriveAtom(117.5));
    		}
    		else if(autonChooser.getSelected().equals(3)) // instructions for getting from position #4 to the middle batter
    		{
    			molecule.add(new DriveAtom(48));
    			molecule.add(new GyroTurnAtom(-90));
    			molecule.add(new DriveAtom(5.5));
    			molecule.add(new GyroTurnAtom(90));
    			molecule.add(new DriveAtom(117.5));
    		}
    		else if(autonChooser.getSelected().equals(4)) // instructions for getting from position #5 to the right batter
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
            molecule.initAndRun(); // initializes and runs the molecule
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

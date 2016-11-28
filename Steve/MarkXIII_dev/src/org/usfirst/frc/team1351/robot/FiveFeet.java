//Wheel0 is CANTalon 0
//Wheel1 is CANTalon 1
//Wheel2 is CANTalon 2
//Wheel3 is CANTalon 3
//joystick0 is the left joystick
//joystick1 is the right joystick
//it is simply tank drive
//I am running the motors at a percentage power
//Motors run at the same value as the joystick
//The joystick goes from -1 to 1 and so does percentage so the motors are directly the same


package org.usfirst.frc.team1351.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class FiveFeet extends SampleRobot {
	public CANTalon Wheel0,Wheel1,Wheel2,Wheel3;
	Joystick joystick0;
    public double degPerFt = 171.888;
    public double deg90 = 40;

    //Defining everything
    //deadzone is the plus/minus value to create a deadzone of 5% for the joystick
	
    public FiveFeet() {
    	Wheel0 = new CANTalon(0);
        Wheel1 = new CANTalon(1);
        Wheel2 = new CANTalon(2);
        Wheel3 = new CANTalon(3);
        Wheel0.changeControlMode(TalonControlMode.PercentVbus);
        Wheel1.changeControlMode(TalonControlMode.PercentVbus);
        Wheel2.changeControlMode(TalonControlMode.PercentVbus);
        Wheel3.changeControlMode(TalonControlMode.PercentVbus);
    }
    
    public void robotInit() {
    	
    }
    
    public void autonomous() {
    	while (isEnabled()) {
    		Wheel0.setSafetyEnabled(true);
        	Wheel1.setSafetyEnabled(true);
        	Wheel2.setSafetyEnabled(true);
        	Wheel3.setSafetyEnabled(true);
        	Wheel2.setEncPosition(0);
        	Wheel0.set(-0.1);
    		Wheel1.set(-0.1);
    		Wheel2.set(0.1);
    		Wheel3.set(0.1);
    		while(Wheel2.getEncPosition() < degPerFt * 5){ 
    			
    		}
//    		Wheel0.set(0);
    		Wheel1.set(0);
    		Wheel2.set(0);
    		Wheel3.set(0);
    		Wheel2.setEncPosition(0);
    		Wheel0.set(0.1);
    		Wheel1.set(0.1);
    		Wheel2.set(0.1);
    		Wheel3.set(0.1);
    		while(Wheel2.getEncPosition() < deg90){ 

    		}
    		Wheel0.set(0);
    		Wheel1.set(0);
    		
    		Wheel2.set(0);
    		Wheel3.set(0);
    		Wheel2.setEncPosition(0);
        	Wheel0.set(-0.1);
    		Wheel1.set(-0.1);
    		Wheel2.set(0.1);
    		Wheel3.set(0.1);
    		while(Wheel2.getEncPosition() < degPerFt * 5){ 
    		
    		}
    		Wheel0.set(0);
    		Wheel1.set(0);
    		Wheel2.set(0);
    		Wheel3.set(0);
    	}
    	
    }

    public void operatorControl() {
    	
    }
}

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

public class Robot extends SampleRobot {
	public CANTalon Wheel0,Wheel1,Wheel2,Wheel3,Crate0,Crate1;
	Joystick joystick0, joystick1;
	static double deadzone = 0.05;
    double y0 = 0;
    double y1 = 0;
    public double currentLevel = 0;
    public double stepLength = 10;

    //Defining everything
    //deadzone is the plus/minus value to create a deadzone of 5% for the joystick
	
    public Robot() {
    	Wheel0 = new CANTalon(0);
        Wheel1 = new CANTalon(1);
        Wheel2 = new CANTalon(2);
        Wheel3 = new CANTalon(3);
        Crate0 = new CANTalon(4);
        Crate1 = new CANTalon(5);
        joystick0 = new Joystick(0);
        joystick1 = new Joystick(1);
        Wheel0.changeControlMode(TalonControlMode.PercentVbus);
        Wheel1.changeControlMode(TalonControlMode.PercentVbus);
        Wheel2.changeControlMode(TalonControlMode.PercentVbus);
        Wheel3.changeControlMode(TalonControlMode.PercentVbus);
        Crate0.changeControlMode(TalonControlMode.PercentVbus);
        Crate1.changeControlMode(TalonControlMode.PercentVbus);
    }
    
    public void robotInit() {
    	
    }
    
    public void autonomous() {
    	
    }

    public void operatorControl() {
    	Wheel0.setSafetyEnabled(true);
    	Wheel1.setSafetyEnabled(true);
    	Wheel2.setSafetyEnabled(true);
    	Wheel3.setSafetyEnabled(true);
    	Crate0.setSafetyEnabled(true);
    	Crate1.setSafetyEnabled(true);
    	
    	//turns on safety for motors
    	while (isOperatorControl() && isEnabled()) {
    		if (joystick0.getY() >-deadzone&&joystick0.getY() <deadzone){
        		y0 = 0;
        	}
    		else {
    			y0 = joystick0.getY();
    	    	Wheel2.set(y0);
    	    	Wheel3.set(y0);
    	    	//sets the motors to run at the percent that the left joystick is pushed forward
    		}
    		
    		if (joystick1.getY() >-deadzone&&joystick1.getY() <deadzone){
    			y1 = 0;
    		} 
    		else {
    	    	y1 = joystick1.getY();
        		Wheel0.set(-y1);
        		Wheel1.set(-y1);
        		//sets the motors to run at the percent that the right joystick is pushed forward
    		}
    		if (joystick0.getRawButton(1)) {
    			if(currentLevel == 5){
        			System.out.println("Out of bounds");
        		}
    			else if(currentLevel < 5) {
        			Crate0.set(0.1);
        			Crate1.set(0.1);
        			//turns on crate motors
        			while(Wheel1.getEncPosition() < (currentLevel + 1) * stepLength){ //Check
        				if (joystick0.getRawButton(3)) {
        					break;
        					//safety force quit
        				}
        			}
        			Crate0.set(0);
        			Crate1.set(0);
        			currentLevel++;
        			Timer.delay(1);
        			//delay between checking joystick
        		}
    		} 
    		else if (joystick0.getRawButton(2)) {
    			if(currentLevel == 0){
    				System.out.println("Out of bounds");
    			}
    			else if(currentLevel > 0) {
    				Crate0.set(-0.1);
    				Crate1.set(-0.1);
    				while(Wheel1.getEncPosition() > (currentLevel - 1) * stepLength){ //Check
    					if (joystick0.getRawButton(3)) {
    						break;
    					}
    				}
    				//moves the motors for picking up crates
    				Crate0.set(0);
    				Crate1.set(0);
    				currentLevel--;
    				Timer.delay(1);
    				//delay
    			}
    		}
    		Timer.delay(0.005);
    		//waits for a little bit between reading the joysticks and changing the motor power
    		
    	}
    	
    		
    	
    	
    }

}

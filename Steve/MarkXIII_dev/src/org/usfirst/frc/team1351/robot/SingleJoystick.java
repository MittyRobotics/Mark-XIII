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

public class SingleJoystick extends SampleRobot {
	public CANTalon Wheel0,Wheel1,Wheel2,Wheel3,Crate0,Crate1;
	Joystick joystick0, joystick1;
	static double deadzone = 0.05;
    double y0 = joystick0.getY();
    double xl = 0;
    double xr = 0;
    double powConstant = 0.25;

    //Defining everything
    //deadzone is the plus/minus value to create a deadzone of 5% for the joystick
	
    public SingleJoystick() {
    	Wheel0 = new CANTalon(0);
        Wheel1 = new CANTalon(1);
        Wheel2 = new CANTalon(2);
        Wheel3 = new CANTalon(3);
        joystick0 = new Joystick(0);
        Wheel0.changeControlMode(TalonControlMode.PercentVbus);
        Wheel1.changeControlMode(TalonControlMode.PercentVbus);
        Wheel2.changeControlMode(TalonControlMode.PercentVbus);
        Wheel3.changeControlMode(TalonControlMode.PercentVbus);
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
    	//sets the motors to run at the percent that the left joystick is pushed forward
		if (joystick0.getRawButton(0)) {
			
			if (joystick0.getY() <= 0){
				y0 = ((joystick0.getY() * powConstant) / 2) + powConstant;
				if (joystick0.getX() <= 0){
					xl = (-joystick0.getX() * powConstant)/2 ;
				}
				if (joystick0.getX() >=0) {
					xr = (joystick0.getX() * powConstant)/2 ;
				}
			}
			if (joystick0.getY() >= 0){
				y0 = (joystick0.getY() * powConstant + powConstant);
				if (joystick0.getX() <= 0){
					xl = -joystick0.getX() * powConstant * 2;
				}
				if (joystick0.getX() >=0) {
					xr = joystick0.getX() * powConstant * 2;
				}
			}
			
			Wheel0.set(y0 + 1);
		} else if (joystick0.getRawButton(1)) {
			if (joystick0.getY() <= 0){
				y0 = -((joystick0.getY() * powConstant) / 2) + powConstant;
				if (joystick0.getX() >= 0){
					xl = (-joystick0.getX() * powConstant)/2 ;
				}
				if (joystick0.getX() <=0) {
					xr = (joystick0.getX() * powConstant)/2 ;
				}
			}
			if (joystick0.getY() >= 0){
				y0 = -(joystick0.getY() * powConstant + powConstant);
				if (joystick0.getX() >= 0){
					xl = -joystick0.getX() * powConstant * 2;
				}
				if (joystick0.getX() <=0) {
					xr = joystick0.getX() * powConstant * 2;
				}
			}
			
		} else {
    			
    		Wheel0.set(0);
    		Wheel1.set(0);
    	    Wheel2.set(0);
    	    Wheel3.set(0);
    	    //sets the motors to run at the percent that the left joystick is pushed forward
    		
		}
    	Wheel2.set(y0+xr);
    	Wheel3.set(y0+xr);
    	Wheel0.set(-y0-xl);
		Wheel1.set(-y0-xl);
        Timer.delay(.05);
    }

}

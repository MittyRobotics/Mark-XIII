package org.usfirst.frc.team1351.robot.evom;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;

public class TKOFlywheel implements Runnable {
	CANTalon flyTalon1 = new CANTalon(1), flyTalon2 = new CANTalon(2);
	Joystick stick1 = new Joystick(1), stick2 = new Joystick(2);
	
	//dummy numbers. fix when we know what the ports are going to be
	Encoder encoder = new Encoder(1, 2, false, CounterBase.EncodingType.k4X);
	//PID
	float p = 0;
	float i = 0;
	float d = 0;
	PIDController controller = new PIDController(p, i, d, encoder, flyTalon1);
	double PIDsetpoint = 0;
	double PIDendpoint = 9000;
	Timer timer = new Timer();
	
	//assume that speed based on RPM
	CANTalon.TalonControlMode PercentVbus; 
	
	//90 revs per tick (50 milliseconds) for 5 seconds
	// 5 setpoints (1 every sec)
	public double increaseSpeed(){
		timer.start();
		
		if (PIDsetpoint < PIDendpoint){
			for(double i = PIDsetpoint; i <= PIDendpoint; i += 90){
				PIDsetpoint = i;
				flyTalon1.set(PIDsetpoint);
				flyTalon2.set(flyTalon1.get());
			}
		}
		double volts = flyTalon1.getOutputVoltage();
		controller.disable();
		flyTalon1.set(volts);
		flyTalon2.set(volts);
		
		timer.stop();
		return timer.get();
	}
	
	//Increases the speed of the talon to ~9000 RPM(maybe)
	/*public void increaseSpeed() {
		flyTalon1.set(9000);
		
	}*/
	
}
package org.usfirst.frc.team1351.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot
{
	CANTalon shooterTalonEnc;
	CANTalon shooterTalon;
	CANTalon conveyorTalon;
	CANTalon driveTalon[] = new CANTalon[4];

	DoubleSolenoid armPiston;
	DoubleSolenoid intakePiston;
	DoubleSolenoid gearboxPiston;

	DigitalInput ballSwitch;
	DigitalInput intakeSwitch;
	DigitalInput armSwitch;

	Compressor compressor;
	PowerDistributionPanel pdp;
	Joystick joystick;
	Joystick joy2; 
	XboxController xbox;
	

	double rpmMax = 0.0;
	double PIDsetpoint = 0.0;
	double speed = 7250.0;
	double incrementer = 200.0;

	long lastShift;
	int logSetting = 0;
	
	long buttonActivated = 0; 

	public Robot()
	{

	}

	public void robotInit()
	{
		SmartDashboard.putNumber("Shooter P: ", 0.250);
		SmartDashboard.putNumber("Shooter I: ", 0.);
		SmartDashboard.putNumber("Shooter D: ", 0.);

		joy2 = new Joystick(3); 
		shooterTalonEnc = new CANTalon(4);
		shooterTalonEnc.changeControlMode(TalonControlMode.Speed);
		shooterTalonEnc.setP(SmartDashboard.getNumber("Shooter P: "));
		shooterTalonEnc.setI(SmartDashboard.getNumber("Shooter I: "));
		shooterTalonEnc.setD(SmartDashboard.getNumber("Shooter D: "));
		shooterTalonEnc.enableControl();

		shooterTalon = new CANTalon(5);
		shooterTalon.changeControlMode(TalonControlMode.Follower);
		shooterTalon.set(shooterTalonEnc.getDeviceID());

		conveyorTalon = new CANTalon(8);
		conveyorTalon.changeControlMode(TalonControlMode.PercentVbus);

		driveTalon[0] = new CANTalon(0);
		driveTalon[0].changeControlMode(TalonControlMode.PercentVbus);
		driveTalon[1] = new CANTalon(1);
		driveTalon[1].changeControlMode(TalonControlMode.Follower);
		driveTalon[1].set(driveTalon[0].getDeviceID());
		driveTalon[2] = new CANTalon(2);
		driveTalon[2].changeControlMode(TalonControlMode.PercentVbus);
		driveTalon[3] = new CANTalon(3);
		driveTalon[3].changeControlMode(TalonControlMode.Follower);
		driveTalon[3].set(driveTalon[2].getDeviceID());
		for (int i = 0; i < 4; i++)
		{
			driveTalon[i].enableBrakeMode(false);
		}

		pdp = new PowerDistributionPanel();
		xbox = new XboxController(0);
		joystick = new Joystick(1);

		armPiston = new DoubleSolenoid(2, 3);
		intakePiston = new DoubleSolenoid(4, 5);
		gearboxPiston = new DoubleSolenoid(0, 1);

		ballSwitch = new DigitalInput(0);
		intakeSwitch = new DigitalInput(1);
		armSwitch = new DigitalInput(2);

		compressor = new Compressor(0);

		SmartDashboard.putNumber("Speed: ", speed);
		SmartDashboard.putNumber("Incrementer: ", incrementer);
		SmartDashboard.putNumber("Log setting: ", logSetting);

		lastShift = System.currentTimeMillis();
		buttonActivated = System.currentTimeMillis(); 
	}

	public void disabled()
	{
		compressor.stop();
	}

	public void autonomous()
	{

	}

	void log()
	{
		if (true) //TODO add log settings variable back 
		{
			TKOLogger.getInstance().addMessage("%8.2f\t%8.2f\t%8.2f\t%8.2f\t%8.2f%8.2f\t%8.2f",
					DriverStation.getInstance().getBatteryVoltage(), shooterTalonEnc.getSpeed(),
					(1024 * shooterTalonEnc.getSpeed() / 6000), shooterTalonEnc.getOutputCurrent(), shooterTalon.getOutputCurrent(),
					shooterTalonEnc.getOutputVoltage(), shooterTalon.getOutputVoltage());
		}
	}

	void incrementerSpinUP(double speedTarget, double inc)
	{
		double upperError = speedTarget * 1.01;
		double lowerError = speedTarget * 0.99;
		if (PIDsetpoint < lowerError)
		{
			PIDsetpoint += inc;
		}
		else if (PIDsetpoint > upperError)
		{
			PIDsetpoint -= inc;
		} else if(PIDsetpoint > lowerError && PIDsetpoint < upperError) {
			PIDsetpoint = speedTarget; 
		}
		shooterTalonEnc.set(PIDsetpoint);
		SmartDashboard.putNumber("PID Shooter Setpoint", PIDsetpoint * (1024. / 6000.));
		
	}
	
	long lastThingy = 0; 
	void conveyor() {
//		if(ballSwitch.get() && xbox.getRightTrigger() > 0.5) {
//			conveyorTalon.set(0.2);
//			buttonActivated = System.currentTimeMillis(); 
//		}
//		else if(System.currentTimeMillis() - 500. < buttonActivated){
//			conveyorTalon.set(.1); 
//		}
//		if (joy2.getRawButton(5))
//			conveyorTalon.set(-.3);
//		else if (joy2.getRawButton(4))
//			conveyorTalon.set(.3);
//		else
//			conveyorTalon.set(0.);
		
		if(joy2.getTrigger() && ballSwitch.get()) {
			conveyorTalon.set(0.3); 
			lastThingy = System.currentTimeMillis(); 
		}
		else if(System.currentTimeMillis() - lastThingy <= 200) {
			conveyorTalon.set(-0.2);
		} else if(joy2.getRawButton(4)) {
			conveyorTalon.set(0.3);
		}
		else if(joy2.getRawButton(5)) {
			conveyorTalon.set(-0.90); 
		}
		else {
			conveyorTalon.set(0.); 
		}
	}

	void xboxDrive()
	{
		double leftMove = Math.pow(xbox.getRightY(), 2);
		double rightMove = Math.pow(xbox.getLeftY(), 2);

		// Averages the value so it will move more smoothly hopefully
//		if (leftMove < rightMove + 0.05 && leftMove > rightMove - 0.05)
//		{
//			leftMove = (leftMove + rightMove) / 2;
//			rightMove = leftMove;
//		}

		// Just gets the sign - positive or negative
		double leftSign = Math.abs(xbox.getRightY()) / xbox.getRightY();
		double rightSign = Math.abs(xbox.getLeftY()) / xbox.getLeftY();
//		leftSign = leftSign * (1.0 - (0.5 * xbox.getLeftTrigger()));
//		rightSign = rightSign * (1.0 - (0.5 * xbox.getRightTrigger()));

		driveTalon[0].set(leftMove * leftSign);
		driveTalon[2].set(-1. * rightMove * rightSign);
	}

	void pistonControl()
	{
		if (joystick.getRawButton(2))
		{
			if (System.currentTimeMillis() - lastShift > 250)
			{
				Value currVal = intakePiston.get();
				Value newVal = currVal;
				if (currVal == Value.kForward)
					newVal = Value.kReverse;
				else if (currVal == Value.kReverse)
					newVal = Value.kForward;
				intakePiston.set(newVal);
				lastShift = System.currentTimeMillis();
			}
		}

		if (joystick.getRawButton(3))
		{
			if (System.currentTimeMillis() - lastShift > 250)
			{
				Value currVal = armPiston.get();
				Value newVal = currVal;
				if (currVal == Value.kForward)
					newVal = Value.kReverse;
				else if (currVal == Value.kReverse)
					newVal = Value.kForward;
				armPiston.set(newVal);
				lastShift = System.currentTimeMillis();
			}
		}
		if (xbox.getRightBumper())
		{
			if (System.currentTimeMillis() - lastShift > 250)
			{
				Value currVal = gearboxPiston.get();
				Value newVal = currVal;
				if (currVal == Value.kForward)
					newVal = Value.kReverse;
				else if (currVal == Value.kReverse)
					newVal = Value.kForward;
				gearboxPiston.set(newVal);
				lastShift = System.currentTimeMillis();
			}
		}
	}

	public void operatorControl()
	{
		TKOLogger.getInstance().start();
//		compressor.start();
		rpmMax = 0.;
		shooterTalon.clearIAccum();
		shooterTalonEnc.clearIAccum();
		PIDsetpoint = 0.; 
		
		gearboxPiston.set(Value.kForward);
		intakePiston.set(Value.kForward);
		armPiston.set(Value.kForward);

		while (isOperatorControl() && isEnabled())
		{
			SmartDashboard.putBoolean("Ball Switch" , ballSwitch.get());
			
			xboxDrive();
			pistonControl();
			conveyor();

			if(xbox.getButtonY()) {
				for(int i = 0; i < driveTalon.length; i++) {
					driveTalon[i].enableBrakeMode(true);
				}
			} else {
				for(int i = 0; i < driveTalon.length; i++) {
					driveTalon[i].enableBrakeMode(false);
				}
			}
			
			logSetting = (int) SmartDashboard.getNumber("Log setting: ");
			shooterTalonEnc.setP(SmartDashboard.getNumber("Shooter P: "));
			shooterTalonEnc.setI(SmartDashboard.getNumber("Shooter I: "));
			shooterTalonEnc.setD(SmartDashboard.getNumber("Shooter D: "));

			speed = (6000. / 1024.) * SmartDashboard.getNumber("Speed: ");
			incrementer = SmartDashboard.getNumber("Incrementer: ");
			if(joystick.getTrigger()) {
				shooterTalonEnc.enableControl();
				incrementerSpinUP(speed, incrementer);
			}
			else {
				shooterTalonEnc.disableControl();
			}

			SmartDashboard.putNumber("Current RPM: ", (shooterTalonEnc.getSpeed() * (1024. / 6000.)));
			SmartDashboard.putNumber("Max RPM: ", rpmMax);
			if ((shooterTalonEnc.getSpeed() * (1024. / 6000.)) > rpmMax)
				rpmMax = shooterTalonEnc.getSpeed() * (1024. / 6000.);

			log();
			SmartDashboard.putNumber("Multiplier: ", ((speed * (1024. / 6000.)) / rpmMax));
			Timer.delay(0.01);
		}

		try
		{
			TKOLogger.getInstance().stop();
			TKOLogger.getInstance().loggerThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void test()
	{

	}
}

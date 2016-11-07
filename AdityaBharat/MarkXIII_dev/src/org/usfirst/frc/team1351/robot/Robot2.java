
package org.usfirst.frc.team1351.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public 
class Robot2 extends SampleRobot {
	RobotDrive myRobot;
	CANTalon c0, c1, c2, c3; // declaring variables for cantalon
	Joystick stick; // declaring variables for joystick

	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	SendableChooser chooser;
	double x1; // declaring variables for the x values
	double y1; // declaring variables for the y values
	double constant = 0.02;

	public Robot2() {
		myRobot = new RobotDrive(0, 1);
		myRobot.setExpiration(0.1);
		stick = new Joystick(0);

		//initializing c0 and c1, which are the left cantalons
		c0 = new CANTalon(0);
		c1 = new CANTalon(1);
		// initializing c2 and c3, which are the right cantalons
		c2 = new CANTalon(2);
		c3 = new CANTalon(3);

		c0.changeControlMode(TalonControlMode.PercentVbus);
		c1.changeControlMode(TalonControlMode.PercentVbus);
		c2.changeControlMode(TalonControlMode.PercentVbus);
		c3.changeControlMode(TalonControlMode.PercentVbus);

		
	}
	public void robotInit() {
		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto modes", chooser);
	}
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the if-else structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
	public void autonomous() {
		String autoSelected = (String) chooser.getSelected();
		//		String autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);

		switch(autoSelected) {
		case customAuto:
			myRobot.setSafetyEnabled(false);
			myRobot.drive(-0.5, 1.0);	// spin at half speed
			Timer.delay(2.0);		//    for 2 seconds
			myRobot.drive(0.0, 0.0);	// stop robot
			break;
		case defaultAuto:
		default:

			myRobot.setSafetyEnabled(false);
			myRobot.drive(-0.5, 0.0);	// drive forwards half speed
			Timer.delay(2.0);		//    for 2 seconds
			myRobot.drive(0.0, 0.0);	// stop robot
			break;
		}
	}
	/**
	 * Runs the motors with tank steering.
	 */
	public void operatorControl() {
		/*myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
            myRobot.arcadeDrive(stick); // drive with arcade style (use right stick)
            Timer.delay(0.005);		// wait for a motor update time
        }*/   
		while (isOperatorControl() && isEnabled()) {
			x1 = stick.getX(); // initializes the x values
			y1 = stick.getY(); // initializes the y values 

			if(stick.getRawButton(1)){
				if(stick.getX()<0.05 && -0.05<stick.getX()){
					x1 = 0; // sets deadzone for x-values
					c0.set(constant);
					c1.set(constant);
					c2.set(constant);
					c3.set(constant);
				}
				if(stick.getY()<0.05 && -0.05<stick.getY()){
					y1 = 0; // sets deadzone for y-values
					c0.set(constant);
					c1.set(constant);
					c2.set(constant);
					c3.set(constant);
				}		

				else if(stick.getX()>0 && stick.getY()>0){
					c0.set(constant*4*x1);
					c1.set(constant*4*x1);
					c2.set(constant*2*y1);
					c3.set(constant*2*y1);

				}
				else if(stick.getX()<0 && stick.getY()<0){
					c0.set(constant*-0.5*x1);
					c1.set(constant*-0.5*x1);
					c2.set(constant*-1*y1);
					c3.set(constant*-1*y1);
				}

				else if(stick.getX()>0 && stick.getY()<0){
					c0.set(constant*1*x1) ;
					c1.set(constant*1*x1);
					c2.set(constant*-0.5*y1);
					c3.set(constant*-0.5*y1);
				}

				else if(stick.getX()<0 && stick.getY()>0){
					c0.set(constant*-2*x1);
					c1.set(constant*-2*x1);
					c2.set(constant*4*y1);
					c3.set(constant*4*y1);
				}
			}
			
						
			
		}

	}
	/**
	 * Runs during test mode
	 */
	public void test() {
	}	
}
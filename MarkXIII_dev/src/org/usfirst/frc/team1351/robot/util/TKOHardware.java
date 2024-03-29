package org.usfirst.frc.team1351.robot.util;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;
import edu.wpi.first.wpilibj.util.AllocationException;

public class TKOHardware
{
	/**
	 * The idea behind TKOHardware is to have one common class with all the objects we would need. The code is highly modular, as seen below
	 * where all the arrays are of variable size.
	 */
	protected static XboxController xbox;
	protected static Joystick joysticks[] = new Joystick[Definitions.NUM_JOYSTICKS];
	protected static CANTalon driveTalons[] = new CANTalon[Definitions.NUM_DRIVE_TALONS];
	protected static CANTalon flyTalons[] = new CANTalon[Definitions.NUM_FLY_TALONS];
	protected static CANTalon conveyorTalons[] = new CANTalon[Definitions.NUM_CONVEYOR_TALONS];
	protected static DoubleSolenoid doubleSolenoids[] = new DoubleSolenoid[Definitions.NUM_DSOLENOIDS];
	protected static Solenoid solenoids[] = new Solenoid[Definitions.NUM_SOLENOIDS];
	protected static DigitalInput limitSwitches[] = new DigitalInput[Definitions.NUM_SWITCHES];
	protected static Compressor compressor;
	protected static ADXRS450_Gyro gyro;
	protected static AnalogOutput arduinoSignal = null;

	public TKOHardware()
	{
		xbox = null;
		for (int i = 1; i < Definitions.NUM_JOYSTICKS; i++)
		{
			joysticks[i] = null;
		}

		// After a follower talon is created, it should not be accessed (exception thrown)
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++)
		{
			driveTalons[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_FLY_TALONS; i++)
		{
			flyTalons[i] = null;
		}
		// for (int i = 0; i < Definitions.NUM_SPIKES; i++)
		// {
		// spikes[i] = null;
		// }
		for (int i = 0; i < Definitions.NUM_CONVEYOR_TALONS; i++)
		{
			conveyorTalons[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_DSOLENOIDS; i++)
		{
			doubleSolenoids[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_SOLENOIDS; i++)
		{
			solenoids[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_SWITCHES; i++)
		{
			limitSwitches[i] = null;
		}
		compressor = null;
		gyro = null;
		arduinoSignal = null;
	}

	public static synchronized void initTesting()
	{
		System.out.println("Initializing objects (testing)");
		
		// TODO maybe destroy objects before initializing them?
		if (xbox == null)
			xbox = new XboxController(Definitions.JOYSTICK_ID[0]);

		if (joysticks[1] == null)
			joysticks[1] = new Joystick(1);
		
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++)
		{
			if (driveTalons[i] == null)
			{
				try
				{
					driveTalons[i] = new CANTalon(Definitions.DRIVE_TALON_ID[i]);
				}
				catch (AllocationException | CANMessageNotFoundException e)
				{
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
		for (int i = 0; i < Definitions.NUM_FLY_TALONS; i++)
		{
			if (flyTalons[i] == null)
			{
				try
				{
					flyTalons[i] = new CANTalon(Definitions.FLY_TALON_ID[i]);
				}
				catch (AllocationException | CANMessageNotFoundException e)
				{
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
		for (int i = 0; i < Definitions.NUM_CONVEYOR_TALONS; i++)
		{
			if (conveyorTalons[i] == null)
			{
				try
				{
					conveyorTalons[i] = new CANTalon(Definitions.CONVEYOR_ID[i]);
				}
				catch (AllocationException | CANMessageNotFoundException e)
				{
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
		if (doubleSolenoids[0] == null) // gearbox
			doubleSolenoids[0] = new DoubleSolenoid(0, 1);
		if (doubleSolenoids[1] == null) // arm
			doubleSolenoids[1] = new DoubleSolenoid(2, 3);
		if (doubleSolenoids[2] == null) // intake
			doubleSolenoids[2] = new DoubleSolenoid(4, 5);

		if (limitSwitches[0] == null) // ball switch
			limitSwitches[0] = new DigitalInput(0);
		if (limitSwitches[1] == null) // intake switch
			limitSwitches[1] = new DigitalInput(1);
		if (limitSwitches[2] == null) // arm switch
			limitSwitches[2] = new DigitalInput(2);

		if (compressor == null)
			compressor = new Compressor(0);

		configDriveTalons(Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D, Definitions.DRIVE_TALONS_NORMAL_CONTROL_MODE);
		configFlyTalons(Definitions.SHOOTER_kP, Definitions.SHOOTER_kI, Definitions.SHOOTER_kD, Definitions.FLY_TALONS_NORMAL_CONTROL_MODE);
		configConveyorTalons(Definitions.CONVEYOR_CONTROL_MODE);
		
		System.out.println("Initialized objects (testing)");
	}

	public static synchronized void initObjects()
	{
		// TODO maybe destroy objects before initializing them?
		if (xbox == null)
			xbox = new XboxController(Definitions.JOYSTICK_ID[0]);

		for (int i = 1; i < Definitions.NUM_JOYSTICKS; i++)
		{
			if (joysticks[i] == null)
				joysticks[i] = new Joystick(Definitions.JOYSTICK_ID[i]);
		}
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++)
		{
			if (driveTalons[i] == null)
			{
				try
				{
					driveTalons[i] = new CANTalon(Definitions.DRIVE_TALON_ID[i]);
				}
				catch (AllocationException | CANMessageNotFoundException e)
				{
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
		for (int i = 0; i < Definitions.NUM_FLY_TALONS; i++)
		{
			if (flyTalons[i] == null)
			{
				try
				{
					flyTalons[i] = new CANTalon(Definitions.FLY_TALON_ID[i]);
				}
				catch (AllocationException | CANMessageNotFoundException e)
				{
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
		// for (int i = 0; i < Definitions.NUM_SPIKES; i++)
		// {
		// if (spikes[i] == null)
		// spikes[i] = new Relay(Definitions.ROLLER_ID[i]);
		// }
		for (int i = 0; i < Definitions.NUM_CONVEYOR_TALONS; i++)
		{
			if (conveyorTalons[i] == null)
			{
				try
				{
					conveyorTalons[i] = new CANTalon(Definitions.CONVEYOR_ID[i]);
				}
				catch (AllocationException | CANMessageNotFoundException e)
				{
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
		if (doubleSolenoids[0] == null)
			doubleSolenoids[0] = new DoubleSolenoid(Definitions.SHIFTER_A, Definitions.SHIFTER_B);
		if (doubleSolenoids[1] == null)
			doubleSolenoids[1] = new DoubleSolenoid(Definitions.FLYWHEEL_A, Definitions.FLYWHEEL_B);
		if (doubleSolenoids[2] == null)
			doubleSolenoids[2] = new DoubleSolenoid(Definitions.INTAKE_A, Definitions.INTAKE_B);
		if (doubleSolenoids[3] == null)
			doubleSolenoids[3] = new DoubleSolenoid(Definitions.D_LIFT_A, Definitions.D_LIFT_B);
		if (solenoids[0] == null)
			solenoids[0] = new Solenoid(Definitions.S_LIFT_A, Definitions.S_LIFT_B);
		if (solenoids[1] == null)
			solenoids[1] = new Solenoid(Definitions.PORTCULLIS_A, Definitions.PORTCULLIS_B);

		// if (limitSwitches[0] == null)
		// limitSwitches[0] = new DigitalInput(Definitions.LIFT_BOTTOM_OPTICAL_SWITCH);

		if (compressor == null)
			compressor = new Compressor(Definitions.PCM_ID);

		if (gyro == null)
		{
			gyro = new ADXRS450_Gyro(Definitions.GYRO_SPI_PORT);
			// gyro.initGyro();
			// gyro.setSensitivity(7. / 1000.);
			gyro.reset();
			System.out.println("Gyro initialized: " + Timer.getFPGATimestamp());
		}

		if (arduinoSignal == null)
			arduinoSignal = new AnalogOutput(0);

		configDriveTalons(Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D, Definitions.DRIVE_TALONS_NORMAL_CONTROL_MODE);
//		configFlyTalons(Definitions.LIFT_P, Definitions.LIFT_I, Definitions.LIFT_D, Definitions.FLY_TALONS_NORMAL_CONTROL_MODE);
		configConveyorTalons(Definitions.CONVEYOR_CONTROL_MODE);
	}

	public static synchronized void configDriveTalons(double p, double I, double d, TalonControlMode mode)
	{
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++)
		{
			driveTalons[i].delete();
			driveTalons[i] = null;
			driveTalons[i] = new CANTalon(Definitions.DRIVE_TALON_ID[i]);
			if (driveTalons[i] != null)
			{
				if (i == 1 || i == 3) // if follower
				{
					driveTalons[i].changeControlMode(CANTalon.TalonControlMode.Follower);
					driveTalons[i].set(i - 1); // set to follow the CANTalon with id i - 1;
				}
				else
				// if not follower
				{
					driveTalons[i].changeControlMode(mode);
					driveTalons[i].setFeedbackDevice(Definitions.DRIVE_ENCODER_TYPE);
					driveTalons[i].setPID(p, I, d);
				}
				driveTalons[i].enableBrakeMode(Definitions.DRIVE_BRAKE_MODE[i]);
				driveTalons[i].reverseOutput(Definitions.DRIVE_REVERSE_OUTPUT_MODE[i]);
				driveTalons[i].reverseSensor(Definitions.DRIVE_REVERSE_SENSOR[i]);
				driveTalons[i].setVoltageRampRate(96.);
			}
		}
	}

	private static synchronized void configFlyTalons(double P, double I, double D, TalonControlMode mode)
	{
		for (int j = 0; j < Definitions.NUM_FLY_TALONS; j++)
		{
			flyTalons[j].delete();
			flyTalons[j] = null;
			flyTalons[j] = new CANTalon(Definitions.FLY_TALON_ID[j]);
			if (flyTalons[j] != null)
			{
				if ((Definitions.NUM_DRIVE_TALONS + j) == 5) // if follower
				{
					flyTalons[j].changeControlMode(CANTalon.TalonControlMode.Follower);
					flyTalons[j].set(Definitions.NUM_DRIVE_TALONS + j - 1); // set to follow the CANTalon with id j - 1
				}
				else
				// if not follower
				{
					flyTalons[j].changeControlMode(mode);
					flyTalons[j].setFeedbackDevice(Definitions.FLY_ENCODER_TYPE);
					flyTalons[j].setPID(P, I, D);
				}
				// liftTalons[i].enableBrakeMode(Definitions.LIFT_BRAKE_MODE[i]);
				// liftTalons[i].reverseOutput(Definitions.LIFT_REVERSE_OUTPUT_MODE[i]);
				flyTalons[j].setExpiration(10000.);
				flyTalons[j].setSafetyEnabled(false);
			}
		}
	}

	private static synchronized void configConveyorTalons(TalonControlMode mode)
	{
		for (int j = 0; j < Definitions.NUM_CONVEYOR_TALONS; j++)
		{
			conveyorTalons[j].delete();
			conveyorTalons[j] = null;
			conveyorTalons[j] = new CANTalon(Definitions.CONVEYOR_ID[j]);

			if (conveyorTalons[j] != null)
			{
				if ((Definitions.NUM_FLY_TALONS + j) == 7) // if follower
				{
					conveyorTalons[j].changeControlMode(CANTalon.TalonControlMode.Follower);
					conveyorTalons[j].set(Definitions.NUM_FLY_TALONS + j - 1); // set to follow the CANTalon with id j - 1
				}
				else
				// if not follower
				{
					// TODO Check, test, and fix this entire function
					conveyorTalons[j].changeControlMode(mode);
					conveyorTalons[j].setFeedbackDevice(Definitions.FLY_ENCODER_TYPE);
					// conveyorTalons[j].setPID(P, I, D);
				}
				// liftTalons[i].enableBrakeMode(Definitions.LIFT_BRAKE_MODE[i]);
				// liftTalons[i].reverseOutput(Definitions.LIFT_REVERSE_OUTPUT_MODE[i]);
				conveyorTalons[j].setExpiration(10000.);
				conveyorTalons[j].setSafetyEnabled(false);
			}
		}
	}

	public static synchronized void changeTalonMode(CANTalon target, CANTalon.TalonControlMode newMode, double newP, double newI,
			double newD) throws TKOException
	{
		if (target == null)
			throw new TKOException("ERROR Attempted to change mode of null CANTalon");
		if (newMode == target.getControlMode())
		{
			target.setPID(newP, newI, newD);
			return;
		}

		// if (target.getControlMode() != CANTalon.TalonControlMode.Position && target.getControlMode() != CANTalon.TalonControlMode.Speed)
		target.setFeedbackDevice(Definitions.DEF_ENCODER_TYPE);

		System.out.println(target.getP());
		System.out.println(target.getI());
		System.out.println(target.getD());

		target.changeControlMode(newMode);
		target.setPID(newP, newI, newD);
		target.enableControl();
		System.out.println("CHANGED TALON [" + target.getDeviceID() + "] TO [" + target.getControlMode().getValue() + "]");
	}

	public static synchronized void changeTalonMode(CANTalon target, CANTalon.TalonControlMode newMode) throws TKOException
	{
		if (target == null)
			throw new TKOException("ERROR Attempted to change mode of null CANTalon");

		// if (target.getControlMode() != CANTalon.TalonControlMode.Position && target.getControlMode() != CANTalon.TalonControlMode.Speed)
		target.setFeedbackDevice(Definitions.DEF_ENCODER_TYPE);

		target.changeControlMode(newMode);
		target.enableControl();

		System.out.println("CHANGED TALON [" + target.getDeviceID() + "] TO [" + target.getControlMode().getValue() + "]");
	}

	public static synchronized void autonInit(double p, double i, double d) throws TKOException
	{
		TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.Position, p, i, d);
		TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.Position, p, i, d);
		TKOHardware.getLeftDrive().reverseOutput(false);
		TKOHardware.getRightDrive().reverseOutput(true);
		TKOHardware.getLeftDrive().reverseSensor(true);
		TKOHardware.getRightDrive().reverseSensor(false);
		TKOHardware.getLeftDrive().enableBrakeMode(true);
		TKOHardware.getRightDrive().enableBrakeMode(true);
		TKOHardware.getLeftDrive().setPosition(0); // resets encoder
		TKOHardware.getRightDrive().setPosition(0);
		TKOHardware.getLeftDrive().ClearIaccum(); // stops bounce
		TKOHardware.getRightDrive().ClearIaccum();
		Timer.delay(0.1);
		TKOHardware.getLeftDrive().set(TKOHardware.getLeftDrive().getPosition());
		TKOHardware.getRightDrive().set(TKOHardware.getRightDrive().getPosition());
		TKOHardware.getDSolenoid(0).set(Definitions.SHIFTER_LOW);
	}

	/**
	 * Sets *ALL* drive Talons to given value. CAUTION WHEN USING THIS METHOD, DOES NOT CARE ABOUT FOLLOWER TALONS. Intended for PID Tuning
	 * loop ONLY.
	 * 
	 * @deprecated Try not to use this method. It is very prone to introducing errors. Use getLeftDrive() and getRightDrive() or
	 *             getDriveTalon(int n) instead, unless you know what you are doing.
	 * @param double setTarget - Value to set for all the talons
	 */
	public static synchronized void setAllDriveTalons(double setTarget)
	{
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++)
		{
			if (driveTalons[i] != null)
			{
				driveTalons[i].set(setTarget);
			}
		}
	}

	public static synchronized void destroyObjects()
	{
		if (xbox != null)
			xbox = null;

		for (int i = 0; i < Definitions.NUM_JOYSTICKS; i++)
		{
			if (joysticks[i] != null)
			{
				joysticks[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++)
		{
			if (driveTalons[i] != null)
			{
				driveTalons[i].delete();
				driveTalons[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_FLY_TALONS; i++)
		{
			if (flyTalons[i] != null)
			{
				flyTalons[i].delete();
				flyTalons[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_CONVEYOR_TALONS; i++)
		{
			if (conveyorTalons[i] != null)
			{
				conveyorTalons[i].delete();
				conveyorTalons[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_DSOLENOIDS; i++)
		{
			if (doubleSolenoids[i] != null)
			{
				doubleSolenoids[i].free();
				doubleSolenoids[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_SOLENOIDS; i++)
		{
			if (solenoids[i] != null)
			{
				solenoids[i].free();
				solenoids[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_SWITCHES; i++)
		{
			if (limitSwitches[i] != null)
			{
				limitSwitches[i].free();
				limitSwitches[i] = null;
			}
		}
		if (compressor != null)
		{
			compressor.free();
			compressor = null;
		}
		if (gyro != null)
		{
			gyro.free();
			gyro = null;
		}
		if (arduinoSignal != null)
		{
			arduinoSignal.free();
			arduinoSignal = null;
		}
	}

	public static synchronized DigitalInput getSwitch(int num) throws TKOException
	{
		if (num >= Definitions.NUM_SWITCHES)
		{
			throw new TKOException("Digital input requested out of bounds");
		}
		if (limitSwitches[num] != null)
		{
			return limitSwitches[num];
		}
		else
			throw new TKOException("Digital input " + (num) + "(array value) is null");
	}

	public static synchronized XboxController getXboxController() throws TKOException
	{
		if (xbox == null)
			throw new TKOException("ERROR: Xbox controller is null");
		return xbox;
	}

	public static synchronized Joystick getJoystick(int num) throws TKOException
	{
		if (num >= Definitions.NUM_JOYSTICKS)
		{
			throw new TKOException("Joystick requested out of bounds");
		}
		if (joysticks[num] != null)
			return joysticks[num];
		else
			throw new TKOException("Joystick " + (num) + "(array value) is null");
	}

	/**
	 * To avoid potential problems, use getLeftDrive() and/or getRightDrive() instead
	 */
	public static synchronized CANTalon getDriveTalon(int num) throws TKOException
	{
		if (num >= Definitions.NUM_DRIVE_TALONS)
		{
			throw new TKOException("Drive talon requested out of bounds");
		}
		if (driveTalons[num] != null)
		{
			if (driveTalons[num].getControlMode() == CANTalon.TalonControlMode.Follower)
				throw new TKOException("WARNING: Do not access follower talon");
			else
				return driveTalons[num];
		}
		else
			throw new TKOException("Drive talon " + (num) + "(array value) is null");
	}

	public static synchronized CANTalon getLeftDrive() throws TKOException
	{
		if (driveTalons[0] == null || driveTalons[1] == null)
			throw new TKOException("Left Drive Talon is null");

		return driveTalons[0];
	}

	public static synchronized CANTalon getRightDrive() throws TKOException
	{
		if (driveTalons[2] == null || driveTalons[3] == null)
			throw new TKOException("Right Drive Talon is null");

		return driveTalons[2];
	}

	public static synchronized CANTalon getFlyTalon() throws TKOException
	{
		if (flyTalons[0] == null || flyTalons[1] == null)
			throw new TKOException("Fly Talon is null");

		return flyTalons[0];
	}

	// WARNING: accessing follower talon not advised
	public static synchronized CANTalon getFlyTalon(int n) throws TKOException
	{
		if (flyTalons[n] == null)
			throw new TKOException("Fly Talon is null");

		return flyTalons[n];
	}

	public static synchronized CANTalon getConveyorTalon(int num) throws TKOException // TODO check and test this - crappily added
	{
		if (conveyorTalons[0] == null || conveyorTalons[1] == null || conveyorTalons[2] == null)
			throw new TKOException("Conveyor Talon is null");

		return conveyorTalons[num];
	}

	public static synchronized DoubleSolenoid getDSolenoid(int num) throws TKOException
	{
		if (num >= Definitions.NUM_DSOLENOIDS)
		{
			throw new TKOException("Piston requested out of bounds");
		}
		if (doubleSolenoids[num] != null)
			return doubleSolenoids[num];
		else
			throw new TKOException("Piston " + (num) + "(array value) is null");
	}

	public static synchronized Solenoid getSolenoid(int num) throws TKOException
	{
		if (num >= Definitions.NUM_SOLENOIDS)
		{
			throw new TKOException("Piston requested out of bounds");
		}
		if (solenoids[num] != null)
			return solenoids[num];
		else
			throw new TKOException("Piston " + (num) + "(array value) is null");
	}

	public static synchronized Compressor getCompressor() throws TKOException
	{
		if (compressor == null)
			throw new TKOException("Compressor is null");
		return compressor;
	}

	public static synchronized ADXRS450_Gyro getGyro() throws TKOException
	{
		if (gyro == null)
			throw new TKOException("ERROR: Gyro is null");
		return gyro;
	}

	public static synchronized double getGyroAngle() throws TKOException
	{
		if (gyro == null)
			throw new TKOException("ERROR: Gyro is null");
		return gyro.getAngle();
	}

	public static synchronized void arduinoWrite(double voltage) throws TKOException
	{
		if (arduinoSignal == null)
			throw new TKOException("ARDUINO ANALOG OUT CHANNEL NULL");
		if (voltage < 0. || voltage > 5.)
			throw new TKOException("VOLTAGE OUT OF BOUNDS");
		arduinoSignal.setVoltage(voltage);
	}
}

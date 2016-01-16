// Last edited by Ben Kim
// on 10/08/2015

package org.usfirst.frc.team1351.robot.util;

import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.main.Definitions;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;
import edu.wpi.first.wpilibj.util.AllocationException;

public class TKOHardware
{	
	// TODO Switch initialization; write getSwitch(int) method
	
	/**
	 * The idea behind TKOHardware is to have one common class with all the objects we would need.
	 * The code is highly modular, as seen below where all the arrays are of variable size.
	 */
	protected static Joystick joysticks[] = new Joystick[Definitions.NUM_JOYSTICKS];
	protected static CANTalon driveTalons[] = new CANTalon[Definitions.NUM_DRIVE_TALONS];
	protected static DoubleSolenoid pistonSolenoids[] = new DoubleSolenoid[Definitions.NUM_PISTONS];
	protected static DigitalInput limitSwitches[] = new DigitalInput[Definitions.NUM_SWITCHES];
	protected static Compressor compressor;
	protected static BuiltInAccelerometer acc;
	protected static AnalogGyro gyro;
	protected static AnalogOutput arduinoSignal = null;
	protected static AnalogInput analog[] = new AnalogInput[Definitions.NUM_ANALOG];

	protected static CANTalon.TalonControlMode talonModes[] = new CANTalon.TalonControlMode[Definitions.ALL_TALONS];

	public TKOHardware()
	{
		for (int i = 0; i < Definitions.NUM_JOYSTICKS; i++)
		{
			joysticks[i] = null;
		}
		
		// After a follower talon is created, it should not be accessed (exception thrown)
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++)
		{
			driveTalons[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_PISTONS; i++)
		{
			pistonSolenoids[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_SWITCHES; i++)
		{
			limitSwitches[i] = null;
		}
		for (int i = 0; i < (Definitions.NUM_DRIVE_TALONS + Definitions.NUM_LIFT_TALONS); i++)
		{
			talonModes[i] = null;
		}
		compressor = null;
		acc = null;
		gyro = null;
		arduinoSignal = null;
		for (int i = 0; i < Definitions.NUM_ANALOG; i++)
		{
			analog[i] = null;
		}
	}

	public static synchronized void initObjects()
	{
		// TODO maybe destroy objects before initializing them?
		for (int i = 0; i < Definitions.NUM_JOYSTICKS; i++)
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
					talonModes[i] = null; // null means not initialized
				}
				catch (AllocationException | CANMessageNotFoundException e)
				{
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
//		if (pistonSolenoids[0] == null)
//			pistonSolenoids[0] = new DoubleSolenoid(Definitions.SHIFTER_A, Definitions.SHIFTER_B);

		//		if (limitSwitches[0] == null)
//			limitSwitches[0] = new DigitalInput(Definitions.LIFT_BOTTOM_OPTICAL_SWITCH);

		if (compressor == null)
			compressor = new Compressor(Definitions.PCM_ID);

		if (acc == null)
			acc = new BuiltInAccelerometer();

		if (gyro == null)
		{
			gyro = new AnalogGyro(Definitions.GYRO_ANALOG_CHANNEL);

			gyro.initGyro();
			gyro.setSensitivity(7. / 1000.);
			gyro.reset();

			System.out.println("Gyro initialized: " + Timer.getFPGATimestamp());

		}
		
		if (arduinoSignal == null)
		{
			arduinoSignal = new AnalogOutput(0);
		}

		configDriveTalons(Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D, Definitions.DRIVE_TALONS_NORMAL_CONTROL_MODE);

		if (analog[0] == null)
			analog[0] = new AnalogInput(Definitions.CRATE_SENSOR_ID);
	}

	public static synchronized void configDriveTalons(double p, double I, double d, TalonControlMode mode)
	{
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++)
		{
			driveTalons[i].delete();
			driveTalons[i] = null;
			driveTalons[i] = new CANTalon(Definitions.DRIVE_TALON_ID[i]);
			talonModes[i] = null;
			if (driveTalons[i] != null)
			{
				if (i == 1 || i == 3) // if follower
				{
					driveTalons[i].changeControlMode(CANTalon.TalonControlMode.Follower);
					driveTalons[i].set(i - 1); // set to follow the CANTalon with id i - 1;
					talonModes[i] = CANTalon.TalonControlMode.Follower;
				}
				else
				// if not follower
				{
					if (!(mode instanceof CANTalon.TalonControlMode))
						throw new TKORuntimeException("CODE ERROR! Wrong control mode used (jag vs talon)");

					driveTalons[i].changeControlMode(mode);
					driveTalons[i].setFeedbackDevice(Definitions.DRIVE_ENCODER_TYPE);
					driveTalons[i].setPID(p, I, d);
					talonModes[i] = mode;
				}
				driveTalons[i].enableBrakeMode(Definitions.DRIVE_BRAKE_MODE[i]);
				driveTalons[i].reverseOutput(Definitions.DRIVE_REVERSE_OUTPUT_MODE[i]);
				driveTalons[i].reverseSensor(Definitions.DRIVE_REVERSE_SENSOR[i]);
				driveTalons[i].setVoltageRampRate(96.);
			}
		}

	}

	public static synchronized void changeTalonMode(CANTalon target, CANTalon.TalonControlMode newMode, double newP, double newI, double newD)
			throws TKOException
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
		talonModes[target.getDeviceID()] = newMode;

		System.out.println("CHANGED TALON [" + target.getDeviceID() + "] TO [" + target.getControlMode().getValue() + "]");
	}
	
	public static synchronized void autonInit(double p, double i, double d)
			throws TKOException
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
		TKOHardware.getPiston(0).set(Definitions.SHIFTER_LOW);
		TKOHardware.getPiston(2).set(Definitions.WHEELIE_RETRACT);
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
		for (int i = 0; i < Definitions.NUM_PISTONS; i++)
		{
			if (pistonSolenoids[i] != null)
			{
				pistonSolenoids[i].free();
				pistonSolenoids[i] = null;
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
		for (int i = 0; i < (Definitions.NUM_DRIVE_TALONS + Definitions.NUM_LIFT_TALONS); i++)
		{
			talonModes[i] = null;
		}
		if (compressor != null)
		{
			compressor.free();
			compressor = null;
		}

		if (acc != null)
			acc = null;

		if (gyro != null)
		{
			gyro.free();
			gyro = null;
		}

		for (int i = 0; i < Definitions.NUM_ANALOG; i++)
		{
			if (analog[i] != null)
			{
				analog[i].free();
				analog[i] = null;
			}
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

	public static synchronized AnalogInput getAnalog(int num) throws TKOException
	{
		if (num >= Definitions.NUM_ANALOG)
		{
			throw new TKOException("Analog input requested out of bounds");
		}
		if (analog[num] != null)
			return analog[num];
		else
			throw new TKOException("Analog input " + (num) + "(array value) is null");
	}

	public static double getCrateDistance() throws TKOException
	{
		// return Definitions.INCHES_PER_VOLT / getAnalog(0).getVoltage();
		// return getAnalog(0).getVoltage();
		// TODO arbitrary value?
		return (-6.066 * Math.log(getAnalog(0).getAverageVoltage()) + 4.6772);
		// y = -6.066ln(x) + 4.6772
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
	
	// Use getLeftDrive() and/or getRightDrive() instead
	public static synchronized CANTalon getDriveTalon(int num) throws TKOException
	{
		if (num >= Definitions.NUM_DRIVE_TALONS)
		{
			throw new TKOException("Drive talon requested out of bounds");
		}
		if (driveTalons[num] != null)
		{
			if (driveTalons[num].getControlMode() == CANTalon.TalonControlMode.Follower)
				throw new TKOException("WARNING CANNOT ACCESS FOLLOWER TALON!");
			else if (talonModes[num] == null)
				throw new TKOException("ERROR TRYING TO ACCESS UNINITIALIZED TALON; MODE UNSET!");
			else
				return driveTalons[num];
		}
		else
			throw new TKOException("Drive talon " + (num) + "(array value) is null");
	}
	
	public static synchronized CANTalon getLeftDrive() throws TKOException
	{
		if (driveTalons[0] == null || driveTalons[1] == null)
			throw new TKOException("NULL LEFT DRIVE TALON");
		if (talonModes[0] == null)
			throw new TKOException("ERROR TRYING TO ACCESS UNINITIALIZED TALON; MODE IS UNSET!");
		if (talonModes[1] != CANTalon.TalonControlMode.Follower)
			throw new TKOException("ERROR LEFT DRIVE FOLLOWER TALON IS NOT UNITIALIZED; MODE IS UNSET!");
		return driveTalons[0];
	}

	public static synchronized CANTalon getRightDrive() throws TKOException
	{
		if (driveTalons[2] == null || driveTalons[3] == null)
			throw new TKOException("NULL LEFT DRIVE TALON");
		if (talonModes[2] == null)
			throw new TKOException("ERROR TRYING TO ACCESS UNINITIALIZED TALON; MODE IS UNSET!");
		if (talonModes[3] != CANTalon.TalonControlMode.Follower)
			throw new TKOException("ERROR RIGHT DRIVE FOLLOWER TALON IS NOT UNITIALIZED; MODE IS UNSET!");
		return driveTalons[2];
	}

	public static synchronized DoubleSolenoid getPiston(int num) throws TKOException
	{
		if (num >= Definitions.NUM_PISTONS)
		{
			throw new TKOException("Piston requested out of bounds");
		}
		if (pistonSolenoids[num] != null)
			return pistonSolenoids[num];
		else
			throw new TKOException("Piston " + (num) + "(array value) is null");
	}

	public static synchronized Compressor getCompressor() throws TKOException
	{
		if (compressor == null)
			throw new TKOException("NULL COMPRESSOR");
		return compressor;
	}

	public static synchronized BuiltInAccelerometer getAcc() throws TKOException
	{
		if (acc == null)
			throw new TKOException("NULL ACCELEROMETER OBJECT");
		return acc;
	}

	/**
	 * IMPORTANT: This method is normally open. This function will return true when the lift is in the bottom, false whenever else. This is
	 * opposite from the actual behavior of the optical limit switch.
	 * 
	 * @return
	 * @throws TKOException
	 */
	public static synchronized boolean getLiftBottom() throws TKOException
	{
		if (limitSwitches[0] == null)
			throw new TKOException("NULL BOTTOM LIMIT SWITCH");
		return !limitSwitches[0].get();
	}

	/**
	 * IMPORTANT: This method is normally open. This function will return true when the lift is in the bottom, false whenever else. This is
	 * opposite from the actual behavior of the optical limit switch.
	 * 
	 * @return
	 * @throws TKOException
	 */
	public static synchronized boolean getLiftTop() throws TKOException
	{
		if (limitSwitches[1] == null)
			throw new TKOException("NULL TOP LIMIT SWITCH");
		return !limitSwitches[1].get();
	}

	public static synchronized AnalogGyro getGyro() throws TKOException
	{
		if (gyro == null)
			throw new TKOException("GYRO NULL");
		return gyro;
	}

	public static synchronized double getGyroAngle() throws TKOException
	{
		if (gyro == null)
			throw new TKOException("GYRO NULL");
		return gyro.getAngle();
	}
	
	public static synchronized void arduinoWrite(double voltage) throws TKOException
	{
		if (arduinoSignal == null)
			throw new TKOException("ARDUINO ANALOGOUT CHANNEL NULL");
		if (voltage < 0. || voltage > 5.)
			throw new TKOException("VOLTAGE OUT OF BOUNDS");
		arduinoSignal.setVoltage(voltage);
	}
}

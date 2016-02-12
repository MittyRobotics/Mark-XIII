// Last edited by Ben Kim
// on 01/17/2015

package org.usfirst.frc.team1351.robot.main;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Definitions
{
	// Autonomous constants

	public static final double TICKS_PER_INCH = 332.5020781;
	public static final double AUTON_DRIVE_P = 0.370; // .5
	public static final double AUTON_DRIVE_I = -0.1; // -0.1
	public static final double AUTON_DRIVE_D = 0.0;
	public static final double AUTON_GYRO_TURN_P = 0.025;
	public static final double AUTON_GYRO_TURN_I = 0.001; // 0.1
	public static final double AUTON_GYRO_TURN_D = 0.1;
	public static final double DRIVE_ATOM_INCREMENTER = 75.;
	public static final double TURN_ATOM_INCREMENTER = 0.35;
	public static final double AUTON_DRIVE_VBUS_MULT = 0.3;

	// Drive constants

	public static final long[] CURRENT_TIMEOUT_LENGTH =
	{ 1000L, 1000L, 1000L, 1000L, 1000L, 1000L };
	public static final int DEF_DATA_REPORTING_THREAD_WAIT = 250;
	public static final double PULSES_PER_INCH = 332.5020781;
	public static final boolean[] DRIVE_BRAKE_MODE =
	{ false, false, false, false };

	public static final CANTalon.FeedbackDevice DRIVE_ENCODER_TYPE = CANTalon.FeedbackDevice.QuadEncoder;
	public static final CANTalon.FeedbackDevice DEF_ENCODER_TYPE = CANTalon.FeedbackDevice.QuadEncoder;
	public static final CANTalon.TalonControlMode DRIVE_TALONS_NORMAL_CONTROL_MODE = CANTalon.TalonControlMode.Current;

	public static final double DRIVE_P = 3.5;
	public static final double DRIVE_I = 0.01;
	public static final double DRIVE_D = 0;
	public static final double[] DRIVE_MULTIPLIER =
	{ -1., -1., 1., 1. };

	public static final double DRIVE_MULTIPLIER_LEFT = DRIVE_MULTIPLIER[0];
	public static final double DRIVE_MULTIPLIER_RIGHT = DRIVE_MULTIPLIER[2];

	public static final boolean[] DRIVE_REVERSE_OUTPUT_MODE =
	{ true, false, false, false };
	public static final boolean[] DRIVE_REVERSE_SENSOR =
	{ false, false, true, false };

	// Hardware constants

	public static final int[] DRIVE_TALON_ID =
	{ 0, 1, 2, 3 };
	public static final int[] FLY_TALON_ID =
	{ 4, 5 };
	public static final int[] CONVEYOR_ID =	// 2 are spikes, 1 is talon?
	{ 6, 7, 8 };
	public static final int[] JOYSTICK_ID =
	{ 0, 1, 2, 3 };

	public static final int NUM_ANALOG = 2;
	public static final int NUM_DRIVE_TALONS = 4;
	public static final int NUM_FLY_TALONS = 2;
//	public static final int NUM_SPIKES = 3;
	public static final int NUM_CONVEYOR_TALONS = 3; 
	public static final int ALL_TALONS = 0; // TODO fix this

	public static final int NUM_ENCODERS = 3;
	public static final int NUM_JOYSTICKS = 4;

	public static final int NUM_DSOLENOIDS = 5;
	public static final int NUM_SOLENOIDS = 2;

	public static final int NUM_SWITCHES = 3; // 5 for state machine + 2 for gripper top/bottom
	public static final int PCM_ID = 0;

	// Evom constants

	public static final int GYRO_ANALOG_CHANNEL = 1;
	public static final double INCHES_PER_VOLT = 1.;
	public static final int LIFT_BOTTOM_OPTICAL_SWITCH = 0;

	public static final boolean[] LIFT_BRAKE_MODE =
	{ false, false };
	public static final double LIFT_CALIBRATION_POWER = .4;
	public static final int LIFT_CONTROL_STICK = 3;

	public static final CANTalon.FeedbackDevice FLY_ENCODER_TYPE = CANTalon.FeedbackDevice.QuadEncoder;
	public static final int LIFT_GRIPPER_SWITCH = 2;

	public static final double SHOOTER_kP = 0.;
	public static final double SHOOTER_kI = 0.;
	public static final double SHOOTER_kD = 0.;
	
	public static final double LIFT_P = 1.1; // 1
	public static final double LIFT_I = 0.02; // 0.005
	public static final double LIFT_D = 0.1; // 0.1
	public static final double LIFT_PID_INCREMENTER = 250.;

	public static final boolean[] INTAKE_REVERSE_OUTPUT_MODE =
	{ false, false };

	public static final boolean[] LIFT_REVERSE_OUTPUT_MODE =
	{ true, false };

	public static final CANTalon.TalonControlMode FLY_TALONS_NORMAL_CONTROL_MODE = CANTalon.TalonControlMode.Position;

	public static final int LIFT_TOP_OPTICAL_SWITCH = 1;
	public static final double MAX_CURRENT_LEFT = 10.; // used for current driving
	public static final double MAX_CURRENT_RIGHT = 10.;

	public static final double[] TALON_CURRENT_TIMEOUT =
	{ 5, 5, 5, 5, 5, 5 };

	// Pneumatics constants

	/**
	 * PISTONS:
	 * [0] - drivetrain
	 * [1] - flywheel
	 * [2] - intake
	 * [3] - lift
	 * [0] - lift
	 * [1] - portcullis
	 */

	public static final DoubleSolenoid.Value SHIFTER_LOW = DoubleSolenoid.Value.kForward;
	public static final DoubleSolenoid.Value SHIFTER_HIGH = DoubleSolenoid.Value.kReverse;

	public static final int SHIFTER_A = 2; // drive train shifting piston
	public static final int SHIFTER_B = 3;
	public static final int FLYWHEEL_A = 0;
	public static final int FLYWHEEL_B = 1;
	public static final int INTAKE_A = 0;
	public static final int INTAKE_B = 1;
	public static final int D_LIFT_A = 0;
	public static final int D_LIFT_B = 1;
	public static final int S_LIFT_A = 0;
	public static final int S_LIFT_B = 1;
	public static final int PORTCULLIS_A = 0;
	public static final int PORTCULLIS_B = 1;

	// Thread definitions

	public static ArrayList<String> threadNames = new ArrayList<String>();
	public static HashMap<String, Integer> threadPriorities;

	public static void addThreadName(String name)
	{
		threadNames.add(name);
	}

	public static int getPriority(String name)
	{
		switch (name)
		{
		case "drive":
			return 2;
		case "pneumatics":
			return 3;
		case "conveyor":
			return 4;
		case "lift":
			return 5;
		case "shooter":
			return 6;
		case "vision":
			return 7;
		case "ledArduino":
			return 8;
		case "logger":
			return 9;
		case "talonSafety":
			return Thread.MAX_PRIORITY; // 10

		default:
			return Thread.NORM_PRIORITY; // 5
		}
	}
}

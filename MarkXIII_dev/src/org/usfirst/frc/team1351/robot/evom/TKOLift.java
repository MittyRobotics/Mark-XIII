package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Molecule;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

// FOR SECOND ROBOT: fix this entire class
public class TKOLift
{
	// These are currently imaginary things that might need to be added to TKOHardware.
	static DigitalInput liftMagSensor1;
	static DigitalInput liftMagSensor2;
	private static TKOLift m_Instance = null;
	public TKOThread liftThread = null;

	// private int sequence = 0;
	// private boolean ranLift = false;

	protected TKOLift()
	{
		// TODO update TKOHardware
		liftMagSensor1 = new DigitalInput(2);
		liftMagSensor2 = new DigitalInput(3);
	}

	public static synchronized TKOLift getInstance()
	{
		if (TKOLift.m_Instance == null)
		{
			m_Instance = new TKOLift();
		}
		return m_Instance;
	}

	//the failed climb method (because we never put on the part to climb)
	public static void TKOClimb()
	{
		// Need 68 in
		// It's fine(?)
		// pistons are extended or not, no in between
		boolean hasGrabbed = false;
		try
		{
			if (liftMagSensor1.get() != true && liftMagSensor2.get() != true && hasGrabbed == false)
			{
				TKOHardware.getDSolenoid(3).set(Value.kReverse);
				TKOHardware.getDSolenoid(0).set(Value.kReverse);
			}
			if (liftMagSensor1.get() == true && liftMagSensor2.get() == true)
			{
				hasGrabbed = true;
				TKOHardware.getDSolenoid(3).set(Value.kForward);
				TKOHardware.getDSolenoid(0).set(Value.kForward);
			}
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}
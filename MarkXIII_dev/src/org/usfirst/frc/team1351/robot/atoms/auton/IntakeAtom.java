package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Timer;

public class IntakeAtom extends Atom
{
	public IntakeAtom()
	{

	}

	@Override
	public void init()
	{
		TKOConveyor.getInstance().start();
	}

	@Override
	public void execute()
	{
		Timer t = new Timer();
		System.out.println("Executing intake atom");
		TKOConveyor.getInstance().startConveyorForward();
		t.reset();
		t.start();
		while (t.get() < 3.0)
		{

		}
		TKOConveyor.getInstance().stopConveyor();
		/*
		 * long timeout = 0; while (TKOHardware.getSwitch(0).get()) { TKOConveyor.getInstance().startConveyorForward(); timeout =
		 * System.currentTimeMillis(); } while (System.currentTimeMillis() - timeout <= 200) {
		 * TKOConveyor.getInstance().startConveyorBackward(); } TKOConveyor.getInstance().stopConveyor();
		 */
		TKOConveyor.getInstance().stop();
		System.out.println("Done executing intake atom");
	}
}

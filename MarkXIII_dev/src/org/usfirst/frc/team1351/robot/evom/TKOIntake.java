package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.Relay;

public class TKOIntake implements Runnable {
	public TKOThread intakeThread = null;
	private static TKOIntake m_Instance = null;

	protected TKOIntake() {
		reset();
	}

	public static synchronized TKOIntake getInstance() {
		if (m_Instance == null) {
			m_Instance = new TKOIntake();
			m_Instance.intakeThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start() {
		if (!intakeThread.isAlive() && m_Instance != null) {
			intakeThread = new TKOThread(m_Instance);
			intakeThread.setPriority(Definitions.getPriority("TKOIntake"));
		}
		if (!intakeThread.isThreadRunning()) {
			intakeThread.setThreadRunning(true);
		}
	}

	public void stop() {
		if (intakeThread.isThreadRunning()) {
			intakeThread.setThreadRunning(false);
		}
	}

	public synchronized void reset() {
		try {
			for (int i = 0; i < Definitions.NUM_SPIKES; i++) {
				TKOHardware.getSpike(i).set(Relay.Value.kOff);
			}
		} catch (TKOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (intakeThread.isThreadRunning()) {
				spikeControl();
				synchronized (intakeThread) {
					intakeThread.wait(100);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void spikeControl() {
		// Joystick 2, button 4 to be reverse and 5 is forward
		// stick 2= TKOHardware.getJoystick.getRawButton();
		// Button 4= Full Backward; //this is a boolean statement
		// Button 5= Full Forward; //this is a boolean statement
		try {
			if (TKOHardware.getJoystick(2).getRawButton(4)) { 
			    TKOHardware.getSpike(0).set(Relay.Value.kReverse);
			    TKOHardware.getSpike(1).set(Relay.Value.kReverse);
			}else if (TKOHardware.getJoystick(2).getRawButton(5)){
				TKOHardware.getSpike(0).set(Relay.Value.kForward);
				TKOHardware.getSpike(1).set(Relay.Value.kForward);
			}else{
				TKOHardware.getSpike(0).set(Relay.Value.kOff); 
				TKOHardware.getSpike(1).set(Relay.Value.kOff); 
			}
		} catch (TKOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

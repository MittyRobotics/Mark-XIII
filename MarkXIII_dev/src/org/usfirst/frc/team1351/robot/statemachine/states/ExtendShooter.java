package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.evom.TKOShooter;
import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

// start spinning flywheel up to speed
public class ExtendShooter implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Extend shooter state");
		
		if (StateMachine.createIntFromBoolArray(data) != 1)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_EXTEND_INTAKE;
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    
	    // TODO reminder to fix TKOShooter...
	    TKOShooter.getInstance().setSpeed(9000, 250, 150);
	    
	    StateMachine.getIntakePiston().set(DoubleSolenoid.Value.kForward);
		
	    int sensors = StateMachine.getSensorData(data);
	    while (sensors != 5 && sensors == 1)
	    {
	    	if (StateMachine.getTimer().get() > StateMachine.PISTON_EXTEND_TIMEOUT)
	    	{
	    		StateMachine.getTimer().stop();
	            StateMachine.getTimer().reset();
	            return StateEnum.STATE_ERROR;
	    	}
	    	Timer.delay(0.1);
	    }
	    
	    StateMachine.getTimer().stop();
	    StateMachine.getTimer().reset();
	    if (sensors != 5)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_READY_TO_FIRE;
	}
}
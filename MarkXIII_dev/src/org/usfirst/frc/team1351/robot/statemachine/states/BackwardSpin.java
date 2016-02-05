package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.Timer;

// rollers controlled independent from state machine

public class BackwardSpin implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{	
		System.out.println("Entering: Backward spin state");
		
		if (StateMachine.createIntFromBoolArray(data) != 6)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_FORWARD_SPIN;
	    int sensors = StateMachine.getSensorData(data);
	    
	    while (sensors != 2 && sensors == 6)
	    {
	    	Timer.delay(0.1);
	    }
	    
	    StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    while (StateMachine.getTimer().get() < StateMachine.LOW_GOAL_TIMEOUT)
	    {
	    	Timer.delay(0.1);
	    }

	    if (sensors != 2)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_RETRACT_INTAKE;
	}
}
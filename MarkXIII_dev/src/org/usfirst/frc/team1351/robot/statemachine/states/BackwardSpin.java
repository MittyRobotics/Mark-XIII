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
		
		if (data.sensorValues != StateMachine.GOT_BALL)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_BACKWARD_SPIN;
	    
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    while (data.sensorValues != StateMachine.INTAKE_EXTENDED &&
	    		data.sensorValues == StateMachine.GOT_BALL &&
	    		StateMachine.getTimer().get() < StateMachine.BALL_SWITCH_TIMEOUT)
	    {
	    	// operator should be spinning rollers right now
	    	Timer.delay(0.1);
	    	data.sensorValues = StateMachine.getSensorData(data);
	    }

	    StateMachine.getTimer().stop();
	    StateMachine.getTimer().reset();
	    if (data.sensorValues != StateMachine.INTAKE_EXTENDED)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_LOW_GOAL_DONE;
	}
}
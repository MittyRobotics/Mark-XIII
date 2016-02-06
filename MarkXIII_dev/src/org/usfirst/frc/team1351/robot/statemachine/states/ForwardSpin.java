package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.Timer;

// rollers controlled independent from state machine

public class ForwardSpin implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{	
		System.out.println("Entering: Forward spin state");
		if (StateMachine.createIntFromBoolArray(data) != 2)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_FORWARD_SPIN;
	    int sensors = StateMachine.getSensorData(data);
	    
	    while (sensors != 3 && sensors == 2)
	    {
	    	if (StateMachine.getJoystick().getRawButton(11)) // override
	    	{
	    		System.out.println("Override: retry chosen by operator");
	    		return StateEnum.STATE_RETRY;
	    	}
	    	Timer.delay(0.1);
	    }

	    if (sensors != 3)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_RETRACT_INTAKE;
	}
}
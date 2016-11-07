package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
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
		if (data.sensorValues != StateMachine.INTAKE_EXTENDED)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_FORWARD_SPIN;
		
		TKOConveyor.getInstance().startConveyorForward();
	    
	    while (data.sensorValues != StateMachine.GOT_BALL &&
	    		data.sensorValues == StateMachine.INTAKE_EXTENDED)
	    {
	    	if (StateMachine.getJoystick().getRawButton(2)) // override
	    	{
	    		TKOConveyor.getInstance().stopConveyor();
	    		System.out.println("Override: retry chosen by operator");
	    		return StateEnum.STATE_RETRY;
	    	}
	    	Timer.delay(0.1);
	    	data.sensorValues = StateMachine.getSensorData(data);
	    }

	    TKOConveyor.getInstance().stopConveyor();
	    
	    if (data.sensorValues != StateMachine.GOT_BALL)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_CHOOSE_GOAL;
	}
}
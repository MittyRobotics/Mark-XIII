package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.Timer;

public class ErrorState implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: ERROR");
		
		/**
		 * This recoverable error state is when the ball falls out of the robot.
		 * If/when this happens, we run the conveyor again until the ball switch is actuated.
		 */
		if (data.curState == StateEnum.STATE_CHOOSE_GOAL || data.curState == StateEnum.STATE_READY_TO_FIRE)
		{
			StateMachine.getTimer().reset();
			StateMachine.getTimer().start();
			TKOConveyor.getInstance().startConveyorForward();
			long timeout = System.currentTimeMillis();
			while (data.sensorValues != StateMachine.GOT_BALL &&
		    		data.sensorValues == StateMachine.INTAKE_EXTENDED &&
		    		StateMachine.getTimer().get() < 3.0)
		    {
		    	Timer.delay(0.1);
		    	data.sensorValues = StateMachine.getSensorData(data);
		    	timeout = System.currentTimeMillis();
		    }
		    while (System.currentTimeMillis() - timeout <= 200)
			{
				TKOConveyor.getInstance().startConveyorBackward();
			}
			TKOConveyor.getInstance().stopConveyor();
			
			if (data.sensorValues != StateMachine.GOT_BALL)
				return StateEnum.STATE_ERROR;
			if (data.curState == StateEnum.STATE_CHOOSE_GOAL)
				return StateEnum.STATE_CHOOSE_GOAL;
			if (data.curState == StateEnum.STATE_READY_TO_FIRE)
				return StateEnum.STATE_READY_TO_FIRE;
		}
		
		/** TODO
		 * This recoverable error is when the intake is not down for whatever reason.
		 * If/when this happens, check if it is safe to extend the intake and/or simply check if the switch is actuated now.
		 */
		
		data.curState = StateEnum.STATE_ERROR;
		return StateEnum.STATE_ERROR;
	}	
}

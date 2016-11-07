package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

public class ErrorState implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: ERROR");
		
		/**
		 * The recoverable error state is when the ball falls out of the robot.
		 * If/when this happens, we run the conveyor again until the ball switch is actuated.
		 */
		if (data.curState == StateEnum.STATE_CHOOSE_GOAL || data.curState == StateEnum.STATE_READY_TO_FIRE)
		{
			StateMachine.getTimer().reset();
			StateMachine.getTimer().start();
			while (data.sensorValues == StateMachine.INTAKE_EXTENDED
					&& data.sensorValues != StateMachine.GOT_BALL
					&& StateMachine.getTimer().get() < 3.0)
			{
				TKOConveyor.getInstance().startConveyorForward();
			}
			TKOConveyor.getInstance().stopConveyor();
			if (data.curState == StateEnum.STATE_CHOOSE_GOAL)
				return StateEnum.STATE_CHOOSE_GOAL;
			if (data.curState == StateEnum.STATE_READY_TO_FIRE)
				return StateEnum.STATE_READY_TO_FIRE;
		}
		
		data.curState = StateEnum.STATE_ERROR;
		return StateEnum.STATE_ERROR;
	}	
}

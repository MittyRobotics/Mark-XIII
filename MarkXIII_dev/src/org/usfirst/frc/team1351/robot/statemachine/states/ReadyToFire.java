package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.evom.TKOShooter;
import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.Timer;

public class ReadyToFire implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Ready to fire");

		data.curState = StateEnum.STATE_READY_TO_FIRE;
		/*if (StateMachine.isPorkyUp()) // switch is actuated, porky is up
		{
			TKOShooter.getInstance().spinUp(StateMachine.porkyUpSpeed, StateMachine.incrementer);
		}
		else if (!StateMachine.isPorkyUp()) // / switch is not actuated, porky is down
		{
			TKOShooter.getInstance().spinUp(StateMachine.porkyDownSpeed, StateMachine.incrementer);
		}*/
		TKOShooter.getInstance().spinUp(StateMachine.porkyDownSpeed, StateMachine.incrementer);
		StateMachine.getInstance().startLogging(true);

		if (data.sensorValues == StateMachine.GOT_BALL)
		{
			try
			{
				while (!(StateMachine.getJoystick().getTrigger() || !TKOHardware.getXboxController().getButtonX()))
				{
					if (data.sensorValues != StateMachine.GOT_BALL)
						return StateEnum.STATE_ERROR;

					Timer.delay(0.1);
					data.sensorValues = StateMachine.getSensorData(data);
				}
			}
			catch (TKOException e)
			{
				e.printStackTrace();
			}
			System.out.println("Exiting ready to fire state");
			return StateEnum.STATE_HIGH_GOAL_DONE;
		}

		return StateEnum.STATE_ERROR;
	}
}
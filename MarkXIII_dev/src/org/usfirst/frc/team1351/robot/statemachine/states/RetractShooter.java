package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.evom.TKOShooter;
import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class RetractShooter implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Retract shooter state");
		
		if (data.sensorValues != StateMachine.SHOOTER_EXTENDED)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_RETRACT_SHOOTER;
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();

	    TKOShooter.getInstance().spinDown();
	    StateMachine.getIntakePiston().set(DoubleSolenoid.Value.kReverse);
		
	    while (data.sensorValues != StateMachine.BALL_IN &&
	    		data.sensorValues == StateMachine.SHOOTER_EXTENDED)
	    {
	    	if (StateMachine.getTimer().get() > StateMachine.PISTON_RETRACT_TIMEOUT)
	    	{
	    		StateMachine.getTimer().stop();
	            StateMachine.getTimer().reset();
	            return StateEnum.STATE_ERROR;
	    	}
	    	Timer.delay(0.1);
	    	data.sensorValues = StateMachine.getSensorData(data);
	    }
	    
	    StateMachine.getTimer().stop();
	    StateMachine.getTimer().reset();
	    if (data.sensorValues != StateMachine.BALL_IN)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_CHOOSE_GOAL;
	}
}
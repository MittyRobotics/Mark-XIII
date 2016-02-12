package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class ExtendLoadedIntake implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Extend loaded intake state");
		
		if (data.sensorValues != StateMachine.BALL_IN)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_EXTEND_LOADED_INTAKE;
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    
//	    StateMachine.getIntakePiston().set(DoubleSolenoid.Value.kForward);
		
	    while (data.sensorValues != StateMachine.GOT_BALL && data.sensorValues == StateMachine.BALL_IN)
	    {
	    	if (StateMachine.getTimer().get() > StateMachine.PISTON_EXTEND_TIMEOUT)
	    	{
	    		StateMachine.getTimer().stop();
	            StateMachine.getTimer().reset();
	            System.out.println("ERROR: piston timeout");
	            return StateEnum.STATE_ERROR;
	    	}
	    	Timer.delay(0.1);
	    	data.sensorValues = StateMachine.getSensorData(data);
	    }
	    
	    StateMachine.getTimer().stop();
	    StateMachine.getTimer().reset();
	    if (data.sensorValues != StateMachine.GOT_BALL)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_BACKWARD_SPIN;
	}
}
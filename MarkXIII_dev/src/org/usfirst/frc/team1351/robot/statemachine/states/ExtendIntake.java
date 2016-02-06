package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class ExtendIntake implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Extend intake state");
		
		if (StateMachine.createIntFromBoolArray(data) != 0)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_EXTEND_INTAKE;
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    
	    StateMachine.getIntakePiston().set(DoubleSolenoid.Value.kForward);
		
	    int sensors = StateMachine.getSensorData(data);
	    while (sensors != 2 && sensors == 0)
	    {
	    	if (StateMachine.getTimer().get() > StateMachine.PISTON_EXTEND_TIMEOUT)
	    	{
	    		StateMachine.getTimer().stop();
	            StateMachine.getTimer().reset();
	            System.out.println("ERROR: piston timeout");
	            return StateEnum.STATE_ERROR;
	    	}
	    	Timer.delay(0.1);
	    }
	    
	    StateMachine.getTimer().stop();
	    StateMachine.getTimer().reset();
	    if (sensors != 2)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_FORWARD_SPIN;
	}
}
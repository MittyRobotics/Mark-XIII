package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

// essentially the same as RetractIntake but with different exit
public class LowGoalDone implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Low goal done state");
		
		if (StateMachine.createIntFromBoolArray(data) != 2)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_RETRACT_INTAKE;
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    
	    StateMachine.getIntakePiston().set(DoubleSolenoid.Value.kReverse);
		
	    int sensors = StateMachine.getSensorData(data);
	    while (sensors != 0 && sensors == 2)
	    {
	    	if (StateMachine.getTimer().get() > StateMachine.PISTON_EXTEND_TIMEOUT)
	    	{
	    		StateMachine.getTimer().stop();
	            StateMachine.getTimer().reset();
	            return StateEnum.STATE_ERROR;
	    	}
	    	Timer.delay(0.1);
	    }
	    
	    StateMachine.getTimer().stop();
	    StateMachine.getTimer().reset();
	    if (sensors != 0)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_EMPTY;
	}
}
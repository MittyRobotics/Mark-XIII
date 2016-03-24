package org.usfirst.frc.team1351.robot.statemachine;

public enum StateEnum
{
	STATE_FORWARD_SPIN(0),
	STATE_CHOOSE_GOAL(1),
	STATE_BACKWARD_SPIN(2),
	STATE_READY_TO_FIRE(3),
	STATE_HIGH_GOAL_DONE(4),
	STATE_ERROR(5);

	private int value;

	StateEnum(int val)
	{
		value = val;
	}

	public int getValue()
	{
		return value;
	}
}

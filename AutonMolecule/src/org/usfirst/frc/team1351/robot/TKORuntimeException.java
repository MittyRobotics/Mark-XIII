package org.usfirst.frc.team1351.robot;

import org.usfirst.frc.team1351.robot.TKOLogger;

public class TKORuntimeException extends RuntimeException
{
	private static final long serialVersionUID = -2332222109657661651L;
	
	public TKORuntimeException(String message)
	{
		super(message);
		System.out.println("RUNTIME EXCEPTION: " + message);
		recordMessage(message);
	}

	private void recordMessage(String message)
	{
		TKOLogger.getInstance().addMessage("EXCEPTION THROWN! " + message);
		TKOLogger.getInstance().addMessage(super.getStackTrace().toString());
	}
}

package org.usfirst.frc.team1351.robot.util;

import org.usfirst.frc.team1351.robot.logger.TKOLogger;

public class TKOException extends Exception
{
	private static final long serialVersionUID = -2332222109657661651L;
	
	public TKOException(String message)
	{
		super(message);
		System.out.println("EXCEPTION: " + message);
		recordMessage(message);
	}

	private void recordMessage(String message)
	{
		TKOLogger.getInstance().addMessage("EXCEPTION THROWN! " + message);
		TKOLogger.getInstance().addMessage(super.getStackTrace().toString());
	}
}

package org.usfirst.frc.team1351.robot;


public class TKOThread extends Thread
{
	private volatile boolean isThreadRunning = false;

	public TKOThread(Runnable r)
	{
		super(r);
		super.setName(r.toString());
	}

	public synchronized void setThreadRunning(boolean status)
	{
		isThreadRunning = status;
		if (isThreadRunning)
		{
			if (!this.isAlive())
			{
				try {
					this.start();
				} catch (IllegalThreadStateException e)
				{
					System.out.println("Thread is already started!");
				}
			}
		}
	}

	public synchronized boolean isThreadRunning()
	{
		//System.out.println(this.isAlive());
		return isThreadRunning;
	}
}

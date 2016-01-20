// Last edited by Ben Kim
// on 01/12/16

package org.usfirst.frc.team1351.robot.vision;

import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.vision.AxisCamera;

// TODO find a way to toggle USB camera feeds

public class TKOVision implements Runnable
{
	public TKOThread visionThread = null;
	private static TKOVision m_Instance = null;
	
	int session;
	Image frame;
	Image binaryFrame;
	int imaqError;
	NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);
//	Scores scores = new Scores();
	
	protected TKOVision()
	{
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        session = NIVision.IMAQdxOpenCamera("cam0",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
	}

	public static synchronized TKOVision getInstance()
	{
		if (TKOVision.m_Instance == null)
		{
			m_Instance = new TKOVision();
			m_Instance.visionThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start()
	{
		System.out.println("Starting vision task");

		if (!visionThread.isAlive() && m_Instance != null)
			visionThread = new TKOThread(m_Instance);
//			visionThread.setPriority(newPriority);

		if (!visionThread.isThreadRunning())
		{
			visionThread.setThreadRunning(true);
		}
		
		NIVision.IMAQdxStartAcquisition(session);

		System.out.println("Started vision task");
	}

	public void stop()
	{
		System.out.println("Stopping vision task");
		
		NIVision.IMAQdxStopAcquisition(session);
		
		if (visionThread.isThreadRunning())
			visionThread.setThreadRunning(false);

		System.out.println("Stopped vision task");
	}
	
	public void init()
	{

	}

	public void process()
	{
		NIVision.IMAQdxGrab(session, frame, 1);       
        CameraServer.getInstance().setImage(frame);
	}

	@Override
	public void run()
	{
		try
		{
			while (visionThread.isThreadRunning())
			{
				process();
				synchronized (visionThread)
				{
					visionThread.wait(50);
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
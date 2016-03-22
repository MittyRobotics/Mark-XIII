// Last edited by Ishan Shah
// on 01/23/16
// reference: http://wpilib.screenstepslive.com/s/4485/m/50711/l/479908-reading-array-values-published-by-networktables

package org.usfirst.frc.team1351.robot.vision;

import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.XboxController;
import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TKOVision implements Runnable
{
	public TKOThread visionThread = null;
	private static TKOVision m_Instance = null;

	int cameraChoice;
	boolean isFrontCamera;
	boolean isCameraInit;
	int sessionCamFront, sessionCamBack;

	Image frame;
	Image binaryFrame;
	int imaqError;
	NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0, 0, 1, 1);

	NetworkTable table;
	double[] defaultValue = new double[0];
	
	double distance = 0.; // inches: in real life at the angle from target
	double cameraAngle = 0.; // Shows literal angle of robot from target - to be set by findAngle()
	double tanFnc = (Math.tan(cameraAngle) / 2);
	double tarCenterToEdge = 0.; 
	double targetPixelWidth = 0.;
	double targetPixelHeight = 0.;
	double targetWidth = 16.;
	double imageWidth = 480.;
	double height = 85.; // height of target from floor is 85"
	double imageTarget = (imageWidth * targetWidth);

	protected TKOVision()
	{
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		sessionCamFront = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		sessionCamBack = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		isFrontCamera = true; // First input is from the isFrontCamera
		isCameraInit = false; // Camera is not originally initialized, use this as a bad toggle
		table = NetworkTable.getTable("GRIP/myContoursReport");
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

		visionThread.setPriority(Definitions.getPriority("vision"));

		if (!visionThread.isThreadRunning())
			visionThread.setThreadRunning(true);

		init();

		System.out.println("Started vision task");
	}

	public void stop()
	{
		System.out.println("Stopping vision task");

		// NIVision.IMAQdxStopAcquisition(sessionCamFront);
		// NIVision.IMAQdxStopAcquisition(sessionCamBack);

		if (visionThread.isThreadRunning())
			visionThread.setThreadRunning(false);

		System.out.println("Stopped vision task");
	}

	private void init()
	{
		cameraChoice = sessionCamFront;
		NIVision.IMAQdxConfigureGrab(cameraChoice);
		NIVision.IMAQdxStartAcquisition(cameraChoice);
		isCameraInit = true;
	}

	public void viewCamera(int id)
	{
		NIVision.IMAQdxGrab(id, frame, 1);
		CameraServer.getInstance().setImage(frame);
	}

	public void chooseCamera()
	{
		// Following for initializing and setting cameras properly
		if (!isCameraInit)
		{
			if (isFrontCamera)
			{
				NIVision.IMAQdxStopAcquisition(cameraChoice);
				cameraChoice = sessionCamFront;
				isCameraInit = true;
				NIVision.IMAQdxConfigureGrab(cameraChoice);
				NIVision.IMAQdxStartAcquisition(cameraChoice);
			}
			else
			{
				NIVision.IMAQdxStopAcquisition(cameraChoice);
				cameraChoice = sessionCamBack;
				isCameraInit = true;
				NIVision.IMAQdxConfigureGrab(cameraChoice);
				NIVision.IMAQdxStartAcquisition(cameraChoice);
			}
			isCameraInit = true;
		}
	}

	@Override
	public void run()
	{
		try
		{
			while (visionThread.isThreadRunning())
			{
				if (TKOHardware.getXboxController().getButtonY())
				{
					isFrontCamera = !isFrontCamera;
					// Turns camera "off"
					isCameraInit = false;
					// Turns off the camera in use
				}

				chooseCamera();
				viewCamera(cameraChoice);
				printTable();

				synchronized (visionThread)
				{
					visionThread.wait(50);
				}
			}
		}
		catch (TKOException | InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	// TODO test distance and angle calculations

	public double getFloorDistance()
	{
		distance = (imageTarget / (2 * targetPixelWidth) * tanFnc);
		return Math.sqrt((distance * distance) - (height * height));
	}

	public double getRobotAngle()
	{
		cameraAngle = ((targetWidth * Math.abs((imageWidth / 2) - tarCenterToEdge)) / (targetPixelWidth * distance));
		return cameraAngle;
	}

	// Original Equation: d = wR/2ntan(a/2)
	public double getHeight()
	{
		return 9488.347 / targetPixelHeight;
	}

	public double getWidth()
	{
		return 13121.945 / targetPixelWidth;
	}

	// Original Equation: theta = arcsin(((2x/R)-1)tan(a/2))
	public double getTurnAngle()
	{
		double theta = Math.asin(((0.488 * tarCenterToEdge) - 156.074) / 320.);
		return theta;
	}
	
	public void printTable()
	{
		double def = 0;
		System.out.println("\n \n \n");
		double areas = table.getNumber("area", def);
		System.out.print("areas: ");

		System.out.print(areas + ", ");
		SmartDashboard.putNumber("Area", areas);

		double centerX = table.getNumber("x", def);
		System.out.print("centerX: ");

		System.out.print(centerX + ", ");
		SmartDashboard.putNumber("Center X", centerX);

		double centerY = table.getNumber("y", def);
		System.out.print("centerY: ");

		System.out.print(centerY + ", ");
		SmartDashboard.putNumber("Center Y", centerY);

		double heights = table.getNumber("height", def);
		System.out.print("height: ");

		System.out.print(height + ", ");
		SmartDashboard.putNumber("Height", height);

		double widths = table.getNumber("width", def);
		System.out.print("width: ");

		System.out.print(widths + ", ");
		SmartDashboard.putNumber("Width", widths);

		SmartDashboard.putNumber("Height Distance of Robot", getHeight());
		SmartDashboard.putNumber("Width Distance of Robot", getWidth());
		SmartDashboard.putNumber("Angle of Robot to Target", getTurnAngle());

		tarCenterToEdge = centerX;
		targetPixelWidth = widths;
		targetPixelHeight = heights;
	}
}
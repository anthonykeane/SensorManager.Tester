package com.ubhave.sensormanager.tester.pull;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.tester.ApplicationContext;

public class ExampleSensorConfigUpdater
{
	private final int sensorType;
	private ESSensorManagerInterface sensorManager;
	
	public ExampleSensorConfigUpdater(int sensor)
	{
		this.sensorType = sensor;
		try
		{
			ESSensorManager.startSensorManager(ApplicationContext.getContext());
			sensorManager = ESSensorManager.getSensorManager();
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getSensorName()
	{
		try
		{
			return SensorUtils.getSensorName(sensorType);
		}
		catch (ESException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void setSensorSampleWindow(long millis)
	{
		// TODO
	}
	
	public void setSensorSleepWindow(long millis)
	{
		// TODO
	}
	
	public int getSensorSampleWindow()
	{
		// TODO
		return 0;
	}
	
	public int getSensorSleepWindow()
	{
		// TODO
		return 0;
	}
	
	
}

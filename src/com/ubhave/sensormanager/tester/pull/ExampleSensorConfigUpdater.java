package com.ubhave.sensormanager.tester.pull;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.config.SensorConfig;
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
		try {
			sensorManager.setSensorConfig(sensorType, SensorConfig.SENSOR_SAMPLE_INTERVAL, millis);
		}
		catch(ESException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setSensorSleepWindow(long millis)
	{
		try {
			sensorManager.setSensorConfig(sensorType, SensorConfig.SENSOR_SAMPLE_INTERVAL, millis);
		}
		catch(ESException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getSensorSampleWindow()
	{
		try {
			Long sampleWindow = (Long) sensorManager.getSensorConfigValue(sensorType, SensorConfig.SENSOR_SAMPLE_INTERVAL);
			return (int) (sampleWindow / 1000);
		}
		catch(ESException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getSensorSleepWindow()
	{
		try {
			Long sampleWindow = (Long) sensorManager.getSensorConfigValue(sensorType, SensorConfig.SENSOR_SLEEP_INTERVAL);
			return (int) (sampleWindow / 1000);
		}
		catch(ESException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	
}

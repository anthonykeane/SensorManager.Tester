package com.ubhave.sensormanager.tester.pull;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.tester.ApplicationContext;


public class ExampleSensorDataListener implements SensorDataListener
{
	private final int sensorType;
	private final SensorDataUI userInterface;

	private ESSensorManagerInterface sensorManager;
	private int sensorSubscriptionId;

	public ExampleSensorDataListener(int sensorType, SensorDataUI userInterface)
	{
		this.sensorType = sensorType;
		this.userInterface = userInterface;
		try
		{
			sensorManager = ESSensorManager.getSensorManager(ApplicationContext.getContext());
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	public void subscribeToSensorData()
	{
		try
		{
			sensorSubscriptionId = sensorManager.subscribeToSensorData(sensorType, this);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	public void unsubscribeFromSensorData()
	{
		try
		{
			sensorManager.unsubscribeFromSensorData(sensorSubscriptionId);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDataSensed(SensorData data)
	{
		userInterface.updateUI(data);
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

}

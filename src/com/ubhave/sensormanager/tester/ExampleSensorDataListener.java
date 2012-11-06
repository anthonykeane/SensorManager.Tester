package com.ubhave.sensormanager.tester;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;


public class ExampleSensorDataListener implements SensorDataListener
{
	private final int sensorType;
	private final SensorDataUI userInterface;

	private ESSensorManagerInterface sensorManager;
	private int sensorSubscriptionId;
	private boolean isSubscribed;

	public ExampleSensorDataListener(int sensorType, SensorDataUI userInterface)
	{
		this.sensorType = sensorType;
		this.userInterface = userInterface;
		isSubscribed = false;
		try
		{
			sensorManager = ESSensorManager.getSensorManager(ApplicationContext.getContext());
			sensorManager.setGlobalConfig(GlobalConfig.LOW_BATTERY_THRESHOLD, 39);
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
			isSubscribed = true;
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
			isSubscribed = false;
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDataSensed(SensorData data)
	{
		userInterface.updateUI(data.getDataString());
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
	
	public boolean isSubscribed()
	{
		return isSubscribed;
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		try {
			if (isBelowThreshold)
			{
				userInterface.updateUI("Sensing stopped: low battery");
				sensorManager.pauseSubscription(sensorSubscriptionId);
			}
			else {
				userInterface.updateUI("Sensing unpaused: battery healthy");
				sensorManager.unPauseSubscription(sensorSubscriptionId);
			}
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
		
	}

}

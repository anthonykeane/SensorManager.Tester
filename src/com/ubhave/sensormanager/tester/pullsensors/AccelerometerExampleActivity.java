package com.ubhave.sensormanager.tester.pullsensors;

import android.os.Bundle;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class AccelerometerExampleActivity extends AbstractPullSensorExampleActivity
{

	private static final int ACCELEROMETER_SENSOR = SensorUtils.SENSOR_TYPE_ACCELEROMETER;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			// UI Update Sets the "Sensor Type:"
			super.setSensorTypeField(SensorUtils.getSensorName(ACCELEROMETER_SENSOR));
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDataSensed(final SensorData data)
	{
		runOnUiThread(new Runnable()
		{
			public void run()
			{
				setSensorDataField(data.getDataString());
				setSensorDataTime(data.getTimestamp());
			}
		});
	}

	@Override
	protected void subscribeToSensorData()
	{
		try
		{
			sensorSubscriptionId = sensorManager.subscribeToSensorData(ACCELEROMETER_SENSOR, this);
		}
		catch (ESException e)
		{
			e.printStackTrace();
			super.setSensorDataField(e.getLocalizedMessage());
		}

	}

}

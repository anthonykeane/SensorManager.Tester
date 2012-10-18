package com.ubhave.sensormanager.tester;

import android.util.Log;

import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

public class TestSensorDataListener implements SensorDataListener
{

	private static String TAG = "TestSensorDataListener";

	@Override
	public void onDataSensed(SensorData data)
	{
		Log.d(TAG, data.toString());
	}

}

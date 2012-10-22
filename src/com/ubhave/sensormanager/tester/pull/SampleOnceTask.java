package com.ubhave.sensormanager.tester.pull;

import android.os.AsyncTask;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.tester.ApplicationContext;

public class SampleOnceTask extends AsyncTask<Integer, Void, SensorData>
{
	private ESSensorManager sensorManager;

	public SampleOnceTask()
	{
		try
		{
			sensorManager = ESSensorManager.getSensorManager(ApplicationContext.getContext());
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected SensorData doInBackground(Integer... sensorIds)
	{
		try
		{
			int sensorId = sensorIds[0];
			return sensorManager.getDataFromSensor(sensorId);
		}
		catch (ESException e)
		{
			return null;
		}
	}

}

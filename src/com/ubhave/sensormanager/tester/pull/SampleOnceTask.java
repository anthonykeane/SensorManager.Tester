package com.ubhave.sensormanager.tester.pull;

import android.os.AsyncTask;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.tester.ApplicationContext;

public class SampleOnceTask extends AsyncTask<Void, Void, SensorData>
{
	private final ESSensorManager sensorManager;
	private final int sensorType;

	public SampleOnceTask(int sensorType) throws ESException
	{
		this.sensorType = sensorType;
		sensorManager = ESSensorManager.getSensorManager(ApplicationContext.getContext());
	}

	@Override
	protected SensorData doInBackground(Void... params)
	{
		try
		{
			return sensorManager.getDataFromSensor(sensorType);
		}
		catch (ESException e)
		{
			return null;
		}
	}

}

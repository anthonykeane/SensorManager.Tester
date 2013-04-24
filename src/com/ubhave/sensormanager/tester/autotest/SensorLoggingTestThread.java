package com.ubhave.sensormanager.tester.autotest;

import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.ubhave.datahandler.DataManager;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.triggermanager.TriggerException;

public class SensorLoggingTestThread extends Thread implements SensorDataListener
{
	private final static String TAG = "SensorLoggingTestThread";
	private final Context context;
	private DataManager dataManager;
	
	public SensorLoggingTestThread(Context context)
	{
		this.context = context;
		try
		{
			dataManager = DataManager.getInstance(context);
		}
		catch (ESException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TriggerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCrossingLowBatteryThreshold(boolean arg0)
	{
	}

	@Override
	public void onDataSensed(SensorData sensorData)
	{
		try
		{
			Log.d(TAG, "received sensor data " + SensorUtils.getSensorName(sensorData.getSensorType()));
			dataManager.logSensorData(sensorData);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		try
		{
			Thread.sleep(10000);
			Looper.prepare();

			ESSensorManager esSensorManager = ESSensorManager.getSensorManager(context);
			for (int sensorId : SensorUtils.ALL_SENSORS)
			{
				esSensorManager.subscribeToSensorData(sensorId, this);
			}

			for (int i=0; i<10; i++)
			{
				Thread.sleep(30000);
				for (int sensorId : SensorUtils.ALL_SENSORS)
				{
					try
					{
						List<SensorData> list = dataManager.getRecentSensorData(sensorId, 1000 * 60 * 60 * 24);

						System.out.println("=====================================>>>>> "
								+ SensorUtils.getSensorName(sensorId) + "    size " + list.size());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
	}
}

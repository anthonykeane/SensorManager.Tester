package com.ubhave.sensormanager.tester.autotest;

import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.ubhave.datahandler.ESDataManager;
import com.ubhave.datahandler.config.DataStorageConfig;
import com.ubhave.datahandler.config.DataTransferConfig;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SensorLoggingTestThread extends Thread implements SensorDataListener
{
	private final static String TAG = "SensorLoggingTestThread";
	private final Context context;
	private ESDataManager dataManager;
	
	public SensorLoggingTestThread(Context context)
	{
		this.context = context;
		try
		{
			long shortLife = 1000 * 60 * 5;
			dataManager = ESDataManager.getInstance(context);
			dataManager.setConfig(DataStorageConfig.LOCAL_STORAGE_ROOT_DIRECTORY_NAME, "ESDataStorage");
			dataManager.setConfig(DataTransferConfig.POST_DATA_URL, "http://pizza.cl.cam.ac.uk/emotionsense_test/testLogUpload.php");
			dataManager.setConfig(DataTransferConfig.POST_DATA_URL_PASSWD, "test");
			dataManager.setConfig(DataStorageConfig.FILE_LIFE_MILLIS, shortLife);
			dataManager.setConfig(DataTransferConfig.TRANSFER_ALARM_INTERVAL, shortLife / 5);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCrossingLowBatteryThreshold(boolean arg0)
	{
	}

	@Override
	public void onDataSensed(final SensorData sensorData)
	{
		new Thread()
		{

			@Override
			public void run()
			{
				super.run();
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
			
		}.start();
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

			for (int i=0; i<100; i++)
			{
				Thread.sleep(30000);
				for (int sensorId : SensorUtils.ALL_SENSORS)
				{
					List<SensorData> list = dataManager.getRecentSensorData(sensorId, 1000 * 60 * 60 * 24);

					System.out.println("================>>>>> "
							+ SensorUtils.getSensorName(sensorId) + "    size " + list.size());
				}
			}
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
	}
}

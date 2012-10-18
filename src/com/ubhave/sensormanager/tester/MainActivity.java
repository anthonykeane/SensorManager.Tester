package com.ubhave.sensormanager.tester;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MainActivity extends Activity
{
	
	private final static String LOG_TAG = "MainActivity";
	private ESSensorManagerInterface sensorManager;
	private int identifier;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d(LOG_TAG, "Getting Sensor Manager");
		try {
			sensorManager = ESSensorManager.getSensorManager(ApplicationContext.getContext());
			enableStartSensingButton();
			enableStopSensingButton();
		}
		catch(ESException e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	private void enableStartSensingButton()
	{
		Button button = (Button) findViewById(R.id.startSensing);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				TestSensorDataListener listener = new TestSensorDataListener();
				try
				{
					identifier = sensorManager.subscribeToSensorData(SensorUtils.SENSOR_TYPE_ACCELEROMETER, listener);
				}
				catch (ESException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	private void enableStopSensingButton()
	{
		Button button = (Button) findViewById(R.id.stopSensing);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					sensorManager.unsubscribeFromSensorData(identifier);
				}
				catch (ESException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	
}

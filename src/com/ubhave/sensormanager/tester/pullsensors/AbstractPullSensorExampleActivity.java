package com.ubhave.sensormanager.tester.pullsensors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.tester.ApplicationContext;
import com.ubhave.sensormanager.tester.R;

public abstract class AbstractPullSensorExampleActivity extends Activity implements SensorDataListener
{

	// UI Status Strings
	private final static String UNSUBSCRIBED = "(Unsubscribed)";
	private final static String SUBSCRIBED = "(Subscribed)";
	
	// Sensor Manager
	private boolean isSubscribed;
	protected int sensorSubscriptionId;
	protected ESSensorManagerInterface sensorManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_sensor_layout);
		
		try {
			sensorManager = ESSensorManager.getSensorManager(ApplicationContext.getContext());
			
			// UI Components
			enableStartSensingButton();
			enableStopSensingButton();
			setSensorStatusField(UNSUBSCRIBED);
			isSubscribed = false;
		}
		catch(ESException e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		if (isSubscribed)
		{
			unsubscribeFromSensorData();
			setSensorStatusField(UNSUBSCRIBED);
			isSubscribed = false;
		}
	}
	
	/*
	 * UI Components (Buttons, Text Fields)
	 */
	
	private void enableStartSensingButton()
	{
		Button button = (Button) findViewById(R.id.startSensing);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				subscribeToSensorData();
				setSensorStatusField(SUBSCRIBED);
				isSubscribed = true;
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
				unsubscribeFromSensorData();
				setSensorStatusField(UNSUBSCRIBED);
				isSubscribed = false;
			}
		});
	}
	
	protected void setSensorStatusField(String status)
	{
		TextView text = (TextView) findViewById(R.id.status);
		text.setText(status);
	}
	
	protected void setSensorTypeField(String sensorType)
	{
		EditText typeField = (EditText) findViewById(R.id.sensorType);
		typeField.setText(sensorType);
	}
	
	protected void setSensorDataField(String dataString)
	{
		EditText dataField = (EditText) findViewById(R.id.dataText);
		dataField.setText(dataString);
	}
	
	protected void setSensorDataTime(long timestamp)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		
		DateFormat format = new SimpleDateFormat("hh:mm:ss");
		
		TextView text = (TextView) findViewById(R.id.datatime);
		text.setText(format.format(calendar.getTime()));
	}
	
	
	/*
	 * Sensor Data Subscription/Unsubscription Code
	 */
	
	protected abstract void subscribeToSensorData();
	
	protected void unsubscribeFromSensorData()
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
	public abstract void onDataSensed(SensorData arg0);

}

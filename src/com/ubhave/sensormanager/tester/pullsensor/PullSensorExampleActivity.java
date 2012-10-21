package com.ubhave.sensormanager.tester.pullsensor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.tester.R;

public class PullSensorExampleActivity extends Activity implements SensorDataUI
{
	// Intent Strings
	public final static String SENSOR_TYPE_ID = "sensorTypeId";

	// Sensor Manager
	private boolean isSubscribed;
	private ExampleSensorDataListener sensorDataListener;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_sensor_layout);

		// Sensor Data Listener
		Intent intent = getIntent();
		int sensorType = intent.getIntExtra(SENSOR_TYPE_ID, -1);
		sensorDataListener = new ExampleSensorDataListener(sensorType, this);
		

		// UI Components
		enableStartSensingButton();
		enableStopSensingButton();
		
		Log.d("PullSensor", "Sensor type is: "+sensorDataListener.getSensorName());

		setSensorTypeField(sensorDataListener.getSensorName());
		setSensorStatusField();
		setSensorTypeField(sensorDataListener.getSensorName());
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (isSubscribed)
		{
			unsubscribe();
		}
	}

	/*
	 * Sensor control
	 */

	private void subscribe()
	{
		sensorDataListener.subscribeToSensorData();
		isSubscribed = true;

		// Update UI
		setSensorStatusField();
	}

	private void unsubscribe()
	{
		sensorDataListener.unsubscribeFromSensorData();
		isSubscribed = false;

		// Update UI
		setSensorStatusField();
	}

	@Override
	public void updateUI(final SensorData data)
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
				if (!isSubscribed)
				{
					subscribe();
				}
				else
				{
					Toast.makeText(PullSensorExampleActivity.this, "Sensor Listener already subscribed.", Toast.LENGTH_SHORT).show();
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
				if (isSubscribed)
				{
					unsubscribe();
				}
				else
				{
					Toast.makeText(PullSensorExampleActivity.this, "Sensor Listener not subscribed.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	protected void setSensorStatusField()
	{
		Resources r = getResources();
		String status;
		if (isSubscribed)
		{
			status = r.getString(R.string.subscribed);
		}
		else
		{
			status = r.getString(R.string.unsubscribed);
		}

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

}

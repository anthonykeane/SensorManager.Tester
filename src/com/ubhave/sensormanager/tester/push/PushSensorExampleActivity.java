package com.ubhave.sensormanager.tester.push;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.tester.R;
import com.ubhave.sensormanager.tester.SensorDataUI;
import com.ubhave.sensormanager.tester.pull.ExampleSensorDataListener;
import com.ubhave.sensormanager.tester.pull.SampleOnceTask;

public class PushSensorExampleActivity extends Activity implements SensorDataUI
{

	// Intent Strings
	public final static String SENSOR_TYPE_ID = "sensorTypeId";
	private final int UNSUBSCRIBED = 0;
	private final int SUBSCRIBED = 1;
	private final int SAMPLING = 2;

	// Sensor Manager
	private int currentStatus, sensorType;
	private ExampleSensorDataListener sensorDataListener;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_sensor_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Sensor Data Listener
		Intent intent = getIntent();
		sensorType = intent.getIntExtra(SENSOR_TYPE_ID, -1);
		sensorDataListener = new ExampleSensorDataListener(sensorType, this);

		// UI Components
		enableStartSensingButton();
		enableStopSensingButton();
		enableSenseOnceButton();

		setSensorStatusField();
		this.setTitle(sensorDataListener.getSensorName());

		currentStatus = UNSUBSCRIBED;
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (sensorDataListener.isSubscribed())
		{
			unsubscribe();
		}
	}

	/*
	 * Sensor control
	 */

	private void subscribe()
	{
		if (currentStatus == SAMPLING)
		{
			Toast.makeText(PushSensorExampleActivity.this, "Cannot subscribe while pulling data.", Toast.LENGTH_SHORT).show();
		}
		else if (!sensorDataListener.isSubscribed())
		{
			sensorDataListener.subscribeToSensorData();
			currentStatus = SUBSCRIBED;
		}
		else
		{
			Toast.makeText(PushSensorExampleActivity.this, "Sensor Listener already subscribed.", Toast.LENGTH_SHORT).show();
		}
		setSensorStatusField();
	}

	private void unsubscribe()
	{
		if (sensorDataListener.isSubscribed())
		{
			sensorDataListener.unsubscribeFromSensorData();
			currentStatus = UNSUBSCRIBED;
		}
		else
		{
			Toast.makeText(PushSensorExampleActivity.this, "Sensor Listener not subscribed.", Toast.LENGTH_SHORT).show();
		}
		setSensorStatusField();
	}

	private void pullDataOnce()
	{
		if (sensorDataListener.isSubscribed())
		{
			Toast.makeText(PushSensorExampleActivity.this, "Cannot pull data while sensor listener is subscribed.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			new SampleOnceTask()
			{
				@Override
				public void onPreExecute()
				{
					currentStatus = SAMPLING;
					setSensorStatusField();
				}
				
				@Override
				public void onPostExecute(SensorData data)
				{
					currentStatus = UNSUBSCRIBED;
					updateUI(data);
					setSensorStatusField();
				}
				
			}.execute(sensorType);
		}
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
				subscribe();
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
				unsubscribe();
			}
		});
	}

	private void enableSenseOnceButton()
	{
		Button button = (Button) findViewById(R.id.pullOnceButton);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				pullDataOnce();
			}
		});
	}

	protected void setSensorStatusField()
	{
		Resources r = getResources();
		String status;
		
		if (currentStatus == SAMPLING)
		{
			status = r.getString(R.string.sampling);
		}
		else if (sensorDataListener.isSubscribed())
		{
			status = r.getString(R.string.subscribed);
		}
		else
		{
			status = r.getString(R.string.unsubscribed);
		}

		TextView text = (TextView) findViewById(R.id.statusvalue);
		text.setText(status);
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

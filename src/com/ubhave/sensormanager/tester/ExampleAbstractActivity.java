package com.ubhave.sensormanager.tester;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubhave.sensormanager.data.SensorData;

public abstract class ExampleAbstractActivity extends Activity implements SensorDataUI
{
	public final static String SENSOR_TYPE_ID = "sensorTypeId";
	protected final int UNSUBSCRIBED = 0;
	protected final int SUBSCRIBED = 1;

	protected ExampleSensorDataListener sensorDataListener;
	protected int selectedSensorType, currentStatus;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*
		 * Instantiate the sensor data listener
		 */
		selectedSensorType = getIntent().getIntExtra(SENSOR_TYPE_ID, -1);
		sensorDataListener = new ExampleSensorDataListener(selectedSensorType, this);

		/*
		 * Create the user interface
		 */
		setContentView(getInterfaceLayout());
		getActionBar().setDisplayHomeAsUpEnabled(true);

		enableStartSensingButton();
		enableStopSensingButton();

		setSensorStatusField(UNSUBSCRIBED);

		setTitle(sensorDataListener.getSensorName());
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (sensorDataListener.isSubscribed())
		{
			unsubscribe();
		}
	}

	protected abstract int getInterfaceLayout();

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

	/*
	 * Method called by the ExampleSensorDataListener sensorDataListener
	 */

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
	 * Methods to update the UI contents
	 */

	protected void setSensorStatusField(int newStatus)
	{
		currentStatus = newStatus;
		String status = getStatusString();

		TextView text = (TextView) findViewById(R.id.statusvalue);
		text.setText(status);
	}

	protected String getStatusString()
	{
		Resources r = getResources();
		if (sensorDataListener.isSubscribed())
		{
			return r.getString(R.string.subscribed);
		}
		else
		{
			return r.getString(R.string.unsubscribed);
		}
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
	 * Sensor control
	 */

	protected void subscribe()
	{
		if (!sensorDataListener.isSubscribed())
		{
			sensorDataListener.subscribeToSensorData();
			setSensorStatusField(SUBSCRIBED);
		}
		else
		{
			Toast.makeText(this, "Sensor Listener already subscribed.", Toast.LENGTH_SHORT).show();
		}
	}

	private void unsubscribe()
	{
		if (sensorDataListener.isSubscribed())
		{
			sensorDataListener.unsubscribeFromSensorData();
			setSensorStatusField(UNSUBSCRIBED);
		}
		else
		{
			Toast.makeText(this, "Sensor Listener not subscribed.", Toast.LENGTH_SHORT).show();
		}
	}

}

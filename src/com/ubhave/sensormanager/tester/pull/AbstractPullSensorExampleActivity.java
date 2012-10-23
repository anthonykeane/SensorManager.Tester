package com.ubhave.sensormanager.tester.pull;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.tester.ExampleAbstractActivity;
import com.ubhave.sensormanager.tester.R;

public abstract class AbstractPullSensorExampleActivity extends ExampleAbstractActivity
{
	private final int SAMPLING = 2;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/*
		 * Additional UI button for pull sensor
		 */
		enableSenseOnceButton();
		enableUpdateConfigButton();
	}

	protected int getInterfaceLayout()
	{
		return R.layout.pull_sensor_layout;
	}

	/*
	 * UI Components (Buttons, Text Fields)
	 */
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
	
	protected abstract void enableUpdateConfigButton();

	@Override
	protected String getStatusString()
	{
		Resources r = getResources();
		if (currentStatus == SAMPLING)
		{
			return r.getString(R.string.sampling);
		}
		else
		{
			return super.getStatusString();
		}
	}
	
	@Override
	protected void subscribe()
	{
		if (currentStatus == SAMPLING)
		{
			Toast.makeText(this, "Cannot subscribe while pulling data.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			super.subscribe();
		}
	}

	private void pullDataOnce()
	{
		if (sensorDataListener.isSubscribed())
		{
			Toast.makeText(AbstractPullSensorExampleActivity.this, "Cannot pull data while sensor listener is subscribed.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			new SampleOnceTask()
			{
				@Override
				public void onPreExecute()
				{
					setSensorStatusField(SAMPLING);
				}

				@Override
				public void onPostExecute(SensorData data)
				{
					updateUI(data);
					setSensorStatusField(UNSUBSCRIBED);
				}

			}.execute(selectedSensorType);
		}
	}
}
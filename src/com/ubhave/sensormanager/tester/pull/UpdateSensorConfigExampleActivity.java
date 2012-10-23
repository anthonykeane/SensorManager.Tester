package com.ubhave.sensormanager.tester.pull;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ubhave.sensormanager.tester.R;

public class UpdateSensorConfigExampleActivity extends Activity
{
	public final static String SENSOR_TYPE_ID = "sensorTypeId";

	private int selectedSensorType;
	private DecimalFormat formatter;
	private ExampleSensorConfigUpdater updater;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*
		 * Instantiate the sensor data listener
		 */
		selectedSensorType = getIntent().getIntExtra(SENSOR_TYPE_ID, -1);
		updater = new ExampleSensorConfigUpdater(selectedSensorType);

		/*
		 * Create the user interface
		 */
		formatter = new DecimalFormat("#.##");
		
		this.setTitle(updater.getSensorName() + " Config");
		setContentView(R.layout.config_sensor_layout);

		enableDoneButton();
		enableProgressBar(R.id.sampleValue, R.id.sampleProgressBar, true);
		enableProgressBar(R.id.sleepValue, R.id.sleepProgressBar, false);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void enableDoneButton()
	{
		Button button = (Button) findViewById(R.id.doneButton);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				UpdateSensorConfigExampleActivity.this.finish();
			}
		});
	}

	private void enableProgressBar(int textId, int progressId, final boolean isSample)
	{
		final TextView currentStatus = (TextView) findViewById(textId);
		int initialValue;
		if (isSample)
		{
			initialValue = updater.getSensorSampleWindow();
		}
		else
		{
			initialValue = updater.getSensorSleepWindow();
		}

		currentStatus.setText(getTime(initialValue));
		SeekBar progress = (SeekBar) findViewById(progressId);
		progress.setProgress(initialValue);

		progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				int seconds = seekBar.getProgress();
				if (seconds == 0)
				{
					seconds++;
				}

				if (isSample)
				{
					updater.setSensorSampleWindow(seconds * 1000);
				}
				else
				{
					updater.setSensorSleepWindow(seconds * 1000);
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			public void onProgressChanged(SeekBar seekBar, int rating, boolean fromUser)
			{
				if (rating == 0)
				{
					rating = 1;
				}
				currentStatus.setText(getTime(rating));
			}
		});
	}
	
	private String getTime(int seconds)
	{
		if (seconds == 1)
		{
			return "1 second";
		}
		if (seconds <= 120)
		{
			return seconds+" seconds";
		}
		else
		{
			double minutes = (double) seconds / 60;
			return formatter.format(minutes) + " minutes";
		}
	}
}

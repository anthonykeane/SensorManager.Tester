package com.ubhave.sensormanager.tester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.tester.pullsensors.PullSensorExampleActivity;

public class MainActivity extends Activity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button) findViewById(R.id.accelerometer);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(MainActivity.this, PullSensorExampleActivity.class);
				intent.putExtra(PullSensorExampleActivity.SENSOR_TYPE_ID, SensorUtils.SENSOR_TYPE_ACCELEROMETER);
				startActivity(intent);
			}
		});
	}
	
}

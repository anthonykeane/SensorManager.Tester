package com.ubhave.sensormanager.tester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ubhave.sensormanager.tester.pullsensors.AccelerometerExampleActivity;

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
				startActivity(new Intent(MainActivity.this, AccelerometerExampleActivity.class));
			}
		});
	}
	
}

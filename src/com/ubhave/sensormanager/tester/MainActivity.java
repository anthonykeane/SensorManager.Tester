package com.ubhave.sensormanager.tester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ubhave.sensormanager.tester.pullsensors.AccelerometerExampleActivity;

public class MainActivity extends Activity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startActivity(new Intent(this, AccelerometerExampleActivity.class));
	}
	
	
	
	

	
}

package com.ubhave.sensormanager.tester.pull;

import android.app.Activity;
import android.os.Bundle;

public class UpdateSensorConfigExampleActivity extends Activity
{
	public final static String SENSOR_TYPE_ID = "sensorTypeId";
	
	private int selectedSensorType;
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
		this.setTitle(updater.getSensorName()+" Config");
//		setContentView(getInterfaceLayout());
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//
//		enableStartSensingButton();
//		enableStopSensingButton();
//
//		setSensorStatusField(UNSUBSCRIBED);
	}
}

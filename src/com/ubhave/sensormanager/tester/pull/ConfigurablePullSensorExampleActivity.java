package com.ubhave.sensormanager.tester.pull;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ubhave.sensormanager.tester.R;

public class ConfigurablePullSensorExampleActivity extends AbstractPullSensorExampleActivity
{
	
	protected void enableUpdateConfigButton()
	{
		Button button = (Button) findViewById(R.id.changeConfigButton);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				updateSensorConfig();
			}
		});
	}

	private void updateSensorConfig()
	{
		Intent intent = new Intent(this, UpdateSensorConfigExampleActivity.class);
		intent.putExtra(UpdateSensorConfigExampleActivity.SENSOR_TYPE_ID, selectedSensorType);
		
		startActivity(intent);
	}
}

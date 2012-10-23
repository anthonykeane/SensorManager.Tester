package com.ubhave.sensormanager.tester.pull;

import android.view.View;
import android.widget.Button;

import com.ubhave.sensormanager.tester.R;

public class NonConfigurablePullSensorExampleActivity extends AbstractPullSensorExampleActivity
{

	@Override
	protected void enableUpdateConfigButton()
	{
		Button button = (Button) findViewById(R.id.changeConfigButton);
		button.setVisibility(View.INVISIBLE);		
	}
	
}

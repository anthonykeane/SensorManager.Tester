package com.ubhave.sensormanager.tester.push;

import android.widget.EditText;

import com.ubhave.sensormanager.tester.ExampleAbstractActivity;
import com.ubhave.sensormanager.tester.R;

public class PushSensorExampleActivity extends ExampleAbstractActivity
{
	
	protected int getInterfaceLayout()
	{
		return R.layout.push_sensor_layout;
	}
	
	@Override
	protected void setSensorDataField(String dataString)
	{
		EditText dataField = (EditText) findViewById(R.id.dataText);
		String currentText = "";
		try {
			currentText = dataField.getEditableText().toString();
			dataField.setText(currentText+"\n"+dataString);
		}
		catch (NullPointerException e)
		{
			dataField.setText(dataString);
		}
	}
}

package com.ubhave.sensormanager.tester.autotest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.BatteryData;
import com.ubhave.sensormanager.data.pushsensor.ScreenData;
import com.ubhave.sensormanager.process.AbstractProcessor;
import com.ubhave.sensormanager.process.push.BatteryProcessor;
import com.ubhave.sensormanager.process.push.ScreenProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class PushSensorBroadcastReceiver extends BroadcastReceiver
{
	private final static String TAG = "PushSensorBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			Log.d("PushSensorBroadcastReceiver", "Broadcast received");
			String action = intent.getAction();
			if (action != null)
			{
				/*
				 * PUSH SENSORS:
				 * - Battery
				 * 		- ACTION_BATTERY_CHANGED (cannot be received)
				 * 		- ACTION_BATTERY_LOW 
				 * 		- ACTION_BATTERY_OKAY 
				 * - Communications State
				 * 		- ACTION_CALL_BUTTON 
				 * - Phone State 
				 * - Screen (ok)
				 * - SMS
				 */
				SensorData sensorData = null;
				if (action.equals(Intent.ACTION_BATTERY_LOW) || action.equals(Intent.ACTION_BATTERY_OKAY))
				{
					sensorData = buildBatteryData(context, intent);
				}
				else if (action.equals(Intent.ACTION_SCREEN_ON) || action.equals(Intent.ACTION_SCREEN_OFF))
				{
					sensorData = buildScreenData(context, intent);
				}
//				else if (action.equals(Intent.))

				if (sensorData != null)
				{
					logEvent(context, sensorData);
				}
				else
				{
					System.err.println("Null data: "+action);
				}
			}
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	private void logEvent(final Context context, final SensorData data)
	{
		JSONFormatter sensorFormatter = DataFormatter.getJSONFormatter(context, data.getSensorType());
		System.err.println(sensorFormatter.toJSON(data));
	}

	/*
	 * Battery: You can not receive this through components declared in
	 * manifests, only by explicitly registering for it with
	 * Context.registerReceiver(). See ACTION_BATTERY_LOW, ACTION_BATTERY_OKAY,
	 * ACTION_POWER_CONNECTED, and ACTION_POWER_DISCONNECTED for distinct
	 * battery-related broadcasts that are sent and can be received through
	 * manifest receivers.
	 */
	private BatteryData buildBatteryData(final Context context, final Intent intent) throws ESException
	{
		BatteryProcessor processor = (BatteryProcessor) AbstractProcessor.getProcessor(context, SensorUtils.SENSOR_TYPE_BATTERY, true, true);
		return processor.process(System.currentTimeMillis(), null, intent);
	}

	private ScreenData buildScreenData(final Context context, final Intent intent) throws ESException
	{
		ScreenProcessor processor = (ScreenProcessor) AbstractProcessor.getProcessor(context, SensorUtils.SENSOR_TYPE_SCREEN, true, true);
		return processor.process(System.currentTimeMillis(), null, intent);
	}
}

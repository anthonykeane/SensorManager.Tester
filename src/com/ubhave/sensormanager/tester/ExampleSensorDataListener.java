/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This demo application was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.sensormanager.tester;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ExampleSensorDataListener implements SensorDataListener
{
	private final int sensorType;
	private final SensorDataUI userInterface;

	private ESSensorManagerInterface sensorManager;
	private final JSONFormatter formatter;
	
	private int sensorSubscriptionId;
	private boolean isSubscribed;

	public ExampleSensorDataListener(int sensorType, SensorDataUI userInterface)
	{
		this.sensorType = sensorType;
		this.userInterface = userInterface;
		isSubscribed = false;
		
		formatter = DataFormatter.getJSONFormatter(sensorType);
		try
		{
			sensorManager = ESSensorManager.getSensorManager(ApplicationContext.getContext());
			sensorManager.setGlobalConfig(GlobalConfig.LOW_BATTERY_THRESHOLD, 25);
			
			if (sensorType == SensorUtils.SENSOR_TYPE_LOCATION)
			{
				sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_LOCATION, SensorConfig.LOCATION_ACCURACY, SensorConfig.LOCATION_ACCURACY_FINE);
			}
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	public void subscribeToSensorData()
	{
		try
		{
			sensorSubscriptionId = sensorManager.subscribeToSensorData(sensorType, this);
			isSubscribed = true;
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	public void unsubscribeFromSensorData()
	{
		try
		{
			sensorManager.unsubscribeFromSensorData(sensorSubscriptionId);
			isSubscribed = false;
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDataSensed(SensorData data)
	{
		userInterface.updateUI(formatter.toJSON(data).toJSONString());
	}

	public String getSensorName()
	{
		try
		{
			return SensorUtils.getSensorName(sensorType);
		}
		catch (ESException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public boolean isSubscribed()
	{
		return isSubscribed;
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		try
		{
			if (isBelowThreshold)
			{
				userInterface.updateUI("Sensing stopped: low battery");
				sensorManager.pauseSubscription(sensorSubscriptionId);
			}
			else
			{
				userInterface.updateUI("Sensing unpaused: battery healthy");
				sensorManager.unPauseSubscription(sensorSubscriptionId);
			}
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}

	}

}

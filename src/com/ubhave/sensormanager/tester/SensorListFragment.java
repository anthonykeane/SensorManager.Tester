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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.tester.pull.AbstractPullSensorExampleActivity;
import com.ubhave.sensormanager.tester.pull.ConfigurablePullSensorExampleActivity;
import com.ubhave.sensormanager.tester.pull.NonConfigurablePullSensorExampleActivity;
import com.ubhave.sensormanager.tester.push.PushSensorExampleActivity;

public class SensorListFragment extends Fragment
{
	public final static String SENSOR_TYPE = "sensorType";

	private final static int[] pullSensors = new int[] { SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorUtils.SENSOR_TYPE_BLUETOOTH, SensorUtils.SENSOR_TYPE_LOCATION, SensorUtils.SENSOR_TYPE_MICROPHONE, SensorUtils.SENSOR_TYPE_WIFI };
	private final static boolean[] isConfigurablePullSensor = new boolean[] { true, false, false, true, false };

	private final static int[] pushSensors = new int[] { SensorUtils.SENSOR_TYPE_BATTERY, SensorUtils.SENSOR_TYPE_CONNECTION_STATE, SensorUtils.SENSOR_TYPE_PHONE_STATE, SensorUtils.SENSOR_TYPE_PROXIMITY, SensorUtils.SENSOR_TYPE_SCREEN,
			SensorUtils.SENSOR_TYPE_SMS };

	private final static String TITLE = "title";
	private final static String DESCRIPTION = "description";
	private final static String[] from = new String[] { TITLE, DESCRIPTION };
	private final static int[] to = new int[] { R.id.title, R.id.description };

	private List<HashMap<String, String>> sensorList;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.tab_main, container, false);

		Bundle args = this.getArguments();
		final boolean isPullSensorFragment = args.getBoolean(SENSOR_TYPE);
		setSensorList(isPullSensorFragment);

		ListView sensorListView = (ListView) view.findViewById(R.id.pullSensorListView);
		sensorListView.setAdapter(new SimpleAdapter(container.getContext(), sensorList, R.layout.sensorlist_item, from, to));
		sensorListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				if (isPullSensorFragment)
				{
					launchPullSensorActivity(container.getContext(), pullSensors[position], isConfigurablePullSensor[position]);
				}
				else
				{
					launchPushSensorActivity(container.getContext(), pushSensors[position]);
				}
			}
		});
		return view;
	}

	public void setSensorList(boolean isPullSensorFragment)
	{
		sensorList = new ArrayList<HashMap<String, String>>();
		int[] selectedSensors;
		String[] sensorDescriptions;

		if (isPullSensorFragment)
		{
			selectedSensors = pullSensors;
			sensorDescriptions = getResources().getStringArray(R.array.pull_sensors_descriptions);
		}
		else
		{
			selectedSensors = pushSensors;
			sensorDescriptions = getResources().getStringArray(R.array.push_sensors_descriptions);
		}

		for (int i = 0; i < selectedSensors.length; i++)
		{
			try
			{
				HashMap<String, String> entry = new HashMap<String, String>();
				entry.put(TITLE, SensorUtils.getSensorName(selectedSensors[i]));
				entry.put(DESCRIPTION, sensorDescriptions[i]);
				sensorList.add(entry);
			}
			catch (ESException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void launchPullSensorActivity(Context context, int sensorType, boolean isConfigurable)
	{
		Intent intent;
		if (isConfigurable)
		{
			intent = new Intent(context, ConfigurablePullSensorExampleActivity.class);
		}
		else
		{
			intent = new Intent(context, NonConfigurablePullSensorExampleActivity.class);
		}
		intent.putExtra(AbstractPullSensorExampleActivity.SENSOR_TYPE_ID, sensorType);
		startActivity(intent);
	}

	private void launchPushSensorActivity(Context context, int sensorType)
	{
		Intent intent = new Intent(context, PushSensorExampleActivity.class);
		intent.putExtra(AbstractPullSensorExampleActivity.SENSOR_TYPE_ID, sensorType);
		startActivity(intent);
	}
}

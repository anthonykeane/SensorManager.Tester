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
import com.ubhave.sensormanager.tester.pull.PullSensorExampleActivity;

public class SensorListFragment extends Fragment
{
	public final static String SENSOR_TYPE = "sensorType";
	
	private final static int[] pullSensors = new int[] { SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorUtils.SENSOR_TYPE_BLUETOOTH, SensorUtils.SENSOR_TYPE_LOCATION, SensorUtils.SENSOR_TYPE_MICROPHONE, SensorUtils.SENSOR_TYPE_WIFI };
	private final static int[] pushSensors = new int[] { SensorUtils.SENSOR_TYPE_BATTERY, SensorUtils.SENSOR_TYPE_PHONE_STATE, SensorUtils.SENSOR_TYPE_PROXIMITY, SensorUtils.SENSOR_TYPE_SCREEN, SensorUtils.SENSOR_TYPE_SMS };

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
					launchPullSensorActivity(container.getContext(), pullSensors[position]);
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

		for (int i=0; i<selectedSensors.length; i++)
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

	private void launchPullSensorActivity(Context context, int sensorType)
	{
		Intent intent = new Intent(context, PullSensorExampleActivity.class);
		intent.putExtra(PullSensorExampleActivity.SENSOR_TYPE_ID, sensorType);
		startActivity(intent);
	}
}

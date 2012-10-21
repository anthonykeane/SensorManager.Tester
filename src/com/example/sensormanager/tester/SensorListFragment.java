package com.example.sensormanager.tester;

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

import com.example.sensormanager.tester.pull.PullSensorExampleActivity;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SensorListFragment extends Fragment
{
	private final static int[] pullSensors = new int[] {
		SensorUtils.SENSOR_TYPE_ACCELEROMETER,
		SensorUtils.SENSOR_TYPE_BLUETOOTH,
		SensorUtils.SENSOR_TYPE_LOCATION,
		SensorUtils.SENSOR_TYPE_MICROPHONE,
		SensorUtils.SENSOR_TYPE_WIFI
	};
	
	private final static String TITLE = "title";
	private final static String DESCRIPTION = "description";
	private final static String[] from = new String[] { TITLE, DESCRIPTION };
	private final static int[] to = new int[] { R.id.title, R.id.description };
	
	private List<HashMap<String, String>> pullSensorList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.tab_main, container, false);
		
		pullSensorList = new ArrayList<HashMap<String, String>>();
		for (int sensor : pullSensors)
		{
			try
			{
				HashMap<String, String> entry = new HashMap<String, String>();
				entry.put(TITLE, SensorUtils.getSensorName(sensor));
				entry.put(DESCRIPTION, DESCRIPTION);
				pullSensorList.add(entry);
			}
			catch (ESException e)
			{
				e.printStackTrace();
			}
		}
		
		ListView pullSensorListView = (ListView) view.findViewById(R.id.pullSensorListView);
		pullSensorListView.setAdapter(new SimpleAdapter(container.getContext(), pullSensorList, R.layout.sensorlist_item, from, to));
		
		pullSensorListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				launchPullSensorActivity(container.getContext(), pullSensors[position]);
			}
			
		});
		
		return view;
	}
	
	private void launchPullSensorActivity(Context context, int sensorType)
	{
		Intent intent = new Intent(context, PullSensorExampleActivity.class);
		intent.putExtra(PullSensorExampleActivity.SENSOR_TYPE_ID, sensorType);
		
		startActivity(intent);
	}
}

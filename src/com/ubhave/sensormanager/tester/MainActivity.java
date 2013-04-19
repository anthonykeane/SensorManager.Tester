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

import java.util.List;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ubhave.datahandler.DataManager;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
	public static final String TAG = "MainActivity";
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private static boolean enableAutoTest = true;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.pull_sensor).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.push_sensor).setTabListener(this));

		if (enableAutoTest)
		{
			new TestThread().start();
		}
	}

	class TestThread extends Thread implements SensorDataListener
	{

		@Override
		public void onCrossingLowBatteryThreshold(boolean arg0)
		{
		}

		@Override
		public void onDataSensed(SensorData sensorData)
		{
			try
			{
				Log.d(TAG, "received sensor data " + SensorUtils.getSensorName(sensorData.getSensorType()));
				DataManager.getInstance(MainActivity.this.getApplicationContext()).logSensorData(sensorData);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		public void run()
		{
			try
			{
				Thread.sleep(10000);
				Looper.prepare();

				ESSensorManager esSensorManager = ESSensorManager
						.getSensorManager(MainActivity.this.getApplicationContext());

				DataManager dataManager = DataManager.getInstance(MainActivity.this);

				for (int sensorId : SensorUtils.ALL_SENSORS)
				{
					esSensorManager.subscribeToSensorData(sensorId, this);
				}

				for (int i=0; i<10; i++)
				{
					Thread.sleep(30000);
					for (int sensorId : SensorUtils.ALL_SENSORS)
					{
						try
						{
							List<SensorData> list = dataManager.getRecentSensorData(sensorId, 1000 * 60 * 60 * 24);

							System.out.println("=====================================>>>>> "
									+ SensorUtils.getSensorName(sensorId) + "    size " + list.size());
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}

			}
			catch (Exception exp)
			{
				exp.printStackTrace();
			}
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM))
		{
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
		// When the given tab is selected, show the tab contents in the
		// container
		Fragment fragment = new SensorListFragment();
		Bundle args = new Bundle();
		args.putBoolean(SensorListFragment.SENSOR_TYPE, tab.getPosition() == 0);
		fragment.setArguments(args);

		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
	}
}

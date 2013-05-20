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

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.ubhave.sensormanager.tester.autotest.FileSyncTest;
import com.ubhave.sensormanager.tester.autotest.SensorLoggingTestThread;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
	public static final String TAG = "MainActivity";
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private static boolean enableAutoTest = false;
	private static boolean enableFileSyncTest = false;
	
	private FileSyncTest fileSyncTester;

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
			new SensorLoggingTestThread(this).start();
		}
		if (enableFileSyncTest)
		{
			fileSyncTester = new FileSyncTest(this);
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if (fileSyncTester != null)
		{
			fileSyncTester.subscribe();
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		if (fileSyncTester != null)
		{
			fileSyncTester.unsubscribe();
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

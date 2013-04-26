package com.ubhave.sensormanager.tester.autotest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ubhave.datahandler.DataManager;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.sync.FileUpdatedListener;
import com.ubhave.datahandler.sync.SyncRequest;
import com.ubhave.sensormanager.tester.constants.FileSync;

public class FileSyncTest implements FileUpdatedListener
{
	private final Context context;	
	private int subscriptionId;
	private DataManager dataManager;
	private boolean isSubscribed;
	
	public FileSyncTest(Context context)
	{
		this.context = context;
		try
		{
			dataManager = DataManager.getInstance(context);
			isSubscribed = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void subscribe()
	{
		if (!isSubscribed)
		{
			try
			{
				SyncRequest request = new SyncRequest(context, FileSync.BASE_URL, FileSync.TARGET_FILE);
				request.setSyncInterval(1000 * 15);
				subscriptionId = dataManager.subscribeToRemoteFileUpdate(request, this);
				Toast.makeText(context, "File sync subscribed...", Toast.LENGTH_LONG).show();
				isSubscribed = true;
			}
			catch (DataHandlerException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public void unsubscribe()
	{
		if (isSubscribed)
		{
			try
			{
				dataManager.unsubscribeFromRemoteFileUpdate(subscriptionId);
				Toast.makeText(context, "File sync unsubscribed...", Toast.LENGTH_LONG).show();
				isSubscribed = false;
			}
			catch (DataHandlerException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onFileUpdated()
	{
		Log.d("FileSyncTest", "Remote/local file updated.");
		Toast.makeText(context, "Remote file is updated...!", Toast.LENGTH_LONG).show();
		unsubscribe();
	}
}

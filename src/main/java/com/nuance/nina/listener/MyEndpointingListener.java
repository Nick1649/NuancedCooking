/*
 * 
 * File: MyEndpointingListener.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.listener;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import com.nuance.nina.mmf.listeners.EndOfSpeech;
import com.nuance.nina.mmf.listeners.EndpointingListener;
import com.nuance.nina.mmf.listeners.StartOfSpeech;
import com.nuance.nina.ninasample.MainActivity;
import com.nuance.nina.ninasample.ViewAdapter.Views;
import com.nuance.nina.sample.R;

/**
 * Implementation of EndpointingListener
 * Handles the triggers for start and end of speech
 *
 */
public class MyEndpointingListener implements EndpointingListener{
	private Activity myActivity;
	private Context myContext;

	public  MyEndpointingListener(Activity activity){
		myActivity = activity;
		myContext = myActivity.getApplicationContext();
	}

	/**
	 * Event triggered when the application detects that the user started to speak. Endpointing must be enabled to receive these events.
	 */
	@Override
	public void onStartOfSpeech(StartOfSpeech data) {
		Handler handler = new Handler(Looper.getMainLooper()); 
		handler.post(new Runnable() {
			@Override
			public void run() {
				MainActivity.toast = Toast.makeText(myContext, "On Start Of Speech", Toast.LENGTH_SHORT);
				MainActivity.toast.show();
				((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": Start of speech");
			}
		});
	}

	/**
	 * Event triggered when the application detects that the user stopped to speak. Endpointing must be enabled to receive these events.
	 */
	@Override
	public void onEndOfSpeech(EndOfSpeech data) {
		Handler handler = new Handler(Looper.getMainLooper()); 
		handler.post(new Runnable() {	
			@Override
			public void run() {
				MainActivity.toast = Toast.makeText(myContext, "On End Of Speech", Toast.LENGTH_SHORT);
				MainActivity.toast.show();
				buttonStatus(((MainActivity)myActivity).getCurrentFragment());
				((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": End of speech");
			}
		} );
	}
	
	private void buttonStatus(Views currentFragment){
		switch(currentFragment){
		case SAY:
			final Button listenButton = (Button) myActivity.findViewById(R.id.listenButton);
			listenButton.setEnabled(false);
			break;
		case DICTATE:
			final Button dictationButton = (Button) myActivity.findViewById(R.id.dictationButton);
			dictationButton.setEnabled(false);
			break;
		case TYPE:
			break;
		case HINT:
			break;
		default:
			break;
		}
	}
}
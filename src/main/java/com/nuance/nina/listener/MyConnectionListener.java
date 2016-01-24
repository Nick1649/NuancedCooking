/*
 * 
 * File: MyConnectionListener.java
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
import android.widget.Toast;

import com.nuance.nina.mmf.MMFController;
import com.nuance.nina.mmf.PromptType;
import com.nuance.nina.mmf.listeners.Connect;
import com.nuance.nina.mmf.listeners.ConnectError;
import com.nuance.nina.mmf.listeners.ConnectError.Reason;
import com.nuance.nina.mmf.listeners.ConnectionListener;
import com.nuance.nina.mmf.listeners.ConnectionLost;
import com.nuance.nina.mmf.listeners.Disconnect;
import com.nuance.nina.mmf.listeners.DisconnectError;
import com.nuance.nina.ninasample.MainActivity;

/**
 * Implementation of ConnectionListener
 * Handles the connection state of the application and triggers the events associated to the connection events
 *
 */
public class MyConnectionListener implements ConnectionListener{
	private Activity myActivity;
	private Context myContext;

	public  MyConnectionListener(Activity activity){
		myActivity = activity;
		myContext = myActivity.getApplicationContext();
	}

	/**
	 * Event triggered when the application is connected to Nina. The data object contains the conversation initial state.
	 */
	@Override
	public void onConnect(final Connect data) {
		Handler handler = new Handler(Looper.getMainLooper()); 
		handler.post(new Runnable() {
			@Override
			public void run() {
				MainActivity.toast  = Toast.makeText(myContext, "connected!!!", Toast.LENGTH_SHORT);
				MainActivity.toast.show();
				MMFController.getInstance().playPrompt("Welcome to Nina", PromptType.TEXT);
				((MainActivity) myActivity).buttonStatus(true);
				((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": Connected");
			}
		} );
	}

	/**
	 * Event triggered when the connetion to the server fails
	 */
	@Override
	public void onConnectError(final ConnectError data) {
		Handler handler = new Handler(Looper.getMainLooper()); 
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (data.reason == Reason.ILLEGAL_STATE_CONNECTED){
					((MainActivity) myActivity).buttonStatus(true);
					MainActivity.setConnected(true);
				}
				else{
					MainActivity.toast  = Toast.makeText(myContext, "error on connect", Toast.LENGTH_SHORT);
					MainActivity.toast.show();
					((MainActivity) myActivity).buttonStatus(false);
					((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": Connect error");
					((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": " + data.reason.toString());
				}
			}
		} );
	}

	/**
	 * Event triggered if the connection is lost for example due to session expiration.
	 */
	@Override
	public void onConnectionLost(final ConnectionLost data) {
		Handler handler = new Handler(Looper.getMainLooper()); 
		handler.post(new Runnable() {
			@Override
			public void run() {
				MainActivity.toast  = Toast.makeText(myContext, "connection lost", Toast.LENGTH_SHORT);
				MainActivity.toast.show();
				((MainActivity) myActivity).buttonStatus(false);
				((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": Connection lost");
				((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": " + data.reason.toString());
			}
		} );
	}

	/**
	 * Connection triggered when the application is disconnected from Nina
	 */
	@Override
	public void onDisconnect(Disconnect data) {
		Handler handler = new Handler(Looper.getMainLooper()); 
		handler.post(new Runnable() {
			@Override
			public void run() {
            	MainActivity.toast = Toast.makeText(myContext, "disconnected", Toast.LENGTH_SHORT);
				MainActivity.toast.show();
				((MainActivity) myActivity).buttonStatus(false);
				((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": Disconnected");
			}
		} );
	}

	/**
	 * Event triggered if there is a disconnection error for example if attempting to call disconnect from the DISCONNECTED state
	 */
	@Override
	public void onDisconnectError(final DisconnectError data) {
		Handler handler = new Handler(Looper.getMainLooper()); 
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (((MainActivity)myActivity).isConnected()){
					MainActivity.toast = Toast.makeText(myContext, "disconnection error", Toast.LENGTH_SHORT);
					MainActivity.toast.show();
					((MainActivity) myActivity).buttonStatus(true);
					((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": Disconnection error");
					((MainActivity)myActivity).log(DateFormat.getDateTimeInstance().format(new Date()) + ": " + data.reason.toString());
				}
			}
		} );
	}
}
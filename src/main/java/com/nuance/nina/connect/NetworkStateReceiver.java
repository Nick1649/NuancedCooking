/*
 * 
 * File: NetworkStateReceiver.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nuance.nina.configurations.NinaSetup;
import com.nuance.nina.mmf.MMFController;
import com.nuance.nina.ninasample.MainActivity;

/**
 * Data connection listener. It will notify the application when the Internet connection status has changed
 *
 */
public class NetworkStateReceiver extends BroadcastReceiver {
	
	public enum ConnectivityStatus {
		TYPE_NOT_CONNECTED,
		TYPE_WIFI,
		TYPE_MOBILE
	};

    @Override
    public void onReceive(Context context, Intent intent)
    {
    	if (NinaSetup.isActivityVisible()){
            switch(getConnectivityStatus(context)){
                case TYPE_NOT_CONNECTED:
                    MMFController.getInstance().disconnect();
                    MainActivity.connectButtonStatus(false);
                    break;
                case TYPE_WIFI:
                case TYPE_MOBILE:
                    MainActivity.connectButtonStatus(true);
                    break;
            }
    	}
    }

    /**
     * Method to get the connection status, it returns the type (WIFI, MOBILE, NO INTERNET)
     * @param context Application Context
     * @return integer value that represent the connection status
     */
    public static ConnectivityStatus getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return ConnectivityStatus.TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return ConnectivityStatus.TYPE_MOBILE;
        } 
        return ConnectivityStatus.TYPE_NOT_CONNECTED;
    }
}


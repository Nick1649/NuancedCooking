package com.nuance.nina.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.nuance.nina.mmf.MMFController;
import com.nuance.nina.mmf.listeners.Connect;
import com.nuance.nina.mmf.listeners.ConnectError;
import com.nuance.nina.mmf.listeners.ConnectionListener;
import com.nuance.nina.mmf.listeners.ConnectionLost;
import com.nuance.nina.mmf.listeners.Disconnect;
import com.nuance.nina.mmf.listeners.DisconnectError;

public class RecipeDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // TODO: Make connect function call work; establish Nina connection for recipe
        /*final ConnectionListener connectionWatcher = new ConnectionListener(){
            // Unregister temporary listener and re-enable connect button
            void finishListening() {
                MMFController.getInstance().getObserver().unregisterConnectionListener(this);
            }
            @Override
            public void onConnect(Connect data) {
                finishListening();
            }
            @Override
            public void onConnectError(ConnectError data) {
                finishListening();
            }
            @Override
            public void onConnectionLost(ConnectionLost data) {
                finishListening();
            }
            @Override
            public void onDisconnect(Disconnect data) {
                finishListening();
            }
            @Override
            public void onDisconnectError(DisconnectError data) {
                finishListening();
            }
        };
        MMFController.getInstance().getObserver().registerConnectionListener(connectionWatcher);
        connect();*/
    }
}

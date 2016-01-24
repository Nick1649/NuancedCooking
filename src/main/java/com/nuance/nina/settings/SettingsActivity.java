/*
 * 
 * File: SettingsActivity.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.settings;

import java.util.ArrayList;

import com.nuance.nina.configurations.NinaSetup;
import com.nuance.nina.hint.MySpinnerAdapter;
import com.nuance.nina.sample.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Activity class for the settings activity
 *
 */
public class SettingsActivity extends Activity {

	String locale = null;

	private Toast toast;

	private int WAS_CONNECTED = 1;
	private int WAS_NOT_CONNECTED = 0;
	boolean connected;

	private EditText applicationName;
	private EditText applicationVersion;
	private EditText companyName;
	private EditText gatewayHost;
	private EditText gatewayPort;
	private EditText serverAppName;
	private EditText voiceInput;
	private Spinner localeSpinner;
	private EditText nmaid_id;
	private EditText nmaid_key;
	private Button saveSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			connected = extras.getBoolean("connected");
		}
		applicationName = (EditText) findViewById(R.id.applicationNameInput);
		applicationVersion = (EditText) findViewById(R.id.applicationVersionInput);
		companyName = (EditText) findViewById(R.id.companyNameInput);
		gatewayHost = (EditText) findViewById(R.id.gatewayHostInput);
		gatewayPort = (EditText) findViewById(R.id.gatewayPortInput);
		serverAppName = (EditText) findViewById(R.id.serverApplicationNameInput);
		voiceInput = (EditText) findViewById(R.id.voiceInput);
		nmaid_id = (EditText) findViewById(R.id.nmaidIDInput);
		nmaid_key = (EditText) findViewById(R.id.nmaidKeyInput);
		localeSpinner = (Spinner) findViewById(R.id.localeSpinner);
		
		//Spinner locale options
		ArrayList<String> items = new ArrayList<String>();
		items.add("en_US");
		items.add("tr_TR");
		items.add("Choose a locale"); // Last item, hidden from the list

		MySpinnerAdapter adapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		localeSpinner.setAdapter(adapter);

		localeSpinner.setSelection(items.size() - 1);
		localeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				locale = parent.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				locale = null;
			}

		});

		setSettings();

		//Save button and return to home page
		saveSettings = (Button) findViewById(R.id.saveSettings);
		saveSettings.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				saveSettings();
			} 
		});

		toast = new Toast(getApplicationContext());
	}

	/**
	 * Method called to save the new settings
	 */
	public void saveSettings(){
		if(locale != null){
			NinaSetup.setTTS_VOICE_LOCALE(locale);
		}
		if(!applicationName.getText().toString().isEmpty()){
			NinaSetup.setApplicationName(applicationName.getText().toString());
		}
		if(!applicationVersion.getText().toString().isEmpty()){
			NinaSetup.setApplicationVersion(applicationVersion.getText().toString());
		}
		if(!companyName.getText().toString().isEmpty()){
			NinaSetup.setCompanyName(companyName.getText().toString());
		}
		if(!gatewayHost.getText().toString().isEmpty()){
			NinaSetup.setGatewayHost(gatewayHost.getText().toString());
		}
		if(!gatewayPort.getText().toString().isEmpty()){
			NinaSetup.setGatewayPort(Integer.parseInt(gatewayPort.getText().toString()));
		}
		if(!serverAppName.getText().toString().isEmpty()){
			NinaSetup.setServerApplicationName(serverAppName.getText().toString());
		}
		if(!voiceInput.getText().toString().isEmpty()){
			NinaSetup.setTTS_VOICE_NAME(voiceInput.getText().toString());
		}
		if(!nmaid_id.getText().toString().isEmpty()){
			NinaSetup.setNmaidId(nmaid_id.getText().toString());
		}
		if(!nmaid_key.getText().toString().isEmpty()){
			NinaSetup.setNmaid_key_string(nmaid_key.getText().toString());
			NinaSetup.setNmaidKey(NinaSetup.hexStringToByteArray(nmaid_key.getText().toString()));
		}

		if (connected){
			Intent returnIntent = new Intent();
			returnIntent.putExtra("settings",true);
			setResult(WAS_CONNECTED,returnIntent);
			finish();
		}
		else{
			Intent returnIntent = new Intent();
			returnIntent.putExtra("settings",false);
			setResult(WAS_NOT_CONNECTED,returnIntent);
			finish();
		}
	}

	/**
	 * Method called to set the settings fields
	 */
	public void setSettings(){
		applicationName.setText(NinaSetup.getApplicationName());
		applicationVersion.setText(NinaSetup.getApplicationVersion());
		companyName.setText(NinaSetup.getCompanyName());
		gatewayHost.setText(NinaSetup.getGatewayHost());
		gatewayPort.setText(String.valueOf(NinaSetup.getGatewayPort()));
		serverAppName.setText(NinaSetup.getServerApplicationName());
		voiceInput.setText(NinaSetup.getTTS_VOICE_NAME());

		nmaid_id.setText(NinaSetup.getNmaidId());

		nmaid_key.setText(NinaSetup.getNmaid_key_string());

		if(NinaSetup.getTTS_VOICE_LOCALE() == "en_US"){
			localeSpinner.setSelection(0);
		}else if(NinaSetup.getTTS_VOICE_LOCALE() == "tr_TR"){
			localeSpinner.setSelection(1);
		}
	}

	@Override
	protected void onStop(){
		super.onStop();
		finish();

		toast.cancel();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		finish();

		toast.cancel();
	}

	@Override
	protected void onPause(){
		super.onPause();
		finish();

		toast.cancel();
	}

	@Override
	protected void onRestart(){
		super.onRestart();
		finish();
	}
}

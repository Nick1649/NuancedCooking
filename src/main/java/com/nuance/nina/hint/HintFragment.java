/*
 * 
 * File: HintFragment.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.hint;

import java.util.ArrayList;

import com.nuance.nina.mmf.MMFController;
import com.nuance.nina.mmf.MMFController.TextSourceType;
import com.nuance.nina.sample.R;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

/**
 * Fragment class for the Hint view
 *
 */
public class HintFragment extends Fragment {

	private static boolean playing = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_hint, container, false);

		//Spinner listenner
		final Spinner hintSpinner = (Spinner) rootView.findViewById(R.id.hintSpinner);
		final ArrayList<String> items = new ArrayList<String>();
		items.add("Hello nina");
		items.add("Help");
        items.add("Repeat");
        items.add("Choose a hint"); // Last item 

        MySpinnerAdapter adapter = new MySpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintSpinner.setAdapter(adapter);
        
        hintSpinner.setSelection(items.size() - 1);
		hintSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//To get meaning of text from Nina we call this method, it takes 2 arguments: 
				//	1 - Text to find the meaning
				//  2 - Type of the text (here the type is HINT because we are selecting the text from predefined spinner
				if (position != items.size() - 1){
					MMFController.getInstance().findMeaning(parent.getItemAtPosition(position).toString(), TextSourceType.HINT);
					hintSpinner.setEnabled(false);
					setTTSPlaying(true);	
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		return rootView;
	}

	/**
	 * Check if TTS audio is playing
	 * @return boolean playing (true if TTS audio is playing audio, false if no TTS audio is playing)
	 */
	public static boolean isTTSPlaying() {
		return playing;
	}
	/**
	 * Set TTS audio playing value
	 * @param newPlayingValue new TTS audio playing value
	 */
	public static void setTTSPlaying(boolean newPlayingValue) {
		playing = newPlayingValue;
	}
}

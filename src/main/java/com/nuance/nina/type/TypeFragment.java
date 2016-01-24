/*
 * 
 * File: TypeFragment.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.type;

import com.nuance.nina.mmf.MMFController;
import com.nuance.nina.mmf.MMFController.TextSourceType;
import com.nuance.nina.sample.R;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Fragment class for the Type view
 *
 */
public class TypeFragment extends Fragment {

	private static boolean playing = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_type, container, false);

		final Button sendButton = (Button) rootView.findViewById(R.id.sendButton);
		final EditText typeText = (EditText) rootView.findViewById(R.id.typeText);

		if (typeText.getText().toString().isEmpty()){
			sendButton.setEnabled(false);
		}
		else{
			sendButton.setEnabled(true);
		}
		//Done key on soft keyboard
		typeText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (v.length() > 0){
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						sendTextForInterpretation(typeText);
						return true;
					}
					return false;
				}
				return false;
			}
		});
		//Listener on text input to enable/disable button
		typeText.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if(s.toString().trim().length() != 0 ){
					sendButton.setEnabled(true);
				}
				else{
					sendButton.setEnabled(false);
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		});

		sendButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sendTextForInterpretation(typeText);
				sendButton.setEnabled(false);
			}
		});

		return rootView;
	}

	public void sendTextForInterpretation(EditText typeText){
		//Send text to get meaning
		MMFController.getInstance().findMeaning(typeText.getText().toString(), TextSourceType.USER_TEXT);
		//Hide keyboard
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(typeText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		
		setTTSPlaying(true);
		typeText.setEnabled(false);
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

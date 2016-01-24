/*
 * 
 * File: PlayFragment.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.play;

import com.nuance.nina.mmf.MMFController;
import com.nuance.nina.mmf.PromptType;
import com.nuance.nina.ninasample.MainActivity;
import com.nuance.nina.sample.R;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * Fragment class for the Play view
 *
 */
public class PlayFragment extends Fragment {

    private int checked;
    private EditText playText;
    private static boolean playing = false;
    String URL = "https://mt-cs18-mmf01.nuance.com:12443/vocalizer-docs/MMF_plainText.txt";
    String SSML = "<speak version=\"1.0\" xmlns=\"http://www.w3.org/2001/10/synthesis\" xml:lang=\"en-US\"><voice name=\"Carol\"> this Is ssml</voice></speak>";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_play, container, false);
        playText = (EditText) rootView.findViewById(R.id.playText);

        final Button playButton = (Button) rootView.findViewById(R.id.playButton);
        playButton.setEnabled(true);

        final Button stopButton = (Button) rootView.findViewById(R.id.stopButton);
        stopButton.setEnabled(false);

        final RadioGroup optionsRadio = (RadioGroup) rootView.findViewById(R.id.optionsRadio);
        optionsRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton) rootView.findViewById(checkedId);
                changeText(rb);
            }
        });

        //Play button
        playButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Hide keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(playText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                
                if (((MainActivity)getActivity()).isConnected())
                {
                    String text = "";
                    playText.setEnabled(false);
                    switch(checked){
                        case 0:
                            text = playText.getText().toString();
                            setTTSPlaying(true);
                            MMFController.getInstance().playPrompt(text, PromptType.TEXT);
                            stopButton.setEnabled(true);
                            playButton.setEnabled(false);
                            break;
                        case 1:
                            text = playText.getText().toString();
                            setTTSPlaying(true);
                            MMFController.getInstance().playPrompt(text, PromptType.SSML);
                            stopButton.setEnabled(true);
                            playButton.setEnabled(false);
                            break;
                        case 2:
                            text = playText.getText().toString();
                            setTTSPlaying(true);
                            MMFController.getInstance().playPrompt(text, PromptType.URI);
                            stopButton.setEnabled(true);
                            playButton.setEnabled(false);
                            break;
                    }
                }
            }
        });
        //Stop button
        stopButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Hide keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(playText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                setTTSPlaying(false);
                MMFController.getInstance().stopPrompts();
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                playText.setEnabled(true);
            }
        });

        playText.setText("This is a plain Text");

        return rootView;
    }
    protected void changeText(RadioButton rb) {
        if (rb.getText().equals("SSML")){
            playText.setText(SSML);
            checked = 1;
        }
        else if (rb.getText().equals("URL")){
            playText.setText(URL);
            checked = 2;
        }
        else{
            playText.setText("This is a plain Text");
            checked = 0;
        }
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

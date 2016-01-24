/*
 * 
 * File: SayFragment.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.say;

import com.nuance.nina.mmf.MMFController;
import com.nuance.nina.sample.R;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Fragment class for the Say view
 *
 */
public class SayFragment extends Fragment {

    private static boolean recording = false;
    private static boolean doneRecording = true;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_say, container, false);

        final TextView resultSayLabel = (TextView) rootView.findViewById(R.id.resultSayLabel);

        //listen button
        final Button listenButton = (Button) rootView.findViewById(R.id.listenButton);
        listenButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!doneRecording){
                    MMFController.getInstance().stopRecordingAudio();
                    listenButton.setText("Listen");
                    listenButton.setEnabled(false);
                    doneRecording = true;
                }
                else{
                    MMFController.getInstance().startListeningForMeaning();
                    listenButton.setText("Done");
                    doneRecording = false;
                }
            }
        });

        //cancel button
        final Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MMFController.getInstance().cancelInterpretation();
                MMFController.getInstance().stopPrompts();
                listenButton.setText("Listen");
                listenButton.setEnabled(true);
                recording = false;
                doneRecording = true;
                resultSayLabel.setText("");
            }
        });
        return rootView;
    }
	
	/**
	 * Check if recording
	 * @return
	 */
    public static boolean isRecording() {
		return recording;
	}
    /**
     * Set recording value
     * @param newRecordingValue
     */
	public static void setRecording(boolean newRecordingValue) {
		recording = newRecordingValue;
	}
	
	/**
	 * Check if done recording
	 * @return
	 */
	public static boolean isDoneRecording() {
		return doneRecording;
	}
    /**
     * Set done recording value
     * @param newDoneRecordingValue
     */
	public static void setDoneRecording(boolean newDoneRecordingValue) {
		doneRecording = newDoneRecordingValue;
	}
}

/*
 * 
 * File: DictationFragment.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.dictation;

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
 * Fragment class for the Dictation view
 *
 */
public class DictationFragment extends Fragment {

    private static boolean recording = false;
    private static boolean doneRecording = true;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dictation, container, false);

        final TextView resultDictationLabel = (TextView) rootView.findViewById(R.id.resultDictationLabel);

        //listen button
        final Button dictationButton = (Button) rootView.findViewById(R.id.dictationButton);
        dictationButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!doneRecording){
                    MMFController.getInstance().stopRecordingAudio();
                    dictationButton.setText("Dictate");
                    dictationButton.setEnabled(false);
                    doneRecording = true;
                }
                else{
                    MMFController.getInstance().startListeningForRecognition();
                    dictationButton.setText("Done");
                    doneRecording = false;
                }
            }
        });

        //cancel button
        final Button cancelDictationButton = (Button) rootView.findViewById(R.id.cancelDictationButton);
        cancelDictationButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MMFController.getInstance().cancelInterpretation();
                MMFController.getInstance().stopPrompts();
                dictationButton.setText("Dictate");
                recording = false;
                dictationButton.setEnabled(true);
                doneRecording = true;
                resultDictationLabel.setText("");
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
     * @param value
     */
	public static void setRecording(boolean value) {
		recording = value;
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

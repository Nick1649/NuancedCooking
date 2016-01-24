package com.nuance.nina.ninasample;

import java.util.Locale;

import com.nuance.nina.configurations.NinaSetup;
import com.nuance.nina.connect.NetworkStateReceiver;
import com.nuance.nina.dictation.DictationFragment;
import com.nuance.nina.hint.HintFragment;
import com.nuance.nina.mmf.MMFController;
import com.nuance.nina.mmf.listeners.Connect;
import com.nuance.nina.mmf.listeners.ConnectError;
import com.nuance.nina.mmf.listeners.ConnectionListener;
import com.nuance.nina.mmf.listeners.ConnectionLost;
import com.nuance.nina.mmf.listeners.Disconnect;
import com.nuance.nina.mmf.listeners.DisconnectError;
import com.nuance.nina.ninasample.ViewAdapter.Views;
import com.nuance.nina.observer.MyObserver;

import android.app.Activity;
import android.view.View.OnLongClickListener;
import com.nuance.nina.play.PlayFragment;
import com.nuance.nina.sample.R;
import com.nuance.nina.say.SayFragment;
import com.nuance.nina.settings.SettingsActivity;
import com.nuance.nina.type.TypeFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private static boolean connected;
	private MyObserver myObserver;
	private Views currentFragment = ViewAdapter.Views.CONNECT;
	private int SETTINGS = 1;
	private int WAS_CONNECTED = 1;
	private int WAS_NOT_CONNECTED = 0;

	public static Toast toast;

	private TextView log;
	private ScrollView scrollViewLog;

	private PlayFragment playFragment = new PlayFragment();
	private HintFragment hintFragment = new HintFragment();
	private SayFragment sayFragment = new SayFragment();
	private DictationFragment dictateFragment = new DictationFragment();
	private TypeFragment typeFragment = new TypeFragment();


	private static Button settingsButton;
	private static Button connectButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		connected = false;

		//Check if the device locale is "en_US" because the Nina Server are setup to "en_US"
		Locale lDevice = Resources.getSystem().getConfiguration().locale;
		Locale lUs = new Locale("en", "US");
		if (!lDevice.equals(lUs)){
			//Set the device locale to "en_US"
			Locale.setDefault(lUs);
		}
		connectButton = (Button) findViewById(R.id.connectButton);

		//Initialize all the observers with the activity
		myObserver = new MyObserver(this);
		myObserver.registerListeners(); 

		registerButtons();

		//Initial internet check
		detectNetworkStatus();
		NinaSetup.activityResumed();

		toast = new Toast(getApplicationContext());

		LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
		frameLayout.setBackgroundResource(R.drawable.no_border);

		TextView  hiddenTextView = (TextView ) findViewById(R.id.hiddenTextView );
		hiddenTextView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				settingsButton.setVisibility(View.VISIBLE);
				return true;
			}
		});
	}

	/**
	 * Checks if the application is connected to Nina
	 * @return
	 */
	public boolean isConnected(){
		return connected;
	}
	/**
	 * Sets the connection state of the application
	 * @param value
	 */
	public static void setConnected(boolean value){
		connected = value;
	}

	@Override
	protected void onStop(){
		super.onStop();
		toast.cancel();
		settingsButton.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		myObserver.unregisterListeners();
		MMFController.getInstance().stopPrompts();
		MMFController.getInstance().disconnect();
		NinaSetup.activityPaused();
		toast.cancel();
		settingsButton.setVisibility(View.GONE);
	}

	@Override
	protected void onPause(){
		super.onPause();
		settingsButton.setVisibility(View.GONE);
		// Store values between instances here
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI
		editor.putBoolean("connected", connected); 
		// Commit to storage
		editor.commit();
		toast.cancel();
		MMFController.getInstance().stopPrompts();
		switch(currentFragment){
		case SAY:
			if (SayFragment.isRecording()){
				MMFController.getInstance().cancelInterpretation();
				SayFragment.setRecording(false);
				MMFController.getInstance().stopPrompts();
			}
			break;
		case DICTATE:
			if (DictationFragment.isRecording()){
				MMFController.getInstance().cancelInterpretation();
				DictationFragment.setRecording(false);
				MMFController.getInstance().stopPrompts();
			}
			break;
		default:
			break;
		}
	}


	@Override
	protected void onRestart(){
		super.onRestart();
		NinaSetup.activityResumed();
		settingsButton.setVisibility(View.GONE);
	}

	@Override
	protected void onResume(){
		super.onResume();
		NinaSetup.activityResumed();
		settingsButton.setVisibility(View.GONE);
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		savedInstanceState.putBoolean("connected", connected);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		connected = savedInstanceState.getBoolean("connected");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		settingsButton.setVisibility(View.GONE);
		if (requestCode == SETTINGS) {
			if(resultCode == WAS_CONNECTED){
				MMFController.getInstance().disconnect();
				NinaSetup.fillNinaConfig();
				Handler handler = new Handler(Looper.getMainLooper()); 
				handler.post(new Runnable() {
					@Override
					public void run() {
						//To connect to Nina we call this method, it takes 2 arguments: 
						//	1 - The application Context
						//  2 - The Cloud Application Info
						MMFController.getInstance().connect(getApplicationContext(), NinaSetup.getCloudApplicationInfo());
					}
				} );
			}
			else if (resultCode == WAS_NOT_CONNECTED) {
				NinaSetup.fillNinaConfig();
			}
		}
	}
	/**
	 * Method to log status of Nina
	 * @param message
	 */
	public void log(String message){
		log = (TextView) findViewById(R.id.log);
		scrollViewLog = (ScrollView) findViewById(R.id.scrollViewLog);

		log.setText(log.getText()+"\n"+message);
		sendScroll(scrollViewLog);
	}
	/**
	 * Method to clear log
	 * 
	 */
	public void clearLog(){
		log = (TextView) findViewById(R.id.log);
		scrollViewLog = (ScrollView) findViewById(R.id.scrollViewLog);

		log.setText("");
		sendScroll(scrollViewLog);
	}

	/**
	 * Method to scroll the log to botom
	 * @param scrollView
	 */
	private void sendScroll(final ScrollView scrollView){
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {Thread.sleep(100);} catch (InterruptedException e) {}
				handler.post(new Runnable() {
					@Override
					public void run() {
						scrollView.fullScroll(View.FOCUS_DOWN);
					}
				});
			}
		}).start();
	}
	/**
	 * Method to register the buttons to the click listeners
	 */
	private void registerButtons(){
		//Connection button action
		final Button connectButton = (Button) findViewById(R.id.connectButton);
		connectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final ConnectionListener connectionWatcher = new ConnectionListener(){
					// Unregister temporary listener and re-enable connect button
					void finishListening() {
						MMFController.getInstance().getObserver().unregisterConnectionListener(this);
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							@Override
							public void run() {
								connectButton.setEnabled(true);
							}
						} );
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
				connectButton.setEnabled(false);
				connect();
			} 
		});

		//Settings button action
		settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setVisibility(View.GONE);
		settingsButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MMFController.getInstance().stopPrompts();
				MMFController.getInstance().cancelInterpretation();
				Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
				settings.putExtra("connected", connected);
				startActivityForResult(settings, SETTINGS);
			} 
		});

		//Say button action
		final Button fragmentSayButton = (Button) findViewById(R.id.fragmentSayButton);
		fragmentSayButton.setEnabled(false);
		fragmentSayButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hideKeyBoard();
				removeFragment();
				setCurrentFragment(ViewAdapter.Views.SAY);
				sayFragment = new SayFragment();
				addfragment(sayFragment ,false,FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				clearLog();
				disableCurrentFragmentButton();
				SayFragment.setRecording(false);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.border);
			} 
		});

		//Dictate button action
		final Button fragmentDictateButton = (Button) findViewById(R.id.fragmentDictationButton);
		fragmentDictateButton.setEnabled(false);
		fragmentDictateButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hideKeyBoard();
				removeFragment();
				setCurrentFragment(ViewAdapter.Views.DICTATE);
				dictateFragment = new DictationFragment();
				addfragment(dictateFragment ,false,FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				clearLog();
				disableCurrentFragmentButton();
				DictationFragment.setRecording(false);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.border);
			} 
		});

		//Hint button action
		final Button fragmentHintButton = (Button) findViewById(R.id.fragmentHintButton);
		fragmentHintButton.setEnabled(false);
		fragmentHintButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hideKeyBoard();
				removeFragment();
				setCurrentFragment(ViewAdapter.Views.HINT);
				hintFragment = new HintFragment();
				addfragment(hintFragment,false,FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				clearLog();
				disableCurrentFragmentButton();

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.border);
			} 
		});

		//Type button action
		final Button fragmentTypeButton = (Button) findViewById(R.id.fragmentTypeButton);
		fragmentTypeButton.setEnabled(false);
		fragmentTypeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hideKeyBoard();
				removeFragment();
				setCurrentFragment(ViewAdapter.Views.TYPE);
				typeFragment = new TypeFragment();
				addfragment(typeFragment,false,FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				clearLog();
				disableCurrentFragmentButton();
				TypeFragment.setTTSPlaying(false);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.border);
			} 
		});

		//Play button action
		final Button fragmentPlayButton = (Button) findViewById(R.id.fragmentPlayButton);
		fragmentPlayButton.setEnabled(false);
		fragmentPlayButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hideKeyBoard();
				removeFragment();
				setCurrentFragment(ViewAdapter.Views.PLAY);
				playFragment = new PlayFragment();
				addfragment(playFragment,false,FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				clearLog();
				disableCurrentFragmentButton();
				PlayFragment.setTTSPlaying(false);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.border);
			} 
		});
	}

	public void hideKeyBoard(){
		//Hide keyboard
		switch(currentFragment){

		case TYPE:
			EditText typeText = (EditText) findViewById(R.id.typeText);
			InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm1.hideSoftInputFromWindow(typeText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
			break;
		case PLAY:
			EditText playText = (EditText) findViewById(R.id.playText);
			InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm2.hideSoftInputFromWindow(playText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
			break;
		default:
			break;
		}

	}
	/**
	 * Method to add the fragment to the framelayout
	 * @param fragment
	 * @param addBacktoStack
	 * @param transition
	 */
	public void addfragment(Fragment fragment, boolean addBacktoStack, int transition) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment, fragment);
		ft.setTransition(transition);
		if (addBacktoStack)
			ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * Method to remove the fragment from the framelyout before adding the new fragment
	 */
	public void removeFragment(){
		MMFController.getInstance().stopPrompts();
		MMFController.getInstance().cancelInterpretation();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch(currentFragment){
		case SAY:
			if (sayFragment != null) {   
				if (SayFragment.isRecording()){
					SayFragment.setRecording(false);
					MMFController.getInstance().stopRecordingAudio();
				}
				transaction.remove(sayFragment);
				transaction.commitAllowingStateLoss();
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				sayFragment = null;
				setCurrentFragment(ViewAdapter.Views.CONNECT);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.no_border);
			}
			break;
		case DICTATE:
			if (dictateFragment != null) {   
				if (DictationFragment.isRecording()){
					DictationFragment.setRecording(false);
					MMFController.getInstance().stopRecordingAudio();
				}
				transaction.remove(dictateFragment);
				transaction.commitAllowingStateLoss();
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				dictateFragment = null;
				setCurrentFragment(ViewAdapter.Views.CONNECT);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.no_border);
			}
			break;
		case HINT:
			if (hintFragment != null) { 
				if (HintFragment.isTTSPlaying()){
					HintFragment.setTTSPlaying(false);
				}
				transaction.remove(hintFragment);
				transaction.commitAllowingStateLoss();
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				hintFragment = null;
				setCurrentFragment(ViewAdapter.Views.CONNECT);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.no_border);
			}
			break;
		case TYPE:
			if (typeFragment != null) {         
				if (TypeFragment.isTTSPlaying()){
					TypeFragment.setTTSPlaying(false);
				}
				transaction.remove(typeFragment);
				transaction.commitAllowingStateLoss();
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				typeFragment = null;
				setCurrentFragment(ViewAdapter.Views.CONNECT);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.no_border);
			}
			break;
		case PLAY:
			if (playFragment != null) {  
				if (PlayFragment.isTTSPlaying()){
					PlayFragment.setTTSPlaying(false);
				}
				transaction.remove(playFragment);
				transaction.commitAllowingStateLoss();
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				playFragment = null;
				setCurrentFragment(ViewAdapter.Views.CONNECT);

				LinearLayout frameLayout = (LinearLayout) findViewById(R.id.lineralLayoutFrame);
				frameLayout.setBackgroundResource(R.drawable.no_border);
			}
			break;
		default:
			break;
		}

	}
	/**
	 * Method to connect to Nina
	 */
	private void connect(){
		if (isConnected())
		{
			MMFController.getInstance().disconnect();
			toast = Toast.makeText(getApplicationContext(), "Diconnecting...", Toast.LENGTH_SHORT);
			toast.show();

		}
		else 
		{
			MMFController.getInstance().connect(getApplicationContext(), NinaSetup.getCloudApplicationInfo());
			toast = Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	/**
	 * Gets the current fragment visible
	 * @return
	 */
	public Views getCurrentFragment() {
		return currentFragment;
	}
	/**
	 * Sets the current fragments visible
	 * @param current
	 */
	public void setCurrentFragment(Views current) {
		this.currentFragment = current;
	}
	/**
	 * Disable the current fragment button
	 */
	public void disableCurrentFragmentButton(){
		final Button fragmentPlayButton = (Button) findViewById(R.id.fragmentPlayButton);
		final Button fragmentSayButton = (Button) findViewById(R.id.fragmentSayButton);
		final Button fragmentDictationButton = (Button) findViewById(R.id.fragmentDictationButton);
		final Button fragmentTypeButton = (Button) findViewById(R.id.fragmentTypeButton);
		final Button fragmentHintButton = (Button) findViewById(R.id.fragmentHintButton);
		switch(currentFragment){
		case PLAY:
			fragmentPlayButton.setEnabled(false);
			fragmentSayButton.setEnabled(true);
			fragmentDictationButton.setEnabled(true);
			fragmentTypeButton.setEnabled(true);
			fragmentHintButton.setEnabled(true);			
			break;
		case HINT:
			fragmentPlayButton.setEnabled(true);
			fragmentSayButton.setEnabled(true);
			fragmentDictationButton.setEnabled(true);
			fragmentTypeButton.setEnabled(true);
			fragmentHintButton.setEnabled(false);	
			break;
		case DICTATE:
			fragmentPlayButton.setEnabled(true);
			fragmentSayButton.setEnabled(true);
			fragmentDictationButton.setEnabled(false);
			fragmentTypeButton.setEnabled(true);
			fragmentHintButton.setEnabled(true);	
			break;
		case SAY:
			fragmentPlayButton.setEnabled(true);
			fragmentSayButton.setEnabled(false);
			fragmentDictationButton.setEnabled(true);
			fragmentTypeButton.setEnabled(true);
			fragmentHintButton.setEnabled(true);	
			break;
		case TYPE:
			fragmentPlayButton.setEnabled(true);
			fragmentSayButton.setEnabled(true);
			fragmentDictationButton.setEnabled(true);
			fragmentTypeButton.setEnabled(false);
			fragmentHintButton.setEnabled(true);	
			break;
		default:
			break;
		}
	}
	/**
	 * Change the connection button status depending on the Internet connection
	 * @param internet
	 */
	public static void connectButtonStatus(boolean internet){
		connectButton.setEnabled(internet);
	}
	/**
	 * Change the button status depending on the connection
	 * @param connected
	 */
	public void buttonStatus(boolean connected){
		if (connected){
			setConnected(true);
			final Button connectButton = (Button) findViewById(R.id.connectButton);
			connectButton.setText("Disconnect");

			final Button fragmentHintButton = (Button) findViewById(R.id.fragmentHintButton);
			fragmentHintButton.setEnabled(true);

			final Button fragmentTypeButton = (Button) findViewById(R.id.fragmentTypeButton);
			fragmentTypeButton.setEnabled(true);

			final Button fragmentSayButton = (Button) findViewById(R.id.fragmentSayButton);
			fragmentSayButton.setEnabled(true);

			final Button fragmentDictationButton = (Button) findViewById(R.id.fragmentDictationButton);
			fragmentDictationButton.setEnabled(true);

			final Button fragmentPlayButton = (Button) findViewById(R.id.fragmentPlayButton);
			fragmentPlayButton.setEnabled(true);
		}
		else{
			setConnected(false);
			removeFragment();
			final Button connectButton = (Button) findViewById(R.id.connectButton);
			connectButton.setText("Connect");

			final Button fragmentHintButton = (Button) findViewById(R.id.fragmentHintButton);
			fragmentHintButton.setEnabled(false);

			final Button fragmentTypeButton = (Button) findViewById(R.id.fragmentTypeButton);
			fragmentTypeButton.setEnabled(false);

			final Button fragmentSayButton = (Button) findViewById(R.id.fragmentSayButton);
			fragmentSayButton.setEnabled(false);

			final Button fragmentDictationButton = (Button) findViewById(R.id.fragmentDictationButton);
			fragmentDictationButton.setEnabled(false);

			final Button fragmentPlayButton = (Button) findViewById(R.id.fragmentPlayButton);
			fragmentPlayButton.setEnabled(false);
		}
	}

	/**
	 * Checks if there's data connection, used on different lifecycle of the activity
	 */
	public void detectNetworkStatus(){
		switch(NetworkStateReceiver.getConnectivityStatus(getApplicationContext())){
		case TYPE_NOT_CONNECTED:
			buttonStatus(false);
			connectButtonStatus(false);
			break;
		case TYPE_WIFI:
		case TYPE_MOBILE:
			buttonStatus(connected);
			connectButtonStatus(true);
			break;
		}
	}
}

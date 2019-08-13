package edu.luc.etl.cs313.android.simplestopwatch.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIUpdateListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.ConcreteStopwatchModelFacade;
import edu.luc.etl.cs313.android.simplestopwatch.model.StopwatchModelFacade;

/**
 * A thin adapter component for the stopwatch.
 *
 * @author Elena Voinu
 */
public class StopwatchAdapter extends Activity implements StopwatchUIUpdateListener {

    private static String TAG = "stopwatch-android-activity";

    /**
     * The state-based dynamic model.
     */

    private StopwatchModelFacade model;

    // use EditText to accept text input from a user
    private EditText et;   //  variable for editable text

    private int typedTime; //variable for editable text

    protected void setModel(final StopwatchModelFacade model) {
        this.model = model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inject dependency on view so this adapter receives UI events
        setContentView(R.layout.activity_main);

        // inject dependency on model into this so model receives UI events
        this.setModel(new ConcreteStopwatchModelFacade());

        et = (EditText) findViewById(R.id.seconds);

        // Help from Stackoverflow, defines edit text's textwatcher so that the text can be parsed into int
        //and then transferred to the model
        /**
         * The Android documentation shows that the EditText object defines the method addTextChangedListener
         * that takes, as an argument, a TextWatcher object. The TextWatcher defines methods that are invoked
         * when the EditText widget’s content text changes*/

        et.addTextChangedListener(new TextWatcher() { //Android TextWatcher simply passes the changed text
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { //onTextChanged and beforeTextChanged, do nothing.

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { //onTextChanged and beforeTextChanged, do nothing.

            }
            @Override
            public void afterTextChanged(Editable s) {

                //will only do this if the length is greater than 0,// or less than 99
                if (s.length()> 0) {
                typedTime= Integer.valueOf(s.toString()); }
            }
        });

        // inject dependency on this into model to register for UI updates
        model.setUIUpdateListener(this);
    }
    //Will allow the typed time to be enabled during the stopped state and disabled in other states
    public void setEditable(boolean b){ //enable editing set to true
        et.setFocusable(b);
        et.setFocusableInTouchMode(b);
        et.setEnabled(b);
    }

    @Override
    public int getTypedTime() {
        return typedTime;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        model.onStart();
    }

    // TODO remaining lifecycle methods

    /**
     * Updates the seconds and minutes in the UI.
     *
     * @param time
     */
    public void updateTime(final int time) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView tvS = (TextView) findViewById(R.id.seconds);
            final int seconds = time % Constants.MAX_SECONDS;
            tvS.setText(Integer.toString(seconds / 10) + Integer.toString(seconds % 10));
        });
    }
    /**
     * Updates the state name in the UI.
     *
     * @param stateId
     */
    public void updateState(final int stateId) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView stateName = (TextView) findViewById(R.id.stateName);
            stateName.setText(getString(stateId));
        });
    }

    /**
     * Plays the default notification sound (from ClickCounter program)
     */
    @Override
    public void playDefaultNotification() {
        final Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final MediaPlayer mediaPlayer = new MediaPlayer();
        final Context context = getApplicationContext();
        try {
            mediaPlayer.setDataSource(context, defaultRingtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.start();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // forward event listener methods to the model
    public void onStartStopReset(final View view) {
        model.onStartStop();
    }



}







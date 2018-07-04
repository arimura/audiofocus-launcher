package com.hormiga6.audiofocuslauncher;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private int idxStream = 2;
    private int idxAudioFocus = 0;

    private static final String[] streamLabels = new String[]{
            "STREAM_VOICE_CALL",
            "STREAM_SYSTEM",
            "STREAM_RING",
            "STREAM_MUSIC",
            "STREAM_ALARM",
            "STREAM_NOTIFICATION",
            "STREAM_DTMF",
    };

    private static final int[] streamVals = new int[]{
            AudioManager.STREAM_VOICE_CALL,
            AudioManager.STREAM_SYSTEM,
            AudioManager.STREAM_RING,
            AudioManager.STREAM_MUSIC,
            AudioManager.STREAM_ALARM,
            AudioManager.STREAM_NOTIFICATION,
            AudioManager.STREAM_DTMF,
    };

    private static final String[] audioFocusLabels = new String[]{
            "AUDIOFOCUS_GAIN",
            "AUDIOFOCUS_GAIN_TRANSIENT",
            "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK",
            "AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE",
    };

    private static final int[] audioFocuses = new int[]{
            AudioManager.AUDIOFOCUS_GAIN,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE,
    };
    private TextView textViewStream;
    private TextView textViewAudioFocus;
    private EditText editTextDelay;
    private Button buttonSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStream = findViewById(R.id.textViewStream);
        textViewStream.setText(MainActivity.streamLabels[idxStream]);

        textViewAudioFocus = findViewById(R.id.textViewAudioFocus);
        textViewAudioFocus.setText(MainActivity.audioFocusLabels[idxAudioFocus]);

        editTextDelay = findViewById(R.id.editTextDelay);
        editTextDelay.setText("5");

        buttonSchedule = findViewById(R.id.buttonSchedule);
    }

    public void clickStream(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stream")
                .setItems(streamLabels, (DialogInterface dialog, int which) -> {
                    idxStream = which;
                    textViewStream.setText(MainActivity.streamLabels[idxStream]);
        });
        builder.create().show();
    }

    public void clickAudioFocus(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AudioFocus")
                .setItems(audioFocusLabels, (dialog, which) -> {
                    idxAudioFocus = which;
                    textViewAudioFocus.setText(audioFocusLabels[idxAudioFocus]);
        });
        builder.create().show();
    }

    public void clickSchedule(View view){
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int result = am.requestAudioFocus(
                    focusChange -> Log.i(TAG, "audio focus change:" + focusChange)
                    , streamVals[idxStream]
                    , audioFocuses[idxAudioFocus]);
            Log.i(TAG, "audio focus request result:" + result);
        }, Integer.parseInt(editTextDelay.getText().toString()), TimeUnit.SECONDS);
    }
}

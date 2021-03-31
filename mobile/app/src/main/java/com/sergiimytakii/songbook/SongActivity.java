package com.sergiimytakii.songbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SongActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.songbook.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.songbook.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.songbook.EXTRA_DESCRIPTION";
    public static final String EXTRA_TEXT = "com.example.songbook.EXTRA_TEXT";
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView textTextView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        setTitle("");


        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        textTextView = findViewById(R.id.text);

        titleTextView.setText(getIntent().getStringExtra(EXTRA_TITLE));
        descriptionTextView.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
        textTextView.setText(getIntent().getStringExtra(EXTRA_TEXT));
        titleTextView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_transition_animation));
        descriptionTextView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_lefttoright_animation));
        textTextView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_transition_animation));

    }
}
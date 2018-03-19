package com.example.huntergreer.brainteaser;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnGoClickListener, StartGameActivityFragment.DialogEvents {
    public static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";

    private StartGameActivityFragment mRetainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        mRetainedFragment = (StartGameActivityFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        if (mRetainedFragment == null) {
            MainActivityFragment fragment = new MainActivityFragment();
            fm.beginTransaction().add(R.id.game_start_container, fragment).commit();
        } else {
            fm.beginTransaction().replace(R.id.game_start_container, mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
        }
    }

    @Override
    public void onGoClicked() {
        StartGameActivityFragment fragment = new StartGameActivityFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.game_start_container, fragment, TAG_RETAINED_FRAGMENT).commit();
    }

    @Override
    public void onPositiveDialogResult() {
        MainActivityFragment fragment = new MainActivityFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.game_start_container, fragment).commit();
    }

    @Override
    public void onNegativeDialogResult() {
        finish();
    }
}

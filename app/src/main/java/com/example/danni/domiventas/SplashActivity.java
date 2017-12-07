package com.example.danni.domiventas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 2800;
    int optLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent;
                SharedPreferences sharedPrefs = getSharedPreferences("SharedPreferencesVentas", SplashActivity.MODE_PRIVATE);

                optLog = sharedPrefs.getInt("optLog", 0);

                if (optLog == 0) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    //intent.putExtra("Splash", true);

                } else {
                    intent = new Intent(SplashActivity.this, NavDrawerActivity.class);
                }
                startActivity(intent);
                finish();
            }

        };
        Timer timer = new Timer();
        timer.schedule(task,SPLASH_DELAY);

    }
}

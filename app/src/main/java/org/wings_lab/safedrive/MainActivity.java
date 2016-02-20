package org.wings_lab.safedrive;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.att.m2x.android.main.M2XAPI;

public class MainActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        M2XAPI.initialize(getApplicationContext(),SafeDrive.IsEyeClosing.M2X_API_KEY);

        frameLayout = (FrameLayout) findViewById(R.id.main_frag_holder);
        getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new MainFragment()).commit();
    }
}

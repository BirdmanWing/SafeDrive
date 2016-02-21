package org.wings_lab.safedrive;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;


import com.att.m2x.android.main.M2XAPI;

import org.wings_lab.safedrive.fragments.AdminFragment;
import org.wings_lab.safedrive.fragments.LocationManagerFragment;
import org.wings_lab.safedrive.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements SafeCarDeviceDataAccess, View.OnClickListener {
    private FrameLayout frameLayout;
    private LocationManagerFragment locationManagerFragment;
    private LinearLayout m1,m2,m3;
    private MainFragment mainFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m1 = (LinearLayout) findViewById(R.id.menu_item_safe_driver);
        m2 = (LinearLayout) findViewById(R.id.menu_item_map);
        m3 = (LinearLayout) findViewById(R.id.menu_item_admin);
        m1.setOnClickListener(this);
        m2.setOnClickListener(this);
        m3.setOnClickListener(this);
        locationManagerFragment = new LocationManagerFragment();
        M2XAPI.initialize(getApplicationContext(), SafeDrive.IsEyeClosing.M2X_API_KEY);
        setIsCarConnected(false);
        frameLayout = (FrameLayout) findViewById(R.id.main_frag_holder);
        getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new MainFragment()).commit();
        mainFrag = new MainFragment();
    }


    public void showText() {
        Toast.makeText(this, "You car is moving without your permission!\nPlease go to find to approve it.", Toast.LENGTH_SHORT).show();
    }




    @Override
    public boolean isCarMoving() {
        return isCarMoving;
    }

    boolean isCarMoving = false, isCarConnected = false;

    @Override
    public boolean isCarConnected() {
        return isCarConnected;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.menu_item_admin): {
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new AdminFragment()).commit();
                break;
            }
            case (R.id.menu_item_safe_driver): {
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), mainFrag).commit();
                break;
            }
            case (R.id.menu_item_map): {
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), locationManagerFragment).commit();
                break;
            }
        }
    }

    public void setIsCarMoving(boolean isCarMoving) {
        this.isCarMoving = isCarMoving;
    }

    public void setIsCarConnected(boolean isCarConnected) {
        if(!isCarConnected) {
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    while (!isCarConnected()) {
                        try {
                            if (isCarMoving()) publishProgress(params);
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Object[] values) {
                    showText();
                }
            };
            asyncTask.execute();
        }
        this.isCarConnected = isCarConnected;
    }
}

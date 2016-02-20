package org.wings_lab.safedrive;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.att.m2x.android.listeners.ResponseListener;
import com.att.m2x.android.main.M2XAPI;
import com.att.m2x.android.model.Device;
import com.att.m2x.android.model.Distribution;
import com.att.m2x.android.network.ApiV2Response;

import org.w3c.dom.Text;
import org.wings_lab.safedrive.models.IsEyeClosingParsers;
import org.wings_lab.safedrive.parsers.M2XValueObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    private Button btn_go;
    public static final int MAX_CLOSE_SECOND = 5;
    private SafeDrive safeDrive = new SafeDrive(this);
    private CallBack callBack = new CallBack() {
        @Override
        public void onFiveSecondEyeClosed() {
            doVibrate();
        }

        @Override
        public void onAccessingObject(M2XValueObject object) {
            data_log.append("\n" + object.getTimestamp() + " " + object.getValue());
        }
    };

    private TextView data_log;
    private interface IsEyeClosing {
        String M2X_API_KEY = "a34833cea4311a892de2d09b39b877b1  ";
        String STREAMS_NAME = "IsEyeClosing";
        String DEVICE_ID = "1e0393a13b9ffef6325b9fb4bc2aac61";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn_go = (Button)findViewById(R.id.btn_go);
        data_log = (TextView)findViewById(R.id.tv_data_log);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(safeDrive.isSkyDriveOn()) safeDrive.endSafeDrive();
                else safeDrive.startSafeDrive(callBack);
            }
        });

        M2XAPI.initialize(getApplicationContext(), IsEyeClosing.M2X_API_KEY);

    }

    public void doVibrate() {
        btn_go.setBackground(getDrawable(android.R.color.holo_red_dark));
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }


}


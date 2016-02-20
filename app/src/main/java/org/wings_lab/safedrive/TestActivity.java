package org.wings_lab.safedrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.att.m2x.android.listeners.ResponseListener;
import com.att.m2x.android.main.M2XAPI;
import com.att.m2x.android.model.Device;
import com.att.m2x.android.model.Distribution;
import com.att.m2x.android.network.ApiV2Response;

import org.w3c.dom.Text;
import org.wings_lab.safedrive.models.IsEyeClosingParsers;
import org.wings_lab.safedrive.parsers.M2XValueObject;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    private Button btn_go;
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
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test(null);

            }
        });

        M2XAPI.initialize(getApplicationContext(), IsEyeClosing.M2X_API_KEY);


    }
    public void test(View view) {
       Device.listDataStreamValues(this, null, IsEyeClosing.DEVICE_ID, IsEyeClosing.STREAMS_NAME, new ResponseListener() {
           @Override
           public void onRequestCompleted(ApiV2Response result, int requestCode) {
               M2XValueObject[] objects = IsEyeClosingParsers.parse(result.get_json());
           }

           @Override
           public void onRequestError(ApiV2Response error, int requestCode) {

           }
       });
    }


}


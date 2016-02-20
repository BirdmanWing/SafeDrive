package org.wings_lab.safedrive;

import android.content.Context;

import com.att.m2x.android.listeners.ResponseListener;
import com.att.m2x.android.main.M2XAPI;
import com.att.m2x.android.model.Device;
import com.att.m2x.android.network.ApiV2Response;
import com.google.gson.JsonSerializer;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rAYMOND on 2/20/2016.
 */
public class SafeDriveDataGenerator {
    private Context context;

    public SafeDriveDataGenerator(Context context) {
        this.context = context;
    }

    public void endGenerating() {

    }

    public void startGenerating() {
        HashMap<String, Integer> params = new HashMap<>();
        params.put("value", 0);
        JSONObject jsonObject = new JSONObject(params);
        Device.updateDataStreamValue(context, jsonObject, SafeDrive.IsEyeClosing.DEVICE_ID, SafeDrive.IsEyeClosing.STREAMS_NAME, new ResponseListener() {
            @Override
            public void onRequestCompleted(ApiV2Response result, int requestCode) {

            }

            @Override
            public void onRequestError(ApiV2Response error, int requestCode) {

            }
        });
    }
}

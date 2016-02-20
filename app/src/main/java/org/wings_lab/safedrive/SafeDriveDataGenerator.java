package org.wings_lab.safedrive;

import android.content.Context;
import android.os.AsyncTask;

import com.att.m2x.android.listeners.ResponseListener;
import com.att.m2x.android.main.M2XAPI;
import com.att.m2x.android.model.Device;
import com.att.m2x.android.network.ApiV2Response;
import com.google.gson.JsonSerializer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by rAYMOND on 2/20/2016.
 */
public class SafeDriveDataGenerator {
    private Context context;
    private boolean isGenerating = false;


    public boolean isGenerating() {
        return isGenerating;
    }

    public SafeDriveDataGenerator(Context context) {
        this.context = context;
    }

    public void endGenerating() {
        isGenerating = false;
    }

    public void startGenerating() {
        AsyncTask<Integer, Integer, Integer> asyncTask = new AsyncTask<Integer,Integer,Integer>() {
            @Override
            protected Integer doInBackground(Integer[] params) {
                if(isGenerating) return -1;
                isGenerating = true;
                while (isGenerating) {
                    try {
                            HashMap<String, Integer> param = new HashMap<>();
                        int v = (new Random().nextBoolean()) ? 1 : 0;
                        param.put("value", v);
                        JSONObject jsonObject = new JSONObject(param);
                        Device.updateDataStreamValue(context, jsonObject, SafeDrive.IsEyeClosing.DEVICE_ID, SafeDrive.IsEyeClosing.STREAMS_NAME, new ResponseListener() {
                            @Override
                            public void onRequestCompleted(ApiV2Response result, int requestCode) {

                            }

                            @Override
                            public void onRequestError(ApiV2Response error, int requestCode) {

                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return -1;
                    }
                }
                isGenerating = false;
                return 1;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }
}

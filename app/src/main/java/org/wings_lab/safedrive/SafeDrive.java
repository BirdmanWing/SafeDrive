package org.wings_lab.safedrive;

import android.content.Context;
import android.os.AsyncTask;

import com.att.m2x.android.listeners.ResponseListener;
import com.att.m2x.android.model.Device;
import com.att.m2x.android.network.ApiV2Response;

import org.wings_lab.safedrive.models.IsEyeClosingParsers;
import org.wings_lab.safedrive.parsers.M2XValueObject;

import java.util.HashMap;


public class SafeDrive {
    private Context context;
    private boolean isSkyDriveOn = false;
    private int speed = 0;
    public static final int MAX_CLOSE_SECOND = 2;
    public static final long INTERVAL = 1000;
    public SafeDrive(Context context) {
        this.context = context;
    }

    public int getSpeed() {
        return speed;
    }

    private void updateSpeed() {
        Device.listDataStreams(context, IsEyeClosing.DEVICE_ID, new ResponseListener() {
            @Override
            public void onRequestCompleted(ApiV2Response result, int requestCode) {
                M2XValueObject[] objects = IsEyeClosingParsers.parse(result.get_json());
            }

            @Override
            public void onRequestError(ApiV2Response error, int requestCode) {

            }
        });
    }

    public interface IsEyeClosing {
        String M2X_API_KEY = "a34833cea4311a892de2d09b39b877b1";
        String STREAMS_NAME = "IsEyeClosing";
        String DEVICE_ID = "1e0393a13b9ffef6325b9fb4bc2aac61";
    }


    public boolean isSkyDriveOn() {
        return isSkyDriveOn;
    }

    int openCount = 0;
    public boolean startSafeDrive(final CallBack actionCloseForFive) {
        if(isSkyDriveOn) return false;
        isSkyDriveOn = true;
        currentTask = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                while (isSkyDriveOn) {
                    if(getSpeed()>0) continue;
                    try {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("limit", "1");
                        Device.listDataStreamValues(context, param , IsEyeClosing.DEVICE_ID, IsEyeClosing.STREAMS_NAME, new ResponseListener() {
                            @Override
                            public void onRequestCompleted(final ApiV2Response result, int requestCode) {
                                M2XValueObject[] objects = IsEyeClosingParsers.parse(result.get_json());

                                if (objects[0].getValue() == 1 && isSkyDriveOn()){
                                    actionCloseForFive.onEyeCloseForTwoSec();
                                } else {
                                    openCount++;
                                }
                                if(openCount >= 5) {
                                    openCount = 0;
                                    actionCloseForFive.onFiveSecondEyeOpened();
                                }
                            }

                            @Override
                            public void onRequestError(ApiV2Response error, int requestCode) {

                            }
                        });
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return -1;
                    }
                }
                return 1;
            }
        };
        currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        return true;
    }

    private AsyncTask currentTask;

    public void endSafeDrive() {
        if(isSkyDriveOn()) currentTask.cancel(true);
        isSkyDriveOn = false;
    }

}

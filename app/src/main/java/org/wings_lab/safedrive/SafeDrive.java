package org.wings_lab.safedrive;

import android.content.Context;
import android.os.AsyncTask;

import com.att.m2x.android.listeners.ResponseListener;
import com.att.m2x.android.model.Device;
import com.att.m2x.android.network.ApiV2Response;

import org.wings_lab.safedrive.models.IsEyeClosingParsers;
import org.wings_lab.safedrive.parsers.M2XValueObject;


public class SafeDrive {
    private Context context;
    private boolean isSkyDriveOn = false;
    public static final int MAX_CLOSE_SECOND = 5;
    public static final long INTERVAL = 1000;

    public SafeDrive(Context context) {
        this.context = context;
    }

    public interface IsEyeClosing {
        String M2X_API_KEY = "a34833cea4311a892de2d09b39b877b1";
        String STREAMS_NAME = "IsEyeClosing";
        String DEVICE_ID = "1e0393a13b9ffef6325b9fb4bc2aac61";
    }
    public boolean isSkyDriveOn() {
        return isSkyDriveOn;
    }

    public boolean startSafeDrive(final CallBack actionCloseForFive) {
        if(isSkyDriveOn) return false;
        isSkyDriveOn = true;
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                SafeDriveDataGenerator generator = new SafeDriveDataGenerator(context);
                generator.startGenerating();
                while (isSkyDriveOn) {
                    try {
                        Device.listDataStreamValues(context, null, IsEyeClosing.DEVICE_ID, IsEyeClosing.STREAMS_NAME, new ResponseListener() {
                            @Override
                            public void onRequestCompleted(final ApiV2Response result, int requestCode) {
                                M2XValueObject[] objects = IsEyeClosingParsers.parse(result.get_json());

                                String str = "";
                                boolean isEyeClosedForFiveSec = false;
                                int count = 0;
                                for (M2XValueObject obj : objects) {
                                    actionCloseForFive.onAccessingObject(obj);

                                    str += "\n" + obj.getTimestamp() + " " + obj.getValue();
                                    if (count >= MAX_CLOSE_SECOND) {
                                        isEyeClosedForFiveSec = true;
                                        break;
                                    }
                                    if (obj.getValue() == 1) count++;
                                    else count = 0;
                                }
                                if (isEyeClosedForFiveSec)
                                    actionCloseForFive.onFiveSecondEyeClosed();
                            }

                            @Override
                            public void onRequestError(ApiV2Response error, int requestCode) {

                            }
                        });
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        generator.endGenerating();
                        return -1;
                    }
                }
                generator.endGenerating();
                return 1;
            }
        }.execute();
        return true;
    }

    public void endSafeDrive() {
        isSkyDriveOn = false;
    }

}

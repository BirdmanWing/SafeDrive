package org.wings_lab.safedrive;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.android.internal.util.Predicate;
import com.att.m2x.android.listeners.ResponseListener;
import com.att.m2x.android.model.Device;
import com.att.m2x.android.network.ApiV2Response;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by rAYMOND on 2/21/2016.
 */
public class LocationManager {
    private Context context;

    public LocationManager(Context context) {
        this.context = context;
    }

    public interface GetLocationCallBack {
        void onLocationGot(LatLng latLng, String name, String timestamp);
    }

    public void getCarLocation(final GetLocationCallBack callback) {
        final LatLng[] deviceLocation = {null};
        try {
            Device.readDeviceLocation(context, SafeDrive.IsEyeClosing.DEVICE_ID, new ResponseListener() {
                @Override
                public void onRequestCompleted(ApiV2Response result, int requestCode) {
                    JSONObject jsonObject = result.get_json();
                    try {
                        deviceLocation[0] = new LatLng (
                            jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude")
                        );
                        callback.onLocationGot(deviceLocation[0], getAddressByLatLng(deviceLocation[0]), jsonObject.getString("timestamp"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onRequestError(ApiV2Response error, int requestCode) {

                }
            });
        } catch (Exception e){

        }
    }


    public String getAddressByLatLng(LatLng latLng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return knownName;
    }
}

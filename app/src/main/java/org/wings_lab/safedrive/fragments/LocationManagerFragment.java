package org.wings_lab.safedrive.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.wings_lab.safedrive.LocationManager;
import org.wings_lab.safedrive.R;
import org.wings_lab.safedrive.Utils;

public class LocationManagerFragment extends Fragment  {
    private View rootView;
    private LocationManager locationManager;
    TextView tv_location, tv_timestamp;
    MapFragment googleMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_location_manager, container, false);
            locationManager = new LocationManager(getContext());
            tv_location = (TextView) rootView.findViewById(R.id.tv_car_location);
            tv_timestamp = (TextView) rootView.findViewById(R.id.tv_car_last_online);

            locationManager.getCarLocation(new LocationManager.GetLocationCallBack() {
                @Override
                public void onLocationGot(final LatLng latLng, final String name, final String timestamp) {
                    MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            GoogleMapOptions options = new GoogleMapOptions();
                            googleMap.addMarker(new MarkerOptions().position(latLng).title("here"));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
                            tv_location.setText(name);
                            tv_timestamp.setText(Utils.parse(timestamp).toString());
                        }
                    });
                }
            });
        }

        return rootView;
    }


}
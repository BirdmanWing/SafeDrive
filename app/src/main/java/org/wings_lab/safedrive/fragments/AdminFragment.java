package org.wings_lab.safedrive.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ToggleButton;

import org.wings_lab.safedrive.MainActivity;
import org.wings_lab.safedrive.R;

/**
 * Created by rAYMOND on 2/21/2016.
 */
public class AdminFragment extends Fragment implements View.OnClickListener{

    private ToggleButton toggle_button_moving, toggle_button_connected;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_admin, container, false);
        toggle_button_moving = (ToggleButton) rootView.findViewById(R.id.toggle_car_moving);
        toggle_button_connected = (ToggleButton) rootView.findViewById(R.id.toggle_car_connected);
        toggle_button_connected.setOnClickListener(this);
        toggle_button_moving.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == toggle_button_moving.getId()) {
            ((MainActivity)getActivity()).setIsCarMoving(toggle_button_moving.isChecked());
        } else if (v.getId() == toggle_button_connected.getId()) {
            ((MainActivity)getActivity()).setIsCarConnected(toggle_button_connected.isChecked());
        }
     }
}

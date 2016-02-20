package org.wings_lab.safedrive;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.wings_lab.safedrive.parsers.M2XValueObject;


public class MainFragment extends Fragment {
    private View rootView;
    private TextView star;
    private Button btn_toggle_monitor;
    private ProgressBar progressBar;
    private SafeDrive safeDrive;
    private Drawable defaultBtnBg;
    private CallBack callBack = new CallBack() {
        @Override
        public void onFiveSecondEyeClosed() {
            btn_toggle_monitor.setBackground(getContext().getDrawable(android.R.color.holo_red_dark));
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(700);
        }

        @Override
        public void onAccessingObject(M2XValueObject object) {
            if(object.getValue() == 1) {
                star.setTextColor(Color.RED);
            } else star.setTextColor(Color.GREEN);

        }

        @Override
        public void onFiveSecondEyeOpened() {
            btn_toggle_monitor.setBackground(defaultBtnBg);
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(0);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.load_bar);
        safeDrive  = new SafeDrive(getActivity());
        btn_toggle_monitor = (Button) rootView.findViewById(R.id.btn_toggle_monitor);
        star = (TextView) rootView.findViewById(R.id.star);
        defaultBtnBg = btn_toggle_monitor.getBackground();
        btn_toggle_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!safeDrive.isSkyDriveOn()) {
                    btn_toggle_monitor.setText("END DRIVING");
                    safeDrive.startSafeDrive(callBack);
                    progressBar.setIndeterminate(true);
                }
                else {
                    btn_toggle_monitor.setText("START");
                    progressBar.setIndeterminate(false);
                    btn_toggle_monitor.setBackground(defaultBtnBg);
                    Vibrator vi = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vi.vibrate(0);
                    safeDrive.endSafeDrive();

                }
            }
        });
        return rootView;
    }
}

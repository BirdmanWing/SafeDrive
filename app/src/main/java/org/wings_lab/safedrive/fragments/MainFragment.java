package org.wings_lab.safedrive.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
import org.wings_lab.safedrive.CallBack;
import org.wings_lab.safedrive.R;
import org.wings_lab.safedrive.SafeDrive;
import org.wings_lab.safedrive.parsers.M2XValueObject;


public class MainFragment extends Fragment {
    private View rootView;
    private TextView star, status;
    private Button btn_toggle_monitor;
    private ProgressBar progressBar;
    private SafeDrive safeDrive;
    private Drawable defaultBtnBg;
    private CallBack callBack = new CallBack() {
        @Override
        public void onEyeCloseForTwoSec() {
            btn_toggle_monitor.setBackground(getContext().getDrawable(android.R.color.holo_red_dark));
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(5000);

        }

        @Override
        public void onAccessingObject(M2XValueObject object) {
            if(object.getValue() == 1) {
                if(!star.getText().toString().contains("!")) star.setText("");
                star.append("!");
                star.setTextColor(Color.RED);
            } else {
                if(!star.getText().toString().contains("o")) star.setText("");
                star.setTextColor(Color.GREEN);
                star.append("o");
            }

        }

        @Override
        public void onFiveSecondEyeOpened() {
            btn_toggle_monitor.setBackground(defaultBtnBg);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.load_bar);
        safeDrive  = new SafeDrive(getActivity());
        btn_toggle_monitor = (Button) rootView.findViewById(R.id.btn_toggle_monitor);
        star = (TextView) rootView.findViewById(R.id.star);
        status = (TextView) rootView.findViewById(R.id.main_frag_status);
        defaultBtnBg = btn_toggle_monitor.getBackground();
        btn_toggle_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!safeDrive.isSkyDriveOn()) {
                    safeDrive.startSafeDrive(callBack);
                    status.setText("Monitoring");
                    btn_toggle_monitor.setText("END DRIVING");
                    progressBar.setIndeterminate(true);
                }
                else {
                    safeDrive.endSafeDrive();
                    status.setText("IDLE");
                    star.setText("*");
                    star.setTextColor(Color.GRAY);
                    btn_toggle_monitor.setText("START");
                    progressBar.setIndeterminate(false);
                    btn_toggle_monitor.setBackground(defaultBtnBg);
                    Vibrator vi = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vi.vibrate(0);

                }
            }
        });
        return rootView;
    }
}

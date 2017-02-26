package com.example.gdmap0fflinemap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gdmap0fflinemap.R;
import com.example.gdmap0fflinemap.eventBus.IMU_EventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2017/1/30.
 */

public class DataActivity extends Activity {
    TextView textView;
    EventBus eventBus;
    private static final String TAG = "DataActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus.getDefault().register(this);
        setContentView(R.layout.data_activity);
        textView = (TextView) findViewById(R.id.data_content);
    }
    @Subscribe
    public void onEventMainThread(IMU_EventBus imu_eventBus) {
        textView.append(imu_eventBus.getIRoll() + "\n" +
                imu_eventBus.getIPitch() + "\n" +
                imu_eventBus.getIYaw() + "\n" +
                imu_eventBus.getIRollRate()+ "\n" +
                imu_eventBus.getIPitchRate()+ "\n" +
                imu_eventBus.getIYawRate()

        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.getDefault().unregister(this);
    }
}

package com.example.gdmap0fflinemap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gdmap0fflinemap.R;
import com.example.gdmap0fflinemap.eventBus.Accelerated_EventBus;
import com.example.gdmap0fflinemap.eventBus.GPS_EventBus;
import com.example.gdmap0fflinemap.eventBus.Geomagnetism_EventBus;
import com.example.gdmap0fflinemap.eventBus.IMU_EventBus;
import com.example.gdmap0fflinemap.eventBus.Motor_EventBus;
import com.example.gdmap0fflinemap.eventBus.Remote_EventBus;
import com.example.gdmap0fflinemap.eventBus.Speed_EventBus;
import com.example.gdmap0fflinemap.eventBus.StartClose_EventBus;
import com.example.gdmap0fflinemap.eventBus.Status_EventBus;
import com.example.gdmap0fflinemap.eventBus.WayPoint_EventBus;
import com.example.gdmap0fflinemap.eventBus.XBEE_EventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2017/1/30.
 */

public class DataActivity extends Activity {
    /*IMU*/
    TextView mStrRoll;
    TextView mStrPitch;
    TextView mStrYaw;
    TextView mStrRollRate;
    TextView mStrPitchRate;
    TextView mStrYawRate;
    /*加速度*/
    TextView mStrAccX;
    TextView mStrAccY;
    TextView mStrAccZ;
    /*地磁*/
    TextView mStrGeoX;
    TextView mStrGeoY;
    TextView mStrGeoZ;
    /*遥控器*/
    TextView mStrReRoll;
    TextView mStrRePitch;
    TextView mStrReYaw;
    TextView mStrReThrottle;
    /*开关*/
    TextView mStrSW1;
    TextView mStrSW2;
    TextView mStrSW3;
    TextView mStrSW4;
    /*路点*/
    TextView mStrWayID;
    TextView mStrWayCount;
    TextView mStrWayIndex;
    /*电机*/
    TextView mStrMotorFront;
    TextView mStrMotorBack;
    TextView mStrMotorLeft;
    TextView mStrMotorRight;
    TextView mStrMotorX;
    TextView mStrMotorY;
    /*速度*/
    TextView mStrLateral;
    TextView mStrLongitudinal;
    TextView mStrAbout;
    /*GPS*/
    TextView mStrLatitude;
    TextView mStrLongitude;
    TextView mStrStarCount;
    TextView mStrHight;
    /*状态数据*/
    TextView mStrFlyStatus;
    TextView mStrVoltage;
    TextView mStrSendFlag;
    TextView mStrSensorStatus;
    TextView mStrCommStatus;
    TextView mStrPhotoFlag;
    /*XBEE*/
    TextView mStrXBEE;


    EventBus eventBus;
    private static final String TAG = "DataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus.getDefault().register(this);
        setContentView(R.layout.data_activity);
        findViewById();
    }

    private void findViewById() {
        mStrRoll = (TextView) findViewById(R.id.strRoll);
        mStrPitch = (TextView) findViewById(R.id.strPitch);
        mStrYaw = (TextView) findViewById(R.id.strYaw);
        mStrRollRate = (TextView) findViewById(R.id.strRollRate);
        mStrPitchRate = (TextView) findViewById(R.id.strPitchRate);
        mStrYawRate = (TextView) findViewById(R.id.strYawRate);

        mStrAccX = (TextView) findViewById(R.id.strAccX);
        mStrAccY = (TextView) findViewById(R.id.strAccY);
        mStrAccZ = (TextView) findViewById(R.id.strAccZ);

        mStrGeoX = (TextView) findViewById(R.id.strGeoX);
        mStrGeoY = (TextView) findViewById(R.id.strGeoY);
        mStrGeoZ = (TextView) findViewById(R.id.strGeoZ);

        mStrReRoll = (TextView) findViewById(R.id.strReRoll);
        mStrRePitch = (TextView) findViewById(R.id.strRePitch);
        mStrReYaw = (TextView) findViewById(R.id.strReYaw);
        mStrReThrottle = (TextView) findViewById(R.id.strReThrottle);

        mStrSW1 = (TextView) findViewById(R.id.strSW1);
        mStrSW2 = (TextView) findViewById(R.id.strSW2);
        mStrSW3 = (TextView) findViewById(R.id.strSW3);
        mStrSW4 = (TextView) findViewById(R.id.strSW4);

        mStrWayID = (TextView) findViewById(R.id.strwayID);
        mStrWayCount = (TextView) findViewById(R.id.strwayCount);
        mStrWayIndex = (TextView) findViewById(R.id.strwayIndex);

        mStrMotorFront = (TextView) findViewById(R.id.strMotor_Front);
        mStrMotorBack = (TextView) findViewById(R.id.strMotor_Back);
        mStrMotorLeft = (TextView) findViewById(R.id.strMotor_Left);
        mStrMotorRight = (TextView) findViewById(R.id.strMotor_Right);
        mStrMotorX = (TextView) findViewById(R.id.strMotor_X);
        mStrMotorY = (TextView) findViewById(R.id.strMotor_Y);

        mStrLateral = (TextView) findViewById(R.id.strLateral);
        mStrLongitudinal = (TextView) findViewById(R.id.strLongitudinal);
        mStrAbout = (TextView) findViewById(R.id.strAbout);

        mStrLatitude = (TextView) findViewById(R.id.strLatitude);
        mStrLongitude = (TextView) findViewById(R.id.strLongitude);
        mStrStarCount = (TextView) findViewById(R.id.strStarCount);
        mStrHight = (TextView) findViewById(R.id.strHight);

        mStrFlyStatus = (TextView) findViewById(R.id.strFlyStatus);
        mStrVoltage = (TextView) findViewById(R.id.strVoltage);
        mStrSendFlag = (TextView) findViewById(R.id.strSendFlag);
        mStrSensorStatus = (TextView) findViewById(R.id.strSensorStatus);
        mStrCommStatus = (TextView) findViewById(R.id.strCommStatus);
        mStrPhotoFlag = (TextView) findViewById(R.id.strPhotoFlag);

        mStrXBEE = (TextView) findViewById(R.id.strXBEE);

    }

    @Subscribe
    public void onEventMainThread(final IMU_EventBus imu_eventBus) {
        runOnUiThread(new Runnable() {
            IMU_EventBus imuEventBus = imu_eventBus;

            @Override
            public void run() {
                mStrRoll.setText(imuEventBus.getIRoll());
                mStrPitch.setText(imuEventBus.getIPitch());
                mStrYaw.setText(imuEventBus.getIYaw());
                mStrRollRate.setText(imuEventBus.getIRollRate());
                mStrPitchRate.setText(imuEventBus.getIPitchRate());
                mStrYawRate.setText(imuEventBus.getIYawRate());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final Accelerated_EventBus accelerated_eventBus) {
        runOnUiThread(new Runnable() {
            Accelerated_EventBus accelerated = accelerated_eventBus;

            @Override
            public void run() {
                mStrGeoX.setText(accelerated.geX());
                mStrGeoY.setText(accelerated.geY());
                mStrGeoZ.setText(accelerated.geZ());

            }
        });
    }

    @Subscribe
    public void onEventMainThread(final Geomagnetism_EventBus geomagnetism_eventBus) {
        runOnUiThread(new Runnable() {
            Geomagnetism_EventBus geomagnetismEventBus = geomagnetism_eventBus;

            @Override
            public void run() {
                mStrAccX.setText(geomagnetismEventBus.getX());
                mStrAccY.setText(geomagnetismEventBus.getY());
                mStrAccZ.setText(geomagnetismEventBus.getZ());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final Remote_EventBus remote_eventBus) {
        runOnUiThread(new Runnable() {
            Remote_EventBus remoteEventBus = remote_eventBus;

            @Override
            public void run() {
                mStrReRoll.setText(remoteEventBus.getRoll());
                mStrRePitch.setText(remoteEventBus.getPitch());
                mStrReYaw.setText(remoteEventBus.getYaw());
                mStrReThrottle.setText(remoteEventBus.getThrottle());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final StartClose_EventBus startClose_eventBus) {
        runOnUiThread(new Runnable() {
            StartClose_EventBus startCloseeventBus = startClose_eventBus;

            @Override
            public void run() {
                mStrSW1.setText(startCloseeventBus.getSW1());
                mStrSW2.setText(startCloseeventBus.getSW2());
                mStrSW3.setText(startCloseeventBus.getSW3());
                mStrSW4.setText(startCloseeventBus.getSW4());
            }
        });
    }
    @Subscribe
    public void onEventMainThread(final WayPoint_EventBus wayPoint_eventBus) {
        runOnUiThread(new Runnable() {
            WayPoint_EventBus wayPointEventBus = wayPoint_eventBus;

            @Override
            public void run() {
                mStrWayID.setText(wayPointEventBus.getWayID());
                mStrWayCount.setText(wayPointEventBus.getWayCount());
                mStrWayIndex.setText(wayPointEventBus.getWayIndex());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final Motor_EventBus motor_eventBus) {
        runOnUiThread(new Runnable() {
            Motor_EventBus motorEventBus = motor_eventBus;
            @Override
            public void run() {
                mStrMotorFront.setText(motorEventBus.getMotor_Front());
                mStrMotorBack.setText(motorEventBus.getMotor_Back());
                mStrMotorLeft.setText(motorEventBus.getMotor_Left());
                mStrMotorRight.setText(motorEventBus.getMotor_Right());
                mStrMotorX.setText(motorEventBus.getMotor_X());
                mStrMotorY.setText(motorEventBus.getMotor_Y());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final Speed_EventBus speed_eventBus) {
        runOnUiThread(new Runnable() {
            Speed_EventBus speedEventBus = speed_eventBus;

            @Override
            public void run() {
                mStrLateral.setText(speedEventBus.getLateral());
                mStrLongitudinal.setText(speedEventBus.getLongitudinal());
                mStrAbout.setText(speedEventBus.getUpdown());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final GPS_EventBus gps_eventBus) {
        runOnUiThread(new Runnable() {
            GPS_EventBus gpsEventBus = gps_eventBus;

            @Override
            public void run() {
                mStrLatitude.setText(gpsEventBus.getLatitude());
                mStrLongitude.setText(gpsEventBus.getLongitude());
                mStrStarCount.setText(gpsEventBus.getStarCount());
                mStrHight.setText(gpsEventBus.getHight());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final Status_EventBus status_eventBus) {
        runOnUiThread(new Runnable() {
            Status_EventBus statusEventBus = status_eventBus;
            @Override
            public void run() {
                mStrFlyStatus.setText(statusEventBus.getFlyStatus());
                mStrVoltage.setText(statusEventBus.getVoltage());
                mStrSendFlag.setText(statusEventBus.getSendFlag());
                mStrSensorStatus.setText(statusEventBus.getSensorStatus());
                mStrCommStatus.setText(statusEventBus.getCommStatus());
                mStrPhotoFlag.setText(statusEventBus.getPhotoFlag());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final XBEE_EventBus xbee_eventBus) {
        runOnUiThread(new Runnable() {
            XBEE_EventBus xbeeEventBus = xbee_eventBus;

            @Override
            public void run() {
                mStrXBEE.setText(xbeeEventBus.getXBEE());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.getDefault().unregister(this);
    }
}

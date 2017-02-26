package com.example.gdmap0fflinemap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.gdmap0fflinemap.R;

/**
 * Created by Administrator on 2017/2/6.
 */

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 3000);
    }
    protected final int GOTO_MAIN_ACTIVITY = 0;
    Handler mHandler = new Handler(new Handler.Callback() {

        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
            return true;

        }
    });
}

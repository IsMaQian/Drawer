package com.example.gdmap0fflinemap.activity;

import android.widget.TextView;

import com.example.gdmap0fflinemap.R;

/**
 * Created by Administrator on 2017/4/1.
 */

public class TestActivity extends BaseActivity {
    TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.data_activity;
    }

    @Override
    public void initViews() {
        textView = findView(R.id.strwayID);
    }
}

package com.example.gdmap0fflinemap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/4/1.
 */

public abstract class BaseActivity extends Activity {
    private SparseArray<View> mViews;

    public abstract int getLayoutId();

    public abstract void initViews();

    EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = new SparseArray<>();
        eventBus.getDefault().register(this);
        setContentView(getLayoutId());
        initViews();

    }

    public <E extends View> E findView(int viewId) {
        E view = (E) mViews.get(viewId);
        if (view != null) {
            view = (E) findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.getDefault().unregister(this);
    }
}


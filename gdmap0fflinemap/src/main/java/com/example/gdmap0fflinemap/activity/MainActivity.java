package com.example.gdmap0fflinemap.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.gdmap0fflinemap.R;
import com.example.gdmap0fflinemap.adapter.MyPagerAdapter;
import com.example.gdmap0fflinemap.library.NavigationTabBar;
import com.example.gdmap0fflinemap.offlineMap.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationSource ,AMapLocationListener{
    MapView mMapView = null;
    AMap aMap;
    private AMapLocationClient mLocationClient = null;
    //����mLocationOption���󣬶�λ����
    public AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener;
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    LocalActivityManager manager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        manager = new LocalActivityManager(MainActivity.this, true);
        manager.dispatchCreate(savedInstanceState);
        findViewById();

    }

    public void findViewById() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        Button offlinedownload = (Button) findViewById(R.id.downloadMap);
        offlinedownload.setOnClickListener(new OfflineDownload());
    }

//出现toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

//自定义toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                mDrawerLayout.openDrawer(GravityCompat.END);
                initNavigationTabBar();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        etupLocationStyle();
    }

    private void etupLocationStyle(){
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient=null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getAltitude(), amapLocation.getLongitude())));
                StringBuffer buffer = new StringBuffer();
                buffer.append(amapLocation.getLatitude() + "  "
                        + amapLocation.getLongitude() + "\n"
                        +amapLocation.getCountry() + ""
                        + amapLocation.getProvince() + ""
                        + amapLocation.getCity() + ""
                        + amapLocation.getDistrict() + ""
                        + amapLocation.getStreet() + ""
                        + amapLocation.getStreetNum());
                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    //离线地图下载
    private class OfflineDownload implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, OfflineMapActivity.class);
            startActivity(intent);
        }
    }

    public View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void initNavigationTabBar() {
        backgroundAlpha(0.9f);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.navigation);
        final ArrayList<View> list = new ArrayList<View>();
        Intent link_intent = new Intent(this, LinkIPActivity.class);
        list.add(getView("A",link_intent));
        Intent data_link = new Intent(this, DataActivity.class);
        list.add(getView("B", data_link));

        viewPager.setAdapter(new MyPagerAdapter(list));
        viewPager.setCurrentItem(0);

        final int bgColor = Color.parseColor("#343434");
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                getResources().getDrawable(R.drawable.ic_eighth), bgColor
                ).build());
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_eighth), bgColor
                ).build());

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}

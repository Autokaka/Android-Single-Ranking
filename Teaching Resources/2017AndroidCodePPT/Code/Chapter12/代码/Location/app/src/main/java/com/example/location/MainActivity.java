package com.example.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvLag,tvAlt,tvSpeed;
    private LocationManager lm = null;
    private Location mLocation;
    private MyLocationListner mLocationListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLag = (TextView) findViewById(R.id.tvLag);
        tvAlt = (TextView) findViewById(R.id.tvAlt);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        initLocation();
    }
    private void initLocation(){
        //判断GPS是否正常启动
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(MainActivity.this, "请开启GPS...",Toast.LENGTH_SHORT);
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,0);
            return;
        }

        if (mLocationListner == null)
        {
            mLocationListner = new MyLocationListner();
        }

        try{
            mLocation = lm.getLastKnownLocation(lm.GPS_PROVIDER);
            updateView(mLocation);
        }catch (SecurityException se){
        }

        try{
            // 1秒钟更新一次 最小位移变化 2米
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, mLocationListner);
        }catch (SecurityException se){
        }

    }

    private class MyLocationListner implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            updateView(location);
        }
        @Override
        public void onProviderDisabled(String provider)
        {
            updateView(null);
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            try{
                updateView(lm.getLastKnownLocation(provider));
            }catch (SecurityException e){}
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }

    private void updateView(Location location)
    {
        if (location!=null) {
            tvLag.setText("当前经纬度：" + location.getLongitude() + "," + location.getLatitude());
            tvAlt.setText("当前海拔：" + location.getAltitude() + "m");
            tvSpeed.setText("当前速度：" + location.getSpeed() + "m/s");
        }else{
            tvLag.setText("当前经纬度：");
            tvAlt.setText("当前海拔：" );
            tvSpeed.setText("当前速度：");
        }
    }
}

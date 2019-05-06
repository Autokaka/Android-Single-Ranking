package cn.dshitpie.magicalconch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.*;
import com.amap.api.maps.*;
import com.amap.api.maps.model.*;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.*;
import com.amap.api.services.help.Tip;
import cn.dshitpie.magicalconch.R;

import cn.dshitpie.magicalconch.input_tips.InputTipsActivity;

import java.text.DecimalFormat;
import java.util.List;


public class MapActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, AMap.InfoWindowAdapter{
    private MapView mMapView = null;
    private AMap aMap;
    private AMapLocationClient mlocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    //private TextView mLocationErrText;
    private GeocodeSearch geocoderSearch;
    private String geoInfo;
    private MarkerOptions markerOptions;
    private LatLng mypoint;
    private Toolbar mToolBar;
    private Button searchButton;
    private static final int REQUEST_PLACE = 1;
    //private UiSettings Uiset;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);//设置对应的XML布局文件

        Toast.makeText(this, "选择地点后再次点击信息窗以确定。", Toast.LENGTH_LONG).show();

        //显示地图
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        mToolBar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        searchButton = (Button) findViewById(R.id.button_search);

        //markeroption设定
        markerOptions = new MarkerOptions();
        markerOptions.draggable(false);//不可拖拽
        markerOptions.infoWindowEnable(true);//显示信息窗口
        markerOptions.visible(true);

        myIntent = getIntent();


        if (myIntent.getBooleanExtra("location_is_valid", false) == true) {
            mypoint = new LatLng(myIntent.getDoubleExtra("Latlng_Latitude_Return", 91), myIntent.getDoubleExtra("Latlng_Longitude_Return", 181));
        }

        //mToolBar.inflateMenu(R.menu.toolbar_menu);
        //UI控制器  用Intent切换会发生空指针
        //Uiset = aMap.getUiSettings();
        //Uiset.setScaleControlsEnabled(false);
        init();
    }

    //初始化
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setupMap();
        }
        //  mLocationErrText = (TextView)findViewById(R.id.errTextView);
        //  mLocationErrText.setVisibility(View.GONE);
        //  mLocationErrText.setBackgroundColor(getResources().getColor(R.color.lightgray));



        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                //清空marker
                aMap.clear(true);

                //markeroption设定
                markerOptions.draggable(false);//不可拖拽
                markerOptions.infoWindowEnable(true);//显示信息窗口
                markerOptions.visible(true);

                //设置标记点坐标
                markerOptions.position(point);
                //镜头移动
                aMap.animateCamera(CameraUpdateFactory.changeLatLng(point));
                final Marker marker = aMap.addMarker(markerOptions);

                mypoint = point;
                //将point经纬信息传入latlonpoint并转换信息
                LatLonPoint latlonpoint = new LatLonPoint(mypoint.latitude, mypoint.longitude);
                RegeocodeQuery query = new RegeocodeQuery(latlonpoint, 20, GeocodeSearch.GPS);
                geocoderSearch.getFromLocationAsyn(query);
            }
        });

        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent();
                intent.putExtra("Latlng_Latitude_Return", mypoint.latitude);
                intent.putExtra("Latlng_Longitude_Return", mypoint.longitude);
                intent.putExtra("geoInfo", geoInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    //地图设置
    private void setupMap() {
        aMap.setLocationSource(this);
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.radiusFillColor(00000000);
        aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setInfoWindowAdapter(this);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        if (myIntent.getBooleanExtra("location_is_valid", false) == false) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        } else {
            markerOptions.position(mypoint);
            DecimalFormat myFormat = new DecimalFormat(".000000");
            String x = myFormat.format(mypoint.latitude);
            String y = myFormat.format(mypoint.longitude);
            String title = "定位 " + "(" + x + "," + y + ")";
            geoInfo = myIntent.getStringExtra("geoInfo");
            markerOptions.title(title).snippet(geoInfo);
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(mypoint));
            final Marker marker = aMap.addMarker(markerOptions);
            marker.showInfoWindow();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        }

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //地理位置变换监听
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    //位置变换事件
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                //mLocationErrText.setVisibility(View.VISIBLE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                //    mLocationErrText.setVisibility(View.VISIBLE);
                //    mLocationErrText.setText(errText);
            }
        }
    }

    //逆地理位置检索
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        //    mLocationErrText.setVisibility(View.VISIBLE);
        DecimalFormat myFormat = new DecimalFormat(".000000");
        String x = myFormat.format(mypoint.latitude);
        String y = myFormat.format(mypoint.longitude);
        String title = result.getRegeocodeAddress().getCountry() + "(" + x + "," + y + ")";
        geoInfo = result.getRegeocodeAddress().getFormatAddress();
        markerOptions.title(title).snippet(geoInfo);
        //    mLocationErrText.setText(geoInfo);
        final Marker marker = aMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }

    //地理位置检索
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        List<GeocodeAddress> Result = result.getGeocodeAddressList();
        LatLonPoint point = Result.get(0).getLatLonPoint();
        mypoint = new LatLng(point.getLatitude(), point.getLongitude());

        DecimalFormat myFormat = new DecimalFormat(".000000");
        String x = myFormat.format(mypoint.latitude);
        String y = myFormat.format(mypoint.longitude);
        String title = "定位 " + "(" + x + "," + y + ")";
        geoInfo = Result.get(0).getCity();
        markerOptions.position(mypoint);
        markerOptions.title(title).snippet(geoInfo);
        final Marker marker = aMap.addMarker(markerOptions);
        marker.showInfoWindow();
        aMap.animateCamera(CameraUpdateFactory.zoomTo(9));
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(mypoint));
    }

    //searchButton事件
    public void searchOnClick(View view) {
        Intent intent = new Intent(MapActivity.this, InputTipsActivity.class);
        startActivityForResult(intent, REQUEST_PLACE);
    }

    //搜索结果返回与定位
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE) {
            if (resultCode == InputTipsActivity.RESULT_CODE_INPUTTIPS && data != null) {
                final Tip tip = data.getParcelableExtra("tip");
                aMap.clear(true);
                if (tip.getPoint() == null) {
                    String cityName = tip.getName();
                    GeocodeQuery query = new GeocodeQuery(cityName, null);
                    geocoderSearch.getFromLocationNameAsyn(query);
                } else if (tip.getPoint() != null) {
                    //marker选择地点的marker 的设置
                    LatLonPoint search_point = tip.getPoint();
                    LatLng point = new LatLng(search_point.getLatitude(), search_point.getLongitude(), true);

                    markerOptions.position(point);
                    mypoint = point;
                    //将point经纬信息传入latlonpoint并转换信息
                    LatLonPoint latlonpoint = new LatLonPoint(point.latitude, point.longitude);
                    DecimalFormat myFormat = new DecimalFormat(".000000");
                    String x = myFormat.format(mypoint.latitude);
                    String y = myFormat.format(mypoint.longitude);
                    String title = "定位：" + "(" + x + "," + y + ")";
                    if (tip.getAddress() == null) {
                        geoInfo = tip.getName();
                    } else if (tip.getAddress() != null) {
                        geoInfo = tip.getName() + " " + tip.getAddress();
                    }

                    markerOptions.title(title).snippet(geoInfo);
                    //    mLocationErrText.setText(geoInfo);
                    final Marker marker = aMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(point));
                    aMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            }
        }
    }

    //返回按钮重写
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //adapter方法重写
    public View getInfoContents(Marker marker) {
        return null;
    }

    View infoWindow = null;


    public View getInfoWindow(Marker marker) {
        if(infoWindow == null) {
            infoWindow = LayoutInflater.from(this).inflate(
                    R.layout.custom_info_window, null);
        }
        render(marker, infoWindow);
        return infoWindow;
    }

    public void render(Marker marker, View view) {
        TextView detailTv = view.findViewById(R.id.detail_tv);
        TextView titleTv = view.findViewById(R.id.title_tv);
        titleTv.setText(marker.getTitle());
        detailTv.setText(marker.getSnippet());
    }
}
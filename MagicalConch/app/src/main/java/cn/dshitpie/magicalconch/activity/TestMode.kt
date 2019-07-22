package cn.dshitpie.magicalconch.activity

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import cn.dshitpie.magicalconch.R
import cn.dshitpie.magicalconch.utils.PermissionManager
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import java.lang.StringBuilder

/**
 * 测试模式, 用于检查项目内存在的功能性问题
 * 目前支持的功能检查: 定位服务
  */
class TestMode : AppCompatActivity() {
    private var mLocationClient: LocationClient? = null
    private var positionText: TextView? = null

    inner class MyLocationListner : BDLocationListener {
        override fun onReceiveLocation(location: BDLocation?) {
            var currentPosition = StringBuilder()
            currentPosition.append("经度: ").append(location?.latitude).append("\n")
            currentPosition.append("纬度: ").append(location?.longitude).append("\n")
            currentPosition.append("定位方式: ")
            if (location?.locType == BDLocation.TypeGpsLocation) currentPosition.append("GPS")
            else if (location?.locType == BDLocation.TypeNetWorkLocation) currentPosition.append("网络")
            positionText?.text = currentPosition
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocationClient = LocationClient(applicationContext)
        mLocationClient?.registerLocationListener(MyLocationListner())
        setContentView(R.layout.activity_testmode)
        positionText = findViewById(R.id.position_text)
        var permissionManager = PermissionManager(this)
        permissionManager.request(
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.VIBRATE,
            Manifest.permission.WAKE_LOCK)
        requestPosition()
    }

    private fun requestPosition() {
        mLocationClient?.start()
    }
}

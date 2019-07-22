package cn.dshitpie.magicalconch.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast

class PermissionManager: AppCompatActivity {
    private var context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun request(vararg permissions: String) {
        var permissionStatus: Int
        var requestList = ArrayList<String>()
        for (i in permissions.indices) {
            Log.d("testapp", permissions[i])
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) requestList.add(permissions[i])
        }
        if (requestList.isNotEmpty())ActivityCompat.requestPermissions(context as Activity, requestList.toTypedArray(), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "${permissions[i]}权限未允许", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

package cn.dshitpie.filemanager2.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class PermissionManager extends AppCompatActivity {
    private ArrayList<String> permissionList;

    public PermissionManager() {
        permissionList = new ArrayList<String>();
    }

    public PermissionManager(ArrayList<String> permissionList) {
        permissionList = new ArrayList<String>();
        for (int i = 0; i < permissionList.size(); i++) add(permissionList.get(i));
    }

    public void add(String permission) {
        permissionList.add(permission);
    }

    public void request(Activity activity) {
        for (int i = 0; i < permissionList.size(); i++) {
            int permissionStatus = ContextCompat.checkSelfPermission(activity, permissionList.get(i));
            if (permissionStatus != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(activity, new String[]{ permissionList.get(i) }, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "[请注意] App权限未全部允许, 可能无法正常工作", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
    }
}

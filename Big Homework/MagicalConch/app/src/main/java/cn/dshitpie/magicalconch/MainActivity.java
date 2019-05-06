package cn.dshitpie.magicalconch;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cn.dshitpie.magicalconch.R;

import cn.dshitpie.magicalconch.controller.NoteOperator;

import java.util.ArrayList;
import java.util.List;

import cn.dshitpie.magicalconch.service.DeskService;


public class MainActivity extends AppCompatActivity {

    ArrayList list = null;
    TextView instruction = null;
    final NoteOperator noteOperator = new NoteOperator(MainActivity.this);
    private Handler mHandler;
    private List<String> permissionList = new ArrayList<>();
    private static final String TAG = "testapp";
    static AppCompatActivity MainActivity;
    private  ListAdapter listAdapter = null;
    private ListView listView;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity -> onResume");
        refresh();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity = this;
        this.setTitle(R.string.firstPage_name);
        checkPermissions();
        startLocService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        instruction = findViewById(R.id.instruction);
        listView=findViewById(R.id.list_view);
        // SearchView searchView=findViewById(R.id.searchView);
        Log.d(TAG, "MainActivity -> onCreate");
        list = noteOperator.getNoteList();
        setInstruction();
        listAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.item_layout,
                new String[]{"title", "location"}, new int[]{R.id.note_title, R.id.note_location});
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int n=position;
                Intent inte=new Intent();
                inte.setClass(MainActivity.this, detail_page.class);
                inte.putExtra("num",position);
                inte.putExtra("isRead",true);
                MainActivity.this.startActivity(inte);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final int position1=position;
                Builder builder=new Builder(MainActivity.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new  OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteOperator.delete(position1);
                        list.remove(position1);
                        setInstruction();
                        Log.d(TAG, list.toString());
                        ((SimpleAdapter) listAdapter).notifyDataSetChanged();
                        listView.invalidateViews();
                        // Message mes=mHandler.obtainMessage();
                        // mes.arg1=position1;
                        // mHandler.sendMessage(mes);
                    }
                });
                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return true;
            }

        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, detail_page.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void refresh() {
        list.clear();
        list.addAll(noteOperator.getNoteList());
        setInstruction();
        Log.d(TAG, list.toString());
        ((SimpleAdapter) listAdapter).notifyDataSetChanged();
        listView.invalidateViews();
        startLocService();
    }

    private void setInstruction() {
        if (list.isEmpty() && instruction != null) instruction.setText("点击右下角按钮添加事件");
        if (!list.isEmpty() && instruction != null) instruction.setText("");
    }

    private void checkPermissions() {
        Log.d(TAG, "MainActivity -> checkPermissions");
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "MainActivity -> onRequestPermissionsResult");
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default :
                break;
        }
    }
    public void startLocService(){
        Log.d(TAG, "MainActivity -> startLocService");
        Intent intent = new Intent(MainActivity.this, DeskService.class);
        startService(intent);
    }
}

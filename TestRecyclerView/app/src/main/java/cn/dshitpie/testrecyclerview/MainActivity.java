package cn.dshitpie.testrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerViewAdapter adapter;
    List<ContactInfo> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.card_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //初始化mList
        initInfo();
        //实例化MyAdapter并传入mList对象
        adapter = new MyRecyclerViewAdapter(mList);
        //为RecyclerView对象mRecyclerView设置adapter
        mRecyclerView.setAdapter(adapter);

    }

    private void initInfo() {

        //测试数据
        ContactInfo element1 = new ContactInfo("小明", "西门", "feverdg@icloud.com");
        mList.add(element1);
        ContactInfo element2 = new ContactInfo("小红", "南宫", "146793455@icloud.com");
        mList.add(element2);
        ContactInfo element3 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com");
        mList.add(element3);
        ContactInfo element4 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com");
        mList.add(element4);
        ContactInfo element5 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com");
        mList.add(element5);
        ContactInfo element6 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com");
        mList.add(element6);
        ContactInfo element7 = new ContactInfo("小明", "西门", "feverdg@icloud.com");
        mList.add(element7);
        ContactInfo element8 = new ContactInfo("小红", "南宫", "146793455@icloud.com");
        mList.add(element8);
        ContactInfo element9 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com");
        mList.add(element9);
        ContactInfo element10 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com");
        mList.add(element10);
        ContactInfo element11 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com");
        mList.add(element11);
        ContactInfo element12 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com");
        mList.add(element12);
    }
}

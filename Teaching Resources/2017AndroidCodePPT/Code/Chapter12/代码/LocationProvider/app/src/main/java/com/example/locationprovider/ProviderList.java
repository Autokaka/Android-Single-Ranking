package com.example.locationprovider;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.cqupt.test8_1.R;

import java.util.List;

public class ProviderList extends AppCompatActivity {
    LocationManager lm;
    ListView lvProviderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_list);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lvProviderList = (ListView)findViewById(R.id.listViewProvider);


        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(true);
//        String provider = lm.getBestProvider(criteria, true);

        List<String> providerList = lm.getAllProviders();

//        Toast.makeText(ProviderList.this,provider,Toast.LENGTH_SHORT).show();

//        providerList.add(provider);
//
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,providerList);
        lvProviderList.setAdapter(adapter);
    }
}

package android.smart.home.smarthome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.adapter.FragmentAdapter;
import android.smart.home.smarthome.entity.DeviceInfo;
import android.smart.home.smarthome.fragment.DataStreamsShowFragment;
import android.smart.home.smarthome.fragment.DeviceDetailsFragment;
import android.smart.home.smarthome.widget.HeaderLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/11/10.
 *
 */

public class DeviceDetailsActivity extends BaseAppCompatActivity implements
        HeaderLayout.onLeftImageButtonClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        initViews();
    }

    protected void initViews() {
        headerLayout = (HeaderLayout) findViewById(R.id.device_details_actionbar);
        initTopBarForLeft(headerLayout,"设备详情",this);
        tabLayout = (TabLayout) findViewById(R.id.device_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.device_details_viewPager);
        initData();
        FragmentAdapter fragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected void initData(){
        titles.add("基本信息");
        titles.add("数据展示");
        Intent intent=getIntent();
        Bundle deviceData=intent.getExtras();
        String deviceDetails=deviceData.getString("device_details");
        Gson gson=new Gson();
        DeviceInfo details=gson.fromJson(deviceDetails,DeviceInfo.class);
        DeviceInfo.DataBean dataBean=details.getData();
        String detailsData=gson.toJson(dataBean);
        List<DeviceInfo.DataBean.DatastreamsBean> dataStreamsBeanList=new ArrayList<>();
        dataStreamsBeanList=dataBean.getDatastreams();
        ArrayList<String> dataStreamsID=new ArrayList<>();
        for(int i=0;i<dataStreamsBeanList.size();i++){
            dataStreamsID.add(dataStreamsBeanList.get(i).getId());
        }
        Bundle dataBag1=new Bundle();
        dataBag1.putString("details",detailsData);
        DeviceDetailsFragment detailsFragment=new DeviceDetailsFragment();
        detailsFragment.setArguments(dataBag1);
        fragments.add(detailsFragment);

        Bundle dataBag2=new Bundle();
        dataBag2.putStringArrayList("dataStreamsID",dataStreamsID);
        DataStreamsShowFragment dataStreamsShowFragment=new DataStreamsShowFragment();
        dataStreamsShowFragment.setArguments(dataBag2);
        fragments.add(dataStreamsShowFragment);
    }

    @Override
    public void onLeftClick() {
        Intent intent=new Intent();
        intent.setFlags(1);
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

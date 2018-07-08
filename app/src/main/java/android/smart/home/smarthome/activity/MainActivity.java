package android.smart.home.smarthome.activity;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.adapter.MainFragmentAdapter;
import android.smart.home.smarthome.animation.ChangedScaleFloatingTransition;
import android.smart.home.smarthome.fragment.DeviceFragment;
import android.smart.home.smarthome.fragment.HomeFragment;
import android.smart.home.smarthome.fragment.PersonalCenterFragment;
import android.smart.home.smarthome.mqtt.MQTTService;
import android.smart.home.smarthome.widget.HeaderLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ufreedom.floatingview.Floating;
import com.ufreedom.floatingview.FloatingBuilder;
import com.ufreedom.floatingview.FloatingElement;

/**
 * Created by Administrator on 2017/10/21.
 *
 */

public class MainActivity extends BaseAppCompatActivity{
    private MainFragmentAdapter mainFragmentAdapter;
    private ServiceConnection conn;
    private MQTTService mService;
    private MqttReceiver mReceiver;
    private int[] imageResId={R.drawable.home,R.drawable.device,R.drawable.me};
    private int[] imageResIdSelected={R.drawable.home_selected,R.drawable.device_selected,R.drawable.me_selected};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mainFragmentAdapter=new MainFragmentAdapter(getSupportFragmentManager(),this,fragments,titles,imageResId,imageResIdSelected);
        viewPager.setAdapter(mainFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        initEvents();
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MQTTService.MqttBinder mBinder = (MQTTService.MqttBinder) service;
                mService = mBinder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }
        };
        mReceiver = new MqttReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_TRIGGER_ANNOTATION");
        registerReceiver(mReceiver,intentFilter);
        final Intent bindIntent = new Intent(this, MQTTService.class);
        startService(bindIntent);
        bindService(bindIntent,conn, Service.BIND_AUTO_CREATE);
        Intent intent = getIntent();
        if(intent!=null) {
            int page = intent.getFlags();
            initTab(page);
            viewPager.setCurrentItem(page);
        }else{
            initTab(0);
            viewPager.setCurrentItem(0);
        }
    }

    class MqttReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String triggerInfo = intent.getStringExtra("trigger");
            Toast.makeText(context,triggerInfo,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if(mService!=null){
            mService=null;
            unbindService(conn);
        }
        stopService(new Intent(this, MQTTService.class));
        super.onDestroy();
    }

    protected void initViews() {
        headerLayout =(HeaderLayout) findViewById(R.id.main_headerLayout);
        initTopBarForOnlyTitle(headerLayout,"智能家居");
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        initData();
    }

    @Override
    protected void initData() {
        titles.add("首页");
        titles.add("设备");
        titles.add("我");

        HomeFragment homeFragment=new HomeFragment();
        DeviceFragment deviceFragment=new DeviceFragment();
        PersonalCenterFragment personalCenterFragment=new PersonalCenterFragment();
        fragments.add(homeFragment);
        fragments.add(deviceFragment);
        fragments.add(personalCenterFragment);
    }

    protected void initTab(int page){
         for(int i=0;i<tabLayout.getTabCount();i++){
             TabLayout.Tab tab=tabLayout.getTabAt(i);
             if(tab!=null){
                 if(i!=page) {
                     tab.setCustomView(mainFragmentAdapter.getTabView(i,false));
                 }else{
                     tab.setCustomView(mainFragmentAdapter.getTabView(page,true));
                 }
             }
         }
    }

    protected void initEvents() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int posintion = tab.getPosition();
                View tabView = tab.getCustomView();
                if (tabView != null) {
                     ViewParent customParent = tabView.getParent();
                        if (customParent != null) {
                            ((ViewGroup) customParent).removeView(tabView);
                            FloatingElement builder=new FloatingBuilder()
                                    .anchorView(tabView)
                                    .targetView(tabView)
                                    .offsetX(0)
                                    .offsetY(0)
                                    .floatingTransition(new ChangedScaleFloatingTransition(800,10,12))
                                    .build();
                            Floating floating = new Floating(MainActivity.this);
                            floating.startFloating(builder);
                            changTabView(posintion,tabView,true);
                            tab.setCustomView(tabView);
                        }
                    }
                viewPager.setCurrentItem(posintion);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int posintion = tab.getPosition();
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    ViewParent customParent = tabView.getParent();
                    if (customParent != null) {
                        ((ViewGroup) customParent).removeView(tabView);
                        changTabView(posintion,tabView,false);
                        tab.setCustomView(tabView);
                    }
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    public void changTabView(int position,View view,boolean selected){
        TextView textView = (TextView) view.findViewById(R.id.tv_tab);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_tab);
        if(selected){
            textView.setTextColor(getResources().getColor(R.color.deepskyblue));
            imageView.setImageResource(imageResIdSelected[position]);
        }else{
            textView.setTextColor(getResources().getColor(R.color.gray));
            imageView.setImageResource(imageResId[position]);
        }
    }


}

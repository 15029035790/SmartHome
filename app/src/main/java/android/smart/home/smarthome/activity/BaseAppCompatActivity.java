package android.smart.home.smarthome.activity;


import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.smart.home.smarthome.JudgeNetIsConnectedReceiver;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.SmartHomeApplication;
import android.smart.home.smarthome.widget.HeaderLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 *
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    protected HeaderLayout headerLayout;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected JudgeNetIsConnectedReceiver mJudgeNetIsConnectedReceiver;
    protected List<Fragment> fragments=new ArrayList<>();
    protected List<String> titles=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mJudgeNetIsConnectedReceiver=new JudgeNetIsConnectedReceiver();
    }
    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mJudgeNetIsConnectedReceiver,intentFilter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mJudgeNetIsConnectedReceiver!=null){
            this.unregisterReceiver(mJudgeNetIsConnectedReceiver);
        }
    }

    protected void initViews() {}
    protected void initEvents(){}
    protected void initData(){}


    protected void initTopBarForOnlyTitle(HeaderLayout mHeaderLayout,String titleName) {
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }

    protected void initTopBarForLeft(HeaderLayout mHeaderLayout,String titleName,
                                  HeaderLayout.onLeftImageButtonClickListener left_listener) {
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                left_listener);
    }
}

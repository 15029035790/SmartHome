package android.smart.home.smarthome.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smart.home.smarthome.JudgeNetIsConnectedReceiver;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.SmartHomeApplication;
import android.smart.home.smarthome.widget.HeaderLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2017/10/17.
 *
 */

public class SmartHomeActivity extends Activity {
    protected SmartHomeApplication mApplication;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected JudgeNetIsConnectedReceiver mJudgeNetIsConnectedReceiver;
    protected List<AsyncTask<Void, Void, Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Boolean>>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mApplication =(SmartHomeApplication)this.getApplication();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
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

    protected void initViews(){}
    protected void initEvents(){}
    protected Boolean validate(){return false;}

    protected boolean matchEmail(String text) {
        if (Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text)
                .matches()) {
            return true;
        }
        return false;
    }

    Toast mToast;

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text,
                                Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });

        }
    }

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (mToast == null) {
                    mToast = Toast.makeText(SmartHomeActivity.this.getApplicationContext(), resId,
                            Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }


    public void ShowLog(String msg){
        Log.d("ceshi",msg);
    }


    public void initTopBarForOnlyTitle(HeaderLayout mHeaderLayout,String titleName) {
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }


    public void initTopBarForBoth(HeaderLayout mHeaderLayout,String titleName, int rightDrawableId,String text,
                                  HeaderLayout.onLeftImageButtonClickListener  left_listener,
                                  HeaderLayout.onRightImageButtonClickListener right_listener) {
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                left_listener);
        mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId,text,
                right_listener);
    }

    public void initTopBarForBoth(HeaderLayout mHeaderLayout,String titleName, int rightDrawableId,
                                  HeaderLayout.onLeftImageButtonClickListener left_listener,
                                  HeaderLayout.onRightImageButtonClickListener right_listener) {
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                left_listener);
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                right_listener);
    }


    public void initTopBarForLeft(HeaderLayout mHeaderLayout,String titleName,
                                  HeaderLayout.onLeftImageButtonClickListener left_listener) {
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                left_listener);
    }


    public void startAnimActivity(Class<?> next) {
        this.startActivity(new Intent(this, next));
    }


}

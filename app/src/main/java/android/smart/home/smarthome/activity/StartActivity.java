package android.smart.home.smarthome.activity;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.entity.User;
import android.smart.home.smarthome.widget.WowSplashView;
import android.smart.home.smarthome.widget.WowView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import cn.bmob.v3.BmobUser;


/**
 * Created by Administrator on 2017/10/16.
 *
 */

public class StartActivity extends SmartHomeActivity {

    private static final int GO_HOME = 100;
 	private static final int GO_LOGIN = 200;

    private WowSplashView mWowSplashView;
    private WowView mWowView;
    private LinearLayout mStartRoot;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        super.initViews();
        mStartRoot = (LinearLayout) findViewById(R.id.start_root);
        mWowSplashView = (WowSplashView) findViewById(R.id.wowSplash);
        mWowView = (WowView) findViewById(R.id.wowView);
        mWowSplashView.startAnimate();
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        mWowSplashView.setOnEndListener(new WowSplashView.OnEndListener() {
            @Override
            public void onEnd(WowSplashView wowSplashView) {
                AnimationSet animationSetIn = (AnimationSet) AnimationUtils.loadAnimation(StartActivity.this,R.anim.zoom_in);
                AnimationSet animationSetOut = (AnimationSet) AnimationUtils.loadAnimation(StartActivity.this,R.anim.zoom_out);
                animationSetIn.setDuration(400);
                animationSetOut.setDuration(400);
                mWowSplashView.startAnimation(animationSetOut);
                mWowSplashView.setVisibility(View.GONE);
                mStartRoot.setVisibility(View.VISIBLE);
                mStartRoot.startAnimation(animationSetIn);
                //mWowView.setVisibility(View.VISIBLE);
                //mWowView.startAnimate(wowSplashView.getDrawingCache());
                User user = BmobUser.getCurrentUser(User.class);
                if(user!=null){
                     handler.sendEmptyMessageDelayed(GO_HOME,1000);
                }else{
                    handler.sendEmptyMessageDelayed(GO_LOGIN,1000);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case GO_HOME:
                    startAnimActivity(MainActivity.class);
                    finish();
                    break;
                case GO_LOGIN:
                    startAnimActivity(LoginActivity.class);
                    finish();
                    break;
                default: break;
            }
        }
    };


}

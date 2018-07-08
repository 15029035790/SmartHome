package android.smart.home.smarthome;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import cn.bmob.v3.Bmob;


/**
 * Created by Administrator on 2017/10/16.
 * Author:tangziwen
 */

public class SmartHomeApplication extends Application {
    //Bmob云平台应用SmartHome Application ID
    private static final String ApplicationID="16a97046f6b004b2bc3caa9d214b0557";

    @Override
    public void onCreate(){
        super.onCreate();
        Bmob.initialize(this,ApplicationID,"bmob");
    }


}

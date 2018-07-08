package android.smart.home.smarthome;

import android.app.Application;

import cn.bmob.v3.Bmob;


/**
 * Created by Administrator on 2017/10/16.
 * Author:tangziwen
 */

public class SmartHomeApplication extends Application {
    //Bmob云平台应用SmartHome Application ID
    private static final String ApplicationID="ff7f40cbe47f06af8193ccc4a911bcc5";

    @Override
    public void onCreate(){
        super.onCreate();
        Bmob.initialize(this,ApplicationID,"bmob");
    }


}

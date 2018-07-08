package android.smart.home.smarthome.fragment;

import android.os.Bundle;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.entity.DeviceInfo;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.allen.library.SuperTextView;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/11/11.
 *
 */

public class DeviceDetailsFragment extends BaseFragment {
    private SuperTextView stv_title;
    private SuperTextView stv_deviceId;
    private SuperTextView stv_authInfo;
    private SuperTextView stv_protocol;
    private SuperTextView stv_private;
    private boolean online=false;

    @Override
    protected int getResId() {
        return R.layout.fragment_device_details;
    }

    @Override
    protected void onRealViewLoaded(View view) {
        stv_title=(SuperTextView) view.findViewById(R.id.stv_title);
        stv_deviceId=(SuperTextView) view.findViewById(R.id.stv_deviceID);
        stv_authInfo=(SuperTextView) view.findViewById(R.id.stv_auth_info);
        stv_protocol=(SuperTextView) view.findViewById(R.id.stv_protocol);
        stv_private=(SuperTextView) view.findViewById(R.id.stv_private);
        initData();
        initEvents();
    }


    private void initData(){
        Bundle dataBag=getArguments();
        String details=dataBag.getString("details");
        Log.d("ceshi","devicefragmentdetails");
        if (details==null){
            return;
        }
        Gson gson=new Gson();
        DeviceInfo.DataBean dataBean=gson.fromJson(details, DeviceInfo.DataBean.class);
        String title=dataBean.getTitle();
        String create_time=dataBean.getCreate_time();
        String deviceID=dataBean.getId();
        String auth_info=dataBean.getAuth_info();
        String protocol=dataBean.getProtocol();
        boolean privateX=dataBean.isPrivateX();
        online=dataBean.isOnline();
        Log.d("ceshi",title+" "+create_time+ " "+online);
        stv_title.setLeftTopString(title);
        stv_title.setLeftTopTextIsBold(true);
        stv_title.setRightBottomString(create_time);
        stv_title.setSwitchIsChecked(online);
        stv_deviceId.setCenterString(deviceID);
        stv_protocol.setCenterString(protocol);
        stv_authInfo.setCenterString(auth_info);
        String baomi=privateX?"私密":"公开";
        stv_private.setCenterString(baomi);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        stv_title.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                stv_title.setSwitchIsChecked(online);
            }
        });
    }
}

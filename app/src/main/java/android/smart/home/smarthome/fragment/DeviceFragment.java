package android.smart.home.smarthome.fragment;

import android.smart.home.smarthome.Interface.QueryDeviceInfo;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.adapter.DeviceRecycleViewAdapter;
import android.smart.home.smarthome.entity.DeviceInfo;
import android.smart.home.smarthome.util.HttpUtils;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import esptouch.demo_activity.EsptouchDemoActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/10.
 *
 */

public class DeviceFragment extends BaseFragment {
    //private BGARefreshLayout bgaRefreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fab_add_device;
    private static final String ds18b20_device_id = "19877859";
    private static final String dht11_device_id = "29670406";
    private static final String httpApiKey = "5ilhqDdDOv=dn4L56SxlL5cflk8=";
    private static final String mqttApiKey = "ShqzBzcQfDP5XFOw3mQ=Svxbl8M=";


    @Override
    protected int getResId() {
        return R.layout.fragment_devices;
    }

    @Override
    protected void onRealViewLoaded(View view) {
        recyclerView=(RecyclerView) view.findViewById(R.id.devices_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        fab_add_device = (FloatingActionButton) view.findViewById(R.id.fab_add_device);
        fab_add_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimActivity(EsptouchDemoActivity.class);
                getActivity().finish();
            }
        });
        getData();
    }

    private void getData(){
        QueryDeviceInfo queryDeviceInfo = HttpUtils.getInstance().getDeviceInfo();
        Observable<DeviceInfo> ds18b20Observable= queryDeviceInfo.getDevicesStatus(httpApiKey,ds18b20_device_id);
        Observable<DeviceInfo> dht11Observable= queryDeviceInfo.getDevicesStatus(mqttApiKey,dht11_device_id);
        Observable.zip(ds18b20Observable, dht11Observable, new BiFunction<DeviceInfo, DeviceInfo, List<DeviceInfo>>() {
            @Override
            public List<DeviceInfo> apply(DeviceInfo ds18b20, DeviceInfo dht11) throws Exception {
                List<DeviceInfo> devicesInfo =new ArrayList<>();
                devicesInfo.add(ds18b20);
                devicesInfo.add(dht11);
                return devicesInfo;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DeviceInfo>>() {
                    @Override
                    public void accept(List<DeviceInfo> devicesInfo) throws Exception {
                        Gson gson = new Gson();
                        String response = gson.toJson(devicesInfo);
                        Log.d("ceshi", response);
                        DeviceRecycleViewAdapter deviceRecycleViewAdapter=new DeviceRecycleViewAdapter(context,devicesInfo);
                        recyclerView.setAdapter(deviceRecycleViewAdapter);
                        deviceRecycleViewAdapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ShowToast("获取设备信息失败");
                    }
                });
    }
}

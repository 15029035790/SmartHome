package android.smart.home.smarthome.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.activity.DeviceDetailsActivity;
import android.smart.home.smarthome.entity.DeviceInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 *
 */

public class DeviceRecycleViewAdapter extends RecyclerView.Adapter<DeviceRecycleViewAdapter.ViewHolderDevice> {
    private Context context;
    private List<DeviceInfo> deviceInfoList;

    public DeviceRecycleViewAdapter(Context context,List<DeviceInfo> deviceInfoList){
        this.context=context;
        this.deviceInfoList=deviceInfoList;
    }

    @Override
    public ViewHolderDevice onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("ceshi","createViewHolder");
        View view= LayoutInflater.from(context).inflate(R.layout.fragment_device_item,parent,false);
        return new ViewHolderDevice(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDevice holder, int position) {
        final DeviceInfo deviceInfo=deviceInfoList.get(position);
        Log.d("ceshi","bindviewholder");
        DeviceInfo.DataBean dataBean=deviceInfo.getData();
        String title=dataBean.getTitle();
        String create_time=dataBean.getCreate_time();
        holder.stv_deviceInfo.setLeftTopString(title);
        holder.stv_deviceInfo.setCenterBottomString(create_time);
        holder.stv_deviceInfo.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                Intent intent=new Intent();
                intent.setClass(context,DeviceDetailsActivity.class);
                Bundle bundle=new Bundle();
                Gson gson=new Gson();
                String device_details=gson.toJson(deviceInfo);
                bundle.putString("device_details",device_details);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceInfoList.size();
    }


    class ViewHolderDevice extends RecyclerView.ViewHolder{
        SuperTextView stv_deviceInfo;
        private ViewHolderDevice(View itemView) {
            super(itemView);
            stv_deviceInfo = (SuperTextView) itemView.findViewById(R.id.device_item);
        }
    }
}


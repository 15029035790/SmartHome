package android.smart.home.smarthome.fragment;

import android.os.Bundle;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.adapter.DataStreamsShowAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 *
 */

public class DataStreamsShowFragment extends BaseFragment {
    private RecyclerView rv_dataStreams;
    private List<String> dataStreamsID = new ArrayList<>();

    @Override
    protected int getResId() {
        return R.layout.fragment_device_details_datastreams;
    }

    @Override
    protected void onRealViewLoaded(View view) {
        rv_dataStreams = (RecyclerView) view.findViewById(R.id.rv_dataStreams);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_dataStreams.setLayoutManager(layoutManager);
        initData();
        DataStreamsShowAdapter adapter=new DataStreamsShowAdapter(context,dataStreamsID);
        rv_dataStreams.setAdapter(adapter);
        initEvents();
    }

    public void initData(){
        Bundle dataBag = getArguments();
        dataStreamsID=dataBag.getStringArrayList("dataStreamsID");
    }
    @Override
    protected void initEvents() {
        super.initEvents();
    }
}

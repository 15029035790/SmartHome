package android.smart.home.smarthome.adapter;

import android.content.Context;
import android.smart.home.smarthome.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 *
 */

public class DataStreamsShowAdapter extends RecyclerView.Adapter<DataStreamsShowAdapter.ViewHolderDataStream> {
    private Context context;
    private List<String> dataStreamsID;

    public DataStreamsShowAdapter(Context context,List<String> dataStreamsID){
        this.context=context;
        this.dataStreamsID=dataStreamsID;
    }

    @Override
    public ViewHolderDataStream onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.datastreams_item,parent,false);
        return new ViewHolderDataStream(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDataStream holder, int position) {
        String dataStreamId=dataStreamsID.get(position);
        holder.stv_dataStreamId.setLeftString(dataStreamId);
    }


    @Override
    public int getItemCount() {
        return dataStreamsID.size();
    }

    class ViewHolderDataStream extends RecyclerView.ViewHolder{
        SuperTextView stv_dataStreamId;
        public ViewHolderDataStream(View itemView){
            super(itemView);
            stv_dataStreamId= (SuperTextView) itemView.findViewById(R.id.stv_data_stream);
        }
    }
}

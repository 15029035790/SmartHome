package android.smart.home.smarthome.util;

import android.smart.home.smarthome.Interface.AccessTokenInterface;
import android.smart.home.smarthome.Interface.QueryDeviceInfo;
import android.smart.home.smarthome.Interface.HistoryDataInterface;
import android.smart.home.smarthome.Interface.QueryDataPointsList;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by Administrator on 2017/11/1.
 *
 */

public class HttpUtils {
    private volatile static HttpUtils instance;
    private Retrofit retrofit,bigIotRetrofit;

    private HttpUtils(){
        String baseUrl="http://api.heclouds.com/";
        String bigIotBaseUrl = "https://www.bigiot.net/";
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        bigIotRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(bigIotBaseUrl)
                .build();

    }



    public static HttpUtils getInstance(){
        if(instance==null){
            synchronized (HttpUtils.class){
                if(instance==null){
                    instance=new HttpUtils();
                }
            }
        }
        return instance;
    }

    public QueryDataPointsList getDataPoints(){
        return retrofit.create(QueryDataPointsList.class);
    }

    public QueryDeviceInfo getDeviceInfo(){
        return retrofit.create(QueryDeviceInfo.class);
    }

    public AccessTokenInterface getAccessTokenInterface(){
        return bigIotRetrofit.create(AccessTokenInterface.class);
    }

    public HistoryDataInterface getHistoryDataInterface(){
        return bigIotRetrofit.create(HistoryDataInterface.class);
    }
}

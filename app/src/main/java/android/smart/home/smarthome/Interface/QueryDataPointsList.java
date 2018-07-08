package android.smart.home.smarthome.Interface;


import android.smart.home.smarthome.entity.OnenetDataPoints;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Administrator on 2017/11/1.
 *
 */

public interface QueryDataPointsList {
    @Headers("api-key:ShqzBzcQfDP5XFOw3mQ=Svxbl8M=")
    @GET("devices/{device_id}/datapoints")
    Observable<OnenetDataPoints> getDataPointsList(@Path("device_id") String device_id,
                                                   @Query("datastream_id") String datastream_id,
                                                   @Query("start") String start,
                                                   @Query("end") String end,
                                                   @Query("duration") Integer duration,
                                                   @Query("limit") Integer limit,
                                                   @Query("cursor") String cursor);
}

package android.smart.home.smarthome.Interface;

import android.smart.home.smarthome.entity.HistoryDataPoints;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by TangZiWen on 2017/12/14.
 *
 */

public interface HistoryDataInterface {
    @GET("oauth/historydata")
    Observable<List<HistoryDataPoints>> getHumidity(@Query("access_token")String access_token,
                                                    @Query("id")String id);
}

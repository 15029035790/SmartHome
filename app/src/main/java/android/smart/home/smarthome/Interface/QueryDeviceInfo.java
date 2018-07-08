package android.smart.home.smarthome.Interface;

import android.smart.home.smarthome.entity.DeviceInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/11/10.
 *
 */

public interface QueryDeviceInfo {
    @GET("devices/{device_id}")
    Observable<DeviceInfo> getDevicesStatus(@Header("api-key") String api_key,@Path("device_id") String device_id);
}

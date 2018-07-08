package android.smart.home.smarthome.Interface;

import android.smart.home.smarthome.entity.AccessToken;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by administrator on 2017/12/14.
 *
 */

public interface AccessTokenInterface {
    @FormUrlEncoded
    @POST("oauth/token")
    Observable<AccessToken> getAccessToken(@Field("client_id")String client_id,
                                           @Field("client_secret")String client_secret,
                                           @Field("username")String username,
                                           @Field("password")String password,
                                           @Field("grant_type")String grant_type);
}

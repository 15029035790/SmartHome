package android.smart.home.smarthome.entity;

import android.content.Context;
import android.smart.home.smarthome.util.HttpUtils;
import android.smart.home.smarthome.util.SharedPreferencesUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by TangZiWen on 2017/12/14.
 *
 */

public class TokenLoader {
    private static final String TAG = "ceshi";
    private static final String CLIENT_ID ="98";  //应用ID
    private static final String CLIENT_SECRRET ="5d97fae199";//应用密码
    private static final String USERNAME ="3089";//用户ID
    private static final String PASSWORD = "2f94818ef5";//用户APIKEY
    private static final String GRANT_TYPE ="password";
    private static final String TOKEN ="token";
    private static final String REFRESH_TOKEN ="refresh_token";
    private static final String TIME ="time";

    private AtomicBoolean mRefreshing = new AtomicBoolean(false);
    private PublishSubject<AccessToken> mPublishSubject;
    private Observable<AccessToken> mTokenObservable;
    private static Context mContext;


    private TokenLoader() {
        mPublishSubject = PublishSubject.create();
        final Gson gson = new Gson();
        Log.d(TAG,"init TokenLoader");
        mTokenObservable = HttpUtils.getInstance().getAccessTokenInterface()
                .getAccessToken(CLIENT_ID,CLIENT_SECRRET,USERNAME,PASSWORD,GRANT_TYPE);
                mTokenObservable.doOnNext(new Consumer<AccessToken>() {
                    @Override
                    public void accept(AccessToken accessToken) throws Exception {
                        String response = gson.toJson(accessToken);
                        Log.d(TAG, "Token response" + response);
                        SharedPreferencesUtils.put(mContext, TOKEN, accessToken.getAccess_token());
                        SharedPreferencesUtils.put(mContext, REFRESH_TOKEN, accessToken.getRefresh_token());
                        SharedPreferencesUtils.put(mContext, TIME, System.currentTimeMillis());
                        mRefreshing.set(false);
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRefreshing.set(false);
                    }
                }).subscribeOn(Schedulers.io());
    }

    public static TokenLoader getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final TokenLoader INSTANCE = new TokenLoader();
    }

    public String getCacheToken() {
        return (String)SharedPreferencesUtils.get(mContext,TOKEN,"get cache token failed!");
    }

    public Observable<AccessToken> getNetTokenLocked() {
        if (mRefreshing.compareAndSet(false, true)) {
            Log.d(TAG, "没有请求，发起一次新的Token请求");
            startTokenRequest();
        } else {
            Log.d(TAG, "已经有请求，直接返回等待");
        }
        return mPublishSubject;
    }

    private void startTokenRequest() {
        mTokenObservable.subscribe(mPublishSubject);
    }

    public static void setContext(Context context) {
        mContext = context;
    }

}

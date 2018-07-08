package android.smart.home.smarthome.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ViewStubCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/4.
 *
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    private View mView;
    protected Context context;
    private boolean mViewCreated=false;
    private ViewStubCompat mViewStub;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView!=null){
            mViewCreated=true;
            return mView;
        }
        context=inflater.getContext();
        FrameLayout root = new FrameLayout(context);
        mViewStub = new ViewStubCompat(context, null);
        mViewStub.setLayoutResource(getResId());
        root.addView(mViewStub, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        root.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.MATCH_PARENT));

        mView = root;

        mViewCreated = true;
        if (mUserVisible) {
            realLoad();
        }
        return mView;
    }

    private boolean mUserVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mUserVisible = isVisibleToUser;
        if (mUserVisible && mViewCreated) {
            realLoad();
        }

    }

    // 判断是否已经加载
    private boolean mLoaded = false;

    /**
     * 控制只允许加载一次
     */
    private void realLoad() {
        if (mLoaded) {
            return;
        }
        mLoaded = true;
        onRealViewLoaded(mViewStub.inflate());
    }

    @Override
    public void onDestroyView() {
        mViewCreated=false;
        Log.d("ceshi","superDestroy");
        super.onDestroyView();
    }

    /**
     * 获取真正的数据视图
     *
     * @return
     */
    protected abstract int getResId();

    /**
     * 当视图真正加载时调用
     */
    protected abstract void onRealViewLoaded(View view);

    @Override
    public void onClick(View v) {}
    protected void initEvents(){}

    Toast mToast;

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    public void ShowToast(final int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public void startAnimActivity(Class<?> next) {
        startActivity(new Intent(context, next));
    }

}

package android.smart.home.smarthome.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.activity.LoginActivity;
import android.smart.home.smarthome.activity.SetUserInfoActivity;
import android.smart.home.smarthome.entity.User;
import android.smart.home.smarthome.util.SDCardUtils;
import android.view.View;
import android.widget.Toast;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/11/12.
 *
 */

public class PersonalCenterFragment extends BaseFragment implements
        SuperTextView.OnSuperTextViewClickListener{
    private SuperTextView stv_user_header;
    private SuperTextView stv_choose_mode;
    private SuperTextView stv_security;
    private SuperTextView stv_about;
    private SuperTextView stv_helper;
    private SuperButton sbt_logout;


    @Override
    protected int getResId() {
        return R.layout.fragment_userinfo;
    }

    @Override
    protected void onRealViewLoaded(View view) {
        stv_user_header=(SuperTextView) view.findViewById(R.id.stv_user_header);
        stv_choose_mode=(SuperTextView) view.findViewById(R.id.stv_choose_mode);
        stv_security=(SuperTextView) view.findViewById(R.id.stv_security);
        stv_about=(SuperTextView) view.findViewById(R.id.stv_about);
        stv_helper=(SuperTextView) view.findViewById(R.id.stv_helper);
        sbt_logout=(SuperButton) view.findViewById(R.id.sbt_logout);
        final User user=BmobUser.getCurrentUser(User.class);
        if(user!=null){
            stv_user_header.setCenterBottomString(user.getUsername());
            BmobFile bmobFile=user.getAvatar();
            if(bmobFile!=null){
                String fileCachePath=context.getExternalCacheDir().getPath()+File.separator+user.getAvatar().getFilename();
                if(SDCardUtils.isFileExist(fileCachePath)) {
                    Bitmap avatar = SDCardUtils.loadBitmapFromSDCard(fileCachePath);
                    if (avatar != null)
                        stv_user_header.setLeftIcon(new BitmapDrawable(getResources(), avatar));
                }
            }
        }
        initEvents();
    }


    @Override
    protected void initEvents() {
        stv_user_header.setOnSuperTextViewClickListener(this);
        stv_choose_mode.setOnSuperTextViewClickListener(this);
        stv_security.setOnSuperTextViewClickListener(this);
        stv_about.setOnSuperTextViewClickListener(this);
        stv_helper.setOnSuperTextViewClickListener(this);
        sbt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                User currentUser=BmobUser.getCurrentUser(User.class);
                if(currentUser==null){
                    startAnimActivity(LoginActivity.class);
                    getActivity().finish();
                }
            }
        });
    }


    @Override
    public void onClickListener(SuperTextView superTextView) {
        switch(superTextView.getId()){
            case R.id.stv_user_header:
                startAnimActivity(SetUserInfoActivity.class);
                break;
            case R.id.stv_choose_mode:
                Toast.makeText(getActivity(),"有待后续开发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.stv_security:
                Toast.makeText(getActivity(),"有待后续开发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.stv_about:
                Toast.makeText(getActivity(),"开发者：汤子文",Toast.LENGTH_SHORT).show();
                break;
            case R.id.stv_helper:
                Toast.makeText(getActivity(),"有待后续开发",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}

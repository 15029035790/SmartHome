package android.smart.home.smarthome.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.entity.User;
import android.smart.home.smarthome.widget.HeaderLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.allen.library.SuperButton;

import java.lang.ref.WeakReference;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2017/10/22.
 *
 */

public class ForgetPasswordActivity extends SmartHomeActivity implements View.OnClickListener,
        HeaderLayout.onLeftImageButtonClickListener {

    private SuperButton sbt_send_email;
    private TextInputLayout til_verify_email;
    private TextInputEditText tie_verify_email;
    private TextView te_send_tip;
    private HeaderLayout mHeaderLayout;
    private Integer mReSendTime=180;
    private String email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        initViews();
        initEvents();
        initTopBarForLeft(mHeaderLayout,"找回密码",this);
}

    @Override
    protected void initViews() {
        super.initViews();
        mHeaderLayout = (HeaderLayout)findViewById(R.id.find_password_actionbar);
        sbt_send_email = (SuperButton)findViewById(R.id.sbt_send_email);
        til_verify_email = (TextInputLayout) findViewById(R.id.til_verify_email);
        tie_verify_email = (TextInputEditText) til_verify_email.getEditText();
        te_send_tip = (TextView) findViewById(R.id.sendTips);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        sbt_send_email.setOnClickListener(this);
        tie_verify_email.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        if (validate()) resetPasswordByEmail();
    }

    @Override
    protected Boolean validate() {
        email_address=tie_verify_email.getText().toString().trim();
        if (TextUtils.isEmpty(email_address)) {
            til_verify_email.setError(getResources().getString(R.string.error_email_address_null));
            tie_verify_email.requestFocus();
            return false;
        }else if(!matchEmail(email_address)){
            til_verify_email.setError(getResources().getString(R.string.error_validate_email_address));
            tie_verify_email.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            til_verify_email.setErrorEnabled(false);
        }
    };

    private void resetPasswordByEmail() {
        String sendTip="我们将发送验证邮件到你的邮箱：\n"+email_address+"\n记得登录您的邮箱重置密码";
        te_send_tip.setText(sendTip);
        te_send_tip.setVisibility(View.VISIBLE);
        mReSendHandler.sendEmptyMessage(0);
        BmobUser.resetPasswordByEmail(email_address, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ShowToast(R.string.verify_email_has_sended);
                }else{
                    ShowLog("错误码："+e.getErrorCode()+" 错误信息："+e.getMessage());
                }
            }
        });
    }

    @Override
    public void onLeftClick() {
        startAnimActivity(LoginActivity.class);
        finish();
    }
    @SuppressLint("HandlerLeak")
    private Handler mReSendHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mReSendTime > 1) {
                mReSendTime--;
                sbt_send_email.setEnabled(false);
                sbt_send_email.setText("重新发送邮件(" + mReSendTime + ")");
                mReSendHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                mReSendTime = 180;
                sbt_send_email.setEnabled(true);
                sbt_send_email.setText("重发验证邮件");
            }
        }
    };
}

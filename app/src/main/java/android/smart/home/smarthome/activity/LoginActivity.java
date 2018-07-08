package android.smart.home.smarthome.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.entity.User;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.allen.library.SuperButton;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/10/22.
 *
 */

public class LoginActivity extends SmartHomeActivity implements View.OnClickListener{

    private SuperButton sbt_login;
    private Button btn_register;
    private Button btn_forget_password;
    //private Button btn_administrator_login;
    private TextInputLayout til_username,til_password;
    private TextInputEditText tie_username,tie_password;
    private String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        //btn_administrator_login=(Button) findViewById(R.id.btn_administrator_login);
        btn_register = (Button) findViewById(R.id.btn_new_register);
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        til_username = (TextInputLayout) findViewById(R.id.til_login_username);
        til_password = (TextInputLayout) findViewById(R.id.til_login_password);
        tie_username = (TextInputEditText) til_username.getEditText();
        tie_password = (TextInputEditText) til_password.getEditText();
        sbt_login = (SuperButton) findViewById(R.id.sbt_login);
    }

    @Override
    protected void initEvents() {
        btn_forget_password.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        //btn_administrator_login.setOnClickListener(this);
        sbt_login.setOnClickListener(this);
        tie_username.addTextChangedListener(textWatcher);
        tie_password.addTextChangedListener(textWatcher);
        tie_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //inputMethodManager.showSoftInput(getWindow().getDecorView(),InputMethodManager.SHOW_FORCED);//显示
                    inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    if (validate()) {
                        login();
                        return true;
                    } else {
                        return false;
                    }

                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.btn_administrator_login:
               // break;
            case R.id.sbt_login:
                if(validate()) login();
                break;
            case R.id.btn_forget_password:
                startAnimActivity(ForgetPasswordActivity.class);
                finish();
                break;
            case R.id.btn_new_register:
                startAnimActivity(RegisterActivity.class);
                finish();
                break;
        }

    }

    @Override
    protected Boolean validate() {
        username = tie_username.getText().toString().trim();
        password = tie_password.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            til_username.setError(getResources().getString(R.string.error_username_null));
            tie_username.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(password)) {
            til_password.setError(getResources().getString(R.string.error_password_null));
            tie_password.requestFocus();
            return false;
        }else if(password.length()<6) {
            til_password.setError(getResources().getString(R.string.error_password_length));
            tie_password.requestFocus();
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
            til_username.setErrorEnabled(false);
            til_password.setErrorEnabled(false);
            password = tie_password.getText().toString().trim();
            if(!TextUtils.isEmpty(password) && password.length() > 16){
                til_password.setError("密码位数不应超过16位");
            }
        }
    };

    private void login(){
        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("正在验证，请耐心等候...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        final User bu = new User();
        bu.setUsername(username);
        bu.setPassword(password);
        bu.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                progress.dismiss();
                if(e==null){
                    startAnimActivity(MainActivity.class);
                    finish();
                }else{
                    ShowToast("错误码："+e.getErrorCode()+"详情："+e.getMessage());
                }
            }
        });
    }
}

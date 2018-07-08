package android.smart.home.smarthome.activity;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.entity.User;
import android.smart.home.smarthome.widget.HeaderLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.allen.library.SuperButton;

import cn.bmob.v3.BmobRole;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;



public class RegisterActivity extends SmartHomeActivity implements
		HeaderLayout.onLeftImageButtonClickListener{
    private HeaderLayout mHeaderLayout;
	private TextInputLayout til_user,til_pwd,til_email;
	private TextInputEditText tie_user,tie_pwd,tie_email;
	private SuperButton sbt_register;
    private String username,password,email_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initViews();
		initTopBarForLeft(mHeaderLayout,"注册",this);
		initEvents();
	}

	@Override
	protected void initViews() {
		super.initViews();
		mHeaderLayout=(HeaderLayout) findViewById(R.id.register_actionbar);
		til_user = (TextInputLayout) findViewById(R.id.til_rgusername);
		til_email = (TextInputLayout) findViewById(R.id.til_rgemail);
		til_pwd = (TextInputLayout) findViewById(R.id.til_rgpwd);
		tie_user = (TextInputEditText) til_user.getEditText();
		tie_email = (TextInputEditText) til_email.getEditText();
		tie_pwd = (TextInputEditText) til_pwd.getEditText();
	    sbt_register = (SuperButton) findViewById(R.id.sbt_register);
	}

	@Override
	protected void initEvents() {
		super.initEvents();
		sbt_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (validate())
					register();
			}
		});
		tie_email.addTextChangedListener(textWatcher);
		tie_user.addTextChangedListener(textWatcher);
		tie_pwd.addTextChangedListener(textWatcher);
		tie_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					//inputMethodManager.showSoftInput(getWindow().getDecorView(),InputMethodManager.SHOW_FORCED);//显示
					inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
					if (validate()) {
						register();
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
	protected Boolean validate() {
		username = tie_user.getText().toString().trim();
		password = tie_pwd.getText().toString().trim();
		email_address = tie_email.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			til_user.setError(getResources().getString(R.string.error_username_null));
			tie_user.requestFocus();
			return false;
		}else if (TextUtils.isEmpty(password)) {
			til_pwd.setError(getResources().getString(R.string.error_password_null));
			tie_pwd.requestFocus();
			return false;
		}else if(password.length()<6){
			til_pwd.setError(getResources().getString(R.string.error_password_length));
			tie_pwd.requestFocus();
			return false;
		}else if (TextUtils.isEmpty(email_address)) {
			til_email.setError(getResources().getString(R.string.error_email_address_null));
			tie_email.requestFocus();
			return false;
		}else if(!matchEmail(email_address)){
			til_email.setError(getResources().getString(R.string.error_validate_email_address));
			tie_email.requestFocus();
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
			til_user.setErrorEnabled(false);
			til_pwd.setErrorEnabled(false);
			til_email.setErrorEnabled(false);
			if(!TextUtils.isEmpty(password) && password.length() > 16){
				til_pwd.setError("密码位数不应超过16位");
			}
		}
	};
	private void register() {
		final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
		progress.setMessage("正在注册...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();

		final User bu = new User();
		bu.setUsername(username);
		bu.setPassword(password);
		bu.setEmail(email_address);
        bu.setRoot(false);
		bu.signUp(new SaveListener<User>() {

			@Override
			public void done(User user, BmobException e) {
                progress.dismiss();
				if (e == null) {
					ShowToast("注册成功");
					addRole();
					startAnimActivity(MainActivity.class);
					finish();
				} else {
					ShowToast("注册失败," + "错误码:" + e.getErrorCode() + " 错误信息：" + e.getMessage());
				}
			}
		});
	}

	private void addRole(){
		BmobRole users = new BmobRole("users");
		User myUser= BmobUser.getCurrentUser(User.class);
		users.getUsers().add(myUser);
		users.save(new SaveListener<String>() {
			@Override
			public void done(String s, BmobException e) {
				ShowLog("Success");
			}
		});
	}
	@Override
	public void onLeftClick() {
		startAnimActivity(LoginActivity.class);
		finish();
	}
}

package android.smart.home.smarthome.entity;

import java.io.Serializable;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser implements Serializable{

	private String sex;

	private Boolean root;//true表示管理员

	private String birthday;

	private BmobFile avatar;

	public String getBirthday() {return birthday;}

	public String getSex() {
		return sex;
	}

	public Boolean getRoot() {
		return root;
	}

	public void setRoot(Boolean root) {this.root = root;}

	public BmobFile getAvatar() {
		return avatar;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
}

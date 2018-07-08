package android.smart.home.smarthome.activity;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.entity.User;
import android.smart.home.smarthome.util.SDCardUtils;
import android.smart.home.smarthome.widget.HeaderLayout;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.DatePicker;

import com.allen.library.SuperTextView;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/11/12.
 *
 */

public class SetUserInfoActivity extends SmartHomeActivity implements
        HeaderLayout.onLeftImageButtonClickListener,
        SuperTextView.OnSuperTextViewClickListener {
    private HeaderLayout headerLayout;
    private SuperTextView stv_set_avatar;
    private SuperTextView stv_set_sex;
    private SuperTextView stv_set_birthday;
    private User currentUser;
    private String mCurrentPhotoPath;
    private String lastUserAvatarPath;
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_userinfo);
        currentUser = BmobUser.getCurrentUser(User.class);
        Gson gson = new Gson();
        ShowLog(gson.toJson(currentUser));
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        headerLayout = (HeaderLayout) findViewById(R.id.set_userInfo_headerLayout);
        initTopBarForLeft(headerLayout, "智能家居", this);
        stv_set_avatar = (SuperTextView) findViewById(R.id.stv_set_avatar);
        stv_set_sex = (SuperTextView) findViewById(R.id.stv_set_sex);
        stv_set_birthday = (SuperTextView) findViewById(R.id.stv_set_birthday);
        if (currentUser != null) {
            if (currentUser.getSex() != null) {
                stv_set_sex.setRightString(currentUser.getSex());
            }
            if (currentUser.getBirthday() != null) {
                stv_set_birthday.setRightString(currentUser.getBirthday());
            }
            if (currentUser.getAvatar() != null && SDCardUtils.isSDCardMounted()) {
                String fileCachePath=getExternalCacheDir().getPath()+File.separator+currentUser.getAvatar().getFilename();
                if(SDCardUtils.isFileExist(fileCachePath)) {
                    Bitmap avatar = SDCardUtils.loadBitmapFromSDCard(fileCachePath);
                    if(avatar!=null)
                        stv_set_avatar.setRightIcon(new BitmapDrawable(getResources(),avatar));
                }
            }
        }
    }




    @Override
    protected void initEvents() {
        stv_set_avatar.setOnSuperTextViewClickListener(this);
        stv_set_sex.setOnSuperTextViewClickListener(this);
        stv_set_birthday.setOnSuperTextViewClickListener(this);
    }

    @Override
    public void onLeftClick() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null){
                    ShowLog("同步用户信息出错"+e.getErrorCode()+e.getMessage());
                }
            }
        });
        Intent intent=new Intent();
        intent.setFlags(2);
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClickListener(SuperTextView superTextView) {
        switch (superTextView.getId()){
            case R.id.stv_set_avatar:
                showListDialog();
                break;
            case R.id.stv_set_sex:
                showSingleChoiceDialog();
                break;
            case R.id.stv_set_birthday:
                showDatePickerDialog();
                break;
            default:break;
        }
    }

    private void showListDialog(){
        final String[] items = {"拍照","从相册选择"};
        final AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
        listDialog.setTitle("修改头像");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        chooseHeadImageFromCameraCapture();
                        dialog.dismiss();
                        break;
                    case 1:
                        chooseHeadImageFromGallery();
                        dialog.dismiss();
                        break;
                    default:
                        dialog.dismiss();
                        break;
                }
            }
        });
        listDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        listDialog.show();
    }

    private void chooseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        //判断系统中是否有处理该Intent的Activity
        if (intentFromGallery.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
        } else {
            ShowToast("未找到图片查看器");
        }
    }

    private void chooseHeadImageFromCameraCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            // 创建文件来保存拍的照片
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch(IOException e){
                ShowToast("文件路径创建异常");
            }
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, CODE_CAMERA_REQUEST);
            }
        } else {
            ShowToast("无法启动相机");
        }
    }

    /**
     * 创建新文件
     *
     * @return File
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* 文件名 */
                ".jpg",         /* 后缀 */
                storageDir      /* 路径 */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            String filePath = null;
            //判断是哪一个的回调
            if (requestCode == CODE_GALLERY_REQUEST) {
                //返回的是content://的样式
                filePath = getFilePathFromContentUri(data.getData(), this);
            } else if (requestCode == CODE_CAMERA_REQUEST) {
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                }
            }
            if (!TextUtils.isEmpty(filePath)) {
                // 自定义大小，防止OOM
                final Bitmap bitmap = getSmallBitmap(filePath, 200, 200);
                final ProgressDialog progress = new ProgressDialog(SetUserInfoActivity.this);
                progress.setMessage("正在上传头像...");
                progress.show();
                ShowLog(filePath);
                File tempFile=new File(filePath);
                final String fileName=tempFile.getName();
                ShowLog(fileName);
                SDCardUtils.deleteDir(getExternalCacheDir());
                SDCardUtils.saveBitmapToSDCardPrivateCacheDir(bitmap,fileName,SetUserInfoActivity.this);
                String fileCachePath=getExternalCacheDir()+File.separator+fileName;
                final BmobFile avatar = new BmobFile(new File(fileCachePath));
                avatar.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            final User user = new User();
                            user.setAvatar(avatar);
                            user.update(currentUser.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null){
                                        stv_set_avatar.setRightIcon(new BitmapDrawable(getResources(),bitmap));
                                        progress.dismiss();
                                    }else{
                                        ShowLog("头像上传失败"+ e.getMessage() +e.getErrorCode());
                                        progress.dismiss();
                                    }
                                }
                            });
                        }else{
                            ShowLog(e.getMessage()+e.getErrorCode());
                        }
                    }
                });

            }
        }
    }
    /**
     * @param uri     content:// 样式
     * @param context
     * @return real file path
     */
    public static String getFilePathFromContentUri(Uri uri, Context context) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    /**
     * 获取小图片，防止OOM
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    /**
     * 计算图片缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private void showSingleChoiceDialog() {
        final String[] items = {"男", "女"};
        final AlertDialog.Builder chooseDialog = new AlertDialog.Builder(this);
        chooseDialog.setTitle("性别");
        chooseDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        stv_set_sex.setRightString("男");
                        dialog.dismiss();
                        updateUserSex("男");
                        break;
                    case 1:
                        stv_set_sex.setRightString("女");
                        dialog.dismiss();
                        updateUserSex("女");
                        break;
                    default:
                        dialog.dismiss();
                        break;
                }
            }
        });
        chooseDialog.show();
    }

    private void updateUserSex(String sex){
        final ProgressDialog progress = new ProgressDialog(SetUserInfoActivity.this);
        progress.setMessage("正在保存性别...");
        progress.show();
        User user = new User();
        user.setSex(sex);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e!= null){
                    ShowLog("用户信息更新失败 :" + e.getMessage() +e.getErrorCode());
                }
                progress.dismiss();
            }
        });
    }

    private void showDatePickerDialog(){
        final SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        final DatePickerDialog update_birthday = new DatePickerDialog(SetUserInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar birthday = Calendar.getInstance();
                birthday.set(Calendar.YEAR,year);
                birthday.set(Calendar.MONTH,month);
                birthday.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String str_birthday = birthdayFormat.format(birthday.getTime());
                stv_set_birthday.setRightString(str_birthday);
                updateUserBirthday(str_birthday);
            }
        }, 1996,0,1);
        update_birthday.show();
    }

    private void updateUserBirthday(String birthday){
        final ProgressDialog progress = new ProgressDialog(SetUserInfoActivity.this);
        progress.setMessage("正在保存生日...");
        progress.show();
        User user = new User();
        user.setBirthday(birthday);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null){
                    ShowToast("用户信息更新失败"+ e.getMessage() +e.getErrorCode());
                }
                progress.dismiss();
            }
        });
    }

}

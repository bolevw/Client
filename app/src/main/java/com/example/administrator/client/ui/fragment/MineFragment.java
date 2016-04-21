package com.example.administrator.client.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseApplication;
import com.example.administrator.client.base.BaseFragment;
import com.example.administrator.client.model.NameValue;
import com.example.administrator.client.ui.activity.AboutMeActivity;
import com.example.administrator.client.ui.activity.ChangeBirthActivity;
import com.example.administrator.client.ui.activity.ChangePwActivity;
import com.example.administrator.client.ui.activity.IntegralActivity;
import com.example.administrator.client.ui.activity.OrderHistoryActivity;
import com.example.administrator.client.utils.BitmapCompressUtil;
import com.example.administrator.client.utils.PicassoUtils;
import com.example.administrator.client.utils.ToastUtils;
import com.example.administrator.client.utils.album.AlbumIntentUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MineFragment extends BaseFragment {

    private LinearLayout username, changePassword, changeBirth, mineIntegral, aboutMe, logOut, mineOrder;

    private ImageView avatarImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        return v;
    }

    @Override
    protected void initView(View v) {
        username = (LinearLayout) v.findViewById(R.id.usernameLinearLayout);
        changePassword = (LinearLayout) v.findViewById(R.id.changePasswordLinearLayout);
        changeBirth = (LinearLayout) v.findViewById(R.id.changeBirthLinearLayout);
        mineIntegral = (LinearLayout) v.findViewById(R.id.mineIntegralLinearLayout);
        aboutMe = (LinearLayout) v.findViewById(R.id.aboutMeLinearLayout);
        mineOrder = (LinearLayout) v.findViewById(R.id.mineOrderLinearLayout);

        logOut = (LinearLayout) v.findViewById(R.id.logoutLinearLayout);

        avatarImageView = (ImageView) v.findViewById(R.id.avatarImageView);

    }

    @Override
    protected void bind() {
        getData();
    }

    private void getData() {
        AVUser user = AVUser.getCurrentUser();
        AVFile file = user.getAVFile("avatar");
        if (file == null) {
            return;
        } else {
            PicassoUtils.normalShowImage(getActivity(), file.getUrl(), avatarImageView);
        }

    }

    @Override
    protected void setListener() {
        username.setOnClickListener(listener);
        changeBirth.setOnClickListener(listener);
        changePassword.setOnClickListener(listener);
        mineIntegral.setOnClickListener(listener);
        aboutMe.setOnClickListener(listener);
        mineOrder.setOnClickListener(listener);

        logOut.setOnClickListener(listener);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == username.getId()) {
                AlbumIntentUtil.startAlbumActivityForSingleResult(MineFragment.this, "选择照片", true, false, true);
            }
            if (id == changeBirth.getId()) {
                //change birth
                startActivity(new Intent(getActivity(), ChangeBirthActivity.class));
            }

            if (id == changePassword.getId()) {
                // change pw
                startActivity(new Intent(getActivity(), ChangePwActivity.class));
            }

            if (id == mineIntegral.getId()) {
                // mine integral
                startActivity(new Intent(getActivity(), IntegralActivity.class));
            }

            if (id == mineOrder.getId()) {
                // mine order
                startActivity(new Intent(getActivity(), OrderHistoryActivity.class));

            }

            if (id == aboutMe.getId()) {
                startActivity(new Intent(getActivity(), AboutMeActivity.class));
            }

            if (id == logOut.getId()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("是否退出？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preferencesUtil.saveValue(new NameValue("login", false));
                        AVUser.getCurrentUser().logOut();
                        getActivity().finish();
                    }
                });
                builder.create().show();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlbumIntentUtil.REQUEST_CODE_ALBUM) {
            if (resultCode == AlbumIntentUtil.RESULT_OK) {
                ArrayList<String> photos = data.getStringArrayListExtra("selectedPhotos");
                if (photos.size() > 0) {
                    final String path = photos.get(0);
                    if (TextUtils.isEmpty(path)) {
                        ToastUtils.showNormalToast("选择图片");
                    } else {

                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                String targetPath = BaseApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/"
                                        + System.currentTimeMillis() + "_compress_avatar.jpg";
                                String resultPath = BitmapCompressUtil.compressToPath(path, targetPath);
                                subscriber.onNext(resultPath);
                                subscriber.onCompleted();
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(String compressPath) {
                                        uploadAvatar(compressPath);
                                        avatarImageView.setImageBitmap(BitmapFactory.decodeFile(compressPath));

                                    }
                                });


                    }
                } else {
                    ToastUtils.showNormalToast("选择图片");

                }
            }
        }
    }


    private AVFile picFile;

    private void uploadAvatar(String path) {
        try {
            picFile = AVFile.withAbsoluteLocalPath("pic.jpg", path);
            picFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        ToastUtils.showNormalToast("上传照片成功!");
                        AVUser user = AVUser.getCurrentUser();
                        user.put("avatar", picFile);
                        user.saveInBackground();
                    } else {
                        Log.e("error", e.toString());
                        ToastUtils.showNormalToast("上传照片失败,请重新选择照片!");
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void unbind() {

    }
}

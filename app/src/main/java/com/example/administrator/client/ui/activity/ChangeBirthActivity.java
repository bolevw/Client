package com.example.administrator.client.ui.activity;

import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/20.
 */
public class ChangeBirthActivity extends BaseActivity {

    private Button confirmButton;
    private AppCompatEditText changeEditText;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_birth);

        confirmButton = (Button) findViewById(R.id.confirmButton);

        changeEditText = (AppCompatEditText) findViewById(R.id.changeEditText);
    }

    @Override
    protected void setListener() {

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(changeEditText.getText().toString())) {
                    ToastUtils.showNormalToast("请输入生日！");
                    return;
                }
                AVUser user = AVUser.getCurrentUser();
                user.put("birth", changeEditText.getText().toString());
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            ToastUtils.showNormalToast("修改成功！");
                            finish();
                        } else {
                            Log.e("error", e.getCode() + e.getMessage());
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void bind() {

    }

    @Override
    protected void unBind() {

    }
}

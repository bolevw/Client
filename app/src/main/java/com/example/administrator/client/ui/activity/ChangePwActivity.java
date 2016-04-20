package com.example.administrator.client.ui.activity;

import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/20.
 */
public class ChangePwActivity extends BaseActivity {


    private AppCompatEditText changePw, changePwAgain, pwBefore;

    private Button confirm;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_pw);

        pwBefore = (AppCompatEditText) findViewById(R.id.pwEditText);

        changePw = (AppCompatEditText) findViewById(R.id.changeEditText);
        changePwAgain = (AppCompatEditText) findViewById(R.id.changeAgainEditText);

        confirm = (Button) findViewById(R.id.confirmButton);
    }

    @Override
    protected void setListener() {

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = pwBefore.getText().toString();
                String pw1 = changePw.getText().toString();
                String pw2 = changePwAgain.getText().toString();

                if (TextUtils.isEmpty(pw)) {
                    return;
                }
                if (!pw1.equals(pw2)) {
                    ToastUtils.showNormalToast("两次输入密码不一致");
                } else {
                    AVUser user = AVUser.getCurrentUser();
                    user.updatePasswordInBackground(pw, pw2, new UpdatePasswordCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                ToastUtils.showNormalToast("更新成功!");
                                finish();
                            } else {
                                ToastUtils.showNormalToast("更新失败，请重新输入!");
                            }
                        }
                    });
                }
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

package com.example.administrator.client.ui.activity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.NameValue;
import com.example.administrator.client.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/18.
 */
public class RegisterActivity extends BaseActivity {


    private AutoCompleteTextView emailEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatEditText againPasswordEditText;
    private AppCompatEditText birthEditText;

    private AppCompatButton registerButton;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);

        emailEditText = (AutoCompleteTextView) findViewById(R.id.email);
        passwordEditText = (AppCompatEditText) findViewById(R.id.password);
        againPasswordEditText = (AppCompatEditText) findViewById(R.id.passwordAgain);
        birthEditText = (AppCompatEditText) findViewById(R.id.birthEditText);

        registerButton = (AppCompatButton) findViewById(R.id.registerButton);
    }

    @Override
    protected void setListener() {

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });

    }

    private void verify() {
        emailEditText.setError(null);
        passwordEditText.setError(null);
        againPasswordEditText.setError(null);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = againPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("账号不能为空!");
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("密码不能为空");
        } else if (password.length() < 6) {
            passwordEditText.setError("密码长度不能小于6位");
        } else if (!TextUtils.isEmpty(passwordAgain) && !passwordAgain.equals(password)) {
            againPasswordEditText.setError("确认密码与密码不一致");
        } else if (TextUtils.isEmpty(birthEditText.getText().toString())) {
            ToastUtils.showNormalToast("请输入您的生日");
        } else {
            register(email, password, passwordAgain, birthEditText.getText().toString());
        }
    }

    private void register(final String email, String password, String passwordAgain, String s) {

        final AVUser avUser = new AVUser();
        avUser.setUsername(email);
        avUser.setPassword(password);
        avUser.put("birth", s);
        avUser.put("integral", 0);
        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (null == e) {
                    //success
                    preferencesUtil.saveValue(new NameValue("login", true));
                    preferencesUtil.saveValue(new NameValue("userId", avUser.getObjectId()));
                    ToastUtils.showNormalToast("注册成功!");
                    finish();

                } else {
                    if (e.getCode() == 0) {
                        ToastUtils.showNormalToast("请检测您的网络连接!");
                    } else if (e.getCode() == 202) {
                        emailEditText.setError("该用户名已被注册！");
                    }
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

package com.example.administrator.client.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.NameValue;
import com.example.administrator.client.utils.SharedPreferencesUtil;

/**
 * Created by Administrator on 2016/4/18.
 */
public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferencesUtil preferencesUtil;

    private TextView registerTextView;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        // Set up the login form.
        preferencesUtil = SharedPreferencesUtil.getInstance(this);

        registerTextView = (TextView) findViewById(R.id.registerTextView);

        if (preferencesUtil.getBooleanValue("login")) {
            if (!preferencesUtil.getBooleanValue("getNum")) {
                startActivity(new Intent(this, GetNumActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } else {
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }
    }

    @Override
    protected void setListener() {
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    @Override
    protected void bind() {

    }

    @Override
    protected void unBind() {

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        AVUser user = new AVUser();
        user.logInInBackground(mEmailView.getText().toString(), mPasswordView.getText().toString(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                            }
                        });

                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        mProgressView.animate().setDuration(shortAnimTime).alpha(
                                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

                            }
                        });

                    } else {
                        // The ViewPropertyAnimator APIs are not available, so simply show
                        // and hide the relevant UI components.
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }

                    startActivity(new Intent(LoginActivity.this, GetNumActivity.class));
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    preferencesUtil.saveValue(new NameValue("login", true));
                    preferencesUtil.saveValue(new NameValue("userId", avUser.getObjectId()));
                    finish();
                } else {
                    Log.e("error", e.getCode() + " :" + e.getMessage());
                    mPasswordView.setError("密码错误");
                    mEmailView.setError("用户名错误");
                }
            }
        });


    }
}

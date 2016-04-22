package com.example.administrator.client.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.example.administrator.client.R;
import com.example.administrator.client.utils.SharedPreferencesUtil;

/**
 * Created by Administrator on 2016/3/21.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    protected SharedPreferencesUtil preferencesUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.setContext(getApplicationContext());

        preferencesUtil = SharedPreferencesUtil.getInstance(BaseApplication.getContext());

        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bind();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void logError(AVException e) {
        Log.e("error", e.getMessage() + e.getCode());
    }


    public Toolbar getToolbar() {
        return toolbar;
    }

    protected abstract void initView();

    protected abstract void setListener();

    protected abstract void bind();

    protected abstract void unBind();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBind();
    }


}

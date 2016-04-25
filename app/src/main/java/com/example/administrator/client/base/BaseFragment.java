package com.example.administrator.client.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.example.administrator.client.utils.SharedPreferencesUtil;

/**
 * Created by Administrator on 2016/3/21.
 */
public abstract class BaseFragment extends Fragment {

    public static final String TAG = BaseFragment.class.getSimpleName();
    protected SharedPreferencesUtil preferencesUtil;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferencesUtil = SharedPreferencesUtil.getInstance(BaseApplication.getContext());

        initView(view);
        setListener();

    }


    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    public void logError(AVException e) {
        Log.e("error", e.getCode() + e.getMessage());
    }

    protected abstract void initView(View v);

    protected abstract void bind();

    protected abstract void setListener();

    protected abstract void unbind();

    public BaseActivity getBaseActivity() {
        return (BaseActivity) this.getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind();
    }
}

package com.example.administrator.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseFragment;

/**
 * Created by Administrator on 2016/4/18.
 */
public class OrderFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        return v;
    }

    @Override
    protected void initView(View v) {

    }

    @Override
    protected void bind() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void unbind() {

    }
}

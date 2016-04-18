package com.example.administrator.client.ui.activity;

import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.base.BaseFragment;
import com.example.administrator.client.ui.fragment.MenuFragment;
import com.example.administrator.client.ui.fragment.MineFragment;
import com.example.administrator.client.ui.fragment.OrderFragment;
import com.example.administrator.client.utils.FragmentUtils;
import com.example.administrator.client.wedgets.BottomNav;


public class MainActivity extends BaseActivity {

    private BottomNav bottomNav;
    BaseFragment[] fragments = new BaseFragment[3];


    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        bottomNav = (BottomNav) findViewById(R.id.mainBottomNav);
        switchFragment(0);

        getToolbar().setNavigationIcon(R.mipmap.ic_menu);
    }

    @Override
    protected void setListener() {
        bottomNav.setListener(onNavItemClickListener);
        getToolbar().setNavigationOnClickListener(null);
    }

    BottomNav.OnNavItemClickListener onNavItemClickListener = new BottomNav.OnNavItemClickListener() {
        @Override
        public void onItemClick(int position) {
            switchFragment(position);
        }
    };

    private void switchFragment(int p) {
        BaseFragment fragment = fragments[p];
        if (fragment == null) {
            switch (p) {
                case 0:
                    fragment = new MenuFragment();
                    fragments[0] = fragment;
                    break;
                case 1:
                    fragment = new OrderFragment();
                    fragments[1] = fragment;
                    break;
                case 2:
                    fragment = new MineFragment();
                    fragments[2] = fragment;
                    break;
            }

        }

        FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.containerFrameLayout, fragments[p], false, fragment.TAG);
    }

    @Override
    protected void bind() {

    }

    @Override
    protected void unBind() {

    }
}

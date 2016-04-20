package com.example.administrator.client.ui.activity;

import android.widget.TextView;

import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.base.BaseFragment;
import com.example.administrator.client.ui.fragment.MenuFragment;
import com.example.administrator.client.ui.fragment.MineFragment;
import com.example.administrator.client.ui.fragment.OrderFragment;
import com.example.administrator.client.utils.FragmentUtils;
import com.example.administrator.client.wedgets.BottomNav;


public class MainActivity extends BaseActivity implements MenuFragment.ListShowListener {

    private BottomNav bottomNav;
    BaseFragment[] fragments = new BaseFragment[3];

    private TextView numTextView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        bottomNav = (BottomNav) findViewById(R.id.mainBottomNav);
        switchFragment(0);

        numTextView = (TextView) findViewById(R.id.num);
        getToolbar().setNavigationIcon(R.mipmap.ic_menu);

        numTextView.setText("您是" + preferencesUtil.getIntValue("num") + "号桌");
    }

    @Override
    protected void setListener() {
        bottomNav.setListener(onNavItemClickListener);
        getToolbar().setNavigationOnClickListener(null);
    }


    @Override
    public void onBackPressed() {
        if (this.show && currentP == 0) {
            ((MenuFragment) fragments[0]).hideList();
        } else {
            super.onBackPressed();
        }
    }

    private int currentP = 0;
    BottomNav.OnNavItemClickListener onNavItemClickListener = new BottomNav.OnNavItemClickListener() {
        @Override
        public void onItemClick(int position) {
            currentP = position;
            switchFragment(position);
        }
    };

    private void switchFragment(int p) {
        BaseFragment fragment = fragments[p];
        if (fragment == null) {
            switch (p) {
                case 0:
                    fragment = new MenuFragment();
                    ((MenuFragment) fragment).setListShowListener(this);
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

    private boolean show = false;

    @Override
    public void listShowListener(boolean show) {
        this.show = show;
    }
}

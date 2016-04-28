package com.example.administrator.client.ui.activity;

import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/28.
 */
public class EvaluateActivity extends BaseActivity {


    private AppCompatSeekBar speed, manner, encironment, price, appearance;

    private Button confirm;

    private int s = 100, m = 100, e = 100, p = 100, a = 100;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_evaluate);

        speed = (AppCompatSeekBar) findViewById(R.id.speedSeekBar);
        manner = (AppCompatSeekBar) findViewById(R.id.mannerSeekBar);
        encironment = (AppCompatSeekBar) findViewById(R.id.environmentSeekBar);
        price = (AppCompatSeekBar) findViewById(R.id.priceSeekBar);
        appearance = (AppCompatSeekBar) findViewById(R.id.appearanceSeekBar);

        confirm = (Button) findViewById(R.id.confirmButton);
    }

    @Override
    protected void setListener() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });

        speed.setOnSeekBarChangeListener(onSeekBarChangeListener);
        appearance.setOnSeekBarChangeListener(onSeekBarChangeListener);
        price.setOnSeekBarChangeListener(onSeekBarChangeListener);
        encironment.setOnSeekBarChangeListener(onSeekBarChangeListener);
        manner.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == R.id.speedSeekBar) {
                s = progress;
            }

            if (seekBar.getId() == R.id.mannerSeekBar) {
                m = progress;
            }

            if (seekBar.getId() == R.id.environmentSeekBar) {
                e = progress;
            }

            if (seekBar.getId() == R.id.priceSeekBar) {
                p = progress;
            }

            if (seekBar.getId() == R.id.appearanceSeekBar) {
                a = progress;
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void evaluate() {
        AVObject avObject = new AVObject("EvaluateTable");
        avObject.put("speed", s);
        avObject.put("userId", AVUser.getCurrentUser().getObjectId());
        avObject.put("manner", m);
        avObject.put("en", e);
        avObject.put("appearance", a);
        avObject.put("price", p);
        avObject.put("username", AVUser.getCurrentUser().getUsername());
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtils.showNormalToast("评价成功！");
                }
                finish();
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

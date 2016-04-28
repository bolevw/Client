package com.example.administrator.client.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.NameValue;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class GetNumActivity extends BaseActivity {

    private TextView num, speedTextView, priceTextView, environmentTextView, appearanceTextView, mannerTextView;
    private Button getNum, orderButton;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_get_number);

        num = (TextView) findViewById(R.id.numTextView);
        getNum = (Button) findViewById(R.id.getNumButton);
        orderButton = (Button) findViewById(R.id.beginOrderButton);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        environmentTextView = (TextView) findViewById(R.id.environmentTextView);
        appearanceTextView = (TextView) findViewById(R.id.appearanceTextView);
        mannerTextView = (TextView) findViewById(R.id.mannerTextView);

        orderButton.setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        getNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumber();
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesUtil.saveValue(new NameValue("num", count));
                preferencesUtil.saveValue(new NameValue("getNum", true));
                startActivity(new Intent(GetNumActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    int count = 0;

    private void getNumber() {
        AVQuery<AVObject> query = new AVQuery<>("NumberTable");
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null) {
                    count = i + 1;
                    AVObject object = new AVObject("NumberTable");
                    object.put("username", AVUser.getCurrentUser().getUsername());
                    object.put("count", count);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                num.setText("您是" + count + "号桌");
                                getNum.setVisibility(View.GONE);
                                orderButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    count = 0;
                    Log.e("error", e.getMessage() + e.getCode());
                }
            }
        });

    }

    @Override
    protected void bind() {
      getData();
    }

    int s, m, a, p, en;

    private void getData() {
        AVQuery<AVObject> query = new AVQuery<>("EvaluateTable");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (AVObject object : list) {
                            s += object.getNumber("speed").intValue();
                            m += object.getNumber("manner").intValue();
                            a += object.getNumber("appearance").intValue();
                            p += object.getNumber("price").intValue();
                            en += object.getNumber("en").intValue();
                        }

                        speedTextView.setText("上菜速度：" + s + "分");
                        mannerTextView.setText("服务态度：" + m + "分");
                        appearanceTextView.setText("菜品色型" + a + "分");
                        priceTextView.setText("菜品价格：" + p + "分");
                        environmentTextView.setText("餐厅环境：" + en + "分");

                    }
                }
            }
        });
    }

    @Override
    protected void unBind() {

    }
}

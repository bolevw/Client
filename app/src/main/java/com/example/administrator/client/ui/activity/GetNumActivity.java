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
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.NameValue;

/**
 * Created by Administrator on 2016/4/20.
 */
public class GetNumActivity extends BaseActivity {

    private TextView num;
    private Button getNum, orderButton;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_get_number);

        num = (TextView) findViewById(R.id.numTextView);
        getNum = (Button) findViewById(R.id.getNumButton);
        orderButton = (Button) findViewById(R.id.beginOrderButton);

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

    }

    @Override
    protected void unBind() {

    }
}

package com.example.administrator.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class PayActivity extends BaseActivity {
    private static final int ORDER_CLIENT_PAY_FINISH = 6;

    private TextView moneyTextView;
    private Button payButton;

    private RadioGroup radioGroup;
    private String payId;

    String objId;
    String money;

    public static void newInstance(Activity activity, String objId, String money) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("objId", objId);
        intent.putExtra("money", money);
        activity.startActivity(intent);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();
        objId = intent.getStringExtra("objId");
        money = intent.getStringExtra("money");


        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        payButton = (Button) findViewById(R.id.payButton);

        radioGroup = (RadioGroup) findViewById(R.id.payGroup);

        final AVObject payObject = new AVObject("PayTable");
        payObject.put("objId", objId);
        payObject.put("money", money);
        payObject.put("payStatus", 1);
        payObject.put("username", AVUser.getCurrentUser().getUsername());
        payObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    payId = payObject.getObjectId();
                } else {
                    ToastUtils.showNormalToast("参数错误，请重试！");
                    finish();
                    logError(e);
                }
            }
        });

        moneyTextView.setText("共需支付" + money + "元");
    }

    @Override
    protected void setListener() {

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery<AVObject> query = new AVQuery<AVObject>("OrderTable");
                query.whereEqualTo("objectId", objId);
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null && list.size() > 0) {
                            AVObject object = list.get(0);
                            object.put("orderStatus", ORDER_CLIENT_PAY_FINISH);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        AVQuery<AVObject> q = new AVQuery<AVObject>("PayTable");
                                        q.whereEqualTo("objectId", payId);
                                        q.findInBackground(new FindCallback<AVObject>() {
                                            @Override
                                            public void done(List<AVObject> list, AVException e) {
                                                if (e == null && list.size() > 0) {
                                                    AVObject o = list.get(0);
                                                    o.put("payStatus", 2);
                                                    o.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(AVException e) {
                                                            if (e == null) {
                                                                ToastUtils.showNormalToast("支付成功！");
                                                                finish();
                                                            } else {
                                                                logError(e);
                                                            }
                                                        }
                                                    });
                                                } else {

                                                }
                                            }
                                        });
                                    } else {

                                    }
                                }
                            });
                        } else {

                        }
                    }
                });
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.zhifubao) {

                }

                if (checkedId == R.id.weixin) {
                    ToastUtils.showNormalToast("weixin");
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

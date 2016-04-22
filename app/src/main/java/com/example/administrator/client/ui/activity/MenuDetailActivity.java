package com.example.administrator.client.ui.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.SearchResultModel;
import com.example.administrator.client.utils.PicassoUtils;

/**
 * Created by Administrator on 2016/4/22.
 */
public class MenuDetailActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_menu_detail);
        Intent intent = getIntent();
        SearchResultModel model = (SearchResultModel) intent.getSerializableExtra("menu");

        TextView detail = (TextView) findViewById(R.id.menuDetailTextView);
        TextView name = (TextView) findViewById(R.id.menuNameTextView);
        TextView function = (TextView) findViewById(R.id.menuFunction);
        TextView menuType = (TextView) findViewById(R.id.menuTypeTextView);
        TextView menuMethod = (TextView) findViewById(R.id.menuMethod);
        TextView money = (TextView) findViewById(R.id.menuMoneyTextView);

        ImageView imageView = (ImageView) findViewById(R.id.itemMenuImageView);


        PicassoUtils.normalShowImage(this, model.getImageSrc(), imageView);
        detail.setText("食材：" + model.getDetail());
        name.setText("菜名：" + model.getName());
        function.setText("营养价值：" + model.getFunction());
        menuType.setText("菜系：" + model.getType());
        menuMethod.setText("做法：" + model.getMethod());
        money.setText("价格：" + model.getMoney() + "元/份");


    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void bind() {

    }

    @Override
    protected void unBind() {

    }
}

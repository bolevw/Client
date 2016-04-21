package com.example.administrator.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.OrderItemModel;
import com.example.administrator.client.utils.GsonUtils;
import com.example.administrator.client.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class CreateOrderActivity extends BaseActivity {


    private RecyclerView recyclerView;

    private TextView allMoneyTextView;

    private Button submitButton;

    private AppCompatEditText tableNumEditText;

    private List<OrderItemModel> viewData = new ArrayList<>();
    private String allMoney;

    public static void newInstance(Activity activity, List<OrderItemModel> viewData, String allMoney) {
        Intent intent = new Intent(activity, CreateOrderActivity.class);
        intent.putExtra("data", (Serializable) viewData);
        intent.putExtra("allMoney", allMoney);
        activity.startActivity(intent);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_create_order);

        Intent intent = getIntent();
        viewData = (List<OrderItemModel>) intent.getSerializableExtra("data");
        allMoney = intent.getStringExtra("allMoney");


        recyclerView = (RecyclerView) findViewById(R.id.createOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allMoneyTextView = (TextView) findViewById(R.id.totalMoneyTextView);

        submitButton = (Button) findViewById(R.id.submitButton);

        allMoneyTextView.setText(allMoney);

        tableNumEditText = (AppCompatEditText) findViewById(R.id.tableNumEditText);

        tableNumEditText.setText(preferencesUtil.getIntValue("num") + "桌");
    }

    @Override
    protected void setListener() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMenuOrder();
            }
        });

        recyclerView.setAdapter(new RVAdapter());

    }

    private void createMenuOrder() {

        String userId = preferencesUtil.getStringValue("userId");
        AVObject avObject = new AVObject("OrderTable");
        avObject.put("userId", userId);
        avObject.put("username", AVUser.getCurrentUser().getUsername());
        avObject.put("tableNum", preferencesUtil.getIntValue("num") + "");
        avObject.put("menuList", GsonUtils.getInstance().getGson().toJson(viewData));
        avObject.put("orderStatus", 1);
        avObject.put("orderMoney", allMoney);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtils.showNormalToast("创建菜单成功，正在为您做菜!");
                    finish();
                } else {
                    ToastUtils.showNormalToast("网络错误,请检查您的网络!");

                    Log.e("error", e.getCode() + "  " + e.getMessage());
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

    class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_order_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            OrderItemModel itemData = viewData.get(position);
            Log.d("message", itemData.toString());

            vh.nameTextView.setText(itemData.getModel().getName());
            vh.noTextView.setText("x" + itemData.getNum());
            vh.moneyTextView.setText(itemData.getNum() * Integer.parseInt(itemData.getModel().getMoney()) + "元");
        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }

        class VH extends RecyclerView.ViewHolder {
            private TextView nameTextView, noTextView, moneyTextView;


            public VH(View itemView) {
                super(itemView);
                nameTextView = (TextView) itemView.findViewById(R.id.itemMenuName);
                noTextView = (TextView) itemView.findViewById(R.id.itemMenuNo);
                moneyTextView = (TextView) itemView.findViewById(R.id.itemMenuMoney);
            }
        }
    }
}

package com.example.administrator.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.MenuOrder;

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


    private List<MenuOrder> viewData = new ArrayList<>();
    private String allMoney;

    public static void newInstance(Activity activity, List<MenuOrder> viewData, String allMoney) {
        Intent intent = new Intent(activity, CreateOrderActivity.class);
        intent.putExtra("data", (Serializable) viewData);
        intent.putExtra("allMoney", allMoney);
        activity.startActivity(intent);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_create_order);

        Intent intent = getIntent();
        viewData = (List<MenuOrder>) intent.getSerializableExtra("data");
        allMoney = intent.getStringExtra("allMoney");

        recyclerView = (RecyclerView) findViewById(R.id.createOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allMoneyTextView = (TextView) findViewById(R.id.totalMoneyTextView);

        submitButton = (Button) findViewById(R.id.submitButton);

        allMoneyTextView.setText(allMoney);
    }

    @Override
    protected void setListener() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView.setAdapter(new RVAdapter());

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
            MenuOrder itemData = viewData.get(position);
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

package com.example.administrator.client.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.OrderHistoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class OrderHistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;


    private List<OrderHistoryModel> viewData = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_history);

        recyclerView = (RecyclerView) findViewById(R.id.orderHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.orderHistoryRefreshLayout);
    }

    @Override
    protected void setListener() {
        recyclerView.setAdapter(new RVAdapter());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    protected void bind() {
        getData();
    }

    private void getData() {
        AVQuery<AVObject> query = new AVQuery<>("OrderTable");
        query.whereGreaterThanOrEqualTo("orderStatus", 5);
        query.whereEqualTo("username", AVUser.getCurrentUser().getUsername());
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (list.size() > 0) {
                        viewData.clear();
                        for (AVObject object : list) {
                            OrderHistoryModel model = new OrderHistoryModel(object.getObjectId(), object.getString("orderMoney"), object.getUpdatedAt().toString(), object.getString("tableNum"));
                            viewData.add(model);
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    logError(e);
                }
            }
        });
    }

    @Override
    protected void unBind() {

    }

    class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            OrderHistoryModel model = viewData.get(position);
            vh.dateTextView.setText(model.getDate());
            vh.typeTextView.setText(model.getTableNum() + "桌，消费。");
            vh.moneyTextView.setText("+" + model.getMoney() + "元");

        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }

        class VH extends RecyclerView.ViewHolder {
            private TextView typeTextView;
            private TextView moneyTextView;
            private TextView dateTextView;

            public VH(View itemView) {
                super(itemView);
                typeTextView = (TextView) itemView.findViewById(R.id.typeTextView);
                moneyTextView = (TextView) itemView.findViewById(R.id.moneyTextView);
                dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            }
        }
    }
}

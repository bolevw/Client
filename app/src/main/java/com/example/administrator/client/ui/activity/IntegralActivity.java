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
import com.example.administrator.client.model.IntegralModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class IntegralActivity extends BaseActivity {

    private List<IntegralModel> viewData = new ArrayList<>();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView allMoney;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_integral);

        recyclerView = (RecyclerView) findViewById(R.id.integralRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.integralRefreshLayout);
        allMoney = (TextView) findViewById(R.id.allMoneyTextView);

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

    int all = 0;

    private void getData() {
        AVQuery<AVObject> query = new AVQuery<>("PayTable");
        query.whereEqualTo("username", AVUser.getCurrentUser().getUsername());
        query.whereEqualTo("payStatus", 2);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null && list.size() >= 0) {
                    viewData.clear();
                    all = 0;
                    for (AVObject object : list) {
                        IntegralModel model = new IntegralModel();
                        model.setDate(object.getCreatedAt().toString());
                        model.setMoney(object.getString("money"));
                        model.setId(object.getObjectId());
                        model.setType("饭店消费");
                        all = all + Integer.parseInt(model.getMoney());
                        viewData.add(model);
                    }
                    allMoney.setText(all + "分");
                    recyclerView.getAdapter().notifyDataSetChanged();
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
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_integral_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            IntegralModel model = viewData.get(position);
            vh.dateTextView.setText(model.getDate());
            vh.typeTextView.setText(model.getType());
            vh.moneyTextView.setText("+" + model.getMoney() + "分");

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

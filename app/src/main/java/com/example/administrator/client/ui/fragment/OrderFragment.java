package com.example.administrator.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseFragment;
import com.example.administrator.client.model.MenuOrderItemModel;
import com.example.administrator.client.model.MenuOrderModel;
import com.example.administrator.client.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class OrderFragment extends BaseFragment {


    private SwipeRefreshLayout refreshLayout;

    private RecyclerView orderRecyclerView;

    private List<MenuOrderModel> viewData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        return v;
    }

    @Override
    protected void initView(View v) {
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.orderRefreshLayout);

        orderRecyclerView = (RecyclerView) v.findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    protected void bind() {
        getData();
    }

    private void getData() {
        AVQuery<MenuOrderModel> query = new AVQuery<MenuOrderModel>("MenuOrderTable");

        query.whereEqualTo("userId", preferencesUtil.getStringValue("userId"));
        query.whereNotEqualTo("orderStatus", 3);

        query.findInBackground(new FindCallback<MenuOrderModel>() {
            @Override
            public void done(List<MenuOrderModel> list, AVException e) {
                if (e == null) {

                    viewData.clear();
                    viewData.addAll(list);
                } else {
                    Log.e("error", e.getCode() + " mee" + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void setListener() {
        orderRecyclerView.setAdapter(new OrderRVAdapter());
    }

    @Override
    protected void unbind() {

    }

    class OrderRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderVH(LayoutInflater.from(getActivity()).inflate(R.layout.item_order_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            OrderVH vh = (OrderVH) holder;
            MenuOrderModel model = viewData.get(position);
            vh.tableNumTextView.setText(model.getTableNum() + "号桌");
            vh.setItemViewData(model.getItemModel());
            vh.confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showNormalToast("confim table");
                }
            });

        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }

        private class OrderVH extends RecyclerView.ViewHolder {
            private RecyclerView itemOrderRecyclerView;
            private TextView tableNumTextView;
            private Button confirmButton;

            private List<MenuOrderItemModel> itemViewData = new ArrayList<>();

            public List<MenuOrderItemModel> getItemViewData() {
                return itemViewData;
            }

            public void setItemViewData(List<MenuOrderItemModel> itemViewData) {
                this.itemViewData = itemViewData;
                itemOrderRecyclerView.getAdapter().notifyDataSetChanged();
            }

            public OrderVH(View itemView) {
                super(itemView);

                tableNumTextView = (TextView) itemView.findViewById(R.id.tableNumTextView);
                itemOrderRecyclerView = (RecyclerView) itemView.findViewById(R.id.itemOrderRecyclerView);

                confirmButton = (Button) itemView.findViewById(R.id.itemOrderConfirmButton);


                itemOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                itemOrderRecyclerView.setAdapter(new ItemAdapter());
            }


            private class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new ItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_recyclerview_item, parent, false));
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    ItemVH vh = (ItemVH) holder;
                    MenuOrderItemModel model = itemViewData.get(position);

                    vh.name.setText(model.getModel().getName());
                    vh.confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showNormalToast("change status!");
                        }
                    });
                }

                @Override
                public int getItemCount() {
                    return itemViewData.size();
                }

                private class ItemVH extends RecyclerView.ViewHolder {
                    private TextView name;
                    private Button confirmButton;

                    public ItemVH(View itemView) {
                        super(itemView);

                        name = (TextView) itemView.findViewById(R.id.menuNameTextView);
                        confirmButton = (Button) itemView.findViewById(R.id.confirmButton);
                    }
                }
            }
        }
    }
}

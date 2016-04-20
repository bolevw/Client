package com.example.administrator.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseFragment;
import com.example.administrator.client.base.ItemData;
import com.example.administrator.client.model.MenuAVModel;
import com.example.administrator.client.model.OrderAVModel;
import com.example.administrator.client.model.OrderItemAVModel;
import com.example.administrator.client.utils.GsonUtils;
import com.example.administrator.client.utils.ToastUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class OrderFragment extends BaseFragment {


    private SwipeRefreshLayout refreshLayout;

    private RecyclerView orderRecyclerView;

    private List<OrderAVModel> viewData = new ArrayList<>();

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

        AVQuery<AVObject> query = new AVQuery<>("OrderTable");
        query.whereEqualTo("userId", preferencesUtil.getStringValue("userId"));
        query.whereNotEqualTo("orderStatus", 3);

        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                refreshLayout.setRefreshing(false);
                if (e == null) {
                    viewData.clear();
                    for (AVObject object : list) {
                        OrderAVModel model = new OrderAVModel();
                        model.setOrderStatus(object.getNumber("orderStatus").intValue());
                        model.setId(object.getObjectId());
                        model.setTableNum(object.getString("tableNum"));
                        model.setUserId(object.getString("userId"));
                        model.setUsername(object.getString("username"));
                        model.setOrderStatus(object.getNumber("orderStatus").intValue());
                        Type listType = new TypeToken<List<OrderItemAVModel>>() {
                        }.getType();
                        List<OrderItemAVModel> listData = GsonUtils.getInstance().getGson().fromJson(object.getString("menuList"), listType);
                        model.setMenuList(listData);
                        viewData.add(model);
                    }
                    orderRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    logError(e);
                }
            }
        });
    }

    @Override
    protected void setListener() {
        orderRecyclerView.setAdapter(new OrderRVAdapter());
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
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
            OrderAVModel model = viewData.get(position);

            vh.setItemViewData(new ItemData<Integer, List<OrderItemAVModel>>(position, model.getMenuList()));
            if (model.getOrderStatus() == 2) {
                vh.tableNumTextView.setText(model.getTableNum() + "号桌, 厨师已经开始做菜了，请等候服务员上菜");
                vh.confirmButton.setEnabled(true);
            } else if (model.getOrderStatus() == 3) {
                vh.tableNumTextView.setText(model.getTableNum() + "号桌,菜已经上齐了。");
                vh.confirmButton.setEnabled(true);
            } else {
                vh.tableNumTextView.setText(model.getTableNum() + "号桌, 等待厨师确认订单.");
                vh.confirmButton.setEnabled(false);
            }

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

            private ItemData<Integer, List<OrderItemAVModel>> itemViewData = new ItemData<>();

            private String obId;

            public String getObId() {
                return obId;
            }

            public void setObId(String obId) {
                this.obId = obId;
            }

            public ItemData<Integer, List<OrderItemAVModel>> getItemViewData() {

                return itemViewData;
            }

            public void setItemViewData(ItemData<Integer, List<OrderItemAVModel>> itemViewData) {
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
                    return new ItemVH(LayoutInflater.from(getActivity()).inflate(R.layout.item_order_recyclerview_item, parent, false));
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                    final ItemVH vh = (ItemVH) holder;
                    OrderItemAVModel model = itemViewData.getValue().get(position);

                    vh.name.setText(model.getModel().getName());
                    int status = model.getModel().getMenuStatus();
                    vh.confirmButton.setTag(itemViewData.getKey());
                    vh.confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final List<OrderItemAVModel> list1 = itemViewData.getValue();
                            OrderItemAVModel orderItemAVModel = list1.get(position);
                            MenuAVModel menuAVModel = orderItemAVModel.getModel();
                            menuAVModel.setMenuStatus(2);
                            list1.remove(position);
                            orderItemAVModel.setModel(menuAVModel);
                            list1.add(orderItemAVModel);
//                            setItemViewData(new ItemData<Integer, List<OrderItemAVModel>>(itemViewData.getKey(), list));


                            int p = (int) vh.confirmButton.getTag();
                            String obId = viewData.get(p).getId();
                            AVQuery<AVObject> query = new AVQuery<AVObject>("OrderTable");
                            query.whereEqualTo("objectId", obId);
                            query.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null && list.size() > 0) {
                                        AVObject getOb = list.get(0);
                                        getOb.put("menuList", GsonUtils.getInstance().getGson().toJson(list1));
                                        getOb.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    vh.confirmButton.setVisibility(View.GONE);
                                                    ToastUtils.showNormalToast("修改成功");
                                                    getData();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });

                    if (status == 2) {
                        vh.confirmButton.setVisibility(View.GONE);
                    } else {
                        vh.confirmButton.setVisibility(View.VISIBLE);
                    }

                    int orderStatus = viewData.get(itemViewData.getKey()).getOrderStatus();
                    if (orderStatus == 1) {
                        vh.confirmButton.setClickable(false);
                    } else {
                        vh.confirmButton.setClickable(true);
                    }
                }

                @Override
                public int getItemCount() {
                    return itemViewData.getValue().size();
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

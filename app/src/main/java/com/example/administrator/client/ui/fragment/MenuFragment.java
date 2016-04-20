package com.example.administrator.client.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseFragment;
import com.example.administrator.client.base.ItemData;
import com.example.administrator.client.model.MenuModel;
import com.example.administrator.client.model.OrderItemModel;
import com.example.administrator.client.ui.activity.CreateOrderActivity;
import com.example.administrator.client.utils.PicassoUtils;
import com.example.administrator.client.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MenuFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView menuListRecyclerView;

    private List<MenuModel> viewData = new ArrayList();


    private Map<Integer, ItemData<Integer, MenuModel>> nos = new HashMap<>();

    private ArrayList<ItemData<Integer, ItemData<Integer, MenuModel>>> datas = new ArrayList<>();


    private List<OrderItemModel> passData = new ArrayList<>();

    private FrameLayout fragmentContent, menuListContent;

    private int no = 0;

    private TextView moneyTextView;
    private TextView noTextView;

    private Button submitButton;

    private LinearLayout detailContent;


    static ListShowListener listShowListener;

    public ListShowListener getListShowListener() {
        return listShowListener;
    }

    public void setListShowListener(ListShowListener listShowListener) {
        this.listShowListener = listShowListener;
    }

    public interface ListShowListener {
        void listShowListener(boolean show);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        cacluteMenu();
    }

    @Override
    protected void initView(View v) {

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.menuRefreshLayout);
        recyclerView = (RecyclerView) v.findViewById(R.id.menuRecyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        moneyTextView = (TextView) v.findViewById(R.id.allMoneyTextView);
        noTextView = (TextView) v.findViewById(R.id.menuNoTextView);

        fragmentContent = (FrameLayout) v.findViewById(R.id.fragmentContainer);
        menuListContent = (FrameLayout) v.findViewById(R.id.menuListContent);

        submitButton = (Button) v.findViewById(R.id.submitButton);

        menuListRecyclerView = (RecyclerView) v.findViewById(R.id.menuListRecyclerView);

        menuListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        detailContent = (LinearLayout) v.findViewById(R.id.detailContent);


    }


    @Override
    protected void bind() {
        getData();
    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(onRefreshListener);

        recyclerView.setAdapter(new RVAdapter());

        menuListRecyclerView.setAdapter(new MenuListRVAdapter());

        detailContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentContent.setVisibility(fragmentContent.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                menuListRecyclerView.getAdapter().notifyDataSetChanged();
                if (fragmentContent.getVisibility() == View.VISIBLE) {
                    listShowListener.listShowListener(true);
                }
            }
        });

        menuListContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentContent.setVisibility(View.GONE);
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (all == 0) {
                    return;
                }
                CreateOrderActivity.newInstance(getActivity(), passData, all + "");
            }
        });
    }

    /**
     * 隐藏list
     */
    public void hideList() {
        fragmentContent.setVisibility(View.GONE);
    }


    @Override
    protected void unbind() {
        getData();
    }

    int size = 0;
    int all = 0;
    int allSize = 0;

    private void cacluteMenu() {


        datas.clear();
        passData.clear();

        size = nos.size();

        all = 0;
        allSize = 0;

        Set<Integer> set = nos.keySet();
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            int position = iterator.next();
            ItemData<Integer, MenuModel> mo = nos.get(position);
            all = all + mo.getKey() * Integer.parseInt(mo.getValue().getMoney());
            allSize = allSize + mo.getKey();
            datas.add(new ItemData<Integer, ItemData<Integer, MenuModel>>(position, mo));
            passData.add(new OrderItemModel(mo.getKey(), mo.getValue()));
        }

        moneyTextView.setText(all + "");
        noTextView.setText(size + "种菜," + allSize + "份。");
        recyclerView.getAdapter().notifyDataSetChanged();
        menuListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void getData() {
        refreshLayout.setRefreshing(true);
        AVQuery<AVObject> query = new AVQuery<>("MenuTable");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                refreshLayout.setRefreshing(false);
                if (null == e) {
                    if (list.size() != 0) {
                        viewData.clear();

                        for (AVObject object : list) {
                            MenuModel model = new MenuModel();
                            model.setId(object.getObjectId());
                            model.setName(object.getString("name"));
                            model.setDetail(object.getString("detail"));
                            model.setMoney(object.getString("money"));
                            String url = object.getAVFile("picSrc").getUrl();
                            model.setImageSrc(url);
                            model.setHas(object.getBoolean("has"));
                            model.setType(object.getString("type"));
                            model.setTaste(object.getString("taste"));
                            model.setMethod(object.getString("method"));
                            viewData.add(model);
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showNormalToast("请检查您的网络!");
                }
            }
        });
    }


    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getData();
        }
    };


    private class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getActivity()).inflate(R.layout.item_my_menu_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final MenuModel model = viewData.get(position);
            final VH vh = (VH) holder;
            vh.itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            vh.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no = nos.get(position) == null ? 0 : nos.get(position).getKey();
                    no++;
                    nos.put(position, new ItemData<Integer, MenuModel>(no, model));
                    vh.noTextView.setText(String.valueOf(no));
                    cacluteMenu();

                }
            });

            vh.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no = nos.get(position) == null ? 0 : nos.get(position).getKey();

                    if (no > 0) {
                        no--;
                    } else {
                        no = 0;
                    }
                    nos.put(position, new ItemData<Integer, MenuModel>(no, model));
                    vh.noTextView.setText(String.valueOf(no));
                    if (no == 0) {
                        nos.remove(position);
                    }
                    cacluteMenu();
                }
            });
            no = nos.get(position) == null ? 0 : nos.get(position).getKey();
            vh.noTextView.setText(String.valueOf(no));

            vh.moneyTextView.setText(model.getMoney() + "元/份");
            vh.nameTextView.setText(model.getName());
            vh.detailTextView.setText(model.getDetail());

            PicassoUtils.normalShowImage(getActivity(), model.getImageSrc(), vh.menuImageView);

        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }


        private class VH extends RecyclerView.ViewHolder {

            private LinearLayout itemContent;
            private ImageView menuImageView;
            private TextView nameTextView;
            private TextView detailTextView;
            private TextView moneyTextView;
            private TextView noTextView;

            private Button delButton;
            private Button addButton;

            public VH(View itemView) {
                super(itemView);

                itemContent = (LinearLayout) itemView.findViewById(R.id.itemMyMenuContent);

                menuImageView = (ImageView) itemView.findViewById(R.id.itemMenuImageView);
                nameTextView = (TextView) itemView.findViewById(R.id.itemMenuName);
                detailTextView = (TextView) itemView.findViewById(R.id.itemMenuDetail);
                moneyTextView = (TextView) itemView.findViewById(R.id.itemMenuMoney);
                noTextView = (TextView) itemView.findViewById(R.id.noTextView);
                delButton = (Button) itemView.findViewById(R.id.delButton);
                addButton = (Button) itemView.findViewById(R.id.addButton);
            }
        }
    }


    class MenuListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_list_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final VH vh = (VH) holder;
            final ItemData<Integer, MenuModel> itemData = datas.get(position).getValue();
            PicassoUtils.normalShowImage(getActivity(), itemData.getValue().getImageSrc(), vh.menuImageView);
            vh.nameTextView.setText(itemData.getValue().getName());
            vh.moneyTextView.setText(itemData.getValue().getMoney());


            vh.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no = itemData.getKey();
                    no++;
                    vh.noTextView.setText(String.valueOf(no));
                    itemData.setKey(no);
                    itemData.setValue(itemData.getValue());
                    datas.add(position, new ItemData<Integer, ItemData<Integer, MenuModel>>(datas.get(position).getKey(), itemData));
                    nos.put(datas.get(position).getKey(), itemData);
                    cacluteMenu();
                }
            });


            vh.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no = itemData.getKey();
                    if (no > 0) {
                        no--;
                    } else {
                        no = 0;
                    }

                    vh.noTextView.setText(String.valueOf(no));
                    itemData.setKey(no);
                    itemData.setValue(itemData.getValue());


                    if (no == 0) {
                        nos.remove(datas.get(position).getKey());
                        datas.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        datas.add(position, new ItemData<Integer, ItemData<Integer, MenuModel>>(datas.get(position).getKey(), itemData));
                        nos.put(datas.get(position).getKey(), itemData);
                    }
                    cacluteMenu();
                }
            });

            vh.noTextView.setText(String.valueOf(itemData.getKey()));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class VH extends RecyclerView.ViewHolder {

            private ImageView menuImageView;

            private TextView nameTextView;
            private TextView moneyTextView;
            private TextView noTextView;

            private Button addButton;
            private Button delButton;

            public VH(View itemView) {
                super(itemView);

                menuImageView = (ImageView) itemView.findViewById(R.id.itemMenuImageView);
                nameTextView = (TextView) itemView.findViewById(R.id.itemMenuName);
                moneyTextView = (TextView) itemView.findViewById(R.id.itemMenuMoney);
                noTextView = (TextView) itemView.findViewById(R.id.menuListNoTextView);

                addButton = (Button) itemView.findViewById(R.id.menuListAddButton);
                delButton = (Button) itemView.findViewById(R.id.menuListDelButton);

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            //
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class Man extends LinearLayoutManager {

        public Man(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            super.onLayoutChildren(recycler, state);
        }
    }
}

package com.example.administrator.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.administrator.client.utils.PicassoUtils;
import com.example.administrator.client.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MenuFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private List<MenuModel> viewData = new ArrayList();

    private Map<Integer, Integer> nos = new HashMap<>();

    private List<ItemData<Integer, Integer>> list = new ArrayList<>();

    private int no = 0;

    private TextView moneyTextView;
    private TextView noTextView;

    private Button submintButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    protected void initView(View v) {

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.menuRefreshLayout);
        recyclerView = (RecyclerView) v.findViewById(R.id.menuRecyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        moneyTextView = (TextView) v.findViewById(R.id.allMoneyTextView);
        noTextView = (TextView) v.findViewById(R.id.menuNoTextView);

        submintButton = (Button) v.findViewById(R.id.submitButton);

    }

    @Override
    protected void bind() {
        getData();
    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(onRefreshListener);

        recyclerView.setAdapter(new RVAdapter());
    }

    @Override
    protected void unbind() {
        getData();
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
            vh.noTextView.setText("0");


            vh.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no = nos.get(position) == null ? 0 : nos.get(position);
                    no++;
                    nos.put(position, no);
                    vh.noTextView.setText(String.valueOf(no));
                }
            });

            vh.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no = nos.get(position) == null ? 0 : nos.get(position);

                    if (no > 0) {
                        no--;
                    } else {
                        no = 0;
                    }
                    nos.put(position, no);
                    vh.noTextView.setText(String.valueOf(no));
                }
            });

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
}

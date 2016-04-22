package com.example.administrator.client.ui.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.example.administrator.client.base.BaseActivity;
import com.example.administrator.client.model.SearchResultModel;
import com.example.administrator.client.utils.PicassoUtils;
import com.example.administrator.client.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class SearchActivity extends BaseActivity {


    private AppCompatEditText searchKeyEditText;
    private Button searchButton;
    private RecyclerView searchResultRecyclerView;

    private List<SearchResultModel> viewData = new ArrayList();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);

        searchKeyEditText = (AppCompatEditText) findViewById(R.id.searchKeyEditText);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchResultRecyclerView = (RecyclerView) findViewById(R.id.searchResultRecyclerView);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void setListener() {
        searchResultRecyclerView.setAdapter(new MenuListRVAdapter());
        searchKeyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    viewData.clear();
                    searchResultRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchKeyEditText.getText().toString())) {
                    return;
                }

                search(searchKeyEditText.getText().toString());
            }
        });

    }

    private void search(String key) {
        AVQuery<AVObject> detailquery = new AVQuery<>("MenuTable");
        detailquery.whereContains("detail", key);

        AVQuery<AVObject> namequery = new AVQuery<>("MenuTable");
        namequery.whereContains("name", key);

        AVQuery<AVObject> methodquery = new AVQuery<>("MenuTable");
        methodquery.whereContains("method", key);

        AVQuery<AVObject> tastequery = new AVQuery<>("MenuTable");
        tastequery.whereContains("taste", key);

        AVQuery<AVObject> typequery = new AVQuery<>("MenuTable");
        typequery.whereContains("type", key);

        AVQuery<AVObject> functionquery = new AVQuery<>("MenuTable");
        functionquery.whereContains("function", key);


        AVQuery<AVObject> query = AVQuery.or(Arrays.asList(detailquery, namequery, methodquery, tastequery, typequery, functionquery));

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        viewData.clear();
                        for (AVObject object : list) {
                            SearchResultModel model = new SearchResultModel(object.getObjectId(), object.getAVFile("picSrc").getUrl(), object.getString("name"), object.getString("detail"),
                                    object.getString("function"), object.getString("money"), object.getString("type"), object.getString("taste"), object.getString("method"), object.getBoolean("has"), 1);
                            viewData.add(model);
                        }
                        searchResultRecyclerView.getAdapter().notifyDataSetChanged();
                    } else {
                        ToastUtils.showNormalToast("暂无相关菜品，请更换搜索条件!");
                    }
                } else {
                    logError(e);
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

    class MenuListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final VH vh = (VH) holder;
            final SearchResultModel itemData = viewData.get(position);
            PicassoUtils.normalShowImage(SearchActivity.this, itemData.getImageSrc(), vh.menuImageView);
            vh.nameTextView.setText("菜名：" + itemData.getName());
            vh.moneyTextView.setText(itemData.getMoney() + "元/份");

            vh.itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchActivity.this, MenuDetailActivity.class);
                    intent.putExtra("menu", (Serializable) itemData);
                    startActivity(intent);
                    finish();
                }
            });

        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }

        class VH extends RecyclerView.ViewHolder {

            private ImageView menuImageView;

            private TextView nameTextView;
            private TextView moneyTextView;

            private LinearLayout itemContainer;


            public VH(View itemView) {
                super(itemView);

                itemContainer = (LinearLayout) itemView.findViewById(R.id.itemSearchContainer);
                menuImageView = (ImageView) itemView.findViewById(R.id.itemMenuImageView);
                nameTextView = (TextView) itemView.findViewById(R.id.itemMenuName);
                moneyTextView = (TextView) itemView.findViewById(R.id.itemMenuMoney);

            }
        }
    }
}

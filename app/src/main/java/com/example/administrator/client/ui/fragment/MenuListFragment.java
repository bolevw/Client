package com.example.administrator.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.client.R;
import com.example.administrator.client.base.BaseFragment;
import com.example.administrator.client.base.ItemData;
import com.example.administrator.client.model.MenuModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/19.
 */
public class MenuListFragment extends BaseFragment {


    private FrameLayout menuListContent;

    private RecyclerView menuListRecyclerView;


    public static MenuListFragment newInstance(ArrayList<ItemData<Integer, MenuModel>> list, String tag) {
        MenuListFragment fragment = new MenuListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(tag, list);
        fragment.setArguments(bundle);
        return fragment;
    }

    ArrayList<ItemData<Integer, MenuModel>> data = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        data = bundle.getParcelableArrayList("datas");
        Log.d("datas", data.toString());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_list, container, false);
        return v;
    }

    @Override
    protected void initView(View v) {
        menuListContent = (FrameLayout) v.findViewById(R.id.menuListContent);
        menuListRecyclerView = (RecyclerView) v.findViewById(R.id.menuListRecyclerView);

        menuListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    protected void bind() {

    }

    @Override
    protected void setListener() {
        menuListContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    protected void unbind() {

    }

    class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_list_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            ItemData<Integer, MenuModel> itemData = data.get(position);


        }

        @Override
        public int getItemCount() {
            return data.size();
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
                noTextView = (TextView) itemView.findViewById(R.id.noTextView);

                addButton = (Button) itemView.findViewById(R.id.addButton);

            }
        }
    }
}

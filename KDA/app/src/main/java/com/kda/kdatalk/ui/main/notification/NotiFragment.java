package com.kda.kdatalk.ui.main.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.noti.Noti;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.main.newfeed.viewcontent.ViewContentNewFeedActivity;
import com.kda.kdatalk.ui.main.notification.adapter.NotiAdapter;
import com.kda.kdatalk.ui.widget.ProgressView;
import com.kda.kdatalk.utils.UtilLibs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotiFragment extends FragmentBase implements NotiFragmentView {
    private static final String TAG = NotiFragment.class.getSimpleName();

    View viewRoot;
    Context mContext;
    NotiPresenter notiPresenter;


    ArrayList<Noti> list_noti;

    @BindView(R.id.rv_noti)
    RecyclerView rv_noti;

    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;

    @BindView(R.id.progress_bar)
    ProgressView progress_bar;

    @BindView(R.id.lnl_add_load)
    LinearLayout lnl_add_load;

    @BindView(R.id.rl_main)
    RelativeLayout rl_main;

    NotiAdapter adapter;

    public static int currNoti = 1;


    public static NotiFragment newInstance() {
        Bundle args = new Bundle();
        NotiFragment fragment = new NotiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_noti;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        notiPresenter = new NotiPresenterImpl(mContext, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        rl_main.addView(loadmore);

        showProgress(true);

        list_noti = new ArrayList<>();
        notiPresenter.getListNoti(currNoti);

        rv_noti.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
//        manager.setStackFromEnd(true);
        rv_noti.setLayoutManager(manager);
        rv_noti.setNestedScrollingEnabled(false);

        adapter = new NotiAdapter(mContext, rv_noti, list_noti, new NotiAdapter.OnClickReadNoti() {
            @Override
            public void onClickRead(int position, boolean isRead) {
                Log.e(TAG, "onClickRead: " + position);
                Log.e(TAG, "onClickRead: " + list_noti.get(position).isRead);

                if (!isRead) {
                    notiPresenter.readNoti(list_noti.get(position).id);

                }

                Intent intent = new Intent(mContext, ViewContentNewFeedActivity.class);
                intent.putExtra("ID_FEED", list_noti.get(position).id_feed);
                mContext.startActivity(intent);
                // -> goto content feed

            }
        });

        rv_noti.setAdapter(adapter);


        adapter.setOnLoadMore(new NotiAdapter.OnLoadMore() {
            @Override
            public void onLoad() {
                list_noti.add(null);
                adapter.notifyDataSetChanged();
                notiPresenter.getListNoti(currNoti);
            }
        });



        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currNoti = 1;

                Log.e(TAG, "onRefresh: ");

                notiPresenter.getListNoti(currNoti);
            }
        });


        // loadmore


        rv_noti.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                sw_refresh.setEnabled(topRowVerticalPosition >= 0);

            }
        });


    }


    private void showLoadmore(boolean isShow) {

//        if (isShow) {
//
//            try {
//
//               loadmore.setVisibility(View.VISIBLE);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            try {
//                loadmore.setVisibility(View.GONE);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    boolean isCall = false;

    @Override
    public void getNotiSuccess(ArrayList<Noti> data) {

        adapter.setIsloading(false);

        if (currNoti == 1) {
            list_noti.clear();
        }

        if (list_noti.size() > 0 && currNoti > 1) {
            list_noti.remove(list_noti.size()-1);
        }

        isCall = false;
        showLoadmore(false);

        showProgress(false);

        if (data != null && data.size() > 0) {

            if (currNoti <= 1) {
                list_noti.clear();
            }

            currNoti++;

            Log.e(TAG, "getNotiSuccess: " + data.size());
            // have data

            list_noti.addAll(data);

            Log.e(TAG, "getNotiSuccess: " + list_noti.size());


            adapter.setList_noti(list_noti);

        }

        if (sw_refresh.isRefreshing()) {
            sw_refresh.setRefreshing(false);
        }

        adapter.notifyDataSetChanged();

        try {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onError(String mess) {
        UtilLibs.showAlert(mContext, mess);

        showProgress(false);

        if (sw_refresh.isRefreshing()) {
            sw_refresh.setRefreshing(false);
        }

    }

    private void showProgress(boolean isShow) {
        if (isShow) {
            progress_bar.setVisibility(View.VISIBLE);
        } else {
            progress_bar.setVisibility(View.GONE);

        }
    }

}

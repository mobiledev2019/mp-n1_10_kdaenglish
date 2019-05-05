package com.kda.kdatalk.ui.main.newfeed.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kda.kdatalk.R;
import com.kda.kdatalk.model.NewFeed;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.main.newfeed.addnewfeed.AddNewFeedActivity;
import com.kda.kdatalk.ui.main.newfeed.adapter.newfeed.NewFeedAdapter;
import com.kda.kdatalk.ui.main.newfeed.adapter.newfeed.NewFeedClickListener;
import com.kda.kdatalk.ui.main.newfeed.viewcontent.ViewContentNewFeedActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentNewFeed extends FragmentBase implements NewFeedFragmentView {

    private View viewRoot;
    private static final String TAG = FragmentNewFeed.class.getSimpleName();
    private Context mContext;

    @BindView(R.id.fb_add_feed)
    FloatingActionButton fb_add_feed;
    @BindView(R.id.rv_newfeed)
    RecyclerView rv_newFeed;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    NewFeedPresenter newFeedPresenter;
    NewFeedAdapter adapter;

    List<NewFeed> listNewFeed = new ArrayList<>();

    public static FragmentNewFeed newInstance() {
        Bundle args = new Bundle();
        FragmentNewFeed fragment = new FragmentNewFeed();
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
        return R.layout.fragment_newfeed;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
        } catch (Exception e) {

        }
        return viewRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        newFeedPresenter = new NewFeedPresenterImpl(mContext, this);
        listNewFeed = newFeedPresenter.getNewFeed();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new NewFeedAdapter(listNewFeed, mContext, new NewFeedClickListener() {
            @Override
            public void onClickLike(int position, String id_feed) {
                // call api
                Log.e(TAG, "onClickLike: " + id_feed);
            }

            @Override
            public void onClickComment(int position, String id_feed) {
                // show content
                Log.e(TAG, "onClickLike: " + id_feed);

            }

            @Override
            public void onClickViewContent(int position, String id_feed) {
                // show content
                Log.e(TAG, "onClickLike: " + id_feed);
                Intent intent = new Intent(mContext, ViewContentNewFeedActivity.class);
                intent.putExtra("ID_FEED", id_feed);
                mContext.startActivity(intent);

            }

            @Override
            public void onClickViewImage(int position_item, int position_image, String id_feed) {

                // click view Image
                changeFragment(R.id.viewMain,ViewImageFragment.newInstance(listNewFeed.get(position_item).list_image, position_image),true);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rv_newFeed.setLayoutManager(mLayoutManager);
        rv_newFeed.setAdapter(adapter);
    }

    @OnClick(R.id.fb_add_feed)
    public void AddFeed() {
        Intent intent = new Intent(mContext, AddNewFeedActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void showProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);

        }
    }
}

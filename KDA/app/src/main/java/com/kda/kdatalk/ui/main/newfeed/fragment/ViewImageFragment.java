package com.kda.kdatalk.ui.main.newfeed.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.main.newfeed.adapter.newfeed.ImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewImageFragment extends FragmentBase {

    View viewRoot;

    Context mContext;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    ImageViewAdapter adapter ;

    private static final String LISTURL = "LISTURL";
    private static final String NUMSTART = "NUMSTART";

    ArrayList<String> list_url;
    int numStart = -1;

    public static ViewImageFragment newInstance(ArrayList<String> listUrl, int numStart) {
        Bundle args = new Bundle();
        ViewImageFragment fragment = new ViewImageFragment();
        args.putStringArrayList(LISTURL, listUrl);
        args.putInt(NUMSTART, numStart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
        }catch (Exception e) {

        }

        numStart = getArguments().getInt(NUMSTART, -1);
        list_url = getArguments().getStringArrayList(LISTURL);
        return viewRoot;
    }

    @OnClick(R.id.iv_back)
    public void onBackPress() {
        getActivity().onBackPressed();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ImageViewAdapter(mContext, list_url);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(numStart);
    }


    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_view_image;
    }
}

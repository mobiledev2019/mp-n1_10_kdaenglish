package com.kda.kdatalk.ui.main.newfeed.fragment;

import com.kda.kdatalk.model.NewFeed;

import java.util.ArrayList;

public interface NewFeedFragmentView {
    void showProgress();
    void hideProgress();

    void getFeedSuccess(ArrayList<NewFeed> list_feed);

    void onError(String message);



}

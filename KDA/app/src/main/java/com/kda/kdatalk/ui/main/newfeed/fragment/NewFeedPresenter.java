package com.kda.kdatalk.ui.main.newfeed.fragment;

import com.kda.kdatalk.model.NewFeed;

import java.util.List;

import androidx.fragment.app.Fragment;

public interface NewFeedPresenter {
    void setFragment(Fragment fragment);
    void showProgress();
    void hideProgress();

    List<NewFeed> getNewFeed();

}

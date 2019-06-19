package com.kda.kdatalk.ui.main.newfeed.fragment;

import com.kda.kdatalk.model.NewFeed;
import com.kda.kdatalk.utils.ActionFeed;

import java.util.List;

import androidx.fragment.app.Fragment;

public interface NewFeedPresenter {
    void setFragment(Fragment fragment);
    void showProgress();
    void hideProgress();

    void getNewFeed(int currPage);

    void onActionLike(boolean isAction, String id_feed);

    void getLesson();

}

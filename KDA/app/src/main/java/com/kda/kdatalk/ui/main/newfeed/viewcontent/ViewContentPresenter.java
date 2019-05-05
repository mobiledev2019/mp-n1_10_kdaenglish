package com.kda.kdatalk.ui.main.newfeed.viewcontent;

import com.kda.kdatalk.model.Comment;
import com.kda.kdatalk.model.NewFeed;

import androidx.fragment.app.Fragment;

public interface ViewContentPresenter {
    NewFeed getDetailNF(String id_feed);
    void setFragment(Fragment fragment);
    void postComment(Comment comment, String id_feed);
}

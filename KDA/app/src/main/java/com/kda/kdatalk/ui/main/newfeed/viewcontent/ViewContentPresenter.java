package com.kda.kdatalk.ui.main.newfeed.viewcontent;

import com.kda.kdatalk.model.Comment;
import com.kda.kdatalk.model.NewFeed;

import androidx.fragment.app.Fragment;

public interface ViewContentPresenter {
    void getDetailNF(String id_feed);
    void setFragment(Fragment fragment);
    void postComment(String content, String err_str, String corr_str, String id_feed);

    void voteNFup(boolean isUp, String id_comment);
    void voteNFdown(boolean isdown, String id_comment);
}

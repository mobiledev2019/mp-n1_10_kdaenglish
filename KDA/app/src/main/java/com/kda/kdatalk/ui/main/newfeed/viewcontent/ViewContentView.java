package com.kda.kdatalk.ui.main.newfeed.viewcontent;

import com.kda.kdatalk.model.Comment;
import com.kda.kdatalk.model.NewFeed;

import java.util.ArrayList;

public interface ViewContentView {
    void showProgress();
    void hideProgress();

    void getDetailSuccess(boolean isSuccess, NewFeed newFeed, String message);

    void createCommentSuccess(Comment comment);
}

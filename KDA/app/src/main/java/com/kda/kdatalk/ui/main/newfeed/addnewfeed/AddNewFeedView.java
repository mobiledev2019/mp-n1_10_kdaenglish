package com.kda.kdatalk.ui.main.newfeed.addnewfeed;

public interface AddNewFeedView {
    void showProgress();
    void hideProgress();

    void onAddSuccess(boolean isSuccess, String message);
}

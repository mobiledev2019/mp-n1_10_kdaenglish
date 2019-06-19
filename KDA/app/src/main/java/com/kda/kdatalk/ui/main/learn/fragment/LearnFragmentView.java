package com.kda.kdatalk.ui.main.learn.fragment;

import com.kda.kdatalk.model.learn.LearnModel;

import java.util.ArrayList;

public interface LearnFragmentView {
    void onError(String mess);
    void onSuccess(ArrayList<LearnModel> data);

    void showProgress(boolean isShow);

}

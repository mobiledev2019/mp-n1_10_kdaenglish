package com.kda.kdatalk.ui.main.message.fragment;

import com.kda.kdatalk.model.chat.MessageModel;

import java.util.ArrayList;

public interface MessageFragmentView {
    void showProgress();
    void hideProgress();

    void onGetListSuccess(ArrayList<MessageModel> data);

    void onError(String msg);
}

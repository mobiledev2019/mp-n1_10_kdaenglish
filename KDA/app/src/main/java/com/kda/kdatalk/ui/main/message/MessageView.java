package com.kda.kdatalk.ui.main.message;

import com.kda.kdatalk.model.chat.ChatModel;

import java.util.ArrayList;

public interface MessageView {
    void getListMessSucceess(ArrayList<ChatModel> data);
    void onErr(String mes);

    void showProgress(boolean isShow);
}

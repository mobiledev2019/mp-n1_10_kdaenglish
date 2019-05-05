package com.kda.kdatalk.ui.main.message;

import com.kda.kdatalk.model.chat.ChatModel;

import java.util.ArrayList;

public interface Message_Presenter {
    ArrayList<ChatModel> getListChat(String id_mess);
}

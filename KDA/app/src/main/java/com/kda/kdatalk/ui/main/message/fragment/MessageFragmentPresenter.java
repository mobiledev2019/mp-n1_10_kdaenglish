package com.kda.kdatalk.ui.main.message.fragment;

import com.kda.kdatalk.model.chat.MessageModel;

import java.util.ArrayList;

public interface MessageFragmentPresenter {
    ArrayList<MessageModel> getListContact();
    void getListContactBySearch(String _idUser);

}

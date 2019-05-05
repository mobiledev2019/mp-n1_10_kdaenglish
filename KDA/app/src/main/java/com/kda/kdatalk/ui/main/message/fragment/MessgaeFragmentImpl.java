package com.kda.kdatalk.ui.main.message.fragment;

import android.content.Context;

import com.kda.kdatalk.model.chat.MessageModel;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import java.util.ArrayList;

public class MessgaeFragmentImpl implements MessageFragmentPresenter {
    Context mContext;
    MessageFragmentView messageFragmentView;

    ServiceFunction serviceFunction;

    public MessgaeFragmentImpl(Context mContext, MessageFragmentView messageFragmentView) {
        this.mContext = mContext;
        this.messageFragmentView = messageFragmentView;
        serviceFunction = APIUtils.getAPIService();
    }

    @Override
    public ArrayList<MessageModel> getListContact() {
        ArrayList<MessageModel> data = new ArrayList<>();

        //fake

        User user = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user), User.class);

        data.add(new MessageModel("1", user, "ahihi do ngock", "15:55:05"));
        data.add(new MessageModel("12", user, "getInstance do ngock", "22:55:05"));
        data.add(new MessageModel("13", user, "getString ", "21:55:05"));
        data.add(new MessageModel("14", user, "fromJson do getString", "17:00:05"));
        data.add(new MessageModel("15", user, "MessageModel do ArrayList", "22:05:48"));

        return data;
    }

    @Override
    public ArrayList<MessageModel> getListContactBySearch(String query) {
        ArrayList<MessageModel> data = new ArrayList<>();

        //fake

        User user = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user), User.class);

        data.add(new MessageModel("1", user, "ahihi do ngock", "15:55:05"));
        data.add(new MessageModel("12", user, "getInstance do ngock", "22:55:05"));
        data.add(new MessageModel("13", user, "getString ", "21:55:05"));
        data.add(new MessageModel("14", user, "fromJson do getString", "17:00:05"));
        data.add(new MessageModel("15", user, "MessageModel do ArrayList", "22:05:48"));

        return data;
    }
}

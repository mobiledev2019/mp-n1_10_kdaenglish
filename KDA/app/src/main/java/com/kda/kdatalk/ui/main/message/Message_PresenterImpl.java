package com.kda.kdatalk.ui.main.message;

import com.kda.kdatalk.model.chat.ChatModel;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import java.util.ArrayList;

public class Message_PresenterImpl implements Message_Presenter {
    private MessageView messageView;
    private ActivityBase mContext;

    public Message_PresenterImpl(MessageView messageView, ActivityBase mContext) {
        this.messageView = messageView;
        this.mContext = mContext;
    }

    @Override
    public ArrayList<ChatModel> getListChat(String id_mess) {

        ArrayList<ChatModel> list = new ArrayList<>();

        //
        User user = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user), User.class);
        list.add(new ChatModel("1", user, "chào cạu :V", false));
        list.add(new ChatModel("2", user, "chào xì gà gân", false));
        list.add(new ChatModel("3", user, "Cạn lời ", true));
        list.add(new ChatModel("4", user, "Éc éc", false));
        list.add(new ChatModel("5", user, "Message_PresenterImpl", true));
        list.add(new ChatModel("6", user, "ChatModel", true));
        list.add(new ChatModel("7", user, "Test Message", true));


        //

        return list;
    }
}

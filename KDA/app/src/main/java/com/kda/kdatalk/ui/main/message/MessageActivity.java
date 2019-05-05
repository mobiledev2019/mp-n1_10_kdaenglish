package com.kda.kdatalk.ui.main.message;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.chat.ChatModel;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.main.message.adapter.ChatAdapter;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends ActivityBase implements MessageView {

    private static final String ID_MESS = "ID_MESS";

    @BindView(R.id.iv_ava)
    CircleImageView iv_ava;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.rv_chat)
    RecyclerView rv_chat;

    @BindView(R.id.iv_record)
    ImageView iv_record;

    @BindView(R.id.iv_send)
    ImageView iv_send;

    @BindView(R.id.et_chat)
    EditText et_chat;

    private Message_Presenter presenter;
    private String id_mess = "";

    ChatAdapter adapter;

    ArrayList<ChatModel> list_chat = new ArrayList<>();
    User me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        id_mess = getIntent().getStringExtra(ID_MESS);
        presenter = new Message_PresenterImpl(this, this);


        setupData();
    }



    private void setupData() {
        me = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user), User.class);
        list_chat = presenter.getListChat(id_mess);
        adapter = new ChatAdapter(list_chat, this);
        rv_chat.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setStackFromEnd(true);
        rv_chat.setLayoutManager(manager);
        rv_chat.setAdapter(adapter);
    }

    @OnClick({R.id.iv_back, R.id.iv_send, R.id.iv_record})
    public void onClickAction(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                break;

            case R.id.iv_send:

                if (!et_chat.getText().toString().trim().isEmpty()) {
                    ChatModel c = new ChatModel();
                    c.isMe = true;
                    c.content = et_chat.getText().toString();
                    c.id = "10";
                    c.user = me;

                    list_chat.add(c);
                    adapter.notifyDataSetChanged();
                    et_chat.setText("");
                }

//                rv_chat.smoothScrollToPosition(list_chat.size()-1);
                rv_chat.scrollToPosition(list_chat.size()-1);


                break;

            case R.id.iv_record:
                break;
            default:
                break;
        }
    }

}

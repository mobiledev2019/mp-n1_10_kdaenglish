package com.kda.kdatalk.ui.main.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.chat.ChatEmit;
import com.kda.kdatalk.model.chat.ChatModel;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.socket.ChatSocketClient;

import com.kda.kdatalk.socket.ChatUpdateService;
import com.kda.kdatalk.socket.SocketUpdateService;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.main.message.adapter.ChatAdapter;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;
import com.kda.kdatalk.utils.UtilLibs;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;

public class MessageActivity extends ActivityBase implements MessageView {

    private static final String ID_MESS = "ID_MESS";
    private static final String URL_PARTNER = "URL_PARTNER";
    private static final String PARTNER_NAME = "PARTNER_NAME";
    private static final String TAG = MessageActivity.class.getSimpleName();

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

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    private Message_Presenter presenter;
    private String id_mess = "";
    private String url_partner = "";
    private String parter_name = "";

    ChatAdapter adapter;

    ArrayList<ChatModel> list_chat = new ArrayList<>();
    User me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        id_mess = getIntent().getStringExtra(ID_MESS);
        url_partner = getIntent().getStringExtra(URL_PARTNER);
        parter_name = getIntent().getStringExtra(PARTNER_NAME);
        Log.e(TAG, "onCreate: " + id_mess);
        presenter = new Message_PresenterImpl(this, this);

        setupData();

        registerReceiver(receiver, new IntentFilter(AppConstants.NEWCHAT));


        Intent intent = new Intent(this, ChatUpdateService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            registerReceiver(receiver, new IntentFilter(AppConstants.NEWCHAT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupData() {

        Picasso.get().load(url_partner).placeholder(R.drawable.user_no_img)
                .error(R.drawable.user_no_img)
                .into(iv_ava);

        tv_name.setText(parter_name);

        me = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_info), User.class);
        list_chat = new ArrayList<>();
        presenter.getListChat(id_mess);
        adapter = new ChatAdapter(list_chat, this, url_partner);
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
                onBackPressed();
                break;

            case R.id.iv_send:

                if (!et_chat.getText().toString().trim().isEmpty()) {

                    // emit

                    ChatEmit chat = new ChatEmit("text", et_chat.getText().toString(), me._id, id_mess);

                    JSONObject object = new JSONObject();
                    try {
                        object.put("type", chat.type);
                        object.put("detail", chat.detail);
                        object.put("roomId", chat.roomId);
                        object.put("auth", chat.auth);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ChatSocketClient.getInstants().emit("newMessage", object);

                    //

                    ChatModel c = new ChatModel();
                    c.id_user = me._id;
                    c.content = et_chat.getText().toString();
                    c.id = String.valueOf(System.currentTimeMillis());
                    c.isSending = true;

                    list_chat.add(c);
                    adapter.notifyDataSetChanged();
                    et_chat.setText("");
                }

//                rv_chat.smoothScrollToPosition(list_chat.size()-1);
                rv_chat.scrollToPosition(list_chat.size() - 1);


                break;

            case R.id.iv_record:
                break;
            default:
                break;
        }

    }

    HashMap<String, Emitter.Listener> event = new HashMap<>();


    @Override
    public void getListMessSucceess(ArrayList<ChatModel> data) {
        showProgress(false);

        if (data != null && data.size() > 0) {
            list_chat = data;
            adapter.setList_data(list_chat);
            adapter.notifyDataSetChanged();
        } else {
            UtilLibs.showAlert(this, "Đã có lỗi xảy ra!");
        }
    }

    @Override
    public void onErr(String mes) {
        showProgress(false);
        UtilLibs.showAlert(this, mes);

    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            progress_bar.setVisibility(View.VISIBLE);
        } else
            progress_bar.setVisibility(View.GONE);

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra(AppConstants.NEWCHAT);
            Log.e(TAG, "onReceive: " + data);
            if (!data.trim().isEmpty()) {
                try {
                    JSONObject object = new JSONObject(data);
                    ChatModel c = new ChatModel();
                    c.id = object.getString("_id");
                    c.content = object.getString("detail");
                    c.id_user = object.getString("auth");

                    if (c.id_user.equals(me._id)) {
                        // send success

                        ChatModel las = list_chat.get(list_chat.size() - 1);
                        las.id = c.id;
                        las.id_user = c.id_user;
                        las.isSending = false;
                        list_chat.set(list_chat.size() - 1, las);

                    } else {
                        list_chat.add(c);

                    }

//                    adapter.setList_data(list_chat);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    rv_chat.scrollToPosition(list_chat.size() - 1);
                                }
                            });
                        }
                    }, 500);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        stopService(new Intent(MessageActivity.this, ChatUpdateService.class));
        super.onDestroy();
    }
}

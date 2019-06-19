package com.kda.kdatalk.ui.main.message;

import android.util.Log;

import com.kda.kdatalk.model.chat.ChatModel;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Message_PresenterImpl implements Message_Presenter {
    private static final String TAG = Message_PresenterImpl.class.getSimpleName();
    private MessageView messageView;
    private ActivityBase mContext;

    String accessToken = "";

    ServiceFunction serviceFunction;

    public Message_PresenterImpl(MessageView messageView, ActivityBase mContext) {
        this.messageView = messageView;
        this.mContext = mContext;
        serviceFunction = APIUtils.getAPIService();
        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
    }

    @Override
    public void getListChat(String id_mess) {


        //
        JSONObject send = new JSONObject();
        try {
            send.put("accessToken", accessToken);
            send.put("id_mess", id_mess);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Log.e(TAG, "getListChat: " + send.toString());
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        // fake
        String url = "http://35.247.180.113:3000/api/getChat/" + id_mess;

        Log.e(TAG, "getListChat: " + url);
//        String url = "http://104.197.56.98:3000/api/getChatroom/";

        serviceFunction.getListChat(url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: getListChat: " + response.body());

                ArrayList<ChatModel> result = new ArrayList<>();

                if (response.isSuccessful() && response.code() == 200) {

                    try {

                        JSONObject main = new JSONObject(response.body());
                        JSONArray data = main.getJSONArray("chat");

                        for (int i = 0; i < data.length(); i ++) {
                            JSONObject ob = data.getJSONObject(i);

//                            JSONObject data = ob.getJSONObject("chat");

                            ChatModel model = new ChatModel();
                            model.id = ob.getString("_id");
                            model.content = ob.getString("detail");
                            model.id_user = ob.getString("auth");


//                            JSONObject ob_user = ob.getJSONObject("user");

//                            user.email = ob_user.getString("email");
//                            user.address = ob_user.getString("address");
//                            user.name = ob_user.getString("name");
//                            user.url_img_ava = ob_user.getString("url_img_ava");
//                            user.url_img_cover = ob_user.getString("url_img_cover");


//                            Log.e(TAG, "ID->: " + model.id);

                            result.add(model);
                        }

                        //done

                        messageView.getListMessSucceess(result);

                    } catch (JSONException e) {
                        messageView.onErr(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    messageView.onErr(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                messageView.onErr(t.getMessage());
                //
            }
        });

//
//        //
//
//
//
//
//        ArrayList<ChatModel> list = new ArrayList<>();
//
//        //
//        User user = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_info), User.class);
//        list.add(new ChatModel("1", user, "chào cạu :V", false));
//        list.add(new ChatModel("2", user, "chào xì gà gân", false));
//        list.add(new ChatModel("3", user, "Cạn lời ", true));
//        list.add(new ChatModel("4", user, "Éc éc", false));
//        list.add(new ChatModel("5", user, "Message_PresenterImpl", true));
//        list.add(new ChatModel("6", user, "ChatModel", true));
//        list.add(new ChatModel("7", user, "Test Message", true));
//
//
//        //

    }
}

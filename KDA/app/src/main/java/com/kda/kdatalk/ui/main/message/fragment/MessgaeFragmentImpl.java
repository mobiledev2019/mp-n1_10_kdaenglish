package com.kda.kdatalk.ui.main.message.fragment;

import android.content.Context;
import android.util.Log;

import com.kda.kdatalk.model.chat.MessageModel;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
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

public class MessgaeFragmentImpl implements MessageFragmentPresenter {
    private static final String TAG = MessgaeFragmentImpl.class.getSimpleName();

    Context mContext;
    MessageFragmentView messageFragmentView;

    ServiceFunction serviceFunction;

    String accessToken = "";

    public MessgaeFragmentImpl(Context mContext, MessageFragmentView messageFragmentView) {
        this.mContext = mContext;
        this.messageFragmentView = messageFragmentView;
        serviceFunction = APIUtils.getAPIService();
        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
    }

    @Override
    public ArrayList<MessageModel> getListContact() {
        ArrayList<MessageModel> data = new ArrayList<>();

        //fake

        User user = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_info), User.class);

        Log.e("FAKE_DATA", "getListContactBySearch: " + MyCache.getInstance().getString(DraffKey.user_info));

        data.add(new MessageModel("1", user, "ahihi do ngock", "15:55:05"));
        data.add(new MessageModel("12", user, "getInstance do ngock", "22:55:05"));
        data.add(new MessageModel("13", user, "getString ", "21:55:05"));
        data.add(new MessageModel("14", user, "fromJson do getString", "17:00:05"));
        data.add(new MessageModel("15", user, "MessageModel do ArrayList", "22:05:48"));

        return data;
    }

    @Override
    public void getListContactBySearch(String _idUser) {

        //

        JSONObject send = new JSONObject();
        try {
            send.put("accessToken", accessToken);
            send.put("_idUser", _idUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());
        //
        //35.247.180.113:3000/api/getChatroom/5d06094b046848a0fe30f474

        String url = "http://35.247.180.113:3000/api/getChatroom/" + _idUser;

        Log.e(TAG, "getListContactBySearch: " + url);

        serviceFunction.getListContact(url).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: " + response.body());

                ArrayList<MessageModel> result = new ArrayList<>();

                if (response.isSuccessful() && response.code() == 200) {

                    try {
//                        JSONObject main = new JSONObject(response.body());
                        JSONArray data = new JSONArray(response.body());

                        for (int i = 0; i < data.length(); i++) {

                            //


                            JSONObject main = data.getJSONObject(i);

                            // partner

                            JSONObject partner = main.getJSONObject("partner");
                            JSONObject ob_user = partner.getJSONObject("data");

//                            JSONObject ob_user = ob.getJSONObject("user");

                            User user = new User();
                            user._id = ob_user.getString("_id");
                            user.email = ob_user.getString("email");
                            user.address = ob_user.getString("address");
                            user.name = ob_user.getString("name");
                            user.url_img_ava = ob_user.getString("url_img_ava");
                            user.url_img_cover = ob_user.getString("url_img_cover");


                            // room

                            JSONObject room = main.getJSONObject("room");

                            MessageModel model = new MessageModel();
                            model.id = room.getString("_id");
                            JSONObject lastMsg = room.getJSONObject("lastMessage");
                            model.last_str = lastMsg.getString("detail");
                            model.last_time = lastMsg.getString("time");
                            model.seen = lastMsg.getBoolean("seen");

                            model.user = user;

                            Log.e(TAG, "onResponse:ID-> " + model.id);

                            result.add(model);

                        }

                        //done

                        messageFragmentView.onGetListSuccess(result);


                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getMessage());
                        messageFragmentView.onError(e.getMessage());
                        e.printStackTrace();
                    }


                } else {
                    messageFragmentView.onError(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                messageFragmentView.onError(t.getMessage());
            }
        });


        //

//        ArrayList<MessageModel> data = new ArrayList<>();
//
//        //fake
//
//        User user = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_info), User.class);
//
//        Log.e("FAKE_DATA", "getListContactBySearch: " + MyCache.getInstance().getString(DraffKey.user_info));
//
//        data.add(new MessageModel("1", user, "ahihi do ngock", "15:55:05"));
//        data.add(new MessageModel("12", user, "getInstance do ngock", "22:55:05"));
//        data.add(new MessageModel("13", user, "getString ", "21:55:05"));
//        data.add(new MessageModel("14", user, "fromJson do getString", "17:00:05"));
//        data.add(new MessageModel("15", user, "MessageModel do ArrayList", "22:05:48"));
//
//        return data;
    }
}

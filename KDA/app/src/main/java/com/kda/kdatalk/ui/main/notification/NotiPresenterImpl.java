package com.kda.kdatalk.ui.main.notification;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.kda.kdatalk.model.noti.Noti;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotiPresenterImpl implements NotiPresenter {
    private static final String TAG = NotiPresenterImpl.class.getSimpleName();

    Context mContext;
    NotiFragmentView notiFragmentView;

    private ArrayList<Noti> list_noti;

    ServiceFunction serviceFunction;

    String accessToken = "";

    public NotiPresenterImpl(Context mContext, NotiFragmentView notiFragmentView) {
        this.mContext = mContext;
        this.notiFragmentView = notiFragmentView;
        serviceFunction = APIUtils.getAPIService();
        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);

    }

    @Override
    public void setFragment(Fragment fragment) {
        if (fragment instanceof NotiFragment) {
            notiFragmentView = (NotiFragmentView) fragment;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void getListNoti(int currNoti) {

        //

        JSONObject send = new JSONObject();
        try {
            send.put("accessToken", accessToken);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String page = String.valueOf(currNoti);
        if (currNoti == 0) {
            page = "1";
        }


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());


        serviceFunction.getNotification(accessToken, page, "10").enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: URL " + response.raw().request().url());

                Log.e(TAG, "onResponse: " + response.body());


                ArrayList<Noti> result = new ArrayList<>();

                if (response.isSuccessful() && response.code() == 200) {

                    try {
                        JSONObject main = new JSONObject(response.body());
                        JSONObject data = main.getJSONObject("data");

                        JSONArray json_noti = data.getJSONArray("notifications");

                        for (int i = 0; i < json_noti.length(); i++) {
                            JSONObject ob = json_noti.getJSONObject(i);
                            Noti noti = new Noti();
                            noti.id = ob.getString("_id");
                            noti.isRead = ob.getBoolean("isRead");
                            noti.isComment = ob.getBoolean("isComment");
                            noti.contentNoti = ob.getString("contentNoti");
                            noti.id_feed = ob.getString("id_feed");
                            noti.user_name = ob.getString("user_name");
                            noti.user_url = ob.getString("user_url");
                            noti.time = ob.getString("time");

                            result.add(noti);

                        }


                        notiFragmentView.getNotiSuccess(result);
                    } catch (JSONException e) {
                        notiFragmentView.onError(e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    notiFragmentView.onError(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                notiFragmentView.onError(t.getMessage());
                Log.e(TAG, "onFailure: URL " + call.request().url());

            }


        });

    }

    @Override
    public void readNoti(String _idNoti) {
        Log.e(TAG, "readNoti: ");
        JSONObject send = new JSONObject();
        try {
            send.put("id_noti", _idNoti);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());


        serviceFunction.readNoti(accessToken, requestBody).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse:readNoti " + response.body());


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure:readNoti " + t.getMessage());
//                notiFragmentView.onError(t.getMessage());
            }


        });
    }

}

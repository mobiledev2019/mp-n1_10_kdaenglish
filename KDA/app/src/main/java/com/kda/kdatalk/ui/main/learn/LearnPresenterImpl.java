package com.kda.kdatalk.ui.main.learn;

import android.util.Log;

import com.kda.kdatalk.model.learn.VocabModel;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Handler;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearnPresenterImpl implements LearnPresenter {
    private static final String TAG = LearnPresenterImpl.class.getSimpleName();
    LearnView learnView;
    ActivityBase mContext;
    ServiceFunction serviceFunction;
    String accessToken = "";

    public LearnPresenterImpl(LearnView learnView, ActivityBase mContext) {
        this.learnView = learnView;
        this.mContext = mContext;
        serviceFunction = APIUtils.getAPIService();
        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
    }


    @Override
    public void getListVocab(String id_lesson) {

        //

        JSONObject send = new JSONObject();
        try {
            send.put("accessToken", accessToken);
            send.put("id_lesson", id_lesson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        Log.e(TAG, "getListVocab:  SEND DATA" + send.toString());

        serviceFunction.getListVocab(accessToken, requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    try {
                        ArrayList<VocabModel> result = new ArrayList<>();
                        JSONObject main = new JSONObject(response.body());
                        JSONObject data = main.getJSONObject("data");
                        JSONArray arr_data = main.getJSONArray("data");

                        for (int i = 0; i < arr_data.length(); i++) {
                            JSONObject ob = arr_data.getJSONObject(i);
                            VocabModel model = new VocabModel();
                            model.id = ob.getString("id");
                            model.vocab = ob.getString("vocab");
                            model.pronun = ob.getString("pronun");
                            model.url_voice = ob.getString("url_voice");
                            model.point = ob.getInt("point");
                            model.impress_pronun = ob.getString("impress_pronun");
                            model.isComplete = ob.getBoolean("isComplete");

                            result.add(model);
                        }


                        learnView.getListVocabSuccess(result);

                    } catch (JSONException e) {

                        learnView.onError(e.getMessage());
                        e.printStackTrace();
                    }

                    // success
                } else {
                    learnView.onError(response.body());

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                learnView.onError(t.getMessage());

            }
        });

    }

    @Override
    public int getScore(String id_vocab) {
        //

//        learnView.showProgress();


        //call api

//        learnView.hideProgress();

        //fake

        return 60;
    }

    @Override
    public void sendVoiceVocab(String word, String base64) {

        JSONObject send = new JSONObject();
        try {
            send.put("word", word);
            send.put("encode", base64);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "sendVoiceVocab: DATA SEND " + send.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        String url = "http://35.247.180.113:4000/getScore";

        serviceFunction.sendVoiceVocab(url, requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: " + response.body() );

                String result_phonetic = "";
                int result_score;

                if (response.isSuccessful() && response.code() == 200) {

                    try {
                        JSONObject main = new JSONObject(response.body());

//                        JSONArray arr_phonetic = main.getJSONArray("phonetic");
//
//                        for (int i = 0; i < arr_phonetic.length(); i++) {
//                            JSONObject ob = arr_phonetic.getJSONObject(i);
//
//
//                        }

                        result_score = main.getInt("score");

                        result_phonetic = main.getString("result");

                        learnView.resultVoice(result_phonetic, result_score);


                    } catch (JSONException e) {
                        learnView.onError(e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    learnView.onError(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                learnView.onError(t.getMessage());

            }
        });

    }
}

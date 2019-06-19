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
    public ArrayList<VocabModel> getListVocab(String id_lesson) {

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


        ArrayList<VocabModel> listVocab = new ArrayList<>();

        VocabModel vocabModel = new VocabModel("0", "vacation", "vāˈkāSHən", "url", 0, "ca");
        listVocab.add(vocabModel);

        vocabModel = new VocabModel("1", "connect", "kəˈnekt", "url", 0, "ec");
        listVocab.add(vocabModel);

        vocabModel = new VocabModel("2", "legendary", "ledʒənderi", "url", 0, "le");
        listVocab.add(vocabModel);

        vocabModel = new VocabModel("3", "people", "ˈpiːpl", "url", 0, "pe");
        listVocab.add(vocabModel);

        vocabModel = new VocabModel("4", "masturbate", "ˈmæstərbeɪt", "url", 0, "mas");
        listVocab.add(vocabModel);


        return listVocab;
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
    public void sendVoiceVocab(String id_vocab, String base64) {

//        JSONObject send=  new JSONObject();
//        try {
//            send.put("accessToken", accessToken);
//            send.put("id_vocab", id_vocab);
//            send.put("base64", base64);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.e(TAG, "sendVoiceVocab: DATA SEND " + send.toString());
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());
//
//        serviceFunction.sendVoiceVocab(accessToken, requestBody).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful() && response.code() == 200) {
//
//                    try {
//                        JSONObject main = new JSONObject(response.body());
//                        JSONObject data = main.getJSONObject("data");
//
//                        int score = data.getInt("score");
//
//                        learnView.getScoreSuccess(true, score);
//                    } catch (JSONException e) {
//                        learnView.onError(e.getMessage());
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    learnView.onError(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                learnView.onError(t.getMessage());
//
//            }
//        });

    }
}

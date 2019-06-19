package com.kda.kdatalk.ui.main.learn.fragment;

import android.content.Context;
import android.util.Log;

import com.kda.kdatalk.model.learn.LearnModel;
import com.kda.kdatalk.model.learn.LessonModel;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearnFagmentFragmentPresenterImpl implements LearnFragmentPresenter {
    private static final String TAG = LearnFagmentFragmentPresenterImpl.class.getSimpleName();
    private Context mContext;
    private LearnFragmentView learnFragmentView;
    private ServiceFunction serviceFunction;

    String accessToken = "";

    public LearnFagmentFragmentPresenterImpl(Context mContext, LearnFragmentView learnFragmentView) {
        this.mContext = mContext;
        this.learnFragmentView = learnFragmentView;
        serviceFunction = APIUtils.getAPIService();
        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
    }

    @Override
    public void getLearnModel() {

        learnFragmentView.showProgress(true);
        //


        JSONObject send = new JSONObject();
        try {
            send.put("accessToken", accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://35.247.180.113:4000/getLession";

        serviceFunction.getLearnModel(url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: " + response.body());

                if (response.isSuccessful() && response.code() == 200) {

                    ArrayList<LearnModel> arr_learn = new ArrayList<>();

                    try {

                        JSONArray main = new JSONArray(response.body());

                        for (int i = 0 ; i < main.length(); i ++) {
                            JSONObject ob = main.getJSONObject(i);
                            LearnModel model = new LearnModel();
                            model.name = ob.getString("name");
                            model.id = "";
                            JSONArray json_less = ob.getJSONArray("list_lesson");

                            ArrayList<LessonModel> arr_less = new ArrayList<>();

                            for (int j = 0; j < json_less.length(); j ++) {
                                JSONObject les_ob = json_less.getJSONObject(j);
                                LessonModel lessonModel = new LessonModel();
                                lessonModel.id = les_ob.getString("id");
                                lessonModel.difficult = les_ob.getString("difficult");
                                lessonModel.name = les_ob.getString("name");
                                lessonModel.url_image_lesson = les_ob.getString("url_image_lesson");

                                //

                                arr_less.add(lessonModel);

                            }
                            model.list_lesson = arr_less;

                            arr_learn.add(model);

                        }

//                        JSONObject main = new JSONObject(response.body());
//                        JSONObject main_data = main.getJSONObject("data");
//                        JSONArray data = main_data.getJSONArray("data");
//
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject ob = data.getJSONObject(i);
//                            LearnModel model = new LearnModel();
//                            model.id = ob.getString("id");
//                            model.name = ob.getString("name");
//
//                            ArrayList<LessonModel> list_lesson = new ArrayList<>();
//
//                            JSONArray json_less = ob.getJSONArray("list_lesson");
//
//                            for (int j = 0; j < json_less.length(); j++) {
//                                JSONObject less = json_less.getJSONObject(j);
//                                LessonModel lessonModel = new LessonModel();
//                                lessonModel.id = less.getString("id");
//                                lessonModel.url_image_lesson = less.getString("url_image_lesson");
//                                lessonModel.name = less.getString("name");
//                                lessonModel.difficult = less.getString("difficult");
//
//                                list_lesson.add(lessonModel);
//                            }
//
//                            model.list_lesson = list_lesson;
//
//                            arr_learn.add(model);

//                        }

                        learnFragmentView.onSuccess(arr_learn);
                        learnFragmentView.showProgress(false);



                    } catch (JSONException e) {
                        learnFragmentView.onError(e.getMessage());

                        e.printStackTrace();
                    }


                } else {
                    learnFragmentView.onError(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                learnFragmentView.onError(t.getMessage());

            }
        });


//        ArrayList<LearnModel> list_data = new ArrayList<>();
//
//        //fake
//
//        //
//
//        String fakeUrl = "https://scontent.fhan3-3.fna.fbcdn.net/v/t1.0-9/27658021_2113824185513559_169077171140592559_n.jpg?_nc_cat=108&_nc_oc=AQm0Lfl-nQHhddPZBtmbZYQrfVzsViPcMkTP1MjnJVHREO9bfHB8-3qll4Y9zEPyt_8&_nc_ht=scontent.fhan3-3.fna&oh=b8955a5789abdc0061eb269f0442674e&oe=5D42C344";
//        LessonModel lessonModel = new LessonModel("id", fakeUrl, "Lesson 1 - Basic terms", "easy");
//
//        ArrayList<LessonModel> listLesson = new ArrayList<>();
//        listLesson.add(lessonModel);
//        lessonModel = new LessonModel("id", fakeUrl, "Lesson 1 - Basic terms", "easy");
//        listLesson.add(lessonModel);
//        lessonModel = new LessonModel("id", fakeUrl, "Lesson 2 - Listening", "difficult");
//        listLesson.add(lessonModel);
//        lessonModel = new LessonModel("id", fakeUrl, "Lesson 3 - Taking vacation", "normal");
//        listLesson.add(lessonModel);
//        lessonModel = new LessonModel("id", fakeUrl, "Lesson 4 - Choosing a seat", "easy");
//        listLesson.add(lessonModel);
//
//        LearnModel learnModel = new LearnModel("id", "Travel 101", listLesson);
//        list_data.add(learnModel);
//        learnModel = new LearnModel("id", "At the airport", listLesson);
//        list_data.add(learnModel);
//        learnModel = new LearnModel("id", "Tongue twisters", listLesson);
//        list_data.add(learnModel);
//        learnFragmentView.showProgress(false);
//
//
//        Log.e(TAG, "getLearnModel: " + MyGson.getInstance().toJson(list_data));


//        return list_data;
    }
}

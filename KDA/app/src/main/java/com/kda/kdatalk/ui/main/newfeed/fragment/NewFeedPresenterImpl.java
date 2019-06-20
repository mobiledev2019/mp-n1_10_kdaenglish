package com.kda.kdatalk.ui.main.newfeed.fragment;

import android.content.Context;
import android.util.Log;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.Comment;
import com.kda.kdatalk.model.NewFeed;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.model.learn.LearnModel;
import com.kda.kdatalk.model.learn.LessonModel;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.utils.ActionFeed;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFeedPresenterImpl implements NewFeedPresenter {
    private static final String TAG = NewFeedPresenterImpl.class.getSimpleName();
    private Context mContext;
    private NewFeedFragmentView newFeedFragmentView;

    String accessToken = "";


    ServiceFunction serviceFunction;

    private List<NewFeed> list_data;

    public NewFeedPresenterImpl(Context mContext, NewFeedFragmentView newFeedFragmentView) {
        this.mContext = mContext;
        this.newFeedFragmentView = newFeedFragmentView;
        list_data = new ArrayList<>();
        serviceFunction = APIUtils.getAPIService();

        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
    }

    @Override
    public void setFragment(Fragment fragment) {
        if (fragment instanceof FragmentNewFeed) {
            newFeedFragmentView = (NewFeedFragmentView) fragment;
        }
    }

    @Override
    public void showProgress() {
        newFeedFragmentView.showProgress();
    }

    @Override
    public void hideProgress() {
        newFeedFragmentView.hideProgress();
    }

    @Override
    public void getNewFeed(int currPage) {
        // call Api

//        newFeedFragmentView.showProgress();

        JSONObject send = new JSONObject();
        try {
            send.put("accessToken", accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        String page = String.valueOf(currPage);

        serviceFunction.getNewFeed(accessToken, page, "10").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: " + response.body());
                if (response.isSuccessful() && response.code() == 200) {

                    ArrayList<NewFeed> ahihi = new ArrayList<>();

                    try {
                        JSONObject main = new JSONObject(response.body());
                        JSONObject data = main.getJSONObject("data");
                        JSONArray feeds = data.getJSONArray("feeds");

                        for (int i = 0; i < feeds.length(); i++) {
                            JSONObject ob = feeds.getJSONObject(i);
                            String id = ob.getString("id");
                            String content = ob.getString("content");
                            String create_at = ob.getString("create_at");

                            int num_like = ob.getInt("num_like");
                            int num_comment = ob.getInt("num_comment");
                            boolean isLike = ob.getBoolean("isLike");

                            //image


                            JSONArray arr = ob.getJSONArray("list_image");
                            ArrayList<String> list_img = new ArrayList<>();

                            if (arr != null && arr.length() > 0) {

                                for (int j = 0; j < arr.length(); j++) {
                                    list_img.add(arr.getString(j));

                                }

                            }

                            // user

                            JSONObject ob_user = ob.getJSONObject("user");

                            String id_user = ob_user.getString("id");
                            String email = ob_user.getString("email");
                            String address = ob_user.getString("address");
                            String name = ob_user.getString("name");
                            String url_img_ava = ob_user.getString("url_img_ava");
                            String url_img_cover = ob_user.getString("url_img_cover");
//                            User user = new User(id_user, email, address, name, url_img_ava, url_img_cover);


                            NewFeed newFeed = new NewFeed();
                            newFeed.id = id;
                            newFeed.content = content;
                            newFeed.create_at = create_at;
                            newFeed.isLike = isLike;
                            newFeed.list_image = list_img;
                            newFeed.num_like = num_like;
                            newFeed.num_comment = num_comment;
                            newFeed.user_name = name;
                            newFeed.user_url = url_img_ava;

                            ahihi.add(newFeed);

                        }


                        //
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e(TAG, "onResponse: SIZE FEED" + ahihi.size() );

                    newFeedFragmentView.getFeedSuccess(ahihi);
                    newFeedFragmentView.hideProgress();

                } else {

                    newFeedFragmentView.onError(response.body());
                    newFeedFragmentView.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                newFeedFragmentView.onError(t.getMessage());
                newFeedFragmentView.hideProgress();

            }
        });

//        showProgress();
//
//        //
//        String user_url = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/49467888_2205492239691741_6073977895720583168_n.jpg?_nc_cat=105&_nc_oc=AQlnAq4gT6wf3cNlgYB9IxfdPQVBwZR4vbVsmJilJ4gZ_7JAweGQnJAnzzuxiBzQyEA&_nc_ht=scontent.fhan2-4.fna&oh=da1c461ab945159660b34e6566c33504&oe=5D09D1E9";
//        String user_url2 = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/56268549_343887426240652_7399425397634367488_n.jpg?_nc_cat=104&_nc_oc=AQlMvjFKX_eDQ6wVSdHzEbVadlDgYJVJRIZ-wralDDZPtwIjdcvOmEQAZOBrocKJB1mAgGMuLbiaJCaf_TApZwUc&_nc_ht=scontent.fhan2-4.fna&oh=253eab73429043cae685fcad7109d50b&oe=5D4B2FA1";
//
//
//        String image_url = "https://scontent.fhan2-1.fna.fbcdn.net/v/t1.0-9/56158387_101611001027293_8255895292965027840_n.jpg?_nc_cat=103&_nc_oc=AQkASbXBNAb56hFfOpABdQyzK1vcm2S3s_4DytI5ZcJaZ0K3Y1y07ORUF97Fxw39nWD6y8-KxFWMz7F-iySWKddF&_nc_ht=scontent.fhan2-1.fna&oh=49defd2df95e8ba6687d5e17c2a6c50e&oe=5D420FB6";
//        String image1 = "https://scontent.fhan2-3.fna.fbcdn.net/v/t1.0-9/26047171_1506922096094304_1165018699092051405_n.jpg?_nc_cat=109&_nc_oc=AQkKyOLhnfqK1xyu5WxF9_A1J3yblRHrbT2s9lrSaEKxPzsfw9ImTjXl5O_vW_UejklGpK0lkEcC2bap5C-ZQLeZ&_nc_ht=scontent.fhan2-3.fna&oh=57277c81b9232782d94ee79e20e02ec7&oe=5D424257";
//        String image2 = "https://scontent.fhan2-3.fna.fbcdn.net/v/t1.0-9/56757361_632639193846341_200543250094751744_n.jpg?_nc_cat=109&_nc_oc=AQm7VIHJ2N5jUdurQKWQr8VBpdgBN5PzFhl3KjXo63cgBHjLT0MTlYdeCWqvfvEytI9mCqnAfgGSWWaUwdDYi-Mj&_nc_ht=scontent.fhan2-3.fna&oh=dde031e9ac9405880341abd3014b5af5&oe=5D3C15C4";
//        String image3 = "https://scontent.fhan2-3.fna.fbcdn.net/v/t1.0-9/56842672_576861829475041_7200600430810759168_n.jpg?_nc_cat=108&_nc_oc=AQmuYDdOPvLGHO5w76p47DQqUjLmQ8DJeBlz1P9VTYr5A4kh3aiat8OM3Of_fMc8LPa5cysm7GcXHAKmCrsgftLA&_nc_ht=scontent.fhan2-3.fna&oh=5c2958d3ee7d34c517655c420d7a560d&oe=5D4C0984";
//        String image4 = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/57267578_450296289043650_6587918222140375040_n.jpg?_nc_cat=104&_nc_oc=AQnmUrJtdE-7tzLuKbaz8OIsU5aCsDrxRFcCUQELLTsgnfp-OPHwT0FqSGxmrChZTeUJEySq4Qv-A4hPyUSH5cy0&_nc_ht=scontent.fhan2-4.fna&oh=bf7d2e24a4d122af0ee0e9506e635af0&oe=5D383A79";
//        String image5 = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/57403848_688324381591192_4351470326673047552_n.jpg?_nc_cat=104&_nc_oc=AQmxx9e9lPd7WljKsWyOft8OWc8AdpPKYnTHjavBDXZ-L6d8AeyY6RaUV4vc1RVix9cqbbtsPhfOrz-ra1cOauZD&_nc_ht=scontent.fhan2-4.fna&oh=5eac18047fa21155e36b133e45d515d9&oe=5D481FE8";
//        String image6 = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/56723930_787300708311416_7176898434744451072_n.jpg?_nc_cat=100&_nc_oc=AQnv8i87Sj4Z-5zweGkq6hHlDOQwbMYpTZralYZ_5-bynUJOomQz1LZJGXZYbdyPYdl89n7SKNpvAXFlBiNLe0Ef&_nc_ht=scontent.fhan2-4.fna&oh=945ade0f489333ef1fa14898af5ce789&oe=5D744059";
//        //
//
//
//        // fake
//        String content = mContext.getResources().getString(R.string.lorem);
//
//        ArrayList<String> image = new ArrayList<>();
//        image.add(image1);
//        image.add(image2);
//        image.add(image3);
//        image.add(image_url);
//        image.add(image4);
//        image.add(image2);
//        image.add(image1);
//
//        String htmt_str_err = "<span>There <span style=\"color:red\"><strike>is</strike></span> many chickens</span>";
//        String htmt_str_corr = "<span>There <span style=\"color:#30FF00\">is</span> many chickens</span>";
////        String htmt_str_corr = "<p>Day la vi du ve the <span style=\"color:#30FF00\">the strike trong HTML</span></p>";
//
//        User user = new User("100", "", "vuanhlevis@gmail.com", "BN", "Vũ Anh", user_url);
//        Comment comment = new Comment("1", "ahihi do ngok", htmt_str_err, htmt_str_corr, user, "32min", 3, false, false);
//
//        ArrayList<Comment> list_comment = new ArrayList<>();
//        list_comment.add(comment);
//
//        //
//        htmt_str_err = "<span>What <span style=\"color:red\"><strike>do</strike></span> you do yesterday?</span>";
//        htmt_str_corr = "<span>What <span style=\"color:#30FF00\">did</span> you do yesterday?</span>";
//        comment = new Comment("2", "ban sai roi", htmt_str_err, htmt_str_corr, user, "1hr", -9, false, true);
//        //
//
//        list_comment.add(comment);
//
//        //
//        htmt_str_err = "<span>What <span style=\"color:red\"><strike>do</strike></span> you do yesterday?</span>";
//        htmt_str_corr = "<span>What <span style=\"color:#30FF00\">did</span> you do yesterday?</span>";
//        comment = new Comment("3", "this is correct answer", htmt_str_err, htmt_str_corr, user, "3hr", 14, true, false);
//        //
//
//        list_comment.add(comment);
//
//        //
//        htmt_str_err = "<span>What <span style=\"color:red\"><strike>do</strike></span> you do yesterday?</span>";
//        htmt_str_corr = "<span>What <span style=\"color:#30FF00\">did</span> you do yesterday?</span>";
//        comment = new Comment("2", "Too easy =))", htmt_str_err, htmt_str_corr, user, "8hr", 7, false, false);
//        //
//
//        list_comment.add(comment);
//
//        //String id, String content, String user_name, String user_url, List<String> list_image, String create_at, int num_like, int num_comment, boolean isLike
//        list_data.add(new NewFeed("10000", content, "Vũ Cơ", user_url, image, "1 September 2018", 69, 17, true, list_comment));
//        content = mContext.getResources().getString(R.string.newfeed1);
//        image = new ArrayList<>();
//        image.add(image6);
//
//        list_data.add(new NewFeed("10001", content, "Sỹ Junior", image6, image, "17 October 2018", 10, 7, false, list_comment));
//        content = mContext.getResources().getString(R.string.newfeed2);
//        image = new ArrayList<>();
//        image.add(image3);
//
//
//        list_data.add(new NewFeed("10003", content, "Nguyễn Ngọc Huyền", image5, image, "29 January 2019", 102, 0, false, list_comment));
//        content = mContext.getResources().getString(R.string.newfeed3);
//        image = new ArrayList<>();
//        image.add(image4);
//
//        list_data.add(new NewFeed("10002", content, "Hương July", image1, image, "14 February 2019", 6, 1, false, list_comment));
//        content = mContext.getResources().getString(R.string.newfeed4);
//        image = new ArrayList<>();
//        image.add(user_url2);
//
//        list_data.add(new NewFeed("10004", content, "Lưu Mai Thúy", user_url2, image, "04 march 2019", 69, 5, true, list_comment));
//        content = mContext.getResources().getString(R.string.newfeed5);
//        image = new ArrayList<>();
//        image.add(image5);
//
//        list_data.add(new NewFeed("10005", content, "Mai Phạm", image4, image, "17 April 2019", 69, 5, true, list_comment));
//        content = mContext.getResources().getString(R.string.newfeed6);
//        image = new ArrayList<>();
//        image.add(image1);
//
//        list_data.add(new NewFeed("10006", content, "Nhung Kendy", image4, image, "17 April 2019", 69, 5, true, list_comment));
//        content = mContext.getResources().getString(R.string.newfeed7);
//        image = new ArrayList<>();
//        image.add(image3);
//        image.add(image4);
//
//        list_data.add(new NewFeed("10007", content, "Nguyễn Trúc", image4, image, "17 April 2019", 69, 5, true, list_comment));
//        content = mContext.getResources().getString(R.string.newfeed8);
//        image = new ArrayList<>();
//        image.add("https://scontent.fhan2-3.fna.fbcdn.net/v/t1.0-9/56679495_2267330376817662_7792169812909621248_n.jpg?_nc_cat=109&_nc_oc=AQmWNb1U54_xkIzlGm9_gDyAJ_CjYNnuqUYkKgXIAfn7YwWM-yW2_ZUmI1I9iBGOoEs4p-X3wob6OsW3ZmND3O16&_nc_ht=scontent.fhan2-3.fna&oh=a8e462a4f2dd35f83c3d3bad4f1d0c5c&oe=5D444BBC");
//
//        list_data.add(new NewFeed("10008", content, "Nhung Kendy", image4, image, "17 April 2019", 69, 5, true, list_comment));
//
//
//        hideProgress();
//        return list_data;
    }

    @Override
    public void onActionLike(boolean isAction, String id_feed) {

        JSONObject ob = new JSONObject();


        try {
//            ob.put("accessToken", accessToken);


            ob.put("isLike", isAction ? 1 : 0);
            ob.put("id_feed", id_feed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "onActionLike: DATA_SEND " + ob.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ob.toString());

        serviceFunction.actionLike(accessToken, requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful() && response.code() == 200) {
                Log.e(TAG, "onActionLike onResponse: " + response.body());
//
//                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(TAG, "onActionLike onFailure: " + t.getMessage());

            }
        });


    }

    @Override
    public void getLesson() {


        JSONObject send = new JSONObject();
        try {
            send.put("accessToken", accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());
        String url = "http://35.247.180.113:4000/getLession";

        serviceFunction.getLearnModel(url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse:getLesson " + response.body());
                if (response.isSuccessful() && response.code() == 200) {

                    ArrayList<LearnModel> arr_learn = new ArrayList<>();

                    try {
                        JSONObject main = new JSONObject(response.body());
                        JSONArray data = main.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject ob = data.getJSONObject(i);
                            LearnModel model = new LearnModel();
                            model.id = ob.getString("id");
                            model.name = ob.getString("name");

                            ArrayList<LessonModel> list_lesson = new ArrayList<>();

                            JSONArray json_less = ob.getJSONArray("list_lesson");

                            for (int j = 0; j < json_less.length(); j++) {
                                JSONObject less = json_less.getJSONObject(j);
                                LessonModel lessonModel = new LessonModel();
                                lessonModel.id = less.getString("id");
                                lessonModel.url_image_lesson = less.getString("url_image_lesson");
                                lessonModel.name = less.getString("name");
                                lessonModel.difficult = less.getString("difficult");

                                list_lesson.add(lessonModel);
                            }

                            model.list_lesson = list_lesson;

                            arr_learn.add(model);

                        }


                        // save draft

                        saveDraft(arr_learn);


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                } else {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    private void saveDraft(ArrayList<LearnModel> list_learn) {
        MyCache.getInstance().putString(DraffKey.lesson, MyGson.getInstance().toJson(list_learn));
        Log.e(TAG, "saveDraft: " + MyGson.getInstance().toJson(list_learn));
    }
}

package com.kda.kdatalk.ui.main.newfeed.viewcontent;

import android.util.Log;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.Comment;
import com.kda.kdatalk.model.NewFeed;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceConst;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;
import com.kda.kdatalk.utils.UtilLibs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewContentPresenterImpl implements ViewContentPresenter {
    private static final String TAG = ViewContentPresenterImpl.class.getSimpleName();
    ViewContentView viewContentView;
    Fragment_Create_CommentView fragment_create_commentView;
    ActivityBase mContext;

    ServiceFunction serviceFunction;

    String accessToken = "";


    public ViewContentPresenterImpl(ViewContentView viewContentView, ActivityBase mContext) {
        this.viewContentView = viewContentView;
        this.mContext = mContext;
        serviceFunction = APIUtils.getAPIService();
        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);

    }

    @Override
    public void getDetailNF(String id_feed) {

        //String id, String content, String user_name, String user_url, List<String> list_image, String create_at, int num_like, int num_comment, boolean isLike
//        list_data.add();
//        list_data.add(new NewFeed("10001", content, "Khánh Nguyễn", image2, image, "1 September 2018", 10, 7, false));
//        list_data.add(new NewFeed("10002", content, "Dương ml", user_url, image, "1 September 2018", 6, 1, false));
//        list_data.add(new NewFeed("10003", content, "Vãi cả lòn", image2, image, "1 September 2018", 102, 0, false));
//        list_data.add(new NewFeed("10004", content, "éc éc", user_url, image, "1 September 2018", 69, 5, true));

        // fake data

        JSONObject send = new JSONObject();

        try {
            send.put("accessToken", accessToken);
            send.put("id_feed", id_feed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewContentView.showProgress();

        String url = ServiceConst.URL_SERVER_KDA + ServiceConst.DETAIL + id_feed;

        Log.e(TAG, "getDetailNF: URL" + url);

        serviceFunction.getDetail(accessToken, url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: " + response.body());

                if (response.isSuccessful() && response.code() == 200) {
                    try {

                        NewFeed newFeed = new NewFeed();

                        ArrayList<Comment> list_comment = new ArrayList<>();
//                        JSONObject main = new JSONObject(response.body());
                        JSONObject data = new JSONObject(response.body());
                        String id = data.getString("_id");
                        String content = data.getString("content");
                        String create_at = data.getString("create_at");

                        int num_like = data.getInt("num_like");
                        int num_comment = data.getInt("num_comment");
                        boolean isLike = data.getBoolean("isLike");

                        // user newfeed

//                        JSONObject user_nf = data.getJSONObject("user");
//
//                        User nf_user = new User();
////                        JSONObject c_ob = user_nf.getJSONObject("user");
//                        nf_user.email = user_nf.getString("email");
//                        nf_user._id = user_nf.getString("id");
//                        nf_user.address = user_nf.getString("address");
//                        nf_user.name = c_ob.getString("name");
//                        nf_user.url_img_ava = c_ob.getString("url_img_ava");
//                        nf_user.url_img_cover = c_ob.getString("url_img_cover");


                        //image


                        JSONArray arr_image = data.getJSONArray("list_image");
                        ArrayList<String> list_img = new ArrayList<>();

                        if (arr_image != null && arr_image.length() > 0) {

                            for (int j = 0; j < arr_image.length(); j++) {
                                list_img.add(arr_image.getString(j));

                            }

                        }

                        // user

                        JSONObject ob_user = data.getJSONObject("user");

                        String id_user = ob_user.getString("id");
                        String email = ob_user.getString("email");
                        String address = ob_user.getString("address");
                        String name = ob_user.getString("name");
                        String url_img_ava = ob_user.getString("url_img_ava");
                        String url_img_cover = ob_user.getString("url_img_cover");
//                            User user = new User(id_user, email, address, name, url_img_ava, url_img_cover);

                        //comment

                        JSONArray arr_comment = data.getJSONArray("comments");

                        for (int i = 0; i < arr_comment.length(); i++) {
                            JSONObject c = arr_comment.getJSONObject(i);

                            String c_id = c.getString("_id");
                            String c_content = c.getString("content");
                            String c_create_at = c.getString("create_at");
                            String err_text = c.getString("err_str");
                            String corr_text = c.getString("corr_str");
                            int numvote = c.getInt("num_vote");
                            int isvote = c.getInt("isVote");

                            User c_user = new User();
                            JSONObject c_ob = c.getJSONObject("user");
                            c_user.email = c_ob.getString("email");
                            c_user._id = c_ob.getString("id");
                            c_user.address = c_ob.getString("address");
                            c_user.name = c_ob.getString("name");
                            c_user.url_img_ava = c_ob.getString("url_img_ava");
                            c_user.url_img_cover = c_ob.getString("url_img_cover");

                            Comment cm;

                            if (isvote == 0) {
                                cm = new Comment(c_id, c_content, err_text, corr_text, c_user, c_create_at, numvote, false, false);
                            } else if (isvote == 1) {
                                cm = new Comment(c_id, c_content, err_text, corr_text, c_user, c_create_at, numvote, true, false);

                            } else {
                                cm = new Comment(c_id, c_content, err_text, corr_text, c_user, c_create_at, numvote, false, true);

                            }

                            list_comment.add(cm);

//                            Log.e(TAG, "onResponse: ");

                        }

                        newFeed.id = id;
                        newFeed.content = content;
                        newFeed.user_name = name;
                        newFeed.user_url = url_img_ava;
                        newFeed.list_image = list_img;
                        newFeed.create_at = create_at;
                        newFeed.num_like = num_like;
                        newFeed.num_comment = num_comment;
                        newFeed.isLike = isLike;
                        newFeed.list_comment = list_comment;


                        viewContentView.getDetailSuccess(true, newFeed, "");


                        viewContentView.hideProgress();

//
                    } catch (JSONException e) {

                        viewContentView.getDetailSuccess(false, null, e.getMessage());
                        e.printStackTrace();
                    }


                } else {
                    //err
                    viewContentView.getDetailSuccess(false, null, response.body());
                    viewContentView.hideProgress();

                }
                //
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //
                viewContentView.getDetailSuccess(false, null, t.getMessage());
                viewContentView.hideProgress();

            }
        });


    }

    @Override
    public void setFragment(Fragment fragment) {
        if (fragment instanceof Fragment_Create_CommentView) {
            fragment_create_commentView = (Fragment_Create_CommentView) fragment;
        }
    }

    @Override
    public void postComment(String content, String err_str, String corr_str, String id_feed) {

        User me = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_info), User.class);

        Comment comment = new Comment();
        comment.content = content;
        comment.err_text = err_str;
        comment.corr_text = corr_str;
        comment.num_vote = 0;
        comment.user = me;
        comment.rank = 5;
        comment.is_down = false;
        comment.is_up = false;


        long time = System.currentTimeMillis();

        String format_date = "dd/MM/yyyy hh:mm:ss";

        comment.timme = UtilLibs.getDate(time, format_date);

        Log.e(TAG, "postComment: " + UtilLibs.getDate(time, format_date));

        viewContentView.showProgress();
        JSONObject send = new JSONObject();
        try {
//            send.put("accessToken", accessToken);
            send.put("content", content);
            send.put("err_str", err_str);
            send.put("corr_str", corr_str);
            send.put("id_feed", id_feed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "postComment: " + send.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        serviceFunction.postComment(accessToken, requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "onResponse: postComment " + response.body());
                if (response.isSuccessful() && response.code() == 200) {
                    fragment_create_commentView.onSuccess();
                    viewContentView.hideProgress();
                    viewContentView.createCommentSuccess(comment);

                } else {
                    viewContentView.hideProgress();

                    fragment_create_commentView.onError(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                fragment_create_commentView.onError(t.getMessage());
                viewContentView.hideProgress();

            }
        });


    }

    @Override
    public void voteNFup(boolean isUp, String id_comment) {

//        viewContentView.showProgress();
        JSONObject send = new JSONObject();
        try {
//            send.put("accessToken", accessToken);

            if (isUp) {

                send.put("isUp", 1);
            } else {
                send.put("isUp", 0);

            }

            send.put("id_comment", id_comment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "voteNFup: " + send.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        serviceFunction.voteUpComment(accessToken, requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: voteUpComment" + response.body());
//                if (response.isSuccessful() && response.code() == 200) {
//                    fragment_create_commentView.onSuccess();
//                    viewContentView.hideProgress();
//
//                } else {
//                    viewContentView.hideProgress();
//
//                    fragment_create_commentView.onError(response.body());
//                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                fragment_create_commentView.onError(t.getMessage());
//                viewContentView.hideProgress();

            }
        });


    }

    @Override
    public void voteNFdown(boolean isdown, String id_comment) {

//        viewContentView.showProgress();
        JSONObject send = new JSONObject();
        try {
            if (isdown) {
                send.put("isDown", 1);

            } else
                send.put("isDown", 0);
//            send.put("accessToken", accessToken);
            send.put("id_comment", id_comment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "voteNFdown: " + send.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        serviceFunction.voteDownComment(accessToken, requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: voteDownComment" + response.body());
//                if (response.isSuccessful() && response.code() == 200) {
//                    fragment_create_commentView.onSuccess();
//                    viewContentView.hideProgress();
//
//                } else {
//                    viewContentView.hideProgress();
//
//                    fragment_create_commentView.onError(response.body());
//                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                fragment_create_commentView.onError(t.getMessage());
//                viewContentView.hideProgress();

            }
        });

    }

    @Override
    public void actionLike(boolean isAction, String id_feed) {
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


}

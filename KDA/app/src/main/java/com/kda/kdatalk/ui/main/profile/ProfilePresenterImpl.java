package com.kda.kdatalk.ui.main.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.kda.kdatalk.model.User;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceConst;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenterImpl implements ProfilePresenter {
    private static final String TAG = ProfilePresenterImpl.class.getSimpleName();
    Context mContext;
    ProfileView profileView;

    ServiceFunction serviceFunction;
    String accessToken="";
    public ProfilePresenterImpl(Context mContext, ProfileView profileView) {
        this.mContext = mContext;
        this.profileView = profileView;
        serviceFunction = APIUtils.getAPIService();
        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
    }

    @Override
    public void uploadImageToServer(String uri, boolean isAva) {

        String base64 = convertImageToByte(uri);

        JSONObject send = new JSONObject();



        if (isAva) {

            try {
//            send.put("accessToken", accessToken);
                send.put("avatar", base64);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "uploadImageToServer: " + send.toString());

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

            serviceFunction.changeAva(accessToken,requestBody).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        profileView.onUploadSuccess(true, "");
                    } else {
                        profileView.onUploadSuccess(false, response.body());

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    profileView.onUploadSuccess(false, t.getMessage());

                }
            });

        } else {

            try {
//            send.put("accessToken", accessToken);
                send.put("cover", base64);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "uploadImageToServer: " + send.toString());

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

            serviceFunction.changeCover(accessToken,requestBody).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        profileView.onUploadSuccess(true, "");
                    } else {
                        profileView.onUploadSuccess(false, response.body());

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    profileView.onUploadSuccess(false, t.getMessage());

                }
            });
        }


    }

    @Override
    public void getDetailUser(String user_id) {

        JSONObject send=  new JSONObject();
        try {
            send.put("accessToken", accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        String url = ServiceConst.URL_SERVER_KDA + ServiceConst.DETAIL_USER + user_id ;

        Log.e(TAG, "getDetailUser: URL: " + url);

        serviceFunction.getDetailUser(accessToken, url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: " + response.body());

                if (response.isSuccessful() && response.code() == 200) {

                    try {
                        JSONObject main = new JSONObject(response.body());

                        JSONObject data = main.getJSONObject("data");

                        User user = new User();
                        user._id = data.getString("_id");
                        user.name= data.getString("name");
                        user.url_img_cover = data.getString("url_img_cover");
                        user.url_img_ava = data.getString("url_img_ava");
                        user.email = data.getString("email");
                        user.address = data.getString("address");

                        profileView.onGetDetailSuccess(user);

                    } catch (JSONException e) {
                        profileView.onGetDetailSuccess(null);

                        Log.e(TAG, "onResponse: " + e.getMessage());

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                profileView.onGetDetailSuccess(null);


            }
        });

    }

    @Override
    public void setFragment(Fragment fragment) {

        if (fragment instanceof ProfileFragment) {
            profileView = (ProfileView) fragment;
        }

    }


    private String convertImageToByte(String link) {

        String imgString = "";
        try {
            File image = new File(link);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
            imgString = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
            // dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = "data:image/jpeg;base64," + imgString;

        return result;
    }

}

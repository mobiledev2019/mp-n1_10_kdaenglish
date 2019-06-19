package com.kda.kdatalk.ui.login;

import android.app.Activity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.model.UserAuth;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.ui.login.fragment.RegisterFragment;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImpl implements LoginPresenter {

    private static final String TAG = LoginPresenterImpl.class.getName();
    private Activity context;
    private LoginView loginView;
    private LoginFragmentView fragmentLoginView;
    private RegisterFragmentView registerFragment;

    private ServiceFunction serviceFunction;


    private CallbackManager callbackManager;

    public LoginPresenterImpl(Activity context, LoginView loginView, CallbackManager callbackManager) {
        this.context = context;
        this.loginView = loginView;
        this.callbackManager = callbackManager;

        serviceFunction = APIUtils.getAPIService();
    }

    @Override
    public void showProgress() {
        try {
            loginView.showProgress();
        } catch (Exception e) {

        }
    }

    @Override
    public void hideProgress() {
        try {
            loginView.hideProgress();
        } catch (Exception e) {

        }
    }

    @Override
    public void setFragment(Fragment fragment) {
        if (fragment instanceof LoginFragmentView) {
            this.fragmentLoginView = (LoginFragmentView) fragment;
        } else if (fragment instanceof RegisterFragment) {
            this.registerFragment = (RegisterFragment) fragment;
        }
    }


    @Override
    public void loginByServer() {

        loginView.showProgress();

        UserAuth mUser = null;

        mUser = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_login), UserAuth.class);

        JSONObject ob = new JSONObject();
        try {
            ob.put("email", mUser.username);
            ob.put("password", mUser.password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String fake_req = "{email:\"nguyenkhanh97nd@gmail.com\",password:\"123456\"}";

        Log.e(TAG, "loginByServer: DATA SEND: " + ob.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ob.toString());

        serviceFunction.login(requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: BODY" + response.body());
                if (response.isSuccessful() && response.code() == 200) {
                    // save data from server

                    Log.e(TAG, "onResponse: " + response.toString());

                    try {
                        JSONObject main = new JSONObject(response.body().toString());
                        JSONObject data = main.getJSONObject("data");

                        String accessToken = data.getString("accessToken");

                        MyCache.getInstance().putString(DraffKey.accessToken, accessToken);

                        // data user

                        JSONObject ob_user = data.getJSONObject("user");

                        User user = new User();
                        user.address = ob_user.getString("address");
                        user.url_img_ava = ob_user.getString("url_img_ava");
                        user.url_img_cover = ob_user.getString("url_img_cover");
                        user._id = ob_user.getString("_id");
                        user.name = ob_user.getString("name");
                        user.email = ob_user.getString("email");

                        MyCache.getInstance().putString(DraffKey.user_info,MyGson.getInstance().toJson(user));

                        Log.d(TAG, "DRAFT_DATA: " + MyCache.getInstance().getString(DraffKey.user_info) );

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON EXCEPTION: " + e.getMessage());
                        e.printStackTrace();
                    }

                    loginView.onLoginSuccess(true);

                } else {
                    loginView.onError("Đã có lỗi xảy ra!");
                    loginView.onLoginSuccess(false);
                }

                loginView.hideProgress();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loginView.onError(t.getMessage());

                Log.e(TAG, "onFailure: " + t.getMessage());

                hideProgress();
            }
        });

//        serviceFunction.login(requestBody).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                Log.e(TAG, "onResponse: BODY" + response.body().toString());
//                if (response.isSuccessful() && response.code() == 200) {
//                    // save data from server
//
//                    Log.e(TAG, "onResponse: " + response.toString());
//
//                    try {
//                        JSONObject main = new JSONObject(response.body().toString());
//                        JSONObject data = main.getJSONObject("data");
//
//                        String accessToken = data.getString("accessToken");
//
//                        MyCache.getInstance().putString(DraffKey.accessToken, accessToken);
//
//                        // data user
//
//                        JSONObject ob_user = data.getJSONObject("user");
//
//                        User user = new User();
//                        user.address = ob_user.getString("address");
//                        user.url_img_ava = ob_user.getString("url_img_ava");
//                        user.url_img_cover = ob_user.getString("url_img_cover");
//                        user._id = ob_user.getString("_id");
//                        user.name = ob_user.getString("name");
//                        user.email = ob_user.getString("email");
//
//                        MyCache.getInstance().putString(DraffKey.user_info,MyGson.getInstance().toJson(user));
//
//                        Log.d(TAG, "DRAFT_DATA: " + MyCache.getInstance().getString(DraffKey.user_info) );
//
//                    } catch (JSONException e) {
//                        Log.e(TAG, "JSON EXCEPTION: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//
//                    loginView.onLoginSuccess(true);
//
//                } else {
//                    loginView.onError(response.body().toString());
//                    loginView.onLoginSuccess(false);
//                }
//
//                loginView.hideProgress();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                loginView.onError(t.getMessage());
//
//                hideProgress();
//            }
//        });

        //
    }

    @Override
    public void loginByServer(String username, String password) {
//        UserAuth mUser = null;
//
//        mUser = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_login), UserAuth.class);

        loginView.showProgress();

        JSONObject ob = new JSONObject();

        String email = "email";
        String password_ = "password";
        try {
            ob.put(email, username);
            ob.put(password_, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "loginByServer: DATA SEND: " + ob.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ob.toString());


//        serviceFunction.login(requestBody).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                Log.e(TAG, "URL: " + response.raw().request().url());
//                Log.e(TAG, "response: " + response.toString());
//                if (response.isSuccessful() && response.code() == 200) {
//                    // save data from server
//
//                    UserAuth userAuth = new UserAuth(username, password);
//
//                    MyCache.getInstance().putString(DraffKey.user_login,MyGson.getInstance().toJson(userAuth));
//
//                    try {
//                        JSONObject main = new JSONObject(response.body().toString());
//                        JSONObject data = main.getJSONObject("data");
//
//                        String accessToken = data.getString("accessToken");
//
//                        MyCache.getInstance().putString(DraffKey.accessToken, accessToken);
//
//                        // data user
//
//                        JSONObject ob_user = data.getJSONObject("user");
//
//                        User user = new User();
//                        user.address = ob_user.getString("address");
//                        user.url_img_ava = ob_user.getString("url_img_ava");
//                        user.url_img_cover = ob_user.getString("url_img_cover");
//                        user._id = ob_user.getString("_id");
//                        user.name = ob_user.getString("name");
//                        user.email = ob_user.getString("email");
//
//                        MyCache.getInstance().putString(DraffKey.user_info,MyGson.getInstance().toJson(user));
//
//                        Log.d(TAG, "DRAFT_DATA: " + MyCache.getInstance().getString(DraffKey.user_info) );
//
//                    } catch (JSONException e) {
//                        Log.e(TAG, "JSON EXCEPTION: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//
//
//                    loginView.onLoginSuccess(true);
//
//                } else {
//                    loginView.onError(response.body().toString());
//                    loginView.onLoginSuccess(false);
//                }
//                loginView.hideProgress();
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                loginView.onError(t.getMessage());
//
//                loginView.hideProgress();
//
//            }
//        });


        serviceFunction.login(requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "URL: " + response.raw().request().url());
                Log.e(TAG, "response: " + response.toString());
                if (response.isSuccessful() && response.code() == 200) {
                    // save data from server

                    UserAuth userAuth = new UserAuth(username, password);

                    MyCache.getInstance().putString(DraffKey.user_login,MyGson.getInstance().toJson(userAuth));

                    try {
                        JSONObject main = new JSONObject(response.body().toString());
                        JSONObject data = main.getJSONObject("data");

                        String accessToken = data.getString("accessToken");

                        MyCache.getInstance().putString(DraffKey.accessToken, accessToken);

                        // data user

                        JSONObject ob_user = data.getJSONObject("user");

                        User user = new User();
                        user.address = ob_user.getString("address");
                        user.url_img_ava = ob_user.getString("url_img_ava");
                        user.url_img_cover = ob_user.getString("url_img_cover");
                        user._id = ob_user.getString("_id");
                        user.name = ob_user.getString("name");
                        user.email = ob_user.getString("email");

                        MyCache.getInstance().putString(DraffKey.user_info,MyGson.getInstance().toJson(user));

                        Log.d(TAG, "DRAFT_DATA: " + MyCache.getInstance().getString(DraffKey.user_info) );

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON EXCEPTION: " + e.getMessage());
                        e.printStackTrace();
                    }


                    loginView.onLoginSuccess(true);

                } else {
                    loginView.onError(response.body().toString());
                    loginView.onLoginSuccess(false);
                }
                loginView.hideProgress();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loginView.onError(t.getMessage());

                loginView.hideProgress();

            }
        });
    }

    @Override
    public void register(JSONObject data) {

        loginView.showProgress();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data.toString());

        serviceFunction.register(requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    registerFragment.onRegisterSuccess(true, "");
                    // save data from server

                } else {
                    registerFragment.onRegisterSuccess(false, response.body().toString());

                }
                loginView.hideProgress();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                registerFragment.onRegisterSuccess(false, t.getMessage());
                loginView.hideProgress();

            }
        });

    }

}

package com.kda.kdatalk.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import androidx.fragment.app.Fragment;

public class LoginPresenterImpl implements LoginPresenter {

    private static final String TAG = LoginPresenterImpl.class.getName();
    private Activity context;
    private LoginView loginView;
    private LoginFragmentView fragmentLoginView;

    private GoogleSignInClient mGoogleSignInClient;

    private CallbackManager callbackManager;

    public LoginPresenterImpl(Activity context, LoginView loginView, CallbackManager callbackManager) {
        this.context = context;
        this.loginView = loginView;
        this.callbackManager = callbackManager;
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
        }
    }

    @Override
    public void loginByFaceBook() {
        showProgress();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e(TAG, "onSuccess: " + loginResult.toString());
                        Profile profile = Profile.getCurrentProfile();
                        if (profile != null) {
                            User user = new User();
                            user.setStr_id(profile.getId());
                            user.setName(profile.getName());
                            user.setUrl_img_ava(String.valueOf(profile.getProfilePictureUri(400, 400)));
                            user.setAccessToken(loginResult.getAccessToken().getToken());
                            user.setLOGIN_MODE(AppConstants.MODE_FACEBOOK);

                            MyCache.getInstance().putString(DraffKey.user, MyGson.getInstance().toJson(user));

                            Toast.makeText(context, "success-facebook", Toast.LENGTH_SHORT).show();
                            makeRequest();
//                            MyCache.getInstance().putString(DraffKey.accessToken,loginResult.getAccessToken());

                            fragmentLoginView.onSuccess();
                        }

                        //

                        hideProgress();
                    }

                    @Override
                    public void onCancel() {
                        hideProgress();
                        fragmentLoginView.onFailed("Cancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        hideProgress();
                        fragmentLoginView.onFailed(exception.toString());
                    }
                });

        LoginManager.getInstance().logInWithReadPermissions(context, Arrays.asList("public_profile", "user_friends", "user_photos", "email"));

    }


    void makeRequest() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Profile profile = Profile.getCurrentProfile();
        if (accessToken != null && profile != null) {

//            new GraphRequest(
//                    AccessToken.getCurrentAccessToken(),
//                    "/" + profile.getId() + "?fields=cover",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        public void onCompleted(GraphResponse response) {
//                            /* handle the result */
//                            JSONObject data =response.getJSONObject();
//                            try {
//                                String id = data.getString("id");
//                                Log.e(TAG, "onCompleted: " + response.toString());
//                                Log.e(TAG, "onCompleted: " + "https://graph.facebook.com/"+ profile.getId() + "/" +id );
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
////                            Log.e(TAG, "onCompleted: " + AccessToken.getCurrentAccessToken().getToken());
//                        }
//                    }
//            ).executeAsync();

//            new GraphRequest(
//                    AccessToken.getCurrentAccessToken(),
//                    "?fields={source, offset_x}",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        public void onCompleted(GraphResponse response) {
//                            /* handle the result */
//                            Log.e(TAG, "onCompleted: " + response.toString());
//
//                        }
//                    }
//            ).executeAsync();

            String URL = "https://graph.facebook.com/" + profile.getId() + "?fields=cover&access_token=" + AccessToken.getCurrentAccessToken().getToken();
            Log.e(TAG, "makeRequest: " + URL);
        }
    }

    @Override
    public void loginByGoogle() {
        showProgress();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        context.startActivityForResult(signInIntent, 10); // RCSIGNIN
        hideProgress();
    }

    @Override
    public void loginByServer() {
        //
    }

    @Override
    public void setGoogleSignInClient(GoogleSignInClient googleClient) {
        this.mGoogleSignInClient = googleClient;
    }
}

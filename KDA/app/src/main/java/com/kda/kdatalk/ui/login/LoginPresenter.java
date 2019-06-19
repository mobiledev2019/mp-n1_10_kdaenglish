package com.kda.kdatalk.ui.login;


import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

public interface LoginPresenter {
    void showProgress();
    void hideProgress();
    void setFragment(Fragment fragment);

    void loginByServer();
    void loginByServer(String username, String password);

    void register(JSONObject data);

}

package com.kda.kdatalk.ui.login;


import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

import androidx.fragment.app.Fragment;

public interface LoginPresenter {
    void showProgress();
    void hideProgress();
    void setFragment(Fragment fragment);

    void loginByFaceBook();

    void loginByGoogle();

    void loginByServer();

    void setGoogleSignInClient(GoogleSignInClient googleClient);
}

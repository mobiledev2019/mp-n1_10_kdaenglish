package com.kda.kdatalk.ui.login;

public interface LoginView {
    void showProgress();
    void hideProgress();
    void onError(String err);
}

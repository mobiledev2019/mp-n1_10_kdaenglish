package com.kda.kdatalk.ui.main.profile;

import android.content.Context;

public class ProfilePresenterImpl implements ProfilePresenter {
    Context mContext;
    ProfileView profileView;

    public ProfilePresenterImpl(Context mContext, ProfileView profileView) {
        this.mContext = mContext;
        this.profileView = profileView;
    }
}

package com.kda.kdatalk.ui.main.profile;

import com.kda.kdatalk.model.User;

public interface ProfileView {
    void onUploadSuccess(boolean isSuccess, String message);

    void onGetDetailSuccess(User user);

}

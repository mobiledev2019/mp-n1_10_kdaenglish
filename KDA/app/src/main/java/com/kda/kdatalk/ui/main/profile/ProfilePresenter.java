package com.kda.kdatalk.ui.main.profile;

import androidx.fragment.app.Fragment;

public interface ProfilePresenter {

    void uploadImageToServer(String uri, boolean isAva);

    void getDetailUser(String user_id);

    void setFragment(Fragment fragment);

}

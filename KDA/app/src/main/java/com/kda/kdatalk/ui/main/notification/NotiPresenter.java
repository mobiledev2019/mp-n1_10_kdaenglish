package com.kda.kdatalk.ui.main.notification;

import androidx.fragment.app.Fragment;

import com.kda.kdatalk.model.noti.Noti;

import java.util.ArrayList;

public interface NotiPresenter {
    void setFragment(Fragment fragment);

    void showProgress();
    void hideProgress();

    void getListNoti(int currNoti);

    void readNoti(String _idNoti);
}

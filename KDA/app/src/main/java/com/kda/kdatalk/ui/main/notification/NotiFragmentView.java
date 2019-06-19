package com.kda.kdatalk.ui.main.notification;

import com.kda.kdatalk.model.noti.Noti;

import java.util.ArrayList;

public interface NotiFragmentView {
    void getNotiSuccess(ArrayList<Noti> data);
    void onError(String mess);
}

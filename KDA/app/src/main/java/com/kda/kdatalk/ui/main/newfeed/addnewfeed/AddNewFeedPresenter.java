package com.kda.kdatalk.ui.main.newfeed.addnewfeed;

import org.json.JSONObject;

public interface AddNewFeedPresenter {
    String convertImageToByte(String link);

    void addNewFeed(JSONObject data, String token);
}

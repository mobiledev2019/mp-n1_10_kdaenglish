package com.kda.kdatalk.ui.main.learn;

import com.kda.kdatalk.model.learn.VocabModel;

import java.util.ArrayList;

public interface LearnView {
    void showProgress();
    void hideProgress();
    void getListVocabSuccess(ArrayList<VocabModel> list_vocab);

    void onError(String mess);

    void getScoreSuccess(boolean isSuccess, int score);
}

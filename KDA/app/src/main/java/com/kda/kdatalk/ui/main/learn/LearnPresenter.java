package com.kda.kdatalk.ui.main.learn;

import com.kda.kdatalk.model.learn.VocabModel;

import java.util.ArrayList;

public interface LearnPresenter {
    ArrayList<VocabModel> getListVocab(String id_lesson);

    int getScore(String id_vocab);

    void sendVoiceVocab(String id_vocab, String base64);

}

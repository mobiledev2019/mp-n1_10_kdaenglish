package com.kda.kdatalk.model.learn;

public class VocabModel {
    public String id;
    public String vocab; //
    public String pronun; //
    public String url_voice;
    public int point;//max 100
    public String impress_pronun;// trong am
    public boolean isComplete = false;

    public VocabModel() {
    }

    public VocabModel(String id, String vocab, String pronun, String url_voice, int point, String impress_pronun) {
        this.id = id;
        this.vocab = vocab;
        this.pronun = pronun;
        this.url_voice = url_voice;
        this.point = point;
        this.impress_pronun = impress_pronun;
        isComplete = false;
    }
}

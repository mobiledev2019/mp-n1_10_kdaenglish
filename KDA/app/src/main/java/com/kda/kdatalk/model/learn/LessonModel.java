package com.kda.kdatalk.model.learn;

import java.util.ArrayList;

public class LessonModel {
    public String id;
    public String url_image_lesson;
    public String name;
    public String difficult;

    public ArrayList<VocabModel> list_vocab;

    public LessonModel() {
    }

    public LessonModel(String id, String url_image_lesson, String name, String difficult, ArrayList<VocabModel> list_vocab) {
        this.id = id;
        this.url_image_lesson = url_image_lesson;
        this.name = name;
        this.difficult = difficult;
        this.list_vocab = list_vocab;
    }
}

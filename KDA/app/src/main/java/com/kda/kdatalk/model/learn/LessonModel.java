package com.kda.kdatalk.model.learn;

public class LessonModel {
    public String id;
    public String url_image_lesson;
    public String name;
    public String difficult;

    public LessonModel() {
    }

    public LessonModel(String id, String url_image_lesson, String name, String difficult) {
        this.id = id;
        this.url_image_lesson = url_image_lesson;
        this.name = name;
        this.difficult = difficult;
    }
}

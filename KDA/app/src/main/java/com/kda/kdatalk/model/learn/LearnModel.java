package com.kda.kdatalk.model.learn;

import java.util.ArrayList;

public class LearnModel {
    public String id;
    public String name;

    public ArrayList<LessonModel> list_lesson;

    public LearnModel() {
    }

    public LearnModel(String id, String name, ArrayList<LessonModel> list_lesson) {
        this.id = id;
        this.name = name;
        this.list_lesson = list_lesson;
    }
}

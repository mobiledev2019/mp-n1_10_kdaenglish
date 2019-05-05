package com.kda.kdatalk.ui.main.learn.fragment;

import android.content.Context;

import com.kda.kdatalk.model.learn.LearnModel;
import com.kda.kdatalk.model.learn.LessonModel;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;

import java.util.ArrayList;

public class LearnFagmentFragmentPresenterImpl implements LearnFragmentPresenter {
    private Context mContext;
    private LearnFragmentView learnFragmentView;
    private ServiceFunction serviceFunction;

    public LearnFagmentFragmentPresenterImpl(Context mContext, LearnFragmentView learnFragmentView) {
        this.mContext = mContext;
        this.learnFragmentView = learnFragmentView;
        serviceFunction = APIUtils.getAPIService();
    }

    @Override
    public ArrayList<LearnModel> getLearnModel() {

        ArrayList<LearnModel> list_data = new ArrayList<>();

        //fake

        //

        String fakeUrl = "https://scontent.fhan3-3.fna.fbcdn.net/v/t1.0-9/27658021_2113824185513559_169077171140592559_n.jpg?_nc_cat=108&_nc_oc=AQm0Lfl-nQHhddPZBtmbZYQrfVzsViPcMkTP1MjnJVHREO9bfHB8-3qll4Y9zEPyt_8&_nc_ht=scontent.fhan3-3.fna&oh=b8955a5789abdc0061eb269f0442674e&oe=5D42C344";
        LessonModel lessonModel = new LessonModel("id", fakeUrl,"Lesson 1 - Basic terms","easy");

        ArrayList<LessonModel> listLesson = new ArrayList<>();
        listLesson.add(lessonModel);
        lessonModel = new LessonModel("id", fakeUrl,"Lesson 1 - Basic terms","easy");
        listLesson.add(lessonModel);
        lessonModel = new LessonModel("id", fakeUrl,"Lesson 2 - Listening","difficult");
        listLesson.add(lessonModel);
        lessonModel = new LessonModel("id", fakeUrl,"Lesson 3 - Taking vacation","normal");
        listLesson.add(lessonModel);
        lessonModel = new LessonModel("id", fakeUrl,"Lesson 4 - Choosing a seat","easy");
        listLesson.add(lessonModel);

        LearnModel learnModel = new LearnModel("id","Travel 101",listLesson);
        list_data.add(learnModel);
        learnModel = new LearnModel("id","At the airport",listLesson);
        list_data.add(learnModel);
        learnModel = new LearnModel("id","Tongue twisters",listLesson);
        list_data.add(learnModel);


        return list_data;
    }
}

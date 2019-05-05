package com.kda.kdatalk.ui.main.learn;

import com.kda.kdatalk.model.learn.VocabModel;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.ui.base.ActivityBase;

import java.util.ArrayList;
import java.util.logging.Handler;

public class LearnPresenterImpl implements LearnPresenter {
    LearnView learnView;
    ActivityBase mContext;
    ServiceFunction serviceFunction;

    public LearnPresenterImpl(LearnView learnView, ActivityBase mContext) {
        this.learnView = learnView;
        this.mContext = mContext;
        serviceFunction = APIUtils.getAPIService();
    }


    @Override
    public ArrayList<VocabModel> getListVocab(String id_lesson) {

        ArrayList<VocabModel> listVocab = new ArrayList<>();

        VocabModel vocabModel = new VocabModel("0","vacation","vāˈkāSHən","url",0,"ca");
        listVocab.add(vocabModel);

        vocabModel = new VocabModel("1","connect","kəˈnekt","url",0,"ec");
        listVocab.add(vocabModel);

        vocabModel = new VocabModel("2","legendary","ledʒənderi","url",0,"le");
        listVocab.add(vocabModel);

        vocabModel = new VocabModel("3","people","ˈpiːpl","url",0,"pe");
        listVocab.add(vocabModel);

        vocabModel = new VocabModel("4","masturbate","ˈmæstərbeɪt","url",0,"mas");
        listVocab.add(vocabModel);


        return listVocab;
    }

    @Override
    public int getScore(String id_vocab) {
        //

//        learnView.showProgress();

        //call api

//        learnView.hideProgress();

        //fake

        return 60;
    }
}

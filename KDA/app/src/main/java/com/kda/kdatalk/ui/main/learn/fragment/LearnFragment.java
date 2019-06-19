package com.kda.kdatalk.ui.main.learn.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.kda.kdatalk.R;
import com.kda.kdatalk.model.learn.LearnModel;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.main.MainActivity;
import com.kda.kdatalk.ui.main.learn.LearnActivity;
import com.kda.kdatalk.ui.main.learn.adapter.LearnAdapter;
import com.kda.kdatalk.ui.widget.ProgressView;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;
import com.kda.kdatalk.utils.UtilLibs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LearnFragment extends FragmentBase implements LearnFragmentView {

    private static final String TAG = LearnFragment.class.getSimpleName();
    View viewRoot;
    Context mConText;

    @BindView(R.id.rv_learn)
    RecyclerView rv_learn;

    @BindView(R.id.progress_bar)
    ProgressView progress_bar;

    LearnFragmentPresenter presenter;
    LearnAdapter adapter;

    boolean isDraff = false;

    ArrayList<LearnModel> list_data = new ArrayList<>();

    public static LearnFragment newInstance() {
        Bundle args = new Bundle();
        LearnFragment fragment = new LearnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mConText = context;
    }

    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_learn;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new LearnFagmentFragmentPresenterImpl(mConText, this);

        // check draff

//        if (isNetworkConnected(mConText)) {
//            // get data
//
//            presenter.getLearnModel();
//        } else {
//
//            if (!MyCache.getInstance().getString(DraffKey.lesson).isEmpty()) {
//
//                Type type = new TypeToken<List<LearnModel>>() {
//                }.getType();
//                list_data = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.lesson), type);
//
//                if (list_data == null) {
//                    list_data = new ArrayList<>();
//                }
//
//                isDraff = list_data.size() > 0;
//
//                //                for (int i = 0; i < list_data.size(); i++) {
////                    if (list_data.get(i) != null) {
////                        if (player_request.getPlayer_code().equals(((SurveyDraff_P44) arr_data.get(i)).getPlayer_code())) {
////                            surveyDraffP44 = (SurveyDraff_P44) arr_data.get(i);
////                            position = i;
////                            isDraff = true;
////                            break;
////                        }
////                    }
////                }
//
//            }
//        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
        }catch (Exception e) {

        }

        setUpdata();

        return viewRoot;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    AdapterView.OnItemClickListener onItemClickListener =(parent, view, position, id) -> {
        // start activity learn

        if (isNetworkConnected(mConText)) {

            Intent intent = new Intent(mConText, LearnActivity.class);
            intent.putExtra("position_lesson", (int)id);
            intent.putExtra("position_learn", position);
            Toast.makeText(mConText, "parent: " + position + "|=> child: " + id + "data: " + list_data.get(position).list_lesson.get((int)id).id  , Toast.LENGTH_SHORT).show();
            mConText.startActivity(intent);
        } else {
            UtilLibs.showAlert(mConText, "Bạn cần kết nối internet để sử dụng chức năng này!");
        }

    };

    private void setUpdata() {
        LinearLayoutManager manager = new LinearLayoutManager(mConText);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv_learn.setLayoutManager(manager);
        rv_learn.setHasFixedSize(true);
        //

        // save data
        list_data = MainActivity.drafLesson;
//        saveDraft();


        adapter = new LearnAdapter(mConText,list_data,onItemClickListener);

        rv_learn.setAdapter(adapter);

    }

    private void saveDraft() {
        MyCache.getInstance().putString(DraffKey.lesson, MyGson.getInstance().toJson(list_data));
        Log.e(TAG, "saveDraft: " + MyGson.getInstance().toJson(list_data) );
    }

    @Override
    public void onError(String mess) {
        UtilLibs.showAlert(mConText,mess);

    }

    @Override
    public void onSuccess(ArrayList<LearnModel> data) {
        list_data = data;
        adapter.notifyDataSetChanged();

        saveDraft();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void showProgress(boolean isShow) {

        if (progress_bar!= null) {
            if (isShow) {

                progress_bar.setVisibility(View.VISIBLE);

            } else {
                progress_bar.setVisibility(View.GONE);

            }
        }


    }
}

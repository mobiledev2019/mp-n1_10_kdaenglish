package com.kda.kdatalk.ui.main.newfeed.addnewfeed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.main.newfeed.addnewfeed.adapter.GridAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewFeedActivity extends ActivityBase implements AddNewFeedView {

    private static final int PICK_IMAGE = 10;
    private static final int SPEECH_2_TEXT = 11;

    @BindView(R.id.gv_addImage)
    GridView gv_addImage;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.iv_addImage)
    ImageView iv_addImage;

    @BindView(R.id.iv_addvoiceText)
    ImageView iv_addvoiceText;

    @BindView(R.id.iv_canvas)
    ImageView iv_canvas;

    @BindView(R.id.et_content)
    EditText et_content;

    AddNewFeedPresenter newFeedPresenter;

    //    List<Bitmap> bitmapList = new ArrayList<>();
    List<String> listUrl = new ArrayList<>();
//    AddImageAdapter addImageAdapter;

    GridAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newfeed);
        ButterKnife.bind(this);
        adapter = new GridAdapter(this, listUrl, onClickRemove);

        newFeedPresenter = new AddNewFeedPresenterImpl(this, this);
//        addImageAdapter = new AddImageAdapter(bitmapList, this, onClickRemove);

        //

        gv_addImage.setAdapter(adapter);
    }

    AdapterView.OnItemClickListener onClickRemove = (parent, view, position, id) -> {
        listUrl.remove(position);
        adapter.notifyDataSetChanged();
    };


    @OnClick({R.id.iv_addImage, R.id.iv_addvoiceText, R.id.iv_canvas, R.id.iv_back, R.id.tv_action})
    public void clickAction(View v) {
        switch (v.getId()) {
            case R.id.iv_addImage:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                break;

            case R.id.iv_addvoiceText:
                Intent record_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                record_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);
                record_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                startActivityForResult(record_intent, SPEECH_2_TEXT);
                break;

            case R.id.iv_canvas:
                break;

            case R.id.iv_back:
                onBackPressed();

            case R.id.tv_action:
                break;
            default:
                break;
        }
    }

    @Override
    public void showProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE:

                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    listUrl.add(String.valueOf(uri));
                    adapter.notifyDataSetChanged();

                }

                break;
            case SPEECH_2_TEXT:

                if (requestCode == SPEECH_2_TEXT) {
                    if (resultCode == RESULT_OK) {
                        String currData = et_content.getText().toString();
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        et_content.setText((currData) + result.get(0));
                    }
                }
                break;
            default:
                break;
        }


    }
}

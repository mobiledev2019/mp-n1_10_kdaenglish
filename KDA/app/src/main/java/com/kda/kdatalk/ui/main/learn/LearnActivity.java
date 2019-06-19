package com.kda.kdatalk.ui.main.learn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kda.kdatalk.R;
import com.kda.kdatalk.databinding.ActivityLearnBinding;
import com.kda.kdatalk.model.learn.VocabModel;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.utils.UtilLibs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LearnActivity extends ActivityBase implements LearnView {
    private static final String ID_LESSON = "id_lesson";
    private static final String TAG = LearnActivity.class.getName();
    private static final int RECORD_CODE = 5;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_score)
    TextView tv_score;

    @BindView(R.id.iv_record)
    ImageView iv_record;

    @BindView(R.id.iv_next)
    ImageView iv_next;


    private String id_lesson;

    ActivityLearnBinding binding;
    ActivityBase mContext;


    MediaPlayer mPlayer;

    LearnPresenter presenter;
    int curr_score = 0;

    int curr_position = 0;

    ArrayList<VocabModel> listVocab = new ArrayList<>();

    // record
    MediaRecorder myAudioRecorder = null;
    String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";

    String str_base64_record = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_learn);
        ButterKnife.bind(this);
        mContext = this;

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        id_lesson = getIntent().getStringExtra(ID_LESSON);
        presenter = new LearnPresenterImpl(this, this);

//        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//
//            }
//        });

        setupData();
//        Log.e(TAG, "onCreate: " + id_lesson);
//        setContentView(R.layout.activity_learn);

    }

    private void setupData() {

        listVocab = presenter.getListVocab(id_lesson);

        if (listVocab != null) {
            try {
                mPlayer.setDataSource(listVocab.get(0).url_voice);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCompletion(MediaPlayer mp) {
                //
                enableClick();

            }
        });


        tv_score.setText("0");


        setUpDataview();

        setUpLongClick();


    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpLongClick() {

        iv_record.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                checkrecordPermission();

                setUPMediaRecord();

                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        iv_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {

                        if (myAudioRecorder != null) {
                            myAudioRecorder.stop();
                            myAudioRecorder.release();
                            myAudioRecorder = null;
                        }

                        Log.e(TAG, "onTouch: " + outputFile);
                        convertTobase64(outputFile);
                        showProgress();

                        presenter.sendVoiceVocab(listVocab.get(curr_position).id, str_base64_record);

                        listVocab.get(curr_position).point = presenter.getScore(listVocab.get(curr_position).id);
                        listVocab.get(curr_position).isComplete = true;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                            }
                        }, 2000);
                        setUpDataview();

                        Toast.makeText(getApplicationContext(), "Recording stop", Toast.LENGTH_SHORT).show();

                        //stop recording voice if a long hold was detected and a recording started
                        return true; //indicate we're done listening to this touch listener
                    }
                }
                return false;
            }
        });
    }


    private String convertTobase64(String uri) {
        str_base64_record = "";
        try {
            File file;
            FileInputStream in = new FileInputStream(file = new File(uri));
            byte fileContent[] = new byte[(int) file.length()];

            in.read(fileContent, 0, fileContent.length);

            str_base64_record = Base64.encodeToString(fileContent, 0);
            //  Utilities.log("~~~~~~~~ Encoded: ", encoded);

            // dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.e(TAG, "convertTobase64: " + str_base64_record);
        return str_base64_record;
    }

    private void setUPMediaRecord() {
        if (myAudioRecorder == null) {
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            //UUID.randomUUID().toString()
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "_audio_record.3gp";
            myAudioRecorder.setOutputFile(outputFile);
        }
    }


    private void checkrecordPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_CODE);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.iv_back, R.id.iv_next, R.id.iv_listen})
    public void clickView(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                onBackPressed();
                break;

            case R.id.iv_next:
                curr_position++;

                if (curr_position < listVocab.size()) {
                    try {
                        mPlayer.setDataSource(listVocab.get(curr_position).url_voice);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                setUpDataview();
                Log.e(TAG, "clickView: " + "click Next");
                break;


            case R.id.iv_listen:
                dissableClick();

                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer.reset();
                }

                try {
                    mPlayer.setDataSource("https://soundcloud.com/v-anh-levis/2018-01-30-152725a");
                    mPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                mPlayer.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        enableClick();
                    }
                }, 2000);


                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dissableClick() {
        binding.ivListen.setImageDrawable(getDrawable(R.drawable.ic_listen_gray));
        binding.ivListen.setClickable(false);
        binding.ivRecord.setBackground(getDrawable(R.drawable.bg_circle_gray));
        binding.ivRecord.setClickable(false);
        binding.ivNext.setBackground(getDrawable(R.drawable.bg_circle_gray));
        binding.ivNext.setClickable(false);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableClick() {
        binding.ivListen.setImageDrawable(getDrawable(R.drawable.ic_listen));
        binding.ivListen.setClickable(true);
        binding.ivRecord.setBackground(getDrawable(R.drawable.bg_circle_blue));
        binding.ivRecord.setClickable(true);
        binding.ivNext.setBackground(getDrawable(R.drawable.bg_circle_blue));
        binding.ivNext.setClickable(true);


    }

    private void setUpDataview() {
//        Spannable span = new SpannableString(listVocab.get(0).vocab);
        VocabModel model = listVocab.get(curr_position);

        Log.e(TAG, "setUpDataview: " + model.isComplete);

        binding.sbProgress.setMax(listVocab.size() - 1);

        binding.sbProgress.setProgress(curr_position <= listVocab.size() - 1 ? curr_position : listVocab.size() - 1);

        if (curr_position < listVocab.size() - 1) {
            binding.ivNext.setVisibility(model.isComplete ? View.VISIBLE : View.GONE);
        } else {
            binding.ivNext.setVisibility(View.GONE);
            // done
        }

        String vocab = model.vocab;
        String pronun_vocab = model.impress_pronun;

        int start_span = vocab.lastIndexOf(pronun_vocab);

        SpannableString ss1 = new SpannableString(vocab);
        ss1.setSpan(new RelativeSizeSpan(2f), start_span, start_span + pronun_vocab.length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.GREEN), start_span, start_span + pronun_vocab.length(), 0);// set color
        binding.tvVocab.setText(ss1);

        binding.tvPronun.setText(model.pronun);
        //score
        curr_score += model.point;
        tv_score.setText(String.valueOf(curr_score));

        if (listVocab.get(listVocab.size() - 1).isComplete) {
            // done -> show dialog

            UtilLibs.showAlert(mContext, "Bạn đã hoàn thành bài học với số điểm là " + curr_score + "/" + listVocab.size() * 100);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_CODE) {
            if (resultCode == RESULT_OK) {

            } else {
                checkrecordPermission();
            }
        }
    }

    @Override
    public void showProgress() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressBar.setVisibility(View.GONE);

    }

    @Override
    public void getListVocabSuccess(ArrayList<VocabModel> list_vocab) {

        this.listVocab = list_vocab;

    }

    @Override
    public void onError(String mess) {

    }

    @Override
    public void getScoreSuccess(boolean isSuccess, int score) {
        // have score
        listVocab.get(curr_position).point = score;
        listVocab.get(curr_position).isComplete = true;

        setUpDataview();

    }

    @Override
    public void onBackPressed() {
        try {
            hideKeyboard(this);
        } catch (Exception e) {

        }

        if (binding.progressBar.getVisibility() == View.VISIBLE) {
            binding.progressBar.setVisibility(View.GONE);
            try {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            super.onBackPressed();
        }
    }
}

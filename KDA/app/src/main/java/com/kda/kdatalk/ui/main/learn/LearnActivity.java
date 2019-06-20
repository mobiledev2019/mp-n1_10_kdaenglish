package com.kda.kdatalk.ui.main.learn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.kda.kdatalk.ui.main.MainActivity;
import com.kda.kdatalk.utils.RecordWavMaster;
import com.kda.kdatalk.utils.UtilLibs;
import com.pixplicity.htmlcompat.HtmlCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private static final String POSITION_LESSON = "position_lesson";
    private static final String POSITION_LEARN = "position_learn";
    private static final String TAG = LearnActivity.class.getName();
    private static final int RECORD_CODE = 5;

    private int RECORDER_SAMPLERATE = 16000;
    private int RECORDER_CHANNELS = 1;
    private int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_score)
    TextView tv_score;

    @BindView(R.id.iv_record)
    ImageView iv_record;

    @BindView(R.id.iv_next)
    ImageView iv_next;

    @BindView(R.id.tv_result_phonetic)
    TextView tv_result_phonetic;

    String url_media_fake = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3";

    int learn_position = 0;
    int less_position = 0;
    private String id_lesson;

    ActivityLearnBinding binding;
    ActivityBase mContext;


    MediaPlayer mPlayer;

    LearnPresenter presenter;
    int curr_score = 0;

    int curr_position = 0;

    ArrayList<VocabModel> listVocab = new ArrayList<>();

    RecordWavMaster recordManager;


    // record
    AudioRecord myAudioRecorder = null;
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
        learn_position = getIntent().getIntExtra(POSITION_LEARN, 0);

        less_position = getIntent().getIntExtra(POSITION_LESSON, 0);

        listVocab = MainActivity.drafLesson.get(learn_position).list_lesson.get(less_position).list_vocab;


        presenter = new LearnPresenterImpl(this, this);


        recordManager = new RecordWavMaster();

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

//        listVocab = presenter.getListVocab(id_lesson);

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
//                mPlayer.release();
                mPlayer.reset();
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

                recordManager.recordWavStart();

                setUPMediaRecord();

//                try {
//                    myAudioRecorder.startRecording();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        iv_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {

                        recordManager.recordWavStop();
//                        stopRecording();

//                        if (myAudioRecorder != null) {
//                            try {
//                                myAudioRecorder.stop();
//                            }catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            myAudioRecorder.release();
//                            myAudioRecorder = null;
//                        }

                        Log.e(TAG, "onTouch: " + outputFile);
                        convertTobase64(outputFile);

//                        Log.e(TAG, "onTouch: DELETE_FILE" + deleteFile(outputFile));
                        deleteFilee(outputFile);

                        if (isNetworkConnected()) {
                            showProgress();

                            presenter.sendVoiceVocab(listVocab.get(curr_position).vocab, str_base64_record);

//                        listVocab.get(curr_position).point = presenter.getScore(listVocab.get(curr_position).id);
                            listVocab.get(curr_position).isComplete = true;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgress();
                                }
                            }, 2000);
                            setUpDataview();

                            Toast.makeText(getApplicationContext(), "Recording stop", Toast.LENGTH_SHORT).show();
                        } else {
                            UtilLibs.showAlert(mContext, "Không có kết nối internet!");
                        }

                        //stop recording voice if a long hold was detected and a recording started
                        return true; //indicate we're done listening to this touch listener
                    }
                }
                return false;
            }
        });
    }

    private void deleteFilee(String outputFile) {
        File file = new File(outputFile);

        boolean delete = file.delete();
        Log.e(TAG, "deleteFilee: WAV" + delete);

        File file_raw = new File("/storage/emulated/0/_audio_record.raw");

        delete = file_raw.delete();

        Log.e(TAG, "deleteFilee: RAW" + delete);


    }


    private String convertTobase64(String uri) {


        str_base64_record = "";
        try {
            File file;
            FileInputStream in = new FileInputStream(file = new File(uri));
            byte fileContent[] = new byte[(int) file.length()];

            in.read(fileContent, 0, fileContent.length);

            str_base64_record = Base64.encodeToString(fileContent, Base64.NO_WRAP);
            //  Utilities.log("~~~~~~~~ Encoded: ", encoded);

            // dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG, "convertTobase64: " + str_base64_record);
        return str_base64_record;
    }

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    private void setUPMediaRecord() {

//        int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
//                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
//
//        if (myAudioRecorder == null) {
//            myAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,RECORDER_SAMPLERATE,RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);
//
////            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
////            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
////            myAudioRecorder.setAudioSamplingRate(6000);
////    //            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
////            //UUID.randomUUID().toString()
////            myAudioRecorder.setOutputFile(outputFile);
//        }
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "_audio_record.wav";
//
//        myAudioRecorder.startRecording();
//        isRecording = true;
//
//        recordingThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                writeAudioDataToFile(outputFile);
//            }
//        });
//
//        recordingThread.start();

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
                tv_result_phonetic.setVisibility(View.GONE);
                curr_position++;

//                if (curr_position < listVocab.size()) {
//                    try {
//                        mPlayer.setDataSource(listVocab.get(curr_position).url_voice);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                setUpDataview();
                Log.e(TAG, "clickView: " + "click Next");
                break;


            case R.id.iv_listen:
                if (isNetworkConnected()) {
                    dissableClick();

                    try {
                        String url = "http://" + listVocab.get(curr_position).url_voice;
                        Log.e(TAG, "clickView: " + url);

                        try {
                            mPlayer.reset();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                    url_media_fake
                        mPlayer.setDataSource(url);
                        mPlayer.prepare();
                        mPlayer.start();
//                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mp) {
//                            mPlayer.start();
//                        }
//                    });
////                    mPlayer.prepare();
                    } catch (IOException e) {
                        enableClick();

                        UtilLibs.showAlert(mContext, "Lỗi nguồn âm thanh!");
                        Log.e(TAG, "clickView: " + e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    UtilLibs.showAlert(mContext, "Không có kết nối internet!");
                }

//                if (mPlayer != null) {
//                    mPlayer.stop();
//                    mPlayer.release();
//                    mPlayer.reset();
//                }
//
//                try {
//                    mPlayer.setDataSource("https://soundcloud.com/v-anh-levis/2018-01-30-152725a");
//                    mPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                mPlayer.start();

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        enableClick();
//                    }
//                }, 2000);


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

            UtilLibs.showAlert(mContext, "Bạn đã hoàn thành bài học với số điểm là " + curr_score + "/" + listVocab.size() * 100, new UtilLibs.ListenerAlert() {
                @Override
                public void cancel() {
                    onBackPressed();
                }

                @Override
                public void agree() {
                    onBackPressed();
                }
            });

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

        hideProgress();

        UtilLibs.showAlert(mContext,mess);

    }

    @Override
    public void getScoreSuccess(boolean isSuccess, int score) {
        // have score
        listVocab.get(curr_position).point = score;
        listVocab.get(curr_position).isComplete = true;

        setUpDataview();


    }

    @Override
    public void resultVoice(String phonetic, int score) {
        listVocab.get(curr_position).point = score;
        listVocab.get(curr_position).isComplete = true;

        Log.e(TAG, "resultVoice: " + phonetic);
        Spanned fromHtml_corr = HtmlCompat.fromHtml(mContext, phonetic, 0);

        tv_result_phonetic.setText(fromHtml_corr);
        tv_result_phonetic.setVisibility(View.VISIBLE);
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

    private void writeAudioDataToFile(String path) {
        // Write the output audio in byte

        String filePath = path;

//        File file = new File(path);
//
//        try {
//            file.createNewFile();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            myAudioRecorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private boolean isRecording = false;
    private Thread recordingThread = null;

    private void stopRecording() {
        // stops the recording activity
        if (null != myAudioRecorder) {
            isRecording = false;
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
            recordingThread = null;
        }
    }


//    public void recordWavStart() {
//        isRecording = true;
//        myAudioRecorder.startRecording();
//        mRecording = getFile("raw");
//        startBufferedWrite(mRecording);
//    }

//    public String recordWavStop() {
//        try {
//            mIsRecording = false;
//            mRecorder.stop();
//            File waveFile = getFile("wav");
//            rawToWave(mRecording, waveFile);
//            Log.e("path_audioFilePath",audioFilePath);
//            return audioFilePath;
//        } catch (Exception e) {
//            Log.e("Error saving file : ", e.getMessage());
//        }
//        return  null;
//    }
//
//
//    private void rawToWave(final File rawFile, final File waveFile) throws IOException {
//
//        byte[] rawData = new byte[(int) rawFile.length()];
//        DataInputStream input = null;
//        try {
//            input = new DataInputStream(new FileInputStream(rawFile));
//            input.read(rawData);
//        } finally {
//            if (input != null) {
//                input.close();
//            }
//        }
//        DataOutputStream output = null;
//        try {
//            output = new DataOutputStream(new FileOutputStream(waveFile));
//            // WAVE header
//            writeString(output, "RIFF"); // chunk id
//            writeInt(output, 36 + rawData.length); // chunk size
//            writeString(output, "WAVE"); // format
//            writeString(output, "fmt "); // subchunk 1 id
//            writeInt(output, 16); // subchunk 1 size
//            writeShort(output, (short) 1); // audio format (1 = PCM)
//            writeShort(output, (short) 1); // number of channels
//            writeInt(output, SAMPLE_RATE); // sample rate
//            writeInt(output, SAMPLE_RATE * 2); // byte rate
//            writeShort(output, (short) 2); // block align
//            writeShort(output, (short) 16); // bits per sample
//            writeString(output, "data"); // subchunk 2 id
//            writeInt(output, rawData.length); // subchunk 2 size
//            // Audio data (conversion big endian -> little endian)
//            short[] shorts = new short[rawData.length / 2];
//            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
//            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
//            for (short s : shorts) {
//                bytes.putShort(s);
//            }
//            output.write(bytes.array());
//        } finally {
//            if (output != null) {
//                output.close();
//                rawFile.delete();
//            }
//        }
//
//
//    }


}

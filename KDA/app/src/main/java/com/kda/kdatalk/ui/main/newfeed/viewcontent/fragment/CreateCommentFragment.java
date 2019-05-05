package com.kda.kdatalk.ui.main.newfeed.viewcontent.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.main.newfeed.viewcontent.Fragment_Create_CommentView;
import com.kda.kdatalk.ui.main.newfeed.viewcontent.ViewContentNewFeedActivity;
import com.kda.kdatalk.ui.main.newfeed.viewcontent.ViewContentPresenter;
import com.kda.kdatalk.utils.UtilLibs;
import com.pixplicity.htmlcompat.HtmlCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateCommentFragment extends FragmentBase implements Fragment_Create_CommentView {

    private static final String ID_FEED = "ID_FEED";
    private static final String CONTENT_FIX = "CONTENT_FIX";
    private ActivityBase mContext;


    @BindView(R.id.tv_error)
    TextView tv_error;

    @BindView(R.id.et_correct)
    EditText et_correct;

    @BindView(R.id.et_comment)
    EditText et_comment;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_action)
    TextView tv_action;

    //    Runnable runnable;
    boolean isOut = false;
    Timer timer;


    View viewRoot;

    ViewContentPresenter viewContentPresenter;

    String id_feed, content_fix;

    public static CreateCommentFragment newInstance(String id_feed, String content_fix) {
        Bundle args = new Bundle();
        CreateCommentFragment fragment = new CreateCommentFragment();
        args.putString(ID_FEED, id_feed);
        args.putString(CONTENT_FIX, content_fix);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (ActivityBase) context;
        viewContentPresenter = ((ViewContentNewFeedActivity) mContext).getPresenter();
    }

    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_create_comment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title.setText("Viết bình luận");
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<String> str_change = new ArrayList<>();
//                str_change = UtilLibs.compare2String(content_fix, et_correct.getText().toString());
//                //
//                Spanned fromHtml_err = HtmlCompat.fromHtml(mContext, str_change.get(0), 0);
//                Spanned fromHtml_corr = HtmlCompat.fromHtml(mContext, str_change.get(1), 0);
//
//                et_correct.setText(fromHtml_corr);
//                tv_error.setText(fromHtml_err);
//                int end = et_correct.getSelectionEnd();
//                int start = et_correct.getSelectionEnd();
//                Log.e("SELETION", "run: =>[" + end + "] => start =>[" + start + "]");
//                et_correct.setSelection(end);
//            }
//        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
            Bundle bundle = getArguments();
            if (bundle != null) {
                id_feed = bundle.getString(ID_FEED);
                content_fix = bundle.getString(CONTENT_FIX);
                //fake

                content_fix = "what are this shit bro?";
            }
        } catch (Exception e) {

        }
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        configData();
        addListener();
    }

    @OnClick({R.id.iv_back})
    public void clickView(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                UtilLibs.showAlert(mContext, "Hủy Bình Luận?", new UtilLibs.ListenerAlert() {
                    @Override
                    public void cancel() {
                        //
                        isOut = false;
                    }

                    @Override
                    public void agree() {
                        //
                        isOut = true;
                        mContext.onBackPressed();
                    }
                });
                break;

            default:
                break;
        }
    }


    private void configData() {
        tv_error.setText(content_fix);
        et_correct.setText(content_fix);

    }

    private void addListener() {

        et_correct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                if (!s.toString().trim().isEmpty()) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ArrayList<String> str_change = new ArrayList<>();
                            str_change = UtilLibs.compare2String(content_fix, et_correct.getText().toString());
                            //
                            Spanned fromHtml_err = HtmlCompat.fromHtml(mContext, str_change.get(0), 0);
                            Spanned fromHtml_corr = HtmlCompat.fromHtml(mContext, str_change.get(1), 0);

                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int end = et_correct.getSelectionEnd();
                                    int start = et_correct.getSelectionEnd();
                                    et_correct.setText(fromHtml_corr);
                                    tv_error.setText(fromHtml_err);
//                                    Log.e("SELETION", "run: =>[" + end + "] => start =>[" + start + "]");
                                    et_correct.setSelection(end);
                                }
                            });


                        }
                    }, 1500);
                }
            }
        });

    }

    /*


   ArrayList<String> str_change = new ArrayList<>();
                str_change = UtilLibs.compare2String(content_fix,et_correct.getText().toString());
                //
                Spanned fromHtml_err = HtmlCompat.fromHtml(mContext, str_change.get(0), 0);
                Spanned fromHtml_corr = HtmlCompat.fromHtml(mContext, str_change.get(1), 0);

                et_correct.setText(fromHtml_corr);
                tv_error.setText(fromHtml_err);
     */

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String err) {

    }
}

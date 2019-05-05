package com.kda.kdatalk.ui.main.newfeed.viewcontent;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.NewFeed;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.main.newfeed.fragment.ViewImageFragment;
import com.kda.kdatalk.ui.main.newfeed.viewcontent.adapter.Comment_Adapter;
import com.kda.kdatalk.ui.main.newfeed.viewcontent.adapter.ViewImageAdapter;
import com.kda.kdatalk.ui.main.newfeed.viewcontent.fragment.CreateCommentFragment;
import com.kda.kdatalk.utils.UtilLibs;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewContentNewFeedActivity extends ActivityBase implements ViewContentView {
    private static final String ID_FEED = "ID_FEED";

    @BindView(R.id.iv_ava)
    CircleImageView iv_ava;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_numLike)
    TextView tv_numLike;

    @BindView(R.id.tv_numComment)
    TextView tv_numComment;

    @BindView(R.id.iv_like)
    ImageView iv_like;

    @BindView(R.id.iv_comment)
    ImageView iv_comment;

    @BindView(R.id.rl_comment)
    RelativeLayout rl_comment;


    @BindView(R.id.rl_like)
    RelativeLayout rl_like;

    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;

    @BindView(R.id.rv_image)
    RecyclerView rv_image;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.scr_main)
    ScrollView scr_main;

    @BindView(R.id.iv_send)
    ImageView iv_send;

    @BindView(R.id.cv_comment)
    CardView cv_comment;

    @BindView(R.id.et_comment)
    EditText et_comment;

    NewFeed newFeed;

    private ViewContentPresenter presenter;
    private ViewImageAdapter image_adapter;
    private Comment_Adapter comment_adapter;


    String id_feed = "";


    //            RecyclerView rv_image, rv_comment;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content_newfeed);
        ButterKnife.bind(this);
        try {
            id_feed = getIntent().getStringExtra(ID_FEED);
        } catch (Exception e) {

        }
        setUpInput();
        setUpData();
    }


    private void setUpInput() {
        showProgress();
        presenter = new ViewContentPresenterImpl(this, this);
        // getData

        newFeed = presenter.getDetailNF(id_feed);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpData() {
        Picasso.get().load(newFeed.user_url).placeholder(R.drawable.noimg).error(R.drawable.noimg).into(iv_ava);
        tv_content.setText(newFeed.content);
        tv_numComment.setText(String.valueOf(newFeed.num_comment));
        tv_time.setText(newFeed.create_at);
        tv_numLike.setText(String.valueOf(newFeed.num_like));
        iv_like.setBackground(newFeed.isLike ? getDrawable(R.drawable.icon_like_clicked) : getDrawable(R.drawable.icon_like));

        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFeed.isLike = !newFeed.isLike;
                if (newFeed.isLike) {
                    iv_like.setBackground(getDrawable(R.drawable.icon_like_clicked));
                    newFeed.num_like++;
                    tv_numLike.setText(String.valueOf(newFeed.num_like));
                } else {
                    iv_like.setBackground(getDrawable(R.drawable.icon_like));
                    newFeed.num_like--;
                    tv_numLike.setText(String.valueOf(newFeed.num_like));

                }
            }
        });

        rl_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFeed.isLike = !newFeed.isLike;
                if (newFeed.isLike) {
                    iv_like.setBackground(getDrawable(R.drawable.icon_like_clicked));
                    newFeed.num_like++;
                    tv_numLike.setText(String.valueOf(newFeed.num_like));
                } else {
                    iv_like.setBackground(getDrawable(R.drawable.icon_like));
                    newFeed.num_like--;
                    tv_numLike.setText(String.valueOf(newFeed.num_like));

                }
            }
        });

        // image
        image_adapter = new ViewImageAdapter(ViewContentNewFeedActivity.this, newFeed.list_image, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewContentNewFeedActivity.this.changeFragment(R.id.viewMain, ViewImageFragment.newInstance(newFeed.list_image, position), true);

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ViewContentNewFeedActivity.this);
        rv_image.setNestedScrollingEnabled(false);
        rv_image.setLayoutManager(mLayoutManager);
        rv_image.setAdapter(image_adapter);


        // comment

        comment_adapter = new Comment_Adapter(ViewContentNewFeedActivity.this, newFeed.list_comment, new Comment_Adapter.OnClickVote() {
            @Override
            public void voteUp(int position, boolean isVote) {
                // do something vote up
            }

            @Override
            public void voteDown(int position, boolean isVote) {
                // do something vote down
            }
        });

        RecyclerView.LayoutManager manager_comment = new LinearLayoutManager(ViewContentNewFeedActivity.this);

        rv_comment.setNestedScrollingEnabled(false);
        rv_comment.setLayoutManager(manager_comment);
        rv_comment.setAdapter(comment_adapter);

        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goto fragment comment
            }
        });

        rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goto fragment comment
            }
        });
        hideProgress();

        scr_main.setVisibility(View.VISIBLE);
        configScrollView(true);
        cv_comment.setVisibility(View.VISIBLE);

    }

    private void configScrollView(boolean isMargin) {
        if (isMargin) {
            FrameLayout.LayoutParams scroll_params = (FrameLayout.LayoutParams) scr_main.getLayoutParams();
            scroll_params.setMargins(0,0,0,70);
            scr_main.setLayoutParams(scroll_params);
        } else {
            FrameLayout.LayoutParams scroll_params = (FrameLayout.LayoutParams) scr_main.getLayoutParams();
            scroll_params.setMargins(0,0,0,0);
            scr_main.setLayoutParams(scroll_params);
        }
    }

    public ViewContentPresenter getPresenter() {
        return presenter;
    }

    @OnClick({R.id.cv_comment, R.id.et_comment, R.id.iv_send})
    public void createComment() {
        changeFragment(R.id.viewMain, CreateCommentFragment.newInstance(newFeed.id, newFeed.content), true);
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
}

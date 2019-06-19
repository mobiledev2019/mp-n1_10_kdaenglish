package com.kda.kdatalk.ui.main.newfeed.viewcontent.adapter;

import android.os.Build;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.Comment;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.pixplicity.htmlcompat.HtmlCompat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment_Adapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = Comment_Adapter.class.getSimpleName();

    ActivityBase mContext;
    ArrayList<Comment> list_data;

    OnClickVote onClickVote;


    public Comment_Adapter(ActivityBase mContext, ArrayList<Comment> list_data) {
        this.mContext = mContext;
        this.list_data = list_data;
    }

    public void setList_data(ArrayList<Comment> list_data) {
        this.list_data = list_data;
    }

    public Comment_Adapter(ActivityBase mContext, ArrayList<Comment> list_data, OnClickVote onClickVote) {
        this.mContext = mContext;
        this.list_data = list_data;
        this.onClickVote = onClickVote;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    private class CommentViewHolder extends BaseViewHolder {
        CircleImageView iv_ava;
        TextView tv_name, tv_time, tv_correct, tv_error, tv_content, tv_numVote;
        LinearLayout rl_err, rl_correct;
        ImageView iv_up, iv_down, iv_rank;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_ava = itemView.findViewById(R.id.iv_ava);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_correct = itemView.findViewById(R.id.tv_correct);
            tv_error = itemView.findViewById(R.id.tv_error);
            rl_err = itemView.findViewById(R.id.rl_error);
            rl_correct = itemView.findViewById(R.id.rl_correct);
            tv_numVote = itemView.findViewById(R.id.tv_numVote);

            iv_rank = itemView.findViewById(R.id.iv_rank);

            tv_content = itemView.findViewById(R.id.tv_content);

            iv_up = itemView.findViewById(R.id.iv_up);
            iv_down = itemView.findViewById(R.id.iv_down);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBind(int position) {
            Comment comment = list_data.get(position);


            Log.e(TAG, "onBind: " + comment.user.url_img_ava);

            Picasso.get().load(comment.user.url_img_ava)
                    .placeholder(R.drawable.user_no_img)
                    .error(R.drawable.user_no_img)
                    .into(iv_ava);
            updateNumVote(comment);

            tv_content.setText(comment.content.trim().isEmpty() ? "" : comment.content);

            if (comment.corr_text != null && comment.err_text != null) {

                if (!comment.corr_text.trim().isEmpty() && !comment.err_text.trim().isEmpty()) {
                    tv_name.setText(comment.user.name);
                    tv_time.setText(comment.timme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Spanned fromHtml_corr = HtmlCompat.fromHtml(mContext, comment.corr_text, 0);
                        Spanned fromHtml_err = HtmlCompat.fromHtml(mContext, comment.err_text, 0);

                        tv_correct.setText(fromHtml_corr);
                        tv_error.setText(fromHtml_err);
                    } else {
                        Spanned fromHtml_corr = HtmlCompat.fromHtml(mContext, comment.corr_text, 0);
                        Spanned fromHtml_err = HtmlCompat.fromHtml(mContext, comment.err_text, 0);

                        tv_correct.setText(fromHtml_corr);
                        tv_error.setText(fromHtml_err);

                    }
                } else {
                    tv_name.setText(comment.user.name);
                    tv_time.setText(comment.timme);
                    hideCheck();
                }


            } else {
                hideCheck();
            }

            // up num_vote

            iv_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (comment.is_down) {
                        comment.num_vote++;
                    }
                    comment.is_up = !comment.is_up;
                    iv_up.setBackground(comment.is_up ? mContext.getDrawable(R.drawable.ic_sort_up_press) : mContext.getDrawable(R.drawable.ic_sort_up));
                    comment.is_down = false;
                    iv_down.setBackground(mContext.getDrawable(R.drawable.ic_sort_down));

                    if (comment.is_up) {
                        comment.num_vote++;

                    } else {
                        comment.num_vote--;
                    }

                    updateNumVote(comment);

                    onClickVote.voteUp(position, comment.is_up);
                }
            });

            iv_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (comment.is_up) {
                        comment.num_vote--;
                    }

                    comment.is_down = !comment.is_down;
                    iv_down.setBackground(comment.is_down ? mContext.getDrawable(R.drawable.ic_sort_down_press) : mContext.getDrawable(R.drawable.ic_sort_down));
                    comment.is_up = false;
                    iv_up.setBackground(mContext.getDrawable(R.drawable.ic_sort_up));

                    if (comment.is_down) {
                        comment.num_vote--;
                    } else {
                        comment.num_vote++;
                    }

                    updateNumVote(comment);

                    onClickVote.voteDown(position, comment.is_down);
                }
            });


            if (comment.is_down) {
                iv_up.setBackground(mContext.getDrawable(R.drawable.ic_sort_up));
                iv_down.setBackground(mContext.getDrawable(R.drawable.ic_sort_down_press));
            }

            if (comment.is_up) {
                iv_up.setBackground(mContext.getDrawable(R.drawable.ic_sort_up_press));
                iv_down.setBackground(mContext.getDrawable(R.drawable.ic_sort_down));
            }


            configRank(comment);

        }

        private void configRank(Comment comment) {
            if (comment != null) {
                switch (comment.rank) {
                    case 1:
                        iv_rank.setBackgroundResource(R.drawable.rank1);

                        break;
                    case 2:
                        iv_rank.setBackgroundResource(R.drawable.rank2);

                        break;

                    case 3:
                        iv_rank.setBackgroundResource(R.drawable.rank3);

                        break;

                    case 4:
                        iv_rank.setBackgroundResource(R.drawable.rank4);

                        break;

                    case 5:
                        iv_rank.setBackgroundResource(R.drawable.rank5);

                        break;

                    default:
                        iv_rank.setBackgroundResource(R.drawable.rank5);

                        break;
                }

            } else {
                iv_rank.setBackgroundResource(R.drawable.rank5);
            }
        }

        private void updateNumVote(Comment comment) {
            if (comment != null) {
                tv_numVote.setText(String.valueOf(comment.num_vote));

            } else {
                tv_numVote.setText("0");
            }
        }

        private void hideCheck() {
            rl_correct.setVisibility(View.GONE);
            rl_err.setVisibility(View.GONE);
        }
    }

    public interface OnClickVote {
        void voteUp(int position, boolean isVote);

        void voteDown(int position, boolean isVote);
    }
}

package com.kda.kdatalk.ui.main.notification.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.noti.Noti;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotiAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = NotiAdapter.class.getSimpleName();

    private int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    OnLoadMore onLoadMore;

    int visibleThreadHold = 3;
    int lastVisibleItem, totalItem;
    boolean isloading = false;


    Context mContext;
    ArrayList<Noti> list_noti;
    OnClickReadNoti onClickReadNoti;

    public void setList_noti(ArrayList<Noti> list_noti) {
        this.list_noti = list_noti;
    }

    public NotiAdapter(Context mContext, ArrayList<Noti> list_noti, OnClickReadNoti onClickReadNoti) {
        this.mContext = mContext;
        this.list_noti = list_noti;
        this.onClickReadNoti = onClickReadNoti;
    }


    public NotiAdapter(Context mContext, RecyclerView recyclerView, ArrayList<Noti> list_noti, OnClickReadNoti onClickReadNoti) {

        this.mContext = mContext;
        this.list_noti = list_noti;
        this.onClickReadNoti = onClickReadNoti;

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItem = manager.getItemCount();
                lastVisibleItem = manager.findLastVisibleItemPosition();

                if (!isloading && totalItem <= (lastVisibleItem + visibleThreadHold)) {
                    if (onLoadMore != null) {
                        onLoadMore.onLoad();
                        Log.e(TAG, "onScrolled: " + "loadmore");
                    }

                    isloading = true;

                }
            }
        });
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_noti, parent, false);
            return new NotiViewHolder(view);

        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.kda_loadmore_layout, parent, false);
            return new LoadMoreViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        return list_noti.size();
    }

    private class NotiViewHolder extends BaseViewHolder {
        LinearLayout lnl_noti;// to change background when readnoti
        CircleImageView cv_ava; // ava of person act in noti
        TextView tv_content;
        TextView tv_time;

        RelativeLayout rl_image;// rl_small_image -> change background if like or comment
        ImageView iv_comment_or_like; // name call everything

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);

            lnl_noti = itemView.findViewById(R.id.lnl_noti);
            cv_ava = itemView.findViewById(R.id.cv_ava);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            rl_image = itemView.findViewById(R.id.rl_small_image);
            iv_comment_or_like = itemView.findViewById(R.id.iv_comment_or_like);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBind(int position) {
            Noti noti = list_noti.get(position);

            Picasso.get().load(noti.user_url)
                    .placeholder(R.drawable.noimg)
                    .error(R.drawable.noimg)
                    .into(cv_ava);

            // read or not
            lnl_noti.setBackgroundColor(noti.isRead ? Color.parseColor("#ffffff") : Color.parseColor("#4177C5F0"));
//            lnl_noti.setBackground(R.color.white);

            // comment or like
            iv_comment_or_like.setBackground(noti.isComment ? mContext.getDrawable(R.drawable.ic_comment_white) : mContext.getDrawable(R.drawable.ic_like));
            // comment green -> like blue
            rl_image.setBackground(noti.isComment ? mContext.getDrawable(R.drawable.bg_circle_green) : mContext.getDrawable(R.drawable.bg_circle_blue));

            tv_content.setText(Html.fromHtml(noti.contentNoti));
            tv_time.setText(noti.time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickReadNoti.onClickRead(getAdapterPosition(), noti.isRead);
                    noti.isRead = true;
                    notifyDataSetChanged();
                }
            });


        }
    }


    private class LoadMoreViewHolder extends BaseViewHolder {

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(int position) {

        }
    }

    public interface OnLoadMore {
        void onLoad();
    }

    @Override
    public int getItemViewType(int position) {

        return list_noti.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnLoadMore(OnLoadMore onLoadMore) {
        this.onLoadMore = onLoadMore;
    }

    public void setIsloading(boolean isloading) {
        this.isloading = isloading;
    }

    public interface OnClickReadNoti {
        void onClickRead(int position, boolean isRead);
    }
}

package com.kda.kdatalk.ui.main.newfeed.viewcontent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewImageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    ActivityBase mContext;
    ArrayList<String> list_url;

    AdapterView.OnItemClickListener onClickViewImage;

    public ViewImageAdapter(ActivityBase mContext, ArrayList<String> list_url, AdapterView.OnItemClickListener onClickViewImage) {
        this.mContext = mContext;
        this.list_url = list_url;
        this.onClickViewImage = onClickViewImage;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list_url.size();
    }

    private class ImageViewHolder extends BaseViewHolder {

        ImageView imageView;
        LinearLayout relativeLayout;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            relativeLayout = itemView.findViewById(R.id.rlt_item_img);
        }

        @Override
        public void onBind(int position) {
            String url = list_url.get(position);
            Picasso.get().load(url)
                    .placeholder(R.drawable.noimg)
                    .error(R.drawable.noimg)
                    .into(imageView);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) relativeLayout.getLayoutParams();
//
//            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
//            params1.setMargins(0,0,0,0);
            if ((position == (list_url.size() - 1))) {
                params.setMargins(0, 0, 0, 0);
//                relativeLayout.setLayoutParams(params);
//                imageView.setLayoutParams(params1);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickViewImage.onItemClick(null, v, position, position);
                }
            });
        }
    }
}

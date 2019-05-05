package com.kda.kdatalk.ui.main.newfeed.addnewfeed.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddImageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<Bitmap> bitmapList;
    Context mContext;

    AdapterView.OnItemClickListener onClickRemove;


    public AddImageAdapter(List<Bitmap> bitmapList, Context mContext, AdapterView.OnItemClickListener onClickRemove) {
        this.bitmapList = bitmapList;
        this.mContext = mContext;
        this.onClickRemove = onClickRemove;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_image, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public class ImageViewHolder extends BaseViewHolder {
        ImageView iv_image;
        RelativeLayout rlt_remove;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            rlt_remove = itemView.findViewById(R.id.rlt_remove);

        }

        @Override
        public void onBind(int position) {
            Bitmap mBitmap = bitmapList.get(position);
            if (mBitmap != null) {
                iv_image.setImageBitmap(mBitmap);
            }

            rlt_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickRemove.onItemClick(null, v, position, position);
                }
            });

        }
    }
}

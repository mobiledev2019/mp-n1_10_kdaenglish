package com.kda.kdatalk.ui.main.newfeed.adapter.child;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kda.kdatalk.Assymetric.AGVRecyclerViewAdapter;
import com.kda.kdatalk.Assymetric.AsymmetricItem;
import com.kda.kdatalk.R;
import com.kda.kdatalk.model.image.ItemImage;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends AGVRecyclerViewAdapter<ViewHolder> {
    private final List<ItemImage> items;
    private int mDisplay = 0;
    private int mTotal = 0;

    public ImageAdapter(List<ItemImage> items, int mDisplay, int mTotal) {
        this.items = items;
        this.mDisplay = mDisplay;
        this.mTotal = mTotal;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclerViewActivity", "onCreateView");
        return new ViewHolder(parent, viewType, items);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("RecyclerViewActivity", "onBindView position=" + position);
        holder.bind(items, position, mDisplay, mTotal);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AsymmetricItem getItem(int position) {
        return (AsymmetricItem) items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 1 : 0;
    }
}


class ViewHolder extends RecyclerView.ViewHolder {
    private final ImageView mImageView;
    private final TextView textView;

    public ViewHolder(ViewGroup parent, int viewType, List<ItemImage> items) {
        super(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_image, parent, false));

        mImageView = (ImageView) itemView.findViewById(R.id.iv_image);
        textView = (TextView) itemView.findViewById(R.id.tv_count);


    }


    @SuppressLint("CheckResult")
    public void bind(List<ItemImage> item, int position, int mDisplay, int mTotal) {

        Picasso.get()
                .load(String.valueOf(item.get(position).getImagePath()))
                .placeholder(R.drawable.noimg)
                .error(R.drawable.noimg)
                .into(mImageView);


        Picasso.get()
                .load(String.valueOf(item.get(position).getImagePath()))
                .placeholder(R.drawable.noimg)
                .error(R.drawable.noimg)
                .into(mImageView);
//        RequestOptions options = new RequestOptions();
//        options.dontTransform();
//        options.placeholder(R.drawable.noimg);
//        options.diskCacheStrategy(DiskCacheStrategy.ALL);
//
//        Glide.with(mImageView).load(String.valueOf(item.get(position).getImagePath())).apply(options).addListener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                return false;
//            }
//        });


//        ImageLoader.getInstance().displayImage(String.valueOf(item.get(position).getImagePath()), mImageView);
        textView.setText("+" + (mTotal - mDisplay));
        if (mTotal > mDisplay) {
            if (position == mDisplay - 1) {
                textView.setVisibility(View.VISIBLE);
                mImageView.setAlpha(72);
            } else {
                textView.setVisibility(View.INVISIBLE);
                mImageView.setAlpha(255);
            }
        } else {
            mImageView.setAlpha(255);
            textView.setVisibility(View.INVISIBLE);
        }

        // textView.setText(String.valueOf(item.getPosition()));
    }
}

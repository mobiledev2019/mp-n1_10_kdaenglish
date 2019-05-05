package com.kda.kdatalk.ui.main.newfeed.adapter.newfeed;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.kda.kdatalk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImageViewAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> listUrl;

    public ImageViewAdapter(Context mContext, ArrayList<String> listUrl) {
        this.mContext = mContext;
        this.listUrl = listUrl;
    }

    @Override
    public int getCount() {
        return listUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mContext);
        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        photoView.setLayoutParams(params);
        photoView.setAdjustViewBounds(true);

        Picasso.get().load(listUrl.get(position))
                .placeholder(R.drawable.noimg)
                .error(R.drawable.noimg)
                .into(photoView);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

package com.kda.kdatalk.ui.main.newfeed.addnewfeed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.RequestOptions;
import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.widget.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context context;

    private List<String> arrFilePath;
    AdapterView.OnItemClickListener onClickRemove;



    public GridAdapter(Context context, List<String> arrFilePath, AdapterView.OnItemClickListener onClickRemove) {
        this.context = context;
        this.arrFilePath = arrFilePath;
        this.onClickRemove = onClickRemove;
    }

    @Override
    public int getCount() {
        return arrFilePath.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        String url = arrFilePath.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_add_image, null);
        }

        SquareImageView imageView = view.findViewById(R.id.iv_image);
        RelativeLayout rlt_remove = view.findViewById(R.id.rlt_remove);

        Picasso.get().load(url)
                .error(R.drawable.noimg)
                .placeholder(R.drawable.noimg)
                .into(imageView);

        rlt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRemove.onItemClick(null,v,position,position);
            }
        });

        return view;
    }

    public List<String> getArrFilePath() {
        return arrFilePath;
    }

    public void setArrFilePath(List<String> arrFilePath) {
        this.arrFilePath = arrFilePath;
    }
}

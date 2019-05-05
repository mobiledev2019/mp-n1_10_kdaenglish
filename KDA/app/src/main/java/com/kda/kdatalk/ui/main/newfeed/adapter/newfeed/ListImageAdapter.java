package com.kda.kdatalk.ui.main.newfeed.adapter.newfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.ItemImage;
import com.mindorks.placeholderview.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListImageAdapter extends ArrayAdapter<ItemImage> {

    private final List<ItemImage> items;
    private Context mContext;
    private int mDisplay = 0;
    private int mTotal = 0;

    public ListImageAdapter(@NonNull Context context, int resource, List<ItemImage> items) {
        super(context, resource);
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_image, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ItemImage itemImage = items.get(position);
        TextView tv_count = view.findViewById(R.id.tv_count);
        ImageView iv_image = view.findViewById(R.id.iv_image);

        Picasso.get()
                .load(itemImage.getImagePath())
                .placeholder(R.drawable.noimg)
                .error(R.drawable.noimg)
                .into(iv_image);


        return view;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 1 : 0;
    }

    public void appendItems(List<ItemImage> newItems) {
        addAll(newItems);
        notifyDataSetChanged();
    }

    public void setItems(List<ItemImage> moreItems) {
        clear();
        appendItems(moreItems);
    }
}

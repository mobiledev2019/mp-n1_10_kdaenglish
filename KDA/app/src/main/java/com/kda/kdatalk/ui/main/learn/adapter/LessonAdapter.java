package com.kda.kdatalk.ui.main.learn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.learn.LessonModel;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class LessonAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = LessonAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<LessonModel> list_model;
    private AdapterView.OnItemClickListener onItemClickListener;

    public LessonAdapter(Context mContext, ArrayList<LessonModel> list_model, AdapterView.OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.list_model = list_model;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lesson, parent,false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list_model.size();
    }

    private class LessonViewHolder extends BaseViewHolder{

        CircleImageView iv_less;
        View view_line;
        TextView tv_name, tv_difficult;


        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_less = itemView.findViewById(R.id.iv_lesson);
            tv_name = itemView.findViewById(R.id.tv_name_lesson);
            tv_difficult = itemView.findViewById(R.id.tv_difficult);
            view_line = itemView.findViewById(R.id.view_line);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, getAdapterPosition(), getAdapterPosition());
                }
            });

            if (getAdapterPosition() == list_model.size() - 1) {
                view_line.setVisibility(View.GONE);
            }
        }

        @Override
        public void onBind(int position) {
            LessonModel model = list_model.get(position);
            tv_name.setText(model.name);
            tv_difficult.setText(model.difficult);

            Log.e(TAG, "onBind: " + model.url_image_lesson);

            // falke https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/49210821_2212914338949531_2269121412495048704_n.jpg?_nc_cat=110&_nc_oc=AQl5hCOOOs2wFqZM5JdJB0GjxWo3wi4ckjFRBOCXIGau83GcDprhj8zzQ391IWwdcbSL0bolY4od-qX2c2tgRvDq&_nc_ht=scontent.fhan2-4.fna&oh=95ea3986aff858e6e52f96d2038f5430&oe=5DC6ABDE

//            String url = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/49210821_2212914338949531_2269121412495048704_n.jpg?_nc_cat=110&_nc_oc=AQl5hCOOOs2wFqZM5JdJB0GjxWo3wi4ckjFRBOCXIGau83GcDprhj8zzQ391IWwdcbSL0bolY4od-qX2c2tgRvDq&_nc_ht=scontent.fhan2-4.fna&oh=95ea3986aff858e6e52f96d2038f5430&oe=5DC6ABDE";

            Picasso.get().load(R.drawable.icon_lesson)
                    .placeholder(R.drawable.noimg)
                    .error(R.drawable.noimg)
                    .into(iv_less);

            if (position == list_model.size() -1) {
                view_line.setVisibility(View.GONE);

            }

        }
    }
}

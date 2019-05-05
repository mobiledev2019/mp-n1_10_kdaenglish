package com.kda.kdatalk.ui.main.learn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.learn.LearnModel;
import com.kda.kdatalk.ui.base.BaseViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LearnAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private ArrayList<LearnModel> list_learn;
    private AdapterView.OnItemClickListener onItemClickListener;

    public LearnAdapter(Context mContext, ArrayList<LearnModel> list_learn, AdapterView.OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.list_learn = list_learn;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_learn,parent, false);

        return new LearnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list_learn.size();
    }

    private class LearnViewHolder extends BaseViewHolder{
        TextView tv_title;
        RecyclerView rv_lesson;
        LessonAdapter adapter;

        public LearnViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            rv_lesson = itemView.findViewById(R.id.rv_lesson);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            rv_lesson.setHasFixedSize(true);
            rv_lesson.setLayoutManager(manager);
        }

        @Override
        public void onBind(int position) {
            LearnModel model = list_learn.get(position);
            adapter = new LessonAdapter(mContext, model.list_lesson, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemClickListener.onItemClick(null,view,getAdapterPosition(), position);
                    // parent position -> child position
                }
            });
            rv_lesson.setAdapter(adapter);
            tv_title.setText(model.name);

        }
    }
}

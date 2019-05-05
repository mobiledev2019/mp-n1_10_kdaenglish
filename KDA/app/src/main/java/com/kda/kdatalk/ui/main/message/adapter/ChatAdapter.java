package com.kda.kdatalk.ui.main.message.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.chat.ChatModel;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    ArrayList<ChatModel> list_data;
    Context mContext;

    public ChatAdapter(ArrayList<ChatModel> list_data, Context mContext) {
        this.list_data = list_data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false);
            return new LeftMsgViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false);
            return new RightMsgViewHolder(view);

        }
//        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
//        if (list_data.get(position).isMe) {
//            return MSG_TYPE_RIGHT;
//        }
//
        return list_data.get(position).isMe ? MSG_TYPE_RIGHT : MSG_TYPE_LEFT;
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    private class LeftMsgViewHolder extends BaseViewHolder {
        CircleImageView cv_ava;
        TextView tv_content;

        public LeftMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_ava = itemView.findViewById(R.id.cv_ava);
            tv_content = itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void onBind(int position) {
            ChatModel chatModel = list_data.get(position);
            Picasso.get().load(chatModel.user.getUrl_img_ava())
                    .placeholder(R.drawable.user_no_img)
                    .placeholder(R.drawable.user_no_img)
                    .into(cv_ava);

            Log.e("LeftMsgViewHolder", "onBind: " + chatModel.content);
            tv_content.setText(chatModel.content);
        }
    }

    private class RightMsgViewHolder extends BaseViewHolder {
        TextView tv_content;

        public RightMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void onBind(int position) {
            ChatModel chatModel = list_data.get(position);

            tv_content.setText(chatModel.content);
            Log.e("LeftMsgViewHolder", "onBind: " + chatModel.content);

        }
    }
}

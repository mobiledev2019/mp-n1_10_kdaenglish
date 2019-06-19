package com.kda.kdatalk.ui.main.message.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.model.chat.ChatModel;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    String url_parter = "";
    ArrayList<ChatModel> list_data;
    Context mContext;

    User me;

    public void setList_data(ArrayList<ChatModel> list_data) {
        this.list_data = list_data;
    }

    public ChatAdapter(ArrayList<ChatModel> list_data, Context mContext, String url_parter) {
        this.list_data = list_data;
        this.url_parter = url_parter;
        this.mContext = mContext;
        me = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_info), User.class);
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

        if (list_data.get(position).id_user.equals(me._id)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }


//
//        return list_data.get(position).isMe ? MSG_TYPE_RIGHT : MSG_TYPE_LEFT;
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
            Picasso.get().load(url_parter)
                    .placeholder(R.drawable.user_no_img)
                    .placeholder(R.drawable.user_no_img)
                    .into(cv_ava);

            Log.e("LeftMsgViewHolder", "onBind: " + chatModel.content);
            tv_content.setText(chatModel.content);


        }
    }

    private class RightMsgViewHolder extends BaseViewHolder {
        TextView tv_content;
        TextView tv_err;

        ImageView iv_error;

        public RightMsgViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_content = itemView.findViewById(R.id.tv_content);
            tv_err = itemView.findViewById(R.id.tv_error);

            iv_error = itemView.findViewById(R.id.iv_error);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBind(int position) {
            ChatModel chatModel = list_data.get(position);

            if (chatModel.isSending) {
                tv_content.setBackground(mContext.getDrawable(R.drawable.bg_item_chat_right_sending));

            } else {

                tv_content.setBackground(mContext.getDrawable(R.drawable.bg_itemchat_right));
            }


            //

//            if (!chatModel.isSuccess && !chatModel.isSending) {
//                tv_err.setVisibility(View.VISIBLE);
//                iv_error.setVisibility(View.VISIBLE);
//            }

            tv_content.setText(chatModel.content);
            Log.e("LeftMsgViewHolder", "onBind: " + chatModel.content);

        }
    }
}

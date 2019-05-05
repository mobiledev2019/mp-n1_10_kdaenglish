package com.kda.kdatalk.ui.main.message.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.chat.MessageModel;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Message_Adapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private ArrayList<MessageModel> listContact;
    AdapterView.OnItemClickListener onItemClickListener;

    public Message_Adapter(Context mContext, ArrayList<MessageModel> listContact, AdapterView.OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.listContact = listContact;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return listContact.size();
    }

    private class ContactViewHolder extends BaseViewHolder {

        CircleImageView iv_ava;
        TextView tv_name, tv_lastmsg, tv_time;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_ava = itemView.findViewById(R.id.iv_ava);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_lastmsg = itemView.findViewById(R.id.tv_last_msg);
            tv_time = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, getAdapterPosition(), getAdapterPosition());
                }
            });

        }

        @Override
        public void onBind(int position) {
            MessageModel messageModel = listContact.get(position);
            Picasso.get().load(messageModel.user.getUrl_img_ava())
                    .placeholder(R.drawable.user_no_img)
                    .error(R.drawable.user_no_img)
                    .into(iv_ava);
            tv_name.setText(messageModel.user.name);
            tv_lastmsg.setText(messageModel.last_str);
            tv_time.setText(messageModel.last_time);
        }
    }
}

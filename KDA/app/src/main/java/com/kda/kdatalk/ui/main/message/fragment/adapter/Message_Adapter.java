package com.kda.kdatalk.ui.main.message.fragment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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

    private static final String TAG = Message_Adapter.class.getSimpleName();
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
        return listContact != null ? listContact.size() : 0;
    }

    private class ContactViewHolder extends BaseViewHolder {

        CircleImageView iv_ava;
        TextView tv_name, tv_lastmsg, tv_time;

        LinearLayout lnl_seen;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_ava = itemView.findViewById(R.id.iv_ava);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_lastmsg = itemView.findViewById(R.id.tv_last_msg);
            tv_time = itemView.findViewById(R.id.tv_time);
            lnl_seen = itemView.findViewById(R.id.lnl_seen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lnl_seen.setVisibility(View.GONE);

                    onItemClickListener.onItemClick(null, v, getAdapterPosition(), getAdapterPosition());
                }
            });

        }

        @Override
        public void onBind(int position) {
            MessageModel messageModel = listContact.get(position);

            if (messageModel.seen) {
                lnl_seen.setVisibility(View.GONE);
                tv_lastmsg.setTypeface(tv_lastmsg.getTypeface(), Typeface.NORMAL);
                tv_lastmsg.setTextColor(mContext.getResources().getColor(R.color.gray));
            } else {
                lnl_seen.setVisibility(View.VISIBLE);
                tv_lastmsg.setTypeface(tv_lastmsg.getTypeface(), Typeface.BOLD);
                tv_lastmsg.setTextColor(mContext.getResources().getColor(R.color.white));
            }

//            Log.e(TAG, "onBind:  " + listContact.size());
//
//            Log.e(TAG, "onBind: " + messageModel.id);

            if (messageModel.user.getUrl_img_ava() == null) {
                messageModel.user.url_img_ava = "";
            }

            if (messageModel.user.url_img_ava.isEmpty()) {
                messageModel.user.url_img_ava = "null";
            } else {
                Picasso.get().load(messageModel.user.getUrl_img_ava())
                        .placeholder(R.drawable.user_no_img)
                        .error(R.drawable.user_no_img)
                        .into(iv_ava);
            }




            tv_name.setText(messageModel.user.name);
            tv_lastmsg.setText(messageModel.last_str != null ? messageModel.last_str : "");
            tv_time.setText(messageModel.last_time != null ? messageModel.last_time : "");
        }
    }

    public void setListContact(ArrayList<MessageModel> listContact) {
        this.listContact = listContact;
    }
}

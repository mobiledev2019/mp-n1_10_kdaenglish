package com.kda.kdatalk.ui.main.newfeed.adapter.newfeed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.NewFeed;
import com.kda.kdatalk.model.image.ItemImage;
import com.kda.kdatalk.ui.base.BaseViewHolder;
import com.kda.kdatalk.ui.main.newfeed.adapter.child.ImageAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewFeedAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<NewFeed> list_data;

    NewFeedClickListener listener;

    Context context;

    public NewFeedAdapter(List<NewFeed> list_data, Context context, NewFeedClickListener listener) {
        this.list_data = list_data;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newfeed, parent, false);
        return new NewFeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class NewFeedHolder extends BaseViewHolder {

        CircleImageView iv_ava;
        TextView tv_content, tv_name, tv_time, tv_numLike, tv_numComment, tv_countImage;
        ImageView iv_like, iv_comment;

        RelativeLayout rl_comment, rl_like, rlt_more;
        LinearLayout lnl_more, lnl_layout;

        ImageView iv_1, iv_2, iv_3, iv_transparent;

//        AsymmetricRecyclerView rv_image;
//        AsymmetricGridView rv_image;


        public NewFeedHolder(@NonNull View itemView) {
            super(itemView);

            iv_ava = itemView.findViewById(R.id.iv_ava);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_numLike = itemView.findViewById(R.id.tv_numLike);
            tv_numComment = itemView.findViewById(R.id.tv_numComment);
            iv_like = itemView.findViewById(R.id.iv_like);
            iv_comment = itemView.findViewById(R.id.iv_comment);
            lnl_layout = itemView.findViewById(R.id.lnl_layout);

            rl_comment = itemView.findViewById(R.id.rl_comment);
            rl_like = itemView.findViewById(R.id.rl_like);
            iv_1 = itemView.findViewById(R.id.iv_1);
            iv_2 = itemView.findViewById(R.id.iv_2);
            iv_3 = itemView.findViewById(R.id.iv_3);
            iv_transparent = itemView.findViewById(R.id.iv_transparent);

            lnl_more = itemView.findViewById(R.id.lnl_more);
            rlt_more = itemView.findViewById(R.id.rlt_more);
            tv_countImage = itemView.findViewById(R.id.tv_count);


            //setup recycleview
//            rv_image.setRequestedColumnCount(3);
//            rv_image.setDebugging(true);
//            rv_image.setRequestedHorizontalSpacing(Utils.dpToPx(context, 3));
//            rv_image.addItemDecoration(
//                    new SpacesItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.recycler_padding)));


        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("CheckResult")
        @Override
        public void onBind(int position) {
            NewFeed newFeed = list_data.get(position);
            tv_content.setText(newFeed.content);
            tv_name.setText(newFeed.user_name);
            tv_time.setText(newFeed.create_at);
            tv_numComment.setText(String.valueOf(newFeed.num_comment));
            tv_numLike.setText(String.valueOf(newFeed.num_like));

            if (newFeed.isLike) {
                iv_like.setBackground(context.getDrawable(R.drawable.icon_like_clicked));
            } else {
                iv_like.setBackground(context.getDrawable(R.drawable.icon_like));

            }


            List<ItemImage> listImage = new ArrayList<>();
            int id_img = 0;
            int currOffset = -1;
            boolean isCol2Avail = false;

            for (String s : list_data.get(position).list_image) {
                ItemImage image = new ItemImage(id_img++, s, s);

                int colSpan1 = Math.random() < 0.2f ? 2 : 1;
                int rowSpan1 = colSpan1;
                if (colSpan1 == 2 && !isCol2Avail)
                    isCol2Avail = true;
                else if (colSpan1 == 1 && isCol2Avail)
                    colSpan1 = 1;
                image.setColumnSpan(colSpan1);
                image.setRowSpan(rowSpan1);
                image.setPosition(currOffset++);
                listImage.add(image);
            }

            //
            setUpImage();
            //set up adapter image
            setUpOnClick();

            ImageAdapter imageAdapter = new ImageAdapter(listImage, 6, listImage.size());

//            rv_image.setAdapter(new AsymmetricRecyclerViewAdapter<>(context, rv_image, imageAdapter));


            //image
            Picasso.get()
                    .load(newFeed.user_url)
                    .placeholder(R.drawable.user_no_img)
                    .error(R.drawable.user_no_img)
                    .into(iv_ava);

            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newFeed.isLike = !newFeed.isLike;
                    if (newFeed.isLike) {
                        iv_like.setBackground(context.getDrawable(R.drawable.icon_like_clicked));
                        newFeed.num_like++;
                        tv_numLike.setText(String.valueOf(newFeed.num_like));
                    } else {
                        iv_like.setBackground(context.getDrawable(R.drawable.icon_like));
                        newFeed.num_like--;
                        tv_numLike.setText(String.valueOf(newFeed.num_like));

                    }
                    listener.onClickLike(position, newFeed.id);
                }
            });

            rl_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newFeed.isLike = !newFeed.isLike;
                    if (newFeed.isLike) {
                        iv_like.setBackground(context.getDrawable(R.drawable.icon_like_clicked));
                        newFeed.num_like++;
                        tv_numLike.setText(String.valueOf(newFeed.num_like));
                    } else {
                        iv_like.setBackground(context.getDrawable(R.drawable.icon_like));
                        newFeed.num_like--;
                        tv_numLike.setText(String.valueOf(newFeed.num_like));

                    }
                    listener.onClickLike(position, newFeed.id);
                }
            });

            rl_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickComment(position, newFeed.id);
                }
            });


            iv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickComment(position, newFeed.id);
                }
            });

            tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickViewContent(position, newFeed.id);
                }
            });
        }

        private void setUpOnClick() {
            iv_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickViewImage(getAdapterPosition(), 0,list_data.get(getAdapterPosition()).id);
                }
            });

            iv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickViewImage(getAdapterPosition(), 1,list_data.get(getAdapterPosition()).id);

                }
            });

            iv_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickViewImage(getAdapterPosition(), 2,list_data.get(getAdapterPosition()).id);
                }
            });

            iv_transparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickViewImage(getAdapterPosition(), 2,list_data.get(2).id);

                }
            });
        }

        private void setUpImage() {
            List<String> list_image = list_data.get(getAdapterPosition()).list_image;
            int size_img = list_image.size();

            if (size_img == 1) {
                lnl_more.setVisibility(View.GONE);
                Picasso.get()
                        .load(list_image.get(0))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_1);
            } else if (size_img == 2) {
                lnl_layout.setOrientation(LinearLayout.VERTICAL);
                lnl_more.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params_iv1 = (LinearLayout.LayoutParams) iv_1.getLayoutParams();
                params_iv1.setMargins(0,0,0,5);
                iv_1.setLayoutParams(params_iv1);
                iv_3.setVisibility(View.GONE);
                iv_transparent.setVisibility(View.GONE);
                tv_countImage.setVisibility(View.GONE);
                Picasso.get()
                        .load(list_image.get(0))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_1);

                Picasso.get()
                        .load(list_image.get(1))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_2);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_2.getLayoutParams();
                params.setMargins(1,0,0,0);
                iv_2.setLayoutParams(params);

            } else if (size_img == 3) {
                lnl_more.setVisibility(View.VISIBLE);
                iv_transparent.setVisibility(View.GONE);
                tv_countImage.setVisibility(View.GONE);
                Picasso.get()
                        .load(list_image.get(0))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_1);

                Picasso.get()
                        .load(list_image.get(1))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_2);

                Picasso.get()
                        .load(list_image.get(2))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_3);

            } else if (size_img > 3) {
                lnl_more.setVisibility(View.VISIBLE);
                iv_transparent.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(list_image.get(0))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_1);

                Picasso.get()
                        .load(list_image.get(1))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_2);

                Picasso.get()
                        .load(list_image.get(2))
                        .placeholder(R.drawable.noimg)
                        .error(R.drawable.noimg)
                        .into(iv_3);

                ViewGroup.LayoutParams layoutParams = iv_3.getLayoutParams();

                iv_transparent.setLayoutParams(layoutParams);
                tv_countImage.setText(("+ " + (size_img - 3)));
//                rlt_more.setBackgroundColor(context.getResources().getColor());
            }
        }

    }
}

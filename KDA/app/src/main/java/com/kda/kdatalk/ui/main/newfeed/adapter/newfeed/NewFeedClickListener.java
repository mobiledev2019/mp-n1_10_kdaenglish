package com.kda.kdatalk.ui.main.newfeed.adapter.newfeed;

public interface NewFeedClickListener {
    void onClickLike(int position, String id_feed);
    void onClickComment(int position, String id_feed);
    void onClickViewContent(int position, String id_feed);
    void onClickViewImage(int position_item, int position_img, String id_feed);
}

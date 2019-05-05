package com.kda.kdatalk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public class ItemImage implements AsymmetricItem {

    private int ItemImageId;
    private String ImagePath;
    private String Thumb;
    int colSpan;
    int rowSpan;

    public ItemImage(int itemImageId, String imagePath, String thumb, int colSpan, int rowSpan) {
        ItemImageId = itemImageId;
        ImagePath = imagePath;
        Thumb = thumb;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public ItemImage(int itemImageId, String imagePath, String thumb) {
        ItemImageId = itemImageId;
        ImagePath = imagePath;
        Thumb = thumb;
    }

    public int getItemImageId() {
        return ItemImageId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getThumb() {
        return Thumb;
    }

    public void setItemImageId(int itemImageId) {
        ItemImageId = itemImageId;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public void setThumb(String thumb) {
        Thumb = thumb;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    @Override
    public int getColumnSpan() {
        return colSpan;
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ItemImageId);
        dest.writeString(ImagePath);
        dest.writeString(Thumb);

    }


}

package com.kda.kdatalk.ui.main.newfeed.addnewfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.kda.kdatalk.ui.base.ActivityBase;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddNewFeedPresenterImpl implements AddNewFeedPresenter {

    AddNewFeedView newFeedView;
    ActivityBase mContext;

    public AddNewFeedPresenterImpl(AddNewFeedView newFeedView, ActivityBase mContext) {
        this.newFeedView = newFeedView;
        this.mContext = mContext;
    }

    @Override
    public String convertImageToByte(String link) {

        String imgString = "";
        try {
            File image = new File(link);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
            imgString = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
            // dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgString;
    }
}

package com.kda.kdatalk.ui.main.newfeed.addnewfeed;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.ui.base.ActivityBase;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewFeedPresenterImpl implements AddNewFeedPresenter {

    private static final String TAG = AddNewFeedPresenterImpl.class.getSimpleName();
    AddNewFeedView newFeedView;
    ActivityBase mContext;

    ServiceFunction serviceFunction;

    public AddNewFeedPresenterImpl(AddNewFeedView newFeedView, ActivityBase mContext) {
        this.newFeedView = newFeedView;
        this.mContext = mContext;
        this.serviceFunction = APIUtils.getAPIService();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public String convertImageToByte(String link) {
        Uri uri = Uri.parse(link);


        // Will return "image:x*"
        String wholeID = DocumentsContract.getDocumentId(uri);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = mContext.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();



        String imgString = "";
        try {
            File image = new File(filePath);
            Log.e(TAG, "convertImageToByte: " + filePath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
            imgString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            // dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = "data:image/jpeg;base64," + imgString;
        Log.e(TAG, "convertImageToByte: base64-> "  + result);

        return result;
    }




    @Override
    public void addNewFeed(JSONObject data, String token) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data.toString());


        serviceFunction.addNewFeed(token, requestBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse: " + response.body());
                if (response.isSuccessful() && response.code() == 200) {
                    newFeedView.onAddSuccess(true, "");
                    // success
                } else {
                    // failed
                    newFeedView.onAddSuccess(false, response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                newFeedView.onAddSuccess(false, t.getMessage());

            }
        });

    }
}

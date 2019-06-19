package com.kda.kdatalk.ui.main.profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.kda.kdatalk.R;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.login.LoginActivity;
import com.kda.kdatalk.ui.widget.ProgressView;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;
import com.kda.kdatalk.utils.UtilLibs;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends FragmentBase implements ProfileView {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.progress_bar)
    ProgressView progress_bar;

    @BindView(R.id.iv_cover)
    ImageView iv_cover;

    @BindView(R.id.civ_profile)
    CircleImageView civ_profile;

    @BindView(R.id.tie_phone)
    TextInputEditText tie_phone;


    @BindView(R.id.tie_email)
    TextInputEditText tie_email;

    @BindView(R.id.scv_main)
    ScrollView scv_main;

    @BindView(R.id.tie_name)
    TextInputEditText tie_name;

    private static final int PERMISSION_REQUEST_CAMERA = 100;
    View viewRoot;
    private Context mContext;
    ProfilePresenter profilePresenter;
    User user;

    boolean isAva = false;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        profilePresenter = new ProfilePresenterImpl(mContext, this);
        profilePresenter.setFragment(this);
        if (!MyCache.getInstance().getString(DraffKey.user_info).isEmpty()) {
            user = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user_info), User.class);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
        } catch (Exception e) {

        }
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scv_main.setVisibility(View.GONE);
        initData();

        showProgress(true);
        profilePresenter.getDetailUser(user._id);

    }

    private void initData() {
        if (user != null) {

            if (user.url_img_ava.isEmpty()) {
                user.url_img_ava = "null";
            }

            if (user.url_img_cover.isEmpty()) {
                user.url_img_cover = "null";
            }


            Picasso.get().load(user.url_img_cover)
                    .placeholder(R.drawable.noimg)
                    .error(R.drawable.noimg)
                    .into(iv_cover);

            Picasso.get().load(user.getUrl_img_ava())
                    .placeholder(R.drawable.user_no_img)
                    .error(R.drawable.user_no_img)
                    .into(civ_profile);

            tv_name.setText(user.name);

            tie_email.setText(user.email);

            tie_name.setText(user.name);

            tie_phone.setText(user.address);

        }
    }

    @OnClick({R.id.lnl_changeCover, R.id.civ_profile})
    public void ClickView(View v) {
        switch (v.getId()) {
            case R.id.lnl_changeCover:
                showPopUp();
                isAva = false;
                break;


            case R.id.civ_profile:
                showPopUp();
                isAva = true;

                break;

            default:
                break;
        }
    }


    private void showPopUp() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(R.layout.layout_popup_choice_image);

//        dialog.setContentView(R.layout.layout_popup_change_ava);
        TextView tv_title = dialog.findViewById(R.id.tv_title_change_ava);
        LinearLayout lnl_choice = dialog.findViewById(R.id.lnl_choice);
        LinearLayout lnl_chup_anh = dialog.findViewById(R.id.lnl_chup_anh);
        tv_title.setText("Phương thức chọn ảnh");

        LinearLayout bt_huy = dialog.findViewById(R.id.lnl_huy);

        bt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        lnl_choice.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: " + "choose image");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);

                dialog.dismiss();

                // need change log --> getUserOrAdmin
            }
        });

        lnl_chup_anh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: " + "take photo");
                if (!checkPermission()) {
                    requestPermission(PERMISSION_REQUEST_CAMERA);
                } else
                    take_Photo();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext.getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(mContext.getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission(int PERMISSION_REQUEST_CODE) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeAccepted && cameraAccepted)
                        take_Photo();
                    else {
                        Toast.makeText(mContext, "Bạn sẽ không dùng đươc chức năng này nếu không cho phép", Toast.LENGTH_SHORT).show();

                    }
                }

                break;
        }
    }



    public String photoFileName = "photo.jpg";
    File photoFile;
    String mCurrentPhotoPath;

    private void take_Photo() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = getPhotoFileUri(photoFileName);
                Uri fileProvider = FileProvider.getUriForFile(mContext, "com.kda.kdatalk", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, 1);
            }
        } catch (Exception ex) {
            String et = ex.toString();
        }
    }


    private File getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(
                    mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
            }
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            mCurrentPhotoPath = file.getAbsolutePath();
            return file;
        }
        return null;
    }


    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    String url_ava = "";
    String url_cover = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = mContext.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();

                    if (isAva) {
                        url_ava = picturePath;
                    } else {
                        url_cover = picturePath;
                    }

                    // -> upload image to server
                    showProgress(true);

                    profilePresenter.uploadImageToServer(picturePath,isAva);

                    break;

                case 1:
                    File f;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri imageUri = Uri.parse(mCurrentPhotoPath);
                        f = new File(imageUri.getPath());
                    } else {
                        f = new File(Environment.getExternalStorageDirectory().toString());
                        for (File temp : f.listFiles()) {
                            if (temp.getName().equals("photo.jpg")) {
                                f = temp;
                                break;
                            }
                        }
                    }

                    String uri = f.getAbsolutePath();

                    showProgress(true);

                    profilePresenter.uploadImageToServer(uri,isAva);

                    Log.d(TAG, "onActivityResult: " + uri);

                    // upload image to sv
                    //

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onUploadSuccess(boolean isSuccess, String message) {
        showProgress(false);



        if (isSuccess) {
//            if (isAva) {
//                Picasso.get().load(url_ava)
//                        .placeholder(R.drawable.user_no_img)
//                        .error(R.drawable.user_no_img)
//                        .into(civ_profile);
//            } else {
//                Picasso.get().load(url_cover)
//                        .placeholder(R.drawable.noimg)
//                        .error(R.drawable.noimg)
//                        .into(iv_cover);
//            }

            profilePresenter.getDetailUser(user._id);
        } else {
            UtilLibs.showAlert(mContext,message);
        }

    }

    @Override
    public void onGetDetailSuccess(User user) {
        scv_main.setVisibility(View.VISIBLE);

        showProgress(false);

        if (user!= null) {
            //save data
            MyCache.getInstance().putString(DraffKey.user_info, MyGson.getInstance().toJson(user));

            this.user = user;

            // reload data
            initData();
            // success
        } else {
            UtilLibs.showAlert(mContext,"Đã có lỗi xảy ra, vui lòng thử lại sau!");
            // failed
        }

    }


    @OnClick(R.id.bt_logout)
    public void logout() {
        UtilLibs.showAlert(mContext, "Bạn có chắc chắn muốn đăng xuất?", new UtilLibs.ListenerAlert() {
            @Override
            public void cancel() {

            }

            @Override
            public void agree() {
                MyCache.getInstance().clearAll();

                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);

                getActivity().finish();

            }
        });
    }
    private void showProgress(boolean isShow) {
        if (isShow) {
            progress_bar.setVisibility(View.VISIBLE);


        } else {
            progress_bar.setVisibility(View.GONE);

        }
    }


}

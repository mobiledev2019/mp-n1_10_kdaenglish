package com.kda.kdatalk.ui.login.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.login.LoginActivity;
import com.kda.kdatalk.ui.login.LoginPresenter;
import com.kda.kdatalk.ui.login.RegisterFragmentView;
import com.kda.kdatalk.ui.main.MainActivity;
import com.kda.kdatalk.utils.UtilLibs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends FragmentBase implements RegisterFragmentView {

    private static final String TAG = RegisterFragment.class.getSimpleName();
    View viewMain;

    ActivityBase mContext;
    //btnBack

    @BindView(R.id.tv_name)
    TextInputEditText tv_name;

    @BindView(R.id.tv_phone)
    TextInputEditText tv_phone;

    @BindView(R.id.tv_email)
    TextInputEditText tv_email;

    @BindView(R.id.tv_username)
    TextInputEditText tv_username;

    @BindView(R.id.tv_password)
    TextInputEditText tv_password;


    @BindView(R.id.tv_confirm_password)
    TextInputEditText tv_confirm_password;

    LoginPresenter loginPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (ActivityBase) context;
    }

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewMain = super.onCreateView(inflater, container, savedInstanceState);

        try {
            ButterKnife.bind(viewMain);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewMain;
    }

    @OnClick({R.id.btnBack, R.id.btnRegister})
    public void ClickView(View v) {

        hideKeyboard(mContext);

        switch (v.getId()) {
            case R.id.btnBack:

                mContext.onBackPressed();

                break;

            case R.id.btnRegister:

                if (validate()) {
                    JSONObject o = new JSONObject();

                    try {
                        o.put("name", tv_username.getText().toString().trim());
                        o.put("password", tv_password.getText().toString().trim());
//                        o.put("full_name", tv_name.getText().toString().trim());
//                        o.put("phone", tv_phone.getText().toString().trim());
                        o.put("email", tv_email.getText().toString().trim());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e(TAG, "ClickView:DATA SEND " + o.toString() );

                    if (mContext.isNetworkConnected()) {

                        loginPresenter.register(o);
                    } else {
                        // show dialog
                        UtilLibs.showAlert(mContext, "Không có kết nối Internet, vui lòng kiểm tra lại!");
                    }

                }



                break;

            default:
                break;
        }

    }

    private boolean validate() {
        if (tv_email.getText().toString().trim().isEmpty() || tv_confirm_password.getText().toString().trim().isEmpty()
                || tv_name.getText().toString().isEmpty() || tv_password.getText().toString().trim().isEmpty()
                || tv_phone.getText().toString().trim().isEmpty() || tv_username.getText().toString().trim().isEmpty()) {

            UtilLibs.showAlert(mContext, "Bạn cần nhập đầy đủ các trường thông tin!");
//            Toast.makeText(mContext, "Bạn cần nhập đầy đủ các trường thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!tv_password.getText().toString().trim().equals(tv_confirm_password.getText().toString().trim())) {
            UtilLibs.showAlert(mContext, "Mật khẩu không khớp!");
//            Toast.makeText(mContext, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }


    private void init() {
        loginPresenter = ((LoginActivity) mContext).getLoginPresenter();
        loginPresenter.setFragment(this);
    }


    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_register;
    }

    @Override
    public void onRegisterSuccess(boolean isSuccess, String message) {


        if (isSuccess) {
            Toast.makeText(mContext, "Đăng kí thành công", Toast.LENGTH_SHORT).show();

            loginPresenter.loginByServer(tv_username.getText().toString().trim(), tv_password.getText().toString().trim());
            // nav to ...
        } else {
            UtilLibs.showAlert(mContext,message);
        }

    }

    @Override
    public void onLoginSuccess(boolean isSuccess, String message) {

        if (isSuccess) {
            navToMain();
        } else {
            UtilLibs.showAlert(mContext,message);

        }

    }


    private void navToMain() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        mContext.finish();
    }
}

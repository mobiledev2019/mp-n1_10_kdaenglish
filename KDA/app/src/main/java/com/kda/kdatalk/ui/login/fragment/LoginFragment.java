package com.kda.kdatalk.ui.login.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.login.LoginActivity;
import com.kda.kdatalk.ui.login.LoginFragmentView;
import com.kda.kdatalk.ui.login.LoginPresenter;
import com.kda.kdatalk.ui.login.LoginView;
import com.kda.kdatalk.ui.main.MainActivity;
import com.kda.kdatalk.ui.widget.ProgressView;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.UtilLibs;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends FragmentBase implements LoginFragmentView {

    private static final String TAG = LoginFragment.class.getSimpleName();
    View viewRoot;
//
//    @BindView(R.id.ibFbLogin)
//    ImageButton ib_fbLogin;

    @BindView(R.id.tv_register)
    TextView tv_register;

    @BindView(R.id.etEmail)
    TextInputEditText etEmail;

    @BindView(R.id.etPassword)
    TextInputEditText etPassword;

    @BindView(R.id.progress_bar)
    ProgressView progress_bar;

    @BindView(R.id.activityMain)
    RelativeLayout relativeLayout;



    LoginPresenter loginPresenter;
    private ActivityBase mContext;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (ActivityBase) context;
    }

    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
//        @drawable/bg_design
//        Picasso.get().load(R.drawable.bg_design).into(relativeLayout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewRoot;
    }


    @OnClick({R.id.tv_register, R.id.btnServerLogin})
    public void ClickView(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                addFragment(R.id.viewMain,RegisterFragment.newInstance(),true);
                break;

            case R.id.btnServerLogin:
                if (etEmail.getText().toString().trim().isEmpty() || etPassword.getText().toString().trim().isEmpty()) {
                    // show dialog

                    UtilLibs.showAlert(mContext,"Bạn chưa nhập thông tin đăng nhập");

                } else {

                    loginPresenter.loginByServer(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                }
                break;
            default:
                break;
        }
    }



    private void init() {
        loginPresenter = ((LoginActivity) mContext).getLoginPresenter();
        loginPresenter.setFragment(this);
    }

    @Override
    public void onSuccess() {
        //-> save data -> main
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        mContext.finish();
    }

    @Override
    public void onFailed(String err) {
        // show err
        Log.e(TAG, "onFailed: " + err);
    }
}

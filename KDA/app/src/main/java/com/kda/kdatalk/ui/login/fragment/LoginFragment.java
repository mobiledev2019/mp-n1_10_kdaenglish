package com.kda.kdatalk.ui.login.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.login.LoginActivity;
import com.kda.kdatalk.ui.login.LoginFragmentView;
import com.kda.kdatalk.ui.login.LoginPresenter;
import com.kda.kdatalk.ui.login.LoginView;
import com.kda.kdatalk.ui.main.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends FragmentBase implements LoginFragmentView {

    private static final String TAG = LoginFragment.class.getSimpleName();
    View viewRoot;

    @BindView(R.id.ibFbLogin)
    ImageButton ib_fbLogin;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return viewRoot;
    }

    @OnClick({R.id.ibGoogleLogin, R.id.ibFbLogin, R.id.btnServerLogin})
    public void login(View v) {
        switch (v.getId()) {
            case R.id.ibGoogleLogin:
                loginPresenter.loginByGoogle();
                break;

            case R.id.ibFbLogin:
                ib_fbLogin.performClick();
                loginPresenter.loginByFaceBook();
                break;

            case R.id.btnServerLogin:
                loginPresenter.loginByServer();
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
        Intent intent  = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        mContext.finish();
    }

    @Override
    public void onFailed(String err) {
        // show err
        Log.e(TAG, "onFailed: " + err);
    }
}

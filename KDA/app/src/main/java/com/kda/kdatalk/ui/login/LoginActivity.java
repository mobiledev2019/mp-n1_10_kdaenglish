package com.kda.kdatalk.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.kda.kdatalk.R;
import com.kda.kdatalk.network.RetrofitClient;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.login.fragment.LoginFragment;
import com.kda.kdatalk.ui.main.MainActivity;
import com.kda.kdatalk.ui.widget.ProgressView;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.UtilLibs;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kda.kdatalk.network.ServiceConst.URL_SERVER_KDA;

public class LoginActivity extends ActivityBase implements LoginView {
//    private static final int RC_SIGN_IN = 10;
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.iv_splash)
    ImageView iv_splash;

    @BindView(R.id.progressBar)
    ProgressView progressBar;

    private LoginPresenter loginPresenter;
    private CallbackManager callbackManager;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        setUpGoogle();

        callbackManager = CallbackManager.Factory.create();

        loginPresenter = new LoginPresenterImpl(this, this, callbackManager);

//        changeFragment(R.id.fragment, LoginFragment.newInstance(), false);

        if (MyCache.getInstance().contains(DraffKey.user_login)) {

            showProgress();

            if (isNetworkConnected()) {

                loginPresenter.loginByServer();
            } else {
                UtilLibs.showAlert(this, "Không có kết nối Internet, vui lòng kiểm tra lại!");
                changeFragment(R.id.fragment, LoginFragment.newInstance(), false);

                YoYo.with(Techniques.FadeOutDown).duration(500).playOn(this.findViewById(R.id.iv_splash));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.this.findViewById(R.id.iv_splash).setVisibility(View.GONE);
                    }
                }, 500);
            }

            // have data login



            // -> get to login
        } else {
            changeFragment(R.id.fragment, LoginFragment.newInstance(), false);

            YoYo.with(Techniques.FadeOutDown).duration(500).playOn(this.findViewById(R.id.iv_splash));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.this.findViewById(R.id.iv_splash).setVisibility(View.GONE);
                }
            }, 500);

        }

    }


    public LoginPresenter getLoginPresenter() {
        return loginPresenter;
    }

    @Override
    public void onBackPressed() {
        try {
            hideKeyboard(this);
        } catch (Exception e) {

        }

        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            try {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public void showProgress() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void hideProgress() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

            }
        });

    }


    @Override
    public void onError(String err) {

        UtilLibs.showAlert(this, err);

        changeFragment(R.id.fragment, LoginFragment.newInstance(), false);

        YoYo.with(Techniques.FadeOutDown).duration(500).playOn(this.findViewById(R.id.iv_splash));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity.this.findViewById(R.id.iv_splash).setVisibility(View.GONE);
            }
        }, 500);
    }

    @Override
    public void onLoginSuccess(boolean isSuccess) {

        if (isSuccess) {
            // login success

            navToMain();
        } else {

            changeFragment(R.id.fragment, LoginFragment.newInstance(), false);

            YoYo.with(Techniques.FadeOutDown).duration(500).playOn(this.findViewById(R.id.iv_splash));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.this.findViewById(R.id.iv_splash).setVisibility(View.GONE);
                }
            }, 500);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult: " + data);
    }

    private void navToMain() {
//        RetrofitClient.addHeaderRetrofit(URL_SERVER_KDA);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

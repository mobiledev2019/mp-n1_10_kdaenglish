package com.kda.kdatalk.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kda.kdatalk.R;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.login.fragment.LoginFragment;
import com.kda.kdatalk.ui.main.MainActivity;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;
import com.kda.kdatalk.utils.UtilLibs;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends ActivityBase implements LoginView {
    private static final int RC_SIGN_IN = 10;
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.iv_splash)
    ImageView iv_splash;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private LoginPresenter loginPresenter;
    private CallbackManager callbackManager;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setUpGoogle();

        callbackManager = CallbackManager.Factory.create();

        loginPresenter = new LoginPresenterImpl(this, this, callbackManager);

        changeFragment(R.id.fragment, LoginFragment.newInstance(), false);

        YoYo.with(Techniques.FadeOutDown).duration(500).playOn(this.findViewById(R.id.iv_splash));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity.this.findViewById(R.id.iv_splash).setVisibility(View.GONE);
            }
        }, 500);

//        ArrayList<String> result = UtilLibs.compare2String("day la string 1 ahihi docngok", "kia la string 2 vai");
//        Log.e(TAG, "onCreate: " + result.get(0));
//        Log.e(TAG, "onCreate: " + result.get(1));

    }

    private void setUpGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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

        progressBar.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgress() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onError(String err) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult: " + data);

        if (requestCode == RC_SIGN_IN) {
            showProgress();
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                User user = new User();
                user.setName(account.getDisplayName());
                user.setUrl_img_ava(String.valueOf(account.getPhotoUrl()));
                user.setStr_id(account.getId());
                user.setLOGIN_MODE(AppConstants.MODE_GOOGLE);
                user.setAccessToken(account.getIdToken());

                MyCache.getInstance().putString(DraffKey.user, MyGson.getInstance().toJson(user));
                Toast.makeText(this, "success-google", Toast.LENGTH_SHORT).show();
                navToMain();
            }

            hideProgress();


            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LoginActivity.class.getSimpleName(), "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void navToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

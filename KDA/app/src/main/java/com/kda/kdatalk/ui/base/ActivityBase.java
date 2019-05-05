package com.kda.kdatalk.ui.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kda.kdatalk.ui.login.LoginActivity;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;

import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ActivityBase extends AppCompatActivity {

    private static final String TAG = ActivityBase.class.getName();
    public static boolean isAppWentToBg = false;

    public static boolean isWindowFocused = false;

    public static boolean isMenuOpened = false;

    public static boolean isBackPressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationWillEnterForeground();
        registerBroadcastReceiver();
    }

    private void applicationWillEnterForeground() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
            Log.e(TAG, "applicationWillEnterForeground: ");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e(TAG, "onStop ");
        applicationdidenterbackground();

    }

    public void applicationdidenterbackground() {
        if (!isWindowFocused) {
            isAppWentToBg = true;
            Log.e(TAG, "applicationdidenterbackground: ");
//            Log.e(TAG, "applicationdidenterbackground: " + "StopService SocketsUpdateService");
        }
    }

    @Override
    public void onBackPressed() {
//        if (this instanceof HomeActivity) {
//
//        } else {
//            isBackPressed = true;
//        }
//
//        try {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//        }catch (Exception e) {
//
//        }

//        Log.d(TAG,
//                "onBackPressed " + isBackPressed + ""
//                        + this.getLocalClassName());

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        isWindowFocused = hasFocus;

        if (isBackPressed && !hasFocus) {
            isBackPressed = false;
            isWindowFocused = true;
        }

        super.onWindowFocusChanged(hasFocus);
    }

    public User getUserCache() {
        try {

            return new Gson().fromJson(MyCache.getInstance().getString(DraffKey.user), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new User();
    }

    //
    public void changeFragment(int viewContainer, Fragment fragment, boolean isAddToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(viewContainer, fragment);
        ft.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (isAddToBackStack) ft.addToBackStack(null);
        ft.commit();
    }

    protected void addFragment(int viewContainer, Fragment fragment, boolean isAddToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(viewContainer, fragment);
        ft.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (isAddToBackStack) ft.addToBackStack(null);
        ft.commit();
    }

    public void logout() {
        MyCache.getInstance().clearAll();
        Intent intent = new Intent(ActivityBase.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        ActivityBase.this.finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            Log.e("Service", "isMyServiceRunning: " + service.service.getClassName());
            if ("com.ppship.ppship.pps.service.SocketsUpdateService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void registerBroadcastReceiver() {

//        final IntentFilter theFilter = new IntentFilter();
//        /** System Defined Broadcast */
//        theFilter.addAction(Intent.ACTION_SCREEN_ON);
//        theFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        theFilter.addAction(Intent.ACTION_USER_PRESENT);
//
//        BroadcastReceiver screenOnOffReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String strAction = intent.getAction();
//
//                KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//                if (strAction.equals(Intent.ACTION_USER_PRESENT) || strAction.equals(Intent.ACTION_SCREEN_OFF) || strAction.equals(Intent.ACTION_SCREEN_ON))
//                    if (myKM.inKeyguardRestrictedInputMode()) {
//                        stopService(new Intent(ActivityBase.this, SocketsUpdateService.class));
//                        Log.e(TAG, "onReceive: " + "LOCKED");
//
//                    } else {
//                        Log.e(TAG, "onReceive: " + "UNLOCKED");
//                    }
//
//            }
//        };
//
//        try {
//            getApplicationContext().registerReceiver(screenOnOffReceiver, theFilter);
//        } catch (Exception e) {
//
//        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

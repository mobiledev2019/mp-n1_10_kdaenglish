package com.kda.kdatalk.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.kda.kdatalk.ui.login.LoginActivity;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;

import com.google.gson.Gson;
//import com.pps.pickpackship.R;
//import com.pps.pickpackship.model.User;
//import com.pps.pickpackship.utils.CacheData;
//import com.pps.pickpackship.utils.Contranst;
//import com.pps.pickpackship.utils.LogError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

/**
 * Created by AIB on 9/28/2017.
 */

public abstract class FragmentBase extends Fragment {
    private static final String TAG = FragmentBase.class.getName();

    @NonNull
    protected abstract int getContentView();
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(getContentView(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public User getUserCache(){
        try{
            return new Gson().fromJson(MyCache.getInstance().getString(DraffKey.user_info),User.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new User();
    }

    protected void changeFragment(int viewContainer ,Fragment fragment, boolean isAddToBackStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(viewContainer, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (isAddToBackStack) ft.addToBackStack(null);
        ft.commit();
    }

    protected void addFragment(int viewContainer ,Fragment fragment, boolean isAddToBackStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(viewContainer, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (isAddToBackStack) ft.addToBackStack(null);
        ft.commit();
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

    public static void logout(Activity activity) {
        MyCache.getInstance().clearAll();

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();

    }



    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void log_outAPI(Activity context, ProgressBar progressBar) {
//        if (!UtilLibs.isNetworkConnect(context)) {
//            Toast.makeText(context, R.string.lbl_disconnect_network, Toast.LENGTH_SHORT).show();
//        } else {
//
//        }
    }
}

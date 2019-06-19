package com.kda.kdatalk.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kda.kdatalk.R;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.socket.SocketUpdateService;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.main.learn.fragment.LearnFragment;
import com.kda.kdatalk.ui.main.message.fragment.MessageFragment;
import com.kda.kdatalk.ui.main.newfeed.fragment.FragmentNewFeed;
import com.kda.kdatalk.ui.main.notification.NotiFragment;
import com.kda.kdatalk.ui.main.profile.ProfileFragment;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kda.kdatalk.utils.AppConstants.NEW_MSG;

public class MainActivity extends ActivityBase implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    //    @BindView(R.id.nav_bar)
//    BottomNavigationView nav_bar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    ServiceFunction serviceFunction;

    MenuItem prevMenuItem;

    @BindView(R.id.ah_bottom_nav)
    AHBottomNavigation bottom_nav_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        IntentFilter filter = new IntentFilter();
        filter.addAction(NEW_MSG);

        registerReceiver(broadcastReceiver, filter);

        setUpNavBar();

        setUpViewPager();

        getnumNewNoti();

        Intent intent = new Intent(this, SocketUpdateService.class);
        startService(intent);
    }

    private void getnumNewNoti() {
        serviceFunction = APIUtils.getAPIService();

        String accessToken = MyCache.getInstance().getString(DraffKey.accessToken);


//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        serviceFunction.getNumNoti(accessToken).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "onResponse: getnumNewNoti " + response.body());

                try {
                    JSONObject main = new JSONObject(response.body());

                    curr_newNoti = main.getInt("num_noti");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bottom_nav_bar.setNotification(String.valueOf(curr_newNoti), 2);
                        }
                    });




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: getnumNewNoti" + t.getMessage() );

            }
        });


    }

    int curr_newNoti = 0;
    int curr_newmess = 0;

    private void setUpNavBar() {
        AHBottomNavigationItem newfeed = new AHBottomNavigationItem("New Feed", R.drawable.icon_newfeeds, R.color.blue);
        AHBottomNavigationItem mess = new AHBottomNavigationItem("Message", R.drawable.icon_message, R.color.black);
        AHBottomNavigationItem noti = new AHBottomNavigationItem("Notification", R.drawable.icon_noti, R.color.green);
        AHBottomNavigationItem learn = new AHBottomNavigationItem("Learn", R.drawable.icon_learn, R.color.orange);
        AHBottomNavigationItem profile = new AHBottomNavigationItem("Profile", R.drawable.icon_profile, R.color.white);

        bottom_nav_bar.addItem(newfeed);
        bottom_nav_bar.addItem(mess);
        bottom_nav_bar.addItem(noti);
        bottom_nav_bar.addItem(learn);
        bottom_nav_bar.addItem(profile);
        bottom_nav_bar.setBehaviorTranslationEnabled(false);
        bottom_nav_bar.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
        bottom_nav_bar.setAccentColor(Color.parseColor("#000000"));
        bottom_nav_bar.setForceTint(true);

        //
//        bottom_nav_bar.setColored(true);
        bottom_nav_bar.setTranslucentNavigationEnabled(true);

        bottom_nav_bar.setSelectedBackgroundVisible(false);
        bottom_nav_bar.setCurrentItem(0);
//        bottom_nav_bar.setInactiveColor(Color.parseColor("#747474"));

        bottom_nav_bar.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottom_nav_bar.setNotification("", 2);
        bottom_nav_bar.setNotificationBackgroundColor(Color.parseColor("#FF3000"));

        bottom_nav_bar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        bottom_nav_bar.setNotification("", 1);
                        curr_newmess = 0;

                        break;
                    case 2:
                        // noti
                        NotiFragment.currNoti = 0;

                        viewPager.setCurrentItem(2);

                        bottom_nav_bar.setNotification("",2);
                        curr_newNoti = 0;
                        break;

                    case 3:

                        viewPager.setCurrentItem(3);

                        break;
                    case 4:

                        viewPager.setCurrentItem(4);

                        break;
                }

                return true;
            }
        });

    }
    private void setUpViewPager() {

        MainPagerAdapter viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        Fragment newfeed_fragment = FragmentNewFeed.newInstance();
        Fragment mess_fragment = MessageFragment.newInstance();
        Fragment noti_fragment = NotiFragment.newInstance();
        Fragment learn_fragment = LearnFragment.newInstance();
        Fragment profile_fragment = ProfileFragment.newInstance();

        viewPagerAdapter.addFragment(newfeed_fragment);
        viewPagerAdapter.addFragment(mess_fragment);
        viewPagerAdapter.addFragment(noti_fragment);
        viewPagerAdapter.addFragment(learn_fragment);
        viewPagerAdapter.addFragment(profile_fragment);

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    bottom_nav_bar.setCurrentItem(position);
                    Log.e("BOTTOM_NAVBAR", "onPageSelected: " + position);
                } catch (Exception e) {
                    bottom_nav_bar.setCurrentItem(0);
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_newfeed:
                viewPager.setCurrentItem(0);
                break;
            case R.id.item_message:
                viewPager.setCurrentItem(1);
                break;

            case R.id.item_search:
                viewPager.setCurrentItem(2);
                break;

            case R.id.item_learn:
                viewPager.setCurrentItem(3);
                break;

            case R.id.item_profile:
                viewPager.setCurrentItem(4);
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(NEW_MSG);

            registerReceiver(broadcastReceiver, filter);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {

        try {
            unregisterReceiver(broadcastReceiver);

        }catch (Exception e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String msg = intent.getStringExtra(NEW_MSG);
                if (msg.equals(AppConstants.SocketEvent.invalidToken)) {
                    // logout
                } else if (msg.equals(AppConstants.SocketEvent.newNotification)) {
                    curr_newNoti ++;
                    bottom_nav_bar.setNotification(String.valueOf(curr_newNoti), 2);

                    // update num noti



                } else if (msg.equals(AppConstants.SocketEvent.newMessage)) {
                    curr_newmess ++;
                    bottom_nav_bar.setNotification(String.valueOf(curr_newmess), 1);


                    // update num message

                }

            } catch (Exception e) {
                Log.e(TAG, "onReceive: " + e.getMessage() );
            }
        }
    };

    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this, SocketUpdateService.class));
        super.onDestroy();
    }
}

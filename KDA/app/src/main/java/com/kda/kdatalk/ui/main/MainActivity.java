package com.kda.kdatalk.ui.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.reflect.TypeToken;
import com.kda.kdatalk.R;
import com.kda.kdatalk.model.learn.LearnModel;
import com.kda.kdatalk.model.learn.LessonModel;
import com.kda.kdatalk.model.learn.VocabModel;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.socket.SocketUpdateService;
import com.kda.kdatalk.ui.base.ActivityBase;
import com.kda.kdatalk.ui.main.learn.fragment.LearnFragment;
import com.kda.kdatalk.ui.main.message.fragment.MessageFragment;
import com.kda.kdatalk.ui.main.newfeed.fragment.FragmentNewFeed;
import com.kda.kdatalk.ui.main.notification.NotiFragment;
import com.kda.kdatalk.ui.main.profile.ProfileFragment;
import com.kda.kdatalk.ui.widget.ProgressView;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    @BindView(R.id.progress_bar)
    ProgressView progress_bar;

    boolean isDraff = false;

    public static ArrayList<LearnModel> drafLesson = new ArrayList<>();

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel("com.kda.kdatalk", "Notification");
            startForegroundService(new Intent(MainActivity.this, SocketUpdateService.class));
        } else {
            startService(new Intent(MainActivity.this, SocketUpdateService.class));
        }

//        Intent intent = new Intent(this, SocketUpdateService.class);
//        startService(intent);


        // check draf in lesson

        if (!MyCache.getInstance().getString(DraffKey.lesson).isEmpty()) {

            Type type = new TypeToken<List<LearnModel>>() {
            }.getType();
            drafLesson = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.lesson), type);

            if (drafLesson == null) {
                drafLesson = new ArrayList<>();
            }

            isDraff = drafLesson.size() > 0;

            //                for (int i = 0; i < list_data.size(); i++) {
//                    if (list_data.get(i) != null) {
//                        if (player_request.getPlayer_code().equals(((SurveyDraff_P44) arr_data.get(i)).getPlayer_code())) {
//                            surveyDraffP44 = (SurveyDraff_P44) arr_data.get(i);
//                            position = i;
//                            isDraff = true;
//                            break;
//                        }
//                    }
//                }
        }

        if (!isDraff) {
            showProgress(true);

            getLessonData();
        }


//        getLessonData();


    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    private String createNotificationChannel(String channel_id, String channel_name){
//        NotificationChannel chan = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_NONE);
//
//        chan.setLightColor(Color.BLUE);
//        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        assert manager != null;
//        manager.createNotificationChannel(chan);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id);
//
//        return channelId
//    }

    private void showProgress(boolean isShow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    progress_bar.setVisibility(View.VISIBLE);
                } else {
                    progress_bar.setVisibility(View.GONE);

                }
            }
        });
    }

    private void getLessonData() {
        Random rand = new Random();
        String url = "http://35.247.180.113:4000/getLession";

        serviceFunction.getLearnModel(url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "onResponse:getLesson " + response.body());
                if (response.isSuccessful() && response.code() == 200) {

                    ArrayList<LearnModel> arr_learn = new ArrayList<>();

                    try {
                        JSONArray main = new JSONArray(response.body());
//                        JSONArray data = main.getJSONArray("data");

                        Log.e(TAG, "onResponse:LENGTH DATA LESSON->  " + main.length());

                        for (int i = 0; i < main.length(); i++) {
                            JSONObject ob = main.getJSONObject(i);
                            LearnModel model = new LearnModel();
                            model.id = ob.getString("id");
                            model.name = ob.getString("name");

                            ArrayList<LessonModel> list_lesson = new ArrayList<>();

                            JSONArray json_less = ob.getJSONArray("list_lession");

                            for (int j = 0; j < json_less.length(); j++) {
                                JSONObject less = json_less.getJSONObject(j);
                                LessonModel lessonModel = new LessonModel();
                                lessonModel.id = less.getString("id");
                                lessonModel.url_image_lesson = less.getString("url_image_lession");
                                lessonModel.name = less.getString("name");
                                lessonModel.difficult = less.getString("difficult");


                                // list vocab

                                ArrayList<VocabModel> listVocab = new ArrayList<>();

                                JSONArray arr_vocab = less.getJSONArray("vocal");

                                for (int k = 0; k < arr_vocab.length(); k ++) {
                                    JSONObject vo = arr_vocab.getJSONObject(k);
                                    VocabModel vcb = new VocabModel();
                                    vcb.vocab = vo.getString("vocal");

                                    // fake impress pronun

//                                    int ramdom = rand.nextInt(vcb.vocab.length()-3);
                                    int ramdom = 0;

                                    String impress = vcb.vocab.substring(ramdom,ramdom+1);


                                    vcb.pronun = vo.getString("phonetic");
//                                    vcb.vocab = vo.getString("vocal");
                                    vcb.id = "";
                                    vcb.url_voice = vo.getString("link");
                                    vcb.impress_pronun = impress;

                                    listVocab.add(vcb);

                                }

                                lessonModel.list_vocab = listVocab;

                                list_lesson.add(lessonModel);
                            }


                            model.list_lesson = list_lesson;

                            //


                            arr_learn.add(model);

                        }


                        // save draft

                        saveDraft(arr_learn);

                        drafLesson.clear();
                        drafLesson = arr_learn;

                        showProgress(false);
                    } catch (JSONException e) {

                        showProgress(false);
                        Log.e(TAG, "onResponse: " + e.getMessage());

                        e.printStackTrace();
                    }

                } else {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                showProgress(false);
            }
        });
    }


    private void saveDraft(ArrayList<LearnModel> list_learn) {
        MyCache.getInstance().putString(DraffKey.lesson, MyGson.getInstance().toJson(list_learn));
        Log.e(TAG, "saveDraft: " + MyGson.getInstance().toJson(list_learn));
    }

    private void getnumNewNoti() {
        showProgress(true);
        serviceFunction = APIUtils.getAPIService();

        String accessToken = MyCache.getInstance().getString(DraffKey.accessToken);


//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), send.toString());

        serviceFunction.getNumNoti(accessToken).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "onResponse: getnumNewNoti " + response.body());

                try {
                    JSONObject main = new JSONObject(response.body());

                    JSONObject ob = main.getJSONObject("data");

                    curr_newNoti = ob.getInt("num_noti");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bottom_nav_bar.setNotification(String.valueOf(curr_newNoti), 2);
                        }
                    });

                    showProgress(false);



                } catch (JSONException e) {
                    showProgress(false);

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: getnumNewNoti" + t.getMessage() );
                showProgress(false);

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

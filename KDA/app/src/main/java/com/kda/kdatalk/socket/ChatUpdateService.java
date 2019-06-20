package com.kda.kdatalk.socket;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.kda.kdatalk.R;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.UtilLibs;

import java.util.HashMap;

import io.socket.emitter.Emitter;

public class ChatUpdateService extends Service {

    private static final String TAG = ChatUpdateService.class.getName();

//    public static final String CHANNEL_ID = "KDA";

    boolean tokenInvalid = false;
    //    User user = CacheData.getInstance().getDetailUserCache();
    PowerManager pm;
    PowerManager.WakeLock wl;
    AlarmManager alarmManager;


    public ChatUpdateService() {
    }

    static Handler h;
    static Runnable runnable;
    String token = MyCache.getInstance().getString(DraffKey.accessToken);
    int countReconnect = 0;

    HashMap<String, Emitter.Listener> event = new HashMap<>();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        Log.e("Service ", "SocketConnection Service Create -> connect socket");
        registerReceiver(receiverSocketEvent, new IntentFilter(AppConstants.NEW_MSG));
        ChatSocketClient.getInstants().connect(getApplicationContext());
//        Log.e("", "SocketConnection: " + " connected");
//        checkSocketInterval();

    }

    private void checkSocketInterval() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wl.acquire();

        h = new Handler();
        runnable = () -> {
            if (!ChatSocketClient.getInstants().isConnected()) {
                Log.d(TAG, "SocketConnection = false begin reconnect");
                ChatSocketClient.getInstants().connect(getApplicationContext());
            } else {
                ChatSocketClient.getInstants().hasListeners(event);
                Log.d(TAG, "SocketConnection = true");
            }
            long time = 0;
            time += 10000;
            Log.d("Service", "Going for... " + time);
            h.postDelayed(runnable, 10000);
        };
        h.postDelayed(runnable, 10000); // 10 second delay (takes millis)
    }

    private void onDataboardChangeValue() {

        event.put(AppConstants.SocketEvent.addMessage, newMessage);

        //
        ChatSocketClient.getInstants().hasListeners(event);
        //

    }




    Emitter.Listener newMessage = args -> {

        Log.e(TAG, "newMessage: " + args[0].toString());

//        try {
//            JSONObject data = new JSONObject(args[0].toString());
////            String id = data.getString("order_id");
//            String id = data.getString("order_id");
//
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Log.e(TAG, "NEW newMessage");

        sendBroadcastEvent(args[0].toString());

        // need data

    };

    // TODO BroadcastReceiver
    BroadcastReceiver receiverSocketEvent = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(AppConstants.NEW_MSG);
            if (msg != null) {
                if (msg.equals("EVENT_CONNECT")) {
                    onDataboardChangeValue();
                    sendBroadcastEvent(AppConstants.SocketEvent.connected);
                }
                if (msg.equals("EVENT_DISCONNECT")) {
                    if (!UtilLibs.isNetworkConnect(context)) {
                        if (!ChatSocketClient.getInstants().isConnected()) {
                            Log.d("SOCKET EVENT_DISCONNECT", "SocketConnection = false begin reconnect");
                            ChatSocketClient.getInstants().connect(getApplicationContext());//connect(getApplicationContext()
                        }
                    } else
                        sendBroadcastEvent(AppConstants.SocketEvent.disconnect);
                }
                if (msg.equals("EVENT_CONNECT_TIMEOUT")) {
                    Log.e(TAG, "onReceive: " + "reconnect Socket");
                    ChatSocketClient.getInstants().connect(getApplicationContext());
                }
                if (msg.equals("EVENT_CONNECT_ERROR")) {
                    // ChatSocketClient.getInstants().reconnect();
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//            Notification.Builder builder = new Notification.Builder(this, "com.kda.kdatalk")
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText("KDATalk")
////                    .setChannelId(CHANNEL_ID)
//                    .setAutoCancel(true);
//
//            Notification notification = builder.build();
//            startForeground(1, notification);
            checkSocketInterval();
            return START_NOT_STICKY;


        } else {
            checkSocketInterval();
            return START_STICKY;

        }


    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.kda.kdatalk";
        String channelName = "Chat";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);

    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onDestroy() {
        try {
            wl.release();
            h.removeCallbacks(runnable);
            ChatSocketClient.getInstants().disconnect();
            Log.e("SocketConnection", ": disconnect");
            unregisterReceiver(receiverSocketEvent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    private void sendBroadcastEvent(String event) {
        try {
            Intent intent = new Intent(AppConstants.NEWCHAT);
            intent.putExtra(AppConstants.NEWCHAT, event);
            getApplicationContext().sendBroadcast(intent);
        } catch (Exception e) {
            Log.e(TAG, "sendBroadcastEvent: " + e.getMessage());
        }
    }
}

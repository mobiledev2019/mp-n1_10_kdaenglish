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


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.emitter.Emitter;


public class SocketUpdateService extends Service {

    private static final String TAG = SocketUpdateService.class.getName();

//    public static final String CHANNEL_ID = "KDA";

    boolean tokenInvalid = false;
//    User user = CacheData.getInstance().getDetailUserCache();
    PowerManager pm;
    PowerManager.WakeLock wl;
    AlarmManager alarmManager;


    public SocketUpdateService() {
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
        Log.e("Service ", "SocketConnection Service Create -> connect socket");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        registerReceiver(receiverSocketEvent, new IntentFilter(AppConstants.NEW_MSG));
        SocketClient.getInstants().connect(getApplicationContext());
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
            if (!SocketClient.getInstants().isConnected()) {
                Log.d("SOCKET", "SocketConnection = false begin reconnect");
                SocketClient.getInstants().connect(getApplicationContext());
            } else {
                SocketClient.getInstants().hasListeners(event);
                Log.d("SOCKET", "SocketConnection = true");
            }
            long time = 0;
            time += 10000;
            Log.d("Service", "Going for... " + time);
            h.postDelayed(runnable, 10000);
        };
        h.postDelayed(runnable, 10000); // 10 second delay (takes millis)
    }

    private void onDataboardChangeValue() {
//            SocketClient.getInstants().addEmitterListener(Contranst.SocketEvent.OrDerDataBoard,orDerDataboard);
//            SocketClient.getInstants().addEmitterListener(Contranst.SocketEvent.NewRequest, newRequest);
//            SocketClient.getInstants().addEmitterListener(Contranst.SocketEvent.Msg, msgSocket);
//            SocketClient.getInstants().addEmitterListener(Contranst.SocketEvent.CancelRequest, cancelSocket);
//        event.put(Contranst.SocketEvent.orderExpired, orderExpired);

        event.put(AppConstants.SocketEvent.newMessage, newMessage);
        event.put(AppConstants.SocketEvent.newNotification, newNotification);
        event.put(AppConstants.SocketEvent.invalidToken, msgSocket);

        //
        SocketClient.getInstants().hasListeners(event);
        //

    }




    Emitter.Listener newMessage = args -> {

        Log.e("newMessage: ", ": " + args[0].toString());

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
        Log.e("SOCKET_EVENT", "NEW newMessage");

        sendBroadcastEvent(AppConstants.SocketEvent.newMessage);

    };

    Emitter.Listener newNotification = args -> {

//        Log.e("newNotification: ", ": " + args[0].toString());

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
        Log.e("SOCKET_EVENT", "NEW newNotification");

        sendBroadcastEvent(AppConstants.SocketEvent.newNotification);

    };

    //


    Emitter.Listener msgSocket = args -> {
        Log.e(TAG, "SOCKET connect: " + args[0].toString());
        if (String.valueOf(args[0]).equals("Token invalid")) {
            Log.e("SOCKET", "TOKEN INVALID");
            countReconnect++;
            if (countReconnect >= 3) {
                sendBroadcastEvent(AppConstants.SocketEvent.invalidToken);
                tokenInvalid = true;
                onDestroy();
            }
            Log.e("SOCKET_Msg", "Logout Invalid Token");
        }
    };
//
//    Emitter.Listener cancelSocket = args -> {
//        if (String.valueOf(args[0]) != null) {
//            Log.e("SOCKET_EVENT", "Cancel Request");
//            Log.e("SOCKET_EVENT", " data: " + args[0].toString());
//            try {
//                JSONObject obj = new JSONObject(String.valueOf(args[0]));
////                NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class, JsonParser.getStringInJsonObj(obj, "title"), JsonParser.getStringInJsonObj(obj, "message"), false);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.e("SOCKET", "Cancel Request");
//            sendBroadcastEvent(Contranst.NEW_EVENT);
//        }
//    };

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
                        if (!SocketClient.getInstants().isConnected()) {
                            Log.d("SOCKET EVENT_DISCONNECT", "SocketConnection = false begin reconnect");
                            SocketClient.getInstants().connect(getApplicationContext());//connect(getApplicationContext()
                        }
                    } else
                        sendBroadcastEvent(AppConstants.SocketEvent.disconnect);
                }
                if (msg.equals("EVENT_CONNECT_TIMEOUT")) {
                    Log.e(TAG, "onReceive: " + "reconnect Socket");
                    SocketClient.getInstants().connect(getApplicationContext());
                }
                if (msg.equals("EVENT_CONNECT_ERROR")) {
                    // SocketClient.getInstants().reconnect();
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
            SocketClient.getInstants().disconnect();
            Log.e("SocketConnection", ": disconnect");
            unregisterReceiver(receiverSocketEvent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    private void sendBroadcastEvent(String event) {
        try {
            Intent intent = new Intent(AppConstants.NEW_MSG);
            intent.putExtra(AppConstants.NEW_MSG, event);
            getApplicationContext().sendBroadcast(intent);
        } catch (Exception e) {
            Log.e(TAG, "sendBroadcastEvent: " + e.getMessage());
        }
    }
}

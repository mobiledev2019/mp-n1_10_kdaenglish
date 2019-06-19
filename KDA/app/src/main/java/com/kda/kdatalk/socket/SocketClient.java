package com.kda.kdatalk.socket;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kda.kdatalk.network.ServiceConst;
import com.kda.kdatalk.utils.AppConstants;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.UtilLibs;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import okhttp3.OkHttpClient;

public class SocketClient {
    private static final String TAG = SocketClient.class.getSimpleName();
    private static SocketClient socketClient;
    private Socket mSocket;
    public Context context;

    public String URL_SOCKET = "https://51560828.ngrok.io";

    String accessToken = "";


    public static SocketClient getInstants() {
        if (socketClient == null) {
            socketClient = new SocketClient();
        }
        return socketClient;
    }

    public void connect(Context context) {
        this.context = context;

        accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
        try {

            if (mSocket != null && mSocket.connected()) {
                Log.e(getClass().getSimpleName(), "Socket connected - Return");
                return;
            }

            if (!UtilLibs.isNetworkConnect(context)) {
                Log.e(getClass().getSimpleName(), "No network connection - Return");
                return;
            }

            Log.e(getClass().getSimpleName(), "SocketConnection: Start connect");
            if (mSocket != null) {
                offAllListener();
                mSocket.disconnect();
                mSocket = null;
//                if (mSocket.connected()) {
//                    mSocket.disconnect();
//                    mSocket = null;
//                }
            }

            OkHttpClient okHttpClient =  new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30,TimeUnit.SECONDS)
//                    .addNetworkInterceptor(new )
                    .build();




            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);


            IO.Options mOptions = new IO.Options();
            Log.e(getClass().getSimpleName() + "Token Socket",accessToken);
            mOptions.forceNew = true;

            mOptions.reconnection = true;
            mOptions.transports = new String[]{Polling.NAME};
//            mOptions.transports = new String[]{WebSocket.NAME};
            mOptions.query = "&token=" + accessToken + "&platform=ANDROID";//

            mSocket = IO.socket(ServiceConst.URL_SERVER_KDA , mOptions);
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//            mSocket.on(Socket.EVENT, onConnectError);
            Log.e(getClass().getSimpleName(), "connect: " + "start connect");
            mSocket.connect();

        } catch (Exception e) {
            Log.e(TAG, "connect: " + e.getMessage());
        }
    }



    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(getClass().getSimpleName(), "SocketConnection: EVENT_CONNECT");
            Log.e("Socket","Connect Socket Success");
            sendBroadcastEvent("EVENT_CONNECT");
        }
    };

    Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.e("Socket","EVENT_DISCONNECT Data : " + args[0].toString());

            Log.e(getClass().getSimpleName(), "SocketConnection: EVENT_DISCONNECT");
            sendBroadcastEvent("EVENT_DISCONNECT");
        }
    };

    Emitter.Listener onConnectTimeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.e("Socket", "EVENT_CONNECT_TIMEOUT Data : " + args[0].toString());

            Log.e(getClass().getSimpleName(), "SocketConnection: EVENT_CONNECT_TIMEOUT");
            reconnect();
            sendBroadcastEvent("EVENT_CONNECT_TIMEOUT");
        }
    };

    Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.e("Socket", "EVENT_CONNECT_ERROR Data : " + args[0].toString());

            Log.e(getClass().getSimpleName(), "EVENT_CONNECT_ERROR");
            //reconnect();
            sendBroadcastEvent("EVENT_CONNECT_ERROR");
        }
    };

    public boolean isConnected() {
        if (mSocket != null) {
            return mSocket.connected();
        }
        return false;
    }

    public void reconnect() {
        if (mSocket == null) {
            return;
        }
        Log.d(getClass().getSimpleName(), "reconnect: " + "reconnect");
        connect(context);
    }

    private void sendBroadcastEvent(String event) {
        try {
            Intent intent = new Intent(AppConstants.NEW_MSG);
            intent.putExtra(AppConstants.NEW_MSG,event);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e(TAG, "sendBroadcastEvent: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (mSocket != null) {
                if (mSocket.connected()) {
                    Log.e(getClass().getSimpleName(), "disconnect socket");
                    mSocket.disconnect();
                    offAllListener();
                    mSocket = null;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "disconnect: " + e.getMessage());
        }
    }


    private void offAllListener() {
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
    }

//    private void sendBroadcastSocketEvent(String event) {
//        try {
//            Intent intent = new Intent(Constants.RECEIVER_SOCKET_EVENT);
//            intent.putExtra(event, event);
//            context.sendBroadcast(intent);
//        } catch (Exception e) {
//            LogError.loge(e);
//        }
//    }

    public void emit(String event, JSONObject object) {
        if (object == null) {
            return;
        }
        if (mSocket == null || !mSocket.connected()) {
            connect(context);
        }
        mSocket.emit(event, object);

    }

    public void addEmitterListener(String event, Emitter.Listener listener) {
        if (mSocket == null || !mSocket.connected()) {
            connect(context);
            return;
        }
        Log.e(getClass().getSimpleName(), "addEmitterListener: " + event);
        mSocket.on(event, listener);
    }

    public void removeEmitterListener(String event, Emitter.Listener listener) {
        if (mSocket == null) {
            return;
        }
        Log.e(getClass().getSimpleName(), "removeEmitterListener: " + event);
        try {
            mSocket.off(event, listener);
        } catch (Exception e) {
        }
    }

    public void hasListeners(HashMap<String,Emitter.Listener> event){
        if (mSocket == null || !mSocket.connected()) {
            return;
        }
        for(Map.Entry<String, Emitter.Listener> entry : event.entrySet()) {
            if (!mSocket.hasListeners(entry.getKey())) {
                addEmitterListener(entry.getKey(), entry.getValue());
            }
        }
    }


}

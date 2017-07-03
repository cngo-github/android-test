package com.example.cngo.testapp.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import com.example.cngo.testapp.events.NetworkStatusEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by cngo on 7/2/17.
 */
public class NetworkStatusService extends Service {
    private BroadcastReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        // Register receiver for network state changes.
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");

        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("BroadcastReceiver", "Wifi State changed.");
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                boolean enabled = wifiManager.isWifiEnabled();
                EventBus.getDefault().post(new NetworkStatusEvent(enabled));
            }
        };

        super.registerReceiver(this.receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if(this.receiver != null) {
            this.unregisterReceiver(this.receiver);
            this.receiver = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe
    public void onNetworkStatusEvent(NetworkStatusEvent event) {
        Log.i("EventBus","Received NetworkStatusEvent message.");
    }
}

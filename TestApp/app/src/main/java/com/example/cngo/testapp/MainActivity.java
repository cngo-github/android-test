package com.example.cngo.testapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.cngo.testapp.events.NetworkStatusEvent;
import com.example.cngo.testapp.services.NetworkStatusService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver receiver;
    private Service networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        Button button = (Button) this.findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                boolean enabled = wifiManager.isWifiEnabled();
                wifiManager.setWifiEnabled(!enabled);
            }
        });

        //this.networkService = new NetworkStatusService();

        /*
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
        */

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Context context = this.getApplicationContext();
        Intent i = new Intent(context, NetworkStatusService.class);

        context.startService(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if(this.receiver != null) {
            this.unregisterReceiver(this.receiver);
            this.receiver = null;
        }
    }

    @Subscribe
    public void onMessageEvent(NetworkStatusEvent event) {
        TextView textView = (TextView) this.findViewById(R.id.text_id);
        textView.setText(event.toString());
    }
}

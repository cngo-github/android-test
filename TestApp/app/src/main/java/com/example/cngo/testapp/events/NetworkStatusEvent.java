package com.example.cngo.testapp.events;

/**
 * Created by cngo on 7/2/17.
 */
public class NetworkStatusEvent {
    private boolean status;

    public NetworkStatusEvent(boolean status) {
        this.status = status;
    }

    /**
     * @return
     */
    public String toString() {
        return "Network status is enable:" + String.valueOf(this.status);
    }
}

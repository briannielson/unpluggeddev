package com.example.kaeleb.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by Brian on 4/14/2017.
 */

public class MapDataReceiver extends ResultReceiver {
    private Receiver receiver;

    public MapDataReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver r) {
        this.receiver = r;
    }

    public interface Receiver {
        void onReceiveResult(int resultcode, Bundle resultdata);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null)
            receiver.onReceiveResult(resultCode, resultData);
    }
}

package com.example.kaeleb.myapplication;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Brian on 4/14/2017.
 */

public class mapDataActivity extends IntentService {
    public static final String ACTION = "com.example.kaeleb.myapplication.TestService";

    public mapDataActivity() {
        super("debug");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] contents = new byte[1024];
        int length;

        Log.d("mapDataActivity", intent.getStringExtra("url"));

        URL url = null;
        try {
            url = new URL(intent.getStringExtra("url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            while ((length=in.read(contents)) != -1)
                baos.write(contents, 0, length);
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        Log.d("network", baos.toString());

        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);
        in.putExtra("resultValue", baos.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }
}

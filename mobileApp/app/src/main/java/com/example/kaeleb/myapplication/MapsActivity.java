package com.example.kaeleb.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent mapdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void startService() {
        LatLngBounds rect = mMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng northeast  = rect.northeast;
        LatLng southwest  = rect.southwest;
        LatLng location   = mMap.getCameraPosition().target;
        StringBuilder sb = new StringBuilder();
        sb.append("http://104.236.99.76:8080/maprequest/getmapnodes");
        sb.append("?northeast=");
        sb.append(northeast.latitude); sb.append(","); sb.append(northeast.longitude);
        sb.append("&southwest=");
        sb.append(southwest.latitude); sb.append(","); sb.append(southwest.longitude);
        sb.append("&location=");
        sb.append(location.latitude);  sb.append(","); sb.append(location.longitude);

        Log.d("MapsActivity", sb.toString());

        mapdata = new Intent(this, mapDataActivity.class);
        mapdata.putExtra("url",sb.toString());
        startService(mapdata);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(mapDataActivity.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(MapReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(MapReceiver);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        startService();
    }

    private BroadcastReceiver MapReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String resultValue = intent.getStringExtra("resultValue");
                addMapMarkers(resultValue);
            }
        }
    };

    protected void addMapMarkers(String b) {
        mMap.clear();
        JSONArray mapobj;
        try {
            mapobj = new JSONArray(b);
        } catch (JSONException e) {
            mapobj = new JSONArray();
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < mapobj.length(); i++) {
                JSONObject current = mapobj.getJSONObject(i);
                String poster = current.getString("artist") + " at " + current.getString("venue");
                String date = ": " + current.getString("date");
                double lat = current.getDouble("latitude");
                double lng = current.getDouble("long" +
                        "itude");
                LatLng perform = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(perform).title(poster + date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

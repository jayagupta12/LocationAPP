package com.example.locationtestapplication;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.locationtestapplication.Modal.Modal;
import com.example.locationtestapplication.Modal.Result;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> markersArray;
    RecyclerView recyclerView;
    ImageView filter;
    Modal modal;
    double lat, lng;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        recyclerView = findViewById(R.id.recyclerview);
        filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("recyclerclick", "onItemClick: " + modal.getResults().get(position).getName());


                        final LatLng ltlng = new LatLng(modal.getResults().get(position).getGeometry().getLocation().getLat(), modal.getResults().get(position).getGeometry().getLocation().getLng());
                        Marker marker = mMap.addMarker(
                                new MarkerOptions()
                                        .position(ltlng)
                                        .title(modal.getResults().get(position).getName()).snippet(modal.getResults().get(position).getVicinity()));
                        marker.showInfoWindow();
                    }
                })
        );
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
            getLocation();
        } catch (Exception e){
            e.printStackTrace();
        }


    }
    public void getLocation(){
        gpsTracker = new GPSTracker(MapsActivity.this);
        if(gpsTracker.canGetLocation()){
             lat = gpsTracker.getLatitude();
             lng = gpsTracker.getLongitude();

        }else{
            gpsTracker.showSettingsAlert();
        }
    }



    private void callMapApi(String type, double lat, double lng) {

        // add api key here also
        String Url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=[API_KEY]&location="+lat+","+lng+"&radius=5000&type=" + type;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                modal = gson.fromJson(response.toString(), Modal.class);

                if (modal.getStatus().equals("OK")) {
                    ArrayList<Result> results = new ArrayList<>();
                    for (int i = 0; i < modal.getResults().size(); i++) {

                        results.add(modal.getResults().get(i));
                    }
                    callAdapter(results);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

    }

    private void callAdapter(ArrayList<Result> results) {
        DataAdapter dealsAdapter = new DataAdapter(MapsActivity.this, results);
        recyclerView.setAdapter(dealsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        for (int i = 0; i < modal.getResults().size(); i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(modal.getResults().get(i).getGeometry().getLocation().getLat(), modal.getResults().get(i).getGeometry().getLocation().getLng())).title(modal.getResults().get(i).getName())).setSnippet(modal.getResults().get(i).getVicinity());


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("latlng", "onMapReady: " + lat + "-" + lng);
        callMapApi("gym", lat, lng);
        mMap = googleMap;
        Log.e("array", "onMapReady: " + markersArray);
        LatLng myPosition = new LatLng(lat, lng);
        CameraPosition position = new CameraPosition.Builder().
                target(myPosition).zoom(10).bearing(19).tilt(30).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        alertDialog.setTitle("Choose");
        String[] items = {"Gym", "Cafe", "Parks"};
        int checkedItem = 0;
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        callMapApi("gym", lat, lng);

                        break;
                    case 1:
                        callMapApi("cafe", lat, lng);
                        dialog.dismiss();
                        break;

                    case 2:
                        callMapApi("parks", lat, lng);
                        dialog.dismiss();
                        break;

                }
            }
        });
        final AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }


}


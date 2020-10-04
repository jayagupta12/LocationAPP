package com.example.locationtestapplication;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.popup, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.name);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.desc);
        ImageView imageView=view.findViewById(R.id.image);
        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());

        Picasso.get()
                .load(marker.getId())
                .into(imageView);
       // Picasso.with().load(markers.get(arg0.getId())).into(imgMarkerImage);
        return view;
    }
}
package com.example.locationtestapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtestapplication.Modal.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context context;
    ArrayList<Result> results;

    public DataAdapter(MapsActivity mapsActivity, ArrayList<Result> results) {
        this.context = mapsActivity;
        this.results = results;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.datalayout, parent, false);
        DataAdapter.ViewHolder mh = new DataAdapter.ViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = results.get(position).getName();
        holder.title.setText(title);
      //  Bitmap bmp = BitmapFactory.decodeStream(results.get(position).getIcon().openConnection().getInputStream());
        Picasso.get()
                .load(results.get(position).getIcon())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.textview);
            imageView=itemView.findViewById(R.id.img);
        }
    }
}

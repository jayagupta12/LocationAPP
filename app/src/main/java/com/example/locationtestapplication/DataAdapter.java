package com.example.locationtestapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtestapplication.Modal.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context context;
    ArrayList<Result> results;
    String stitle;

    public DataAdapter(MapsActivity mapsActivity, ArrayList<Result> results,String title) {
        this.context = mapsActivity;
        this.results = results;
        this.stitle=title;

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

        if(stitle.equals(title)){

            holder.cardView.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.textview);
            imageView=itemView.findViewById(R.id.img);
            cardView=itemView.findViewById(R.id.cardview);
        }
    }
}

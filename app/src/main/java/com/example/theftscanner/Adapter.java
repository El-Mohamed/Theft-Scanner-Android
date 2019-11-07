package com.example.theftscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;
    private List<Theft> allThefts;


    public Adapter(Context context, List<Theft> thefts) {
        mContext = context;
        allThefts = thefts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.viewholder_details, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Theft tempTheft = allThefts.get(position);
        Picasso.get().load(tempTheft.getImageURL()).into(holder.mImage);
        holder.mBrand.setText(tempTheft.getBrand());
        holder.mModel.setText(tempTheft.getModel());
        holder.mStreet.setText(tempTheft.getStreet());
        holder.mCity.setText(tempTheft.getCity());
    }

    @Override
    public int getItemCount() {
        return allThefts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mBrand;
        TextView mModel;
        TextView mStreet;
        TextView mCity;
        public ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBrand = itemView.findViewById(R.id.detail1);
            mModel = itemView.findViewById(R.id.detail2);
            mStreet = itemView.findViewById(R.id.detail3);
            mCity = itemView.findViewById(R.id.detail4);
            mImage = itemView.findViewById(R.id.vehicle_image);
        }
    }

}

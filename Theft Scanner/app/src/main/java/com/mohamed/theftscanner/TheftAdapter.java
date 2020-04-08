package com.mohamed.theftscanner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TheftAdapter extends RecyclerView.Adapter<TheftAdapter.ViewHolder> {

    private Context mContext;
    private final LayoutInflater mInflater;
    private List<Theft> allThefts;


    public TheftAdapter(Context context, List<Theft> thefts) {
        mContext = context;
        allThefts = thefts;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.viewholder_details, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Theft tempTheft = allThefts.get(position);
        Picasso.get().load(tempTheft.getImageURL()).placeholder(R.mipmap.ic_launcher_foreground).into(holder.mImage);
        holder.mBrand.setText(tempTheft.getBrand().toUpperCase());
        holder.mModel.setText(tempTheft.getModel().toUpperCase());
        holder.mStreet.setText(tempTheft.getStreet().toUpperCase());
        holder.mCity.setText(tempTheft.getCity().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return allThefts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mBrand, mModel, mStreet, mCity;
        final TheftAdapter mAdapter;
        public ImageView mImage;

        public ViewHolder(@NonNull View itemView, TheftAdapter adapter) {
            super(itemView);
            mBrand = itemView.findViewById(R.id.detail1);
            mModel = itemView.findViewById(R.id.detail2);
            mStreet = itemView.findViewById(R.id.detail3);
            mCity = itemView.findViewById(R.id.detail4);
            mImage = itemView.findViewById(R.id.vehicle_image);
            mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            Intent intent = new Intent (mContext, Map.class);
            intent.putExtra("lat", allThefts.get(mPosition).getLatitude());
            intent.putExtra("long", allThefts.get(mPosition).getLongitude());
            mContext.startActivity(intent);
        }
    }
}

package com.example.theftscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> allBrands;
    private List<String> allModels;
    private List<String> allStreets;
    private List<String> allCitys;

    Adapter(Context context, List<String> AllBrands,List<String> AllModels,List<String> AllStreets,List<String> AllCitys) {

        this.layoutInflater = LayoutInflater.from(context);
        this.allBrands = AllBrands;
        this.allModels = AllModels;
        this.allStreets = AllStreets;
        this.allCitys = AllCitys;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.viewholder_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String tempBrand = allBrands.get(position);
        String tempModel = allModels.get(position);
        String tempStreet = allStreets.get(position);
        String tempCity = allCitys.get(position);

        holder.mBrand.setText(tempBrand);
        holder.mModel.setText(tempModel);
        holder.mStreet.setText(tempStreet);
        holder.mCity.setText(tempCity);

    }

    @Override
    public int getItemCount() {
        return allBrands.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mBrand;
        TextView mModel;
        TextView mStreet;
        TextView mCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBrand = itemView.findViewById(R.id.detail1);
            mModel = itemView.findViewById(R.id.detail2);
            mStreet = itemView.findViewById(R.id.detail3);
            mCity = itemView.findViewById(R.id.detail4);
        }
    }

}

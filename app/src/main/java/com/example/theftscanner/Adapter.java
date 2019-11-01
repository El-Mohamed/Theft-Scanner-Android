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
    private String[] allTypes;
    private List<String> allNumbers;

    Adapter(Context context,String[] AllTypes, List<String> AllNumbers) {

        this.layoutInflater = LayoutInflater.from(context);
        this.allTypes = AllTypes;
        this.allNumbers = AllNumbers;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.viewholder_statistics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String tempType = allTypes[position];
        String tempNumber = allNumbers.get(position);
        holder.mType.setText(tempType);
        holder.mNumber.setText(tempNumber);

    }

    @Override
    public int getItemCount() {
        return allTypes.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mType;
        TextView mNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mType = itemView.findViewById(R.id.type_stats);
            mNumber = itemView.findViewById(R.id.number_stats);
        }
    }

}

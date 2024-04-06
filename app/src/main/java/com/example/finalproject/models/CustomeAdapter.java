package com.example.finalproject.models;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder> {

    private ArrayList<Game> dataset;
    public CustomeAdapter(ArrayList<Game> dataSet) {
        this.dataset=dataSet;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        TextView textViewType;
        TextView textViewYear;
        TextView textViewDevComp;
        TextView textViewDesc;
//        ImageView imageView;
        public MyViewHolder(View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.name_c);
            textViewType = itemView.findViewById(R.id.type_c);
            textViewYear = itemView.findViewById(R.id.year_c);
            textViewDevComp = itemView.findViewById(R.id.develop_comp_c);
            textViewDesc = itemView.findViewById(R.id.desc_c);
//            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textViewName.setText(dataset.get(position).getName());;
        holder.textViewType.setText(dataset.get(position).getType());;
        holder.textViewYear.setText(dataset.get(position).getLaunch_Date());;
        holder.textViewDevComp.setText(dataset.get(position).getDevelop_Comp());;
        holder.textViewDesc.setText(dataset.get(position).getDescription());;

//        ImageView imageView = holder.imageView;
        Game game = dataset.get(position);
        // Log dataset contents for debugging
        Log.d("Dataset", "Item at position " + position + ": " + game.getName());

//        imageView.setImageResource(dataset.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}

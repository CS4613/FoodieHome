package com.dileep.foodiehome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectColorsAdapter extends RecyclerView.Adapter<SelectColorsAdapter.SelectcolorsHolder>{
    ArrayList<ColorsPojo> colorsList;
    Context context;
    int fromClass;


    public SelectColorsAdapter(ArrayList<ColorsPojo> colorsList, Context context, int i) {
        this.colorsList = colorsList;
        this.context = context;
        this.fromClass=i;
    }

    @NonNull
    @Override
    public SelectcolorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.colors_list_item,parent,false);
       return new SelectcolorsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectcolorsHolder holder, final int position) {
        holder.bind(position);
        ColorsPojo colorsPojo=colorsList.get(position);
       // holder.textView.setText(colorsPojo.getColorName());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int adapterPosition = holder.getAdapterPosition();

                if (colorsList.get(adapterPosition).isChecked()) {

                    holder.checkBox.setChecked(false);
                    colorsList.get(adapterPosition).setChecked(false);
                    if (fromClass==1) {
                        AddProduct.selectedcolorsList.remove(colorsList.get(adapterPosition).getColorName());
                    }else if (fromClass==2){
                        AddnewOffers.selectedcolorsList.remove(colorsList.get(adapterPosition).getColorName());
                    }else if (fromClass==3){
                        BestSellers.selectedcolorsList.remove(colorsList.get(adapterPosition).getColorName());
                    }
                }
                else {

                    holder.checkBox.setChecked(true);
                    colorsList.get(adapterPosition).setChecked(true);
                    if (fromClass==1) {
                        AddProduct.selectedcolorsList.add(colorsList.get(adapterPosition).getColorName());
                    }else if (fromClass==2){
                        AddnewOffers.selectedcolorsList.add(colorsList.get(adapterPosition).getColorName());
                    }else if (fromClass==3){
                        BestSellers.selectedcolorsList.add(colorsList.get(adapterPosition).getColorName());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorsList.size();
    }

    public class SelectcolorsHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        TextView textView;
        public SelectcolorsHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=(CheckBox)itemView.findViewById(R.id.CheckBox01);
            textView=(TextView)itemView.findViewById(R.id.rowTextView);

        }
        void bind(int position) {
           textView.setText(String.valueOf(colorsList.get(position).getColorName()));
            if (colorsList.get(position).isChecked()) {
                checkBox.setChecked(true);
            }
            else {
                checkBox.setChecked(false);
            }
        }
    }
}

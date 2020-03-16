package com.dileep.foodiehome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SelectedviewAdapter extends RecyclerView.Adapter<SelectedviewAdapter.SelectedViewHolder> {

    ArrayList<String> selectedPicsList;
    Context context;
    int fromClass;

    public SelectedviewAdapter(ArrayList<String> selectedPicsList, Context context, int fromClass) {
        this.selectedPicsList = selectedPicsList;
        this.context = context;
        this.fromClass=fromClass;
    }

    @NonNull
    @Override
    public SelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.selectedpics_list_item,parent,false);
        return new SelectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectedViewHolder holder, final int position) {
        Picasso.get()
                .load(selectedPicsList.get(position))
                .fit()
                .centerCrop()
                .into(holder.imageView1);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positiondil=holder.getAdapterPosition();
              //  AddProduct.selectedpicsList.remove(positiondil);
                removeAt(positiondil);
            }
        });
    }

    public void removeAt(int position) {
        selectedPicsList.remove(position);
        notifyItemRemoved(position);

        if (selectedPicsList.size()>0){
            if (fromClass==1) {
                AddProduct.addimg.setVisibility(View.GONE);
                AddProduct.selectedListview.setVisibility(View.VISIBLE);
            }else if (fromClass==2){
                AddnewOffers.addimg.setVisibility(View.GONE);
                AddnewOffers.selectedListview.setVisibility(View.VISIBLE);
            }else if (fromClass==3){
                BestSellers.addimg.setVisibility(View.GONE);
                BestSellers.selectedListview.setVisibility(View.VISIBLE);
            }
        }else {
            if (fromClass==1) {
                AddProduct.addimg.setVisibility(View.VISIBLE);
                AddProduct.selectedListview.setVisibility(View.GONE);
            }else if (fromClass==2){
                AddnewOffers.addimg.setVisibility(View.VISIBLE);
                AddnewOffers.selectedListview.setVisibility(View.GONE);
            }else if (fromClass==3){
                BestSellers.addimg.setVisibility(View.VISIBLE);
                BestSellers.selectedListview.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return selectedPicsList.size();
    }

    public class SelectedViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView1,remove;

        public SelectedViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1=(ImageView)itemView.findViewById(R.id.imageView1);
            remove=(ImageView)itemView.findViewById(R.id.remove);
        }
    }
}

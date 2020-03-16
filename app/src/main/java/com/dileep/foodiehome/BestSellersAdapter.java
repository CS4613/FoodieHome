package com.dileep.foodiehome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dileep.foodiehome.pojos.BestsellersPojo;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BestSellersAdapter extends RecyclerView.Adapter<BestSellersAdapter.BestSellerHolder> {
    ArrayList<BestsellersPojo> bestsellersPojoArrayList;
    Context context;

    public BestSellersAdapter(ArrayList<BestsellersPojo> bestsellersPojoArrayList, Context context) {
        this.bestsellersPojoArrayList = bestsellersPojoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BestSellerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item,parent,false);
        return new BestSellerHolder(view);
    }

    @Override
    public void onBindViewHolder(BestSellerHolder holder, int position) {

        BestsellersPojo pojo=bestsellersPojoArrayList.get(position);
        holder.name.setText("Name: "+pojo.getProductName());
        holder.cost.setText("Cost: "+pojo.getProductCost());
        holder.id.setText("Id: "+pojo.getProductId());
        Picasso.get()
                .load(pojo.getImageUrls().get(0))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.image)
                .into(holder.image);

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View sheetView = inflater.inflate(R.layout.bestsellers_options, null);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();

                FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

    }

    @Override
    public int getItemCount() {
        return bestsellersPojoArrayList.size();
    }

    public class BestSellerHolder extends RecyclerView.ViewHolder{

        ImageView image,options;
        TextView cost,name,id;
        public BestSellerHolder(@NonNull View itemView) {
            super(itemView);

            image=(ImageView)itemView.findViewById(R.id.image);
            name=(TextView)itemView.findViewById(R.id.name);
            cost=(TextView)itemView.findViewById(R.id.cost);
            id=(TextView)itemView.findViewById(R.id.quantity);
            options=(ImageView)itemView.findViewById(R.id.options);
        }
    }
}

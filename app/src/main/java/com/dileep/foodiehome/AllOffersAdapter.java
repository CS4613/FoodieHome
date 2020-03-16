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

import com.dileep.foodiehome.pojos.OffersPojo;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllOffersAdapter extends RecyclerView.Adapter<AllOffersAdapter.AllOffersholder> {

    ArrayList<OffersPojo> offersPojoArrayList;
    Context context;

    public AllOffersAdapter(ArrayList<OffersPojo> offersPojoArrayList, Context context) {
        this.offersPojoArrayList = offersPojoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllOffersholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_offers_list_item,parent,false);
        return new AllOffersholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllOffersholder holder, int position) {
        OffersPojo offersPojo=offersPojoArrayList.get(position);
        holder.name.setText("Name: "+offersPojo.getProductName());
        holder.cost.setText("Cost: "+offersPojo.getProductCost());
        holder.id.setText("Offer: "+offersPojo.getOfferPercent()+"%");

        Picasso.get()
                .load(offersPojo.getImageUrls().get(0))
                .placeholder(R.drawable.image)
                .fit()
                .centerCrop()
                .into(holder.image);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View sheetView = inflater.inflate(R.layout.offers_options, null);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();

                FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

    }

    @Override
    public int getItemCount() {
        return offersPojoArrayList.size();
    }

    public class AllOffersholder extends RecyclerView.ViewHolder{

        ImageView image,options;
        TextView cost,name,id;
        public AllOffersholder(@NonNull View itemView) {
            super(itemView);

            image=(ImageView)itemView.findViewById(R.id.image);
            name=(TextView)itemView.findViewById(R.id.name);
            cost=(TextView)itemView.findViewById(R.id.cost);
            id=(TextView)itemView.findViewById(R.id.quantity);
            options=(ImageView)itemView.findViewById(R.id.options);

        }
    }
}

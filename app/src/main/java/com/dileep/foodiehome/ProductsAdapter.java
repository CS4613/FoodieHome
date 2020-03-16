package com.dileep.foodiehome;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dileep.foodiehome.pojos.UploadProductPojo;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.Productsholder> {

    ArrayList<UploadProductPojo> productPojoList;
    Context context;
    RequestOptions requestOptions;
    private StorageReference mStorage;

    FirebaseDatabase database;
    DatabaseReference myRef;
    int count=0;
    boolean error=false;
    ProgressDialog progressDialog;
    LinearLayout optionDelete;
    boolean noerror=false;


    public ProductsAdapter(ArrayList<UploadProductPojo> productPojoList, Context context) {
        this.productPojoList = productPojoList;
        this.context = context;
        requestOptions=new RequestOptions();
        requestOptions.fitCenter();
        requestOptions.centerInside();
        requestOptions.placeholder(R.drawable.image);
        mStorage= FirebaseStorage.getInstance().getReference("vegpizza");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("vegpizza");
    }

    @NonNull
    @Override
    public Productsholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item,parent,false);
        return new Productsholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Productsholder holder, final int position) {

                final UploadProductPojo pojo=productPojoList.get(position);
                holder.name.setText("Name: "+pojo.getProductName());
                holder.cost.setText("Cost: "+pojo.getProductCost());
                holder.id.setText("Id: "+pojo.getProductId());
     /*   Picasso.get()
                .load(pojo.getImageUrls().get(0))
                .fit().centerInside()
                .placeholder(R.drawable.image)
                .into(holder.image);*/

        Glide.with(context)
                .load(pojo.getImageUrls().get(0))
                .apply(requestOptions)
                .into(holder.image);

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View sheetView = inflater.inflate(R.layout.chat_options_menu, null);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();

                FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                optionDelete=(LinearLayout)mBottomSheetDialog.findViewById(R.id.option_delete);

                final int itemposition=holder.getAdapterPosition();
                optionDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mBottomSheetDialog.dismiss();
                        deleteitem(productPojoList.get(holder.getAdapterPosition()).getImageUrls(),itemposition,pojo.getProductId());
                    }
                });

            }
        });
    }

    private void deleteitem(final ArrayList<String> imageUrls, final int itemposition, final String productId) {

        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("please wait a sec...");
        progressDialog.setCancelable(false);
        progressDialog.show();;

        for (int i=0;i<imageUrls.size();i++){

            System.out.println("sizedilee:"+imageUrls.size()+"names:"+imageUrls.get(i));
            StorageReference desertRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrls.get(i));
            //StorageReference photoRef = desertRef.

            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully

                    noerror=false;
                    count = count + 1;
                    System.out.println("countdilll:"+count);
                    if (count == imageUrls.size()) {

                        //  progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    noerror=true;
                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    // Uh-oh, an error occurred!
                }
            });

        }

        if (noerror==false){
            deletemetadat(productId,itemposition);
        }


    }

    private void deletemetadat(String productId, int itemposition) {
        try {
            DatabaseReference delref = FirebaseDatabase.getInstance().getReference("vegpizza").child(productId);

            delref.removeValue();
            removeAt(itemposition);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
           Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_SHORT).show();




      /*  Query Query = myRef.child(productId).orderByChild("productId").equalTo(productId);

         Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                error=false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                error=true;
                Toast.makeText(context, "errordb"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("errordb"+databaseError.getMessage());
               // Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

         if (error){
             Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
         }else {
             removeAt(itemposition);
             Toast.makeText(context, "Data removed successfully", Toast.LENGTH_SHORT).show();
         }*/

    }

    public void removeAt(int position){
        productPojoList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return productPojoList.size();
    }

    public class Productsholder extends RecyclerView.ViewHolder{

        ImageView image,options;
        TextView cost,name,id;

        public Productsholder(@NonNull View itemView) {
            super(itemView);

            image=(ImageView)itemView.findViewById(R.id.image);
            name=(TextView)itemView.findViewById(R.id.name);
            cost=(TextView)itemView.findViewById(R.id.cost);
            id=(TextView)itemView.findViewById(R.id.quantity);
            options=(ImageView)itemView.findViewById(R.id.options);

        }
    }
}

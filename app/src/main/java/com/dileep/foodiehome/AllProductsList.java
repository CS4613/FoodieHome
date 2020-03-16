package com.dileep.foodiehome;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dileep.foodiehome.pojos.UploadProductPojo;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllProductsList extends AppCompatActivity {


    RecyclerView recyclerView;
    ProductsAdapter adapter;
    ArrayList<UploadProductPojo> productPojoArrayList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;
    UploadProductPojo uploadProductPojo;
    ProgressBar progressBar;
    Toolbar toolbar;
    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_list);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("vegpizza");
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.products_listview);
        shimmerFrameLayout=(ShimmerFrameLayout)findViewById(R.id.shimmer_view_container);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductsAdapter(productPojoArrayList, AllProductsList.this);
        recyclerView.setAdapter(adapter);
        loadData();

    }

    private void loadTotal() {

    }

    private void loadData() {
       // progressBar.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        //shimmerFrameLayout.setAngle(ShimmerFrameLayout.MaskAngle.CW_180);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                productPojoArrayList.clear();
                System.out.println("datacame:" + dataSnapshot);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    uploadProductPojo = ds.getValue(UploadProductPojo.class);
                    productPojoArrayList.add(uploadProductPojo);
                }
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                //progressBar.setVisibility(View.GONE);
                // progressDialog.dismiss();
                //listview.setAdapter(adapter);
                // products_total.setText(String.valueOf(uploadProductPojoList.size()));

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AllProductsList.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

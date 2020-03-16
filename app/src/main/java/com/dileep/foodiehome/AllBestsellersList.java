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


import com.dileep.foodiehome.pojos.BestsellersPojo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllBestsellersList extends AppCompatActivity {

    RecyclerView recyclerView;
    BestSellersAdapter adapter;
    ArrayList<BestsellersPojo> bestsellersPojoArrayList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;
    BestsellersPojo uploadProductPojo;
    ProgressBar progressBar;
    Toolbar toolbar;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bestsellers_list);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("bestsellers");
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
        adapter = new BestSellersAdapter(bestsellersPojoArrayList, AllBestsellersList.this);
        recyclerView.setAdapter(adapter);
        loadData();
    }


    private void loadData() {
        // progressBar.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                bestsellersPojoArrayList.clear();
                System.out.println("datacame:" + dataSnapshot);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    uploadProductPojo = ds.getValue(BestsellersPojo.class);
                    bestsellersPojoArrayList.add(uploadProductPojo);
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
                Toast.makeText(AllBestsellersList.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.dileep.foodiehome;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dileep.foodiehome.pojos.BestsellersPojo;
import com.dileep.foodiehome.pojos.OffersPojo;
import com.dileep.foodiehome.pojos.UploadProductPojo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CardView addProduct, addOffer, addBestseller,addNonveg;
    Toolbar toolbar;
    FirebaseDatabase database,offersDb,bestesellerDb;
    DatabaseReference reference,offersRef,sellerRef;
    ArrayList<UploadProductPojo> uploadProductPojoList = new ArrayList<>();
    ArrayList<OffersPojo> offersPojoArrayList = new ArrayList<>();
    ArrayList<BestsellersPojo>bestsellersPojoArrayList=new ArrayList<>();
    UploadProductPojo uploadProductPojo = new UploadProductPojo();
    OffersPojo offersPojo=new OffersPojo();
    BestsellersPojo bestsellersPojo=new BestsellersPojo();

    TextView products_total,offers_total,bestseller_total,viewmore;
    Spinner dashspinnerl;
    ArrayList<String> listspin = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("vegpizza");

        offersDb = FirebaseDatabase.getInstance();
        offersRef = database.getReference("offers");

        bestesellerDb = FirebaseDatabase.getInstance();
        sellerRef = database.getReference("bestsellers");

        addProduct = (CardView) findViewById(R.id.addnewpro);
        addOffer = (CardView) findViewById(R.id.offer);
        addBestseller = (CardView) findViewById(R.id.bseller);
        products_total = (TextView) findViewById(R.id.products_total);
        dashspinnerl=(Spinner)findViewById(R.id.dashspinner);
        viewmore=(TextView)findViewById(R.id.viewmore);
        offers_total=(TextView) findViewById(R.id.offers_total);
        bestseller_total=(TextView)findViewById(R.id.incident_total);
        addNonveg=(CardView)findViewById(R.id.addnonveg);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addpro = new Intent(MainActivity.this, AddProduct.class);
                startActivity(addpro);
            }
        });
        addOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addoffer = new Intent(MainActivity.this, AddnewOffers.class);
                startActivity(addoffer);
            }
        });
        addBestseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bestseller = new Intent(MainActivity.this, BestSellers.class);
                startActivity(bestseller);
            }
        });
        addNonveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnonveg = new Intent(MainActivity.this, Addnonveg.class);
                startActivity(addnonveg);
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uploadProductPojoList.clear();
                System.out.println("datacame:" + dataSnapshot);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    uploadProductPojo = ds.getValue(UploadProductPojo.class);
                    uploadProductPojoList.add(uploadProductPojo);
                }
                // progressDialog.dismiss();
                //listview.setAdapter(adapter);
                products_total.setText(String.valueOf(uploadProductPojoList.size()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });

        offersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                offersPojoArrayList.clear();
                System.out.println("datacame:" + dataSnapshot);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    offersPojo = ds.getValue(OffersPojo.class);
                    offersPojoArrayList.add(offersPojo);
                }
                // progressDialog.dismiss();
                //listview.setAdapter(adapter);
                offers_total.setText(String.valueOf(offersPojoArrayList.size()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });

        sellerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bestsellersPojoArrayList.clear();
                System.out.println("datacame:" + dataSnapshot);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    bestsellersPojo = ds.getValue(BestsellersPojo.class);
                    bestsellersPojoArrayList.add(bestsellersPojo);
                }
                // progressDialog.dismiss();
                //listview.setAdapter(adapter);
                bestseller_total.setText(String.valueOf(bestsellersPojoArrayList.size()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });

        listspin.add("Products");
        listspin.add("Offers");
        listspin.add("BestSellers");
        listspin.add("Something");

        ArrayAdapter<String> repeatadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, listspin);
        dashspinnerl.setAdapter(repeatadapter);


        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dashspinnerl.getSelectedItemPosition()){

                    case 0:
                        Intent ProductsView = new Intent(MainActivity.this, AllProductsList.class);
                        startActivity(ProductsView);
                        break;
                    case 1:
                        Intent alloferslist=new Intent(MainActivity.this,AllOffersList.class);
                        startActivity(alloferslist);

                        break;
                    case 2:
                        Intent incident=new Intent(MainActivity.this,AllBestsellersList.class);
                        startActivity(incident);
                        break;
                    case 3:

                       /* Intent notifications=new Intent(DashboardHome.this,NotificationsView.class);
                        startActivity(notifications);*/

                        break;


                }

            }
        });

    }
}

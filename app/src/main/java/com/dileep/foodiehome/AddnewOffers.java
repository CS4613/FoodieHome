package com.dileep.foodiehome;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dileep.foodiehome.pojos.OffersPojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class AddnewOffers extends AppCompatActivity {

    ArrayList category;
    ArrayAdapter rectypeadp;
    Spinner categoryspin;
    String selectedRecordtype,productId,whatsappNum="7729958790";
    Toolbar toolbar,toolbarD;
    EditText proName,proCost,offerpercent;
    Button selectpics,uploadproduct,selectcolors,submitcolor;
    public static RecyclerView selectedListview,selectcolrsView;

    public static ImageView addimg;
    SelectedviewAdapter adapter;
    public static ArrayList<String> selectedpicsList =new ArrayList<>();
    ArrayList<String> imagesPathList=new ArrayList<>();
    private static final int RESULT_LOAD_IMAGE = 501;
    Dialog dialog_primary;
    TextView colorsTextView;
    SelectColorsAdapter colorsAdapter;
    private StorageReference mStorage;
    ProgressBar progressBar;
    public static ArrayList<String> selectedcolorsList=new ArrayList<>();
    ArrayList<ColorsPojo> colorsPojoArrayList=new ArrayList<>();
    private StorageTask mUploadTask;
    int count=0;
    public static ArrayList<String>imageUrls=new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog progressDialog;
    String offerPercent;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_offers);
        mStorage= FirebaseStorage.getInstance().getReference("offers");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("offers");
        selectpics = (Button) findViewById(R.id.selectpics);
        uploadproduct=(Button)findViewById(R.id.uploadpro);
        addimg=(ImageView)findViewById(R.id.addimg);
        selectcolors=(Button)findViewById(R.id.selectcolors);
        colorsTextView=(TextView)findViewById(R.id.colorstextView);
        selectedListview=(RecyclerView)findViewById(R.id.selectedListview);
        offerpercent=(EditText)findViewById(R.id.offerpercent);
        proName=(EditText)findViewById(R.id.proname);
        proCost=(EditText)findViewById(R.id.cost);
        selectedListview.setHasFixedSize(true);
        selectedListview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new SelectedviewAdapter(selectedpicsList, AddnewOffers.this,2);
        selectedListview.setAdapter(adapter);
        categoryspin=(Spinner)findViewById(R.id.categoryspin);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation_back);
        setSupportActionBar(toolbar);
        colors();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });
        selectpics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

        uploadproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Random random = new Random();
                int num = random.nextInt();
                //imageUrls=new ArrayList<>();
                if (selectedpicsList != null && selectedpicsList.size() > 0) {
                    if (!proName.getText().toString().isEmpty()) {

                        if (!selectedRecordtype.isEmpty()) {

                            if (!proCost.getText().toString().isEmpty()) {
                                if (!offerpercent.getText().toString().isEmpty()) {

                                    String productname=proName.getText().toString();
                                    productname=productname.trim().replace(" ", "");
                                    productId = "dil" + productname + proCost.getText() + num;
                                    // progressBar.setVisibility(View.VISIBLE);
                                    progressDialog = new ProgressDialog(AddnewOffers.this);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();


                                    final String[] urls = new String[selectedpicsList.size()];
                                    for (int i = 0; i < selectedpicsList.size(); i++) {

                                        String picLocalpath = selectedpicsList.get(i);
                                        System.out.println("files loaction:" + picLocalpath);
                                        final StorageReference fileReference = mStorage.child(System.currentTimeMillis()
                                                + "." + getFileExtension(Uri.parse(picLocalpath)));

                                        final int finalI1 = i;
                                 /*       fileReference.putFile(Uri.parse(picLocalpath)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        // Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                                                    }
                                                });
                                            }
                                        });*/
                                        mUploadTask = fileReference.putFile(Uri.parse(picLocalpath)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        // Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                                                        String downloadUrl = uri.toString();
                                                        imageUrls.add(downloadUrl);
                                                        System.out.println("downloadUrl:" + downloadUrl);
                                                       // Toast.makeText(AddnewOffers.this, downloadUrl, Toast.LENGTH_SHORT).show();
                                                        count = count + 1;
                                                        if (count == selectedpicsList.size()) {
                                                            //  progressBar.setVisibility(View.GONE);
                                                            progressDialog.dismiss();
                                                            Addmetadata();
                                                        }
                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // progressBar.setVisibility(View.GONE);
                                                progressDialog.dismiss();
                                                Toast.makeText(AddnewOffers.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                            }
                                        });
                                    }

                                }else {
                                    Toast.makeText(AddnewOffers.this, "Please enter percent", Toast.LENGTH_SHORT).show();
                                }
                                // System.out.println("the size:"+selectedpicsList.size());

                            } else {
                                Toast.makeText(AddnewOffers.this, "please enter cost", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddnewOffers.this, "please select category", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddnewOffers.this, "please enter product name", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddnewOffers.this, "Please select images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectcolors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_primary = new Dialog(AddnewOffers.this, android.R.style.Theme_Light_NoTitleBar);
                dialog_primary.setContentView(R.layout.select_colors);
                selectcolrsView=(RecyclerView)dialog_primary.findViewById(R.id.selectcolrsView);
                submitcolor=(Button) dialog_primary.findViewById(R.id.submitcolor);
                toolbarD=(Toolbar)dialog_primary.findViewById(R.id.toolbar);
                toolbarD.setNavigationIcon(R.drawable.navigation_back);
                toolbarD.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (selectedcolorsList.size()>0){
                            colorsTextView.setText(selectedcolorsList.toString());
                            selectcolors.setText("Change selected colors");
                        }else {
                            colorsTextView.setText("No colors selected");
                            selectedcolorsList.clear();
                            selectcolors.setText("Select available colors");
                        }
                        dialog_primary.dismiss();

                    }
                });
                submitcolor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (selectedcolorsList.size()>0){
                            colorsTextView.setText(selectedcolorsList.toString());
                            selectcolors.setText("Change selected colors");
                        }else {
                            colorsTextView.setText("No colors selected");
                            selectedcolorsList.clear();
                            selectcolors.setText("Select available colors");
                        }
                        dialog_primary.dismiss();
                    }
                });

                selectcolrsView.setHasFixedSize(true);
                selectcolrsView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                colorsAdapter=new SelectColorsAdapter(colorsPojoArrayList,AddnewOffers.this, 2);
                selectcolrsView.setAdapter(colorsAdapter);
                dialog_primary.show();



            }
        });

        loadSpinner();
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selectedpicsList.clear();
        selectedcolorsList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectedpicsList.clear();
        selectedcolorsList.clear();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int totalItemsSelected = data.getClipData().getItemCount();
                for (int i=0;i<totalItemsSelected;i++){
                    String url=data.getClipData().getItemAt(i).getUri().toString();
                    selectedpicsList.add(url);

                }
                if (selectedpicsList.size()>0) {
                    addimg.setVisibility(View.GONE);
                    selectedListview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(this, "Please select pics", Toast.LENGTH_SHORT).show();
                    addimg.setVisibility(View.VISIBLE);
                    selectedListview.setVisibility(View.GONE);
                }

            }else if (data.getData()!=null) {
                //Toast.makeText(this, "data empty", Toast.LENGTH_SHORT).show();


                //If uploaded with Android Gallery (max 1 image)
                Uri selectedImage = data.getData();
                InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    selectedpicsList.add(selectedImage.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (selectedpicsList.size() > 0) {
                    addimg.setVisibility(View.GONE);
                    selectedListview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                       /* adapter = new SelectedviewAdapter(imagesPathList, AddProduct.this);
                        selectedListview.setAdapter(adapter);*/
                } else {
                    Toast.makeText(this, "Please select pics", Toast.LENGTH_SHORT).show();
                    addimg.setVisibility(View.VISIBLE);
                    selectedListview.setVisibility(View.GONE);
                }

            } else {
                Toast.makeText(this, "data empty", Toast.LENGTH_SHORT).show();
            }


        }
    }
    private void Addmetadata() {

        System.out.println("imageurls:"+imageUrls);
        OffersPojo offersPojo=new OffersPojo(proName.getText().toString(),productId,proCost.getText().toString(),selectedRecordtype,whatsappNum,offerpercent.getText().toString(),selectedcolorsList,imageUrls);
        /*proName.getText().toString(),productId,proCost.getText().toString(),
                selectedRecordtype,whatsappNum,selectedcolorsList,imageUrls);*/
        myRef.child(productId).setValue(offersPojo);
        Toast.makeText(AddnewOffers.this, "Product successfully uploaded", Toast.LENGTH_SHORT).show();
        imageUrls.clear();
        finish();
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void colors() {
        colorsPojoArrayList.add(new ColorsPojo("Blue color",false));
        colorsPojoArrayList.add(new ColorsPojo("Red color",false));
        colorsPojoArrayList.add(new ColorsPojo("Green color",false));
        colorsPojoArrayList.add(new ColorsPojo("Yellow color",false));
        colorsPojoArrayList.add(new ColorsPojo("Orange color",false));
        colorsPojoArrayList.add(new ColorsPojo("Meroon color",false));
        colorsPojoArrayList.add(new ColorsPojo("Pink color",false));
        colorsPojoArrayList.add(new ColorsPojo("comic color",false));
        colorsPojoArrayList.add(new ColorsPojo("dil color",false));
        colorsPojoArrayList.add(new ColorsPojo("blueee color",false));
        colorsPojoArrayList.add(new ColorsPojo("prank color",false));
        colorsPojoArrayList.add(new ColorsPojo("some color",false));
        colorsPojoArrayList.add(new ColorsPojo("clinki color",false));
        colorsPojoArrayList.add(new ColorsPojo("test color",false));
    }

    private void loadSpinner() {
        category = new ArrayList<>();
        category.add("Select Category");
        category.add("Mobiles");
        category.add("Computers");
        category.add("Men Fashion");
        category.add("Women Fashion");
        category.add("Other");
        rectypeadp = new ArrayAdapter<String>(AddnewOffers.this, android.R.layout.simple_spinner_dropdown_item, category);
        categoryspin.setAdapter(rectypeadp);


        categoryspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedRecordtype = categoryspin.getSelectedItem().toString();
                    System.out.println("selected recordtype:" + selectedRecordtype);
                } else {
                    selectedRecordtype = "";
                    System.out.println("selected recordtype:" + selectedRecordtype);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

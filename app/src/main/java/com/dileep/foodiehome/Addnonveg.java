package com.dileep.foodiehome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.dileep.foodiehome.pojos.UploadProductPojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Addnonveg extends AppCompatActivity {
    ArrayList category;
    ArrayAdapter rectypeadp;
    Spinner categoryspin;

    String selectedRecordtype,productId,whatsappNum="7729958790";
    String admin="likhatech";
    Toolbar toolbar,toolbarD;
    EditText proName,proCost;
    Button selectpics,uploadproduct,selectcolors,submitcolor;
    public static RecyclerView selectedListview,selectcolrsView;
    public static ImageView addimg;
    SelectedviewAdapter adapter;
    public static ArrayList<String> selectedpicsList =new ArrayList<>();
    ArrayList<String> imagesPathList=new ArrayList<>();
    private static final int RESULT_LOAD_IMAGE = 501;
    private static final int CAMERA_REQUEST_CODE = 502;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri mImageUri;
    String getImageUrl;
    private Uri fileUri = null;
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
    public static ArrayList<String>imageNames=new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog progressDialog;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnonveg);

        mStorage= FirebaseStorage.getInstance().getReference("nonvegpizza");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("nonvegpizza");
        categoryspin = (Spinner) findViewById(R.id.categoryspin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation_back);
        selectpics = (Button) findViewById(R.id.selectpics);
        uploadproduct=(Button)findViewById(R.id.uploadpro);
        //  progressBar=(ProgressBar)findViewById(R.id.progressbar);
        addimg=(ImageView)findViewById(R.id.addimg);
        selectcolors=(Button)findViewById(R.id.selectcolors);
        colorsTextView=(TextView)findViewById(R.id.colorstextView);
        selectedListview=(RecyclerView)findViewById(R.id.selectedListview);
        proName=(EditText)findViewById(R.id.proname);
        proCost=(EditText)findViewById(R.id.cost);
        selectedListview.setHasFixedSize(true);
        selectedListview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new SelectedviewAdapter(selectedpicsList, Addnonveg.this,1);
        selectedListview.setAdapter(adapter);
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

                selectImage();

               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
*/
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

                                String productname=proName.getText().toString();
                                productname=productname.trim().replace(" ", "");
                                productId = "dil" + productname + proCost.getText() + num;
                                // progressBar.setVisibility(View.VISIBLE);
                                progressDialog=new ProgressDialog(Addnonveg.this);
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();


                                final String[] urls = new String[selectedpicsList.size()];
                                for (int i = 0; i < selectedpicsList.size(); i++) {

                                    String picLocalpath = selectedpicsList.get(i);
                                    System.out.println("files loaction:" + picLocalpath);

                                    final StorageReference fileReference = mStorage.child("dil"+proName.getText().toString()+proCost.getText().toString()+System.currentTimeMillis()
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
                                                    String filename=fileReference.getName();
                                                    String downloadUrl = uri.toString();
                                                    imageUrls.add(downloadUrl);
                                                    imageNames.add(filename);
                                                    System.out.println("filenamedil:" + filename);
                                                    // Toast.makeText(AddProduct.this, downloadUrl, Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Addnonveg.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                        }
                                    });

                                }


                                // System.out.println("the size:"+selectedpicsList.size());

                            } else {
                                Toast.makeText(Addnonveg.this, "please enter cost", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Addnonveg.this, "please select category", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Addnonveg.this, "please enter product name", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Addnonveg.this, "Please select images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectcolors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_primary = new Dialog(Addnonveg.this, android.R.style.Theme_Light_NoTitleBar);
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
                colorsAdapter=new SelectColorsAdapter(colorsPojoArrayList,Addnonveg.this,1);
                selectcolrsView.setAdapter(colorsAdapter);
                dialog_primary.show();



            }
        });
        loadSpinner();
    }


    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Choose from Gallery")) {
                    selectImageFromGallery();

                } else if (items[item].equals("Take Photo")) {

                    snapPicureFromCamera();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void selectImageFromGallery(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

    }
    public void snapPicureFromCamera(){

       /* Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent, RESULT_LOAD_IMAGE);*/
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
        }
        else {

            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

                // startActivityForResult(pictureIntent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                if (grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "storage permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void Addmetadata() {

        System.out.println("imageurls:"+imageUrls);
        UploadProductPojo uploadProductPojo=new UploadProductPojo(proName.getText().toString(),productId,proCost.getText().toString(),
                selectedRecordtype,whatsappNum,selectedcolorsList,imageUrls,admin,imageNames);

        myRef.child(productId).setValue(uploadProductPojo);
        Toast.makeText(Addnonveg.this, "Product successfully uploaded", Toast.LENGTH_SHORT).show();
        imageUrls.clear();
        finish();
    }

    private String getFileExtension(Uri uri) {

        System.out.println("recieved URI");
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

                    System.out.println("cameraimage:"+url);

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
                    System.out.println("cameraimage:"+selectedImage);
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
                Toast.makeText(this, "Something went wrong. please use Gallery", Toast.LENGTH_SHORT).show();

            }


        }

       /* else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

              Uri   mImageUri = data.getData();
                //mSelectImage.setImageURI(mImageUri);
                //selectedpicsList.add(mImageUri.toString());
           // selectedpicsList.add(mImageUri.toString());
            if (selectedpicsList.size() > 0) {
                addimg.setVisibility(View.GONE);
                selectedListview.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                       *//* adapter = new SelectedviewAdapter(imagesPathList, AddProduct.this);
                        selectedListview.setAdapter(adapter);*//*
            } else {
                Toast.makeText(this, "Please select pics", Toast.LENGTH_SHORT).show();
                addimg.setVisibility(View.VISIBLE);
                selectedListview.setVisibility(View.GONE);
            }






        *//* Bitmap mImageUri1 = (Bitmap) data.getExtras().get("data");
         mSelectImage.setImageBitmap(mImageUri1);

          Toast.makeText(this, "Image saved to:\n" +
                  data.getExtras().get("data"), Toast.LENGTH_LONG).show();


*//*



            }*/

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                // mSelectImage.setImageURI(resultUri);
                selectedpicsList.add(resultUri.toString());
                System.out.println("cameraimage: "+resultUri);
                //   Uri  dilmImageUri = resultUri;

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

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public void remove(int position){
        if (selectedpicsList.size()>0) {
            addimg.setVisibility(View.GONE);
            selectedListview.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, "Please select pics", Toast.LENGTH_SHORT).show();
            addimg.setVisibility(View.VISIBLE);
            selectedListview.setVisibility(View.GONE);
        }
    }
    private void loadSpinner() {
        category = new ArrayList<>();
        category.add("Select Category");
        category.add("Mobiles");
        category.add("Computers");
        category.add("Men Fashion");
        category.add("Women Fashion");
        category.add("Other");
        rectypeadp = new ArrayAdapter<String>(Addnonveg.this, android.R.layout.simple_spinner_dropdown_item, category);
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

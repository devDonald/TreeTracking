package com.seldavine.tree_tracking.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.seldavine.tree_tracking.Model.AllTotal;
import com.seldavine.tree_tracking.Model.BoundaryModel;
import com.seldavine.tree_tracking.R;
import com.seldavine.tree_tracking.Utility.GMailSender;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;

public class BoundaryDisputes extends AppCompatActivity {
    private EditText et_reporter, et_dispute_location, et_manualLat,et_manualLong;
    private EditText et_disputername1,et_disputer_email1, et_disputer_name2,et_no_trees;
    private EditText et_disputer_email2, et_witness_name, et_witness_email;
    private TextView tv_dispute_coordinate;
    private Spinner sp_tree_type;
    private Button reportButon;
    private DatabaseReference boundaryDatabase;
    private StorageReference boundaryImages;
    private ImageView addTree, displayTree, addTree1, addTree2, displayTree1,displayTree2;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE1 = 2;
    private static final int CAMERA_REQUEST_CODE2 = 3;
    private String noTrees, treeType, id, country;
    private String firstname, lastname, dispute_location,manualLat,manualLong,witnessName;
    private String disputerName1,disputerEmail1,disputerName2, disputerEmail2, witnessEmail;
    private double latitude, longitude;
    private ProgressDialog dialog;
    private FirebaseUser user;
    private String uid, postDate;
    private Uri uri_image1,uri_image2;
    private GMailSender sender;
    private KProgressHUD hud;



    @SuppressLint("NewApi")

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boundary_disputes);

        et_reporter=findViewById(R.id.bd_person_name);
        et_dispute_location = findViewById(R.id.bd_resolution_location);
        et_manualLat = findViewById(R.id.bdManualLat);
        et_manualLong = findViewById(R.id.bdManualLong);
        et_disputername1= findViewById(R.id.dbDisputerName1);
        et_disputer_email1 = findViewById(R.id.dbDisputerEmail1);
        et_disputer_name2 = findViewById(R.id.dbDisputerName2);
        et_disputer_email2 = findViewById(R.id.dbDisputerEmail2);
        et_witness_name = findViewById(R.id.dbDisputeWitnessName);
        et_witness_email=findViewById(R.id.dbDisputeWitnessEmail);
        sp_tree_type= findViewById(R.id.bd_spTreeType);
        reportButon = findViewById(R.id.bd_submit);
        addTree = findViewById(R.id.bd_add_tree);
        tv_dispute_coordinate = findViewById(R.id.bd_tree_coordinates);
        displayTree = findViewById(R.id.bd_treeImages);
        dialog = new ProgressDialog(getApplicationContext());
        addTree1 = findViewById(R.id.bd_add_tree1);
        addTree2 = findViewById(R.id.bd_add_tree2);
        et_no_trees = findViewById(R.id.bdNoTrees);
        displayTree1 = findViewById(R.id.bd_treeImages1);
        displayTree2 = findViewById(R.id.bd_treeImages2);
        sender = new GMailSender("treetrackingapp@gmail.com", "fellowship");


        hud = KProgressHUD.create(BoundaryDisputes.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait")
                .setDetailsLabel("Reporting dispute resolution...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(Color.GREEN)
                .setAutoDismiss(true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.

                Builder().permitAll().build();



        StrictMode.setThreadPolicy(policy);

        dialog= new ProgressDialog(BoundaryDisputes.this);
        postDate = DateFormat.getDateTimeInstance().format(new Date());

        boundaryImages = FirebaseStorage.getInstance().getReference().child("BoundaryImages");

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            firstname = bundle.getString("firstname");
            lastname = bundle.getString("lastname");
            latitude = bundle.getDouble("lat");
            longitude = bundle.getDouble("long");
            country = bundle.getString("country");

        }
        boundaryDatabase = FirebaseDatabase.getInstance().getReference().child(country).child("Boundary Disputes");

        id = boundaryDatabase.push().getKey();
        tv_dispute_coordinate.setText(latitude + ", " + longitude);
        et_reporter.setText(lastname + " " + firstname);



        addTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }
            }
        });
        addTree1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE1);
                }
            }
        });

        addTree2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE2);
                }
            }
        });


        reportButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                disputerName1 = et_disputername1.getText().toString().trim();
                disputerEmail1 = et_disputer_email1.getText().toString().trim();
                disputerName2 = et_disputer_name2.getText().toString().trim();
                disputerEmail2=et_disputer_email2.getText().toString().trim();
                witnessName = et_witness_name.getText().toString().trim();
                witnessEmail = et_witness_email.getText().toString().trim();
                treeType = sp_tree_type.getItemAtPosition(sp_tree_type.getSelectedItemPosition()).toString();
                dispute_location = et_dispute_location.getText().toString().trim();
                noTrees = et_no_trees.getText().toString().trim();
                manualLat = et_manualLat.getText().toString().trim();
                manualLong = et_manualLong.getText().toString().trim();

                if (TextUtils.isEmpty(noTrees)){
                    MDToast.makeText(getApplication(),"Pls Add No of trees",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                } else if (treeType.equalsIgnoreCase("Select the tree type")){
                    MDToast.makeText(getApplication(),"Pls Select a valid tree type",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                } else if (TextUtils.isEmpty(dispute_location)){
                    MDToast.makeText(getApplication(),"Pls enter the dispute location",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                    et_dispute_location.setError("location empty");
                }else if (TextUtils.isEmpty(disputerName1)){
                    MDToast.makeText(getApplication(),"Pls enter the disputer name 1",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                    et_disputername1.setError("name empty");
                }else if (TextUtils.isEmpty(disputerEmail1)){
                    MDToast.makeText(getApplication(),"Pls enter the disputer email 1",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                    et_disputer_email1.setError("email empty");
                }else if (TextUtils.isEmpty(disputerName2)){
                    MDToast.makeText(getApplication(),"Pls enter the disputer name 2",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                    et_disputer_name2.setError("name empty");
                }else if (TextUtils.isEmpty(disputerEmail2)){
                    MDToast.makeText(getApplication(),"Pls enter the disputer email 2",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                    et_disputer_email2.setError("email empty");
                }else if (TextUtils.isEmpty(witnessName)){
                    MDToast.makeText(getApplication(),"Pls enter the name of witness",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                    et_witness_name.setError("name empty");
                }else if (TextUtils.isEmpty(witnessEmail)){
                    MDToast.makeText(getApplication(),"Pls enter the email of witness",
                            MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                    et_witness_email.setError("email empty");
                }

                else {
                    try {
                        reportResolution();

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Bitmap bitmap= (Bitmap)data.getExtras().get("data");
            displayTree.setImageBitmap(bitmap);

        }
        if (requestCode == CAMERA_REQUEST_CODE1 && resultCode == RESULT_OK) {

            Bitmap bitmap= (Bitmap)data.getExtras().get("data");
            displayTree1.setImageBitmap(bitmap);

            final StorageReference mountainsRef2 = boundaryImages.child(uid).child(id).child("image1.jpg");
            if (displayTree1!=null) {

                displayTree1.setDrawingCacheEnabled(true);
                displayTree1.buildDrawingCache();
                Bitmap bitmap2 = displayTree1.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();

                UploadTask uploadTask = mountainsRef2.putBytes(data1);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return mountainsRef2.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        uri_image1 = task.getResult();
                    }
                });

            }


        }
        if (requestCode == CAMERA_REQUEST_CODE2 && resultCode == RESULT_OK) {

            Bitmap bitmap= (Bitmap)data.getExtras().get("data");
            displayTree2.setImageBitmap(bitmap);

            final StorageReference mountainsRef3 = boundaryImages.child(uid).child(id).child("image2.jpg");
            if (displayTree2!=null) {

                displayTree2.setDrawingCacheEnabled(true);
                displayTree2.buildDrawingCache();
                Bitmap bitmap2 = displayTree2.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();

                UploadTask uploadTask = mountainsRef3.putBytes(data1);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return mountainsRef3.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        uri_image2 = task.getResult();

                    }
                });
            }

        }

    }

    public void reportResolution(){
        hud.show();
        final StorageReference mountainsRef = boundaryImages.child(uid).child(id).child("image.jpg");
        if (displayTree!=null) {

            displayTree.setDrawingCacheEnabled(true);
            displayTree.buildDrawingCache();
            Bitmap bitmap = displayTree.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);

            Task<Uri> uriTask1 = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return mountainsRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadURI = task.getResult();

                    BoundaryModel model = new BoundaryModel(uid,lastname + " " + firstname,
                            latitude, longitude,disputerName1,disputerName2,dispute_location,
                            disputerEmail1,disputerEmail2,treeType,noTrees,witnessName,witnessEmail,
                            uri_image1.toString(),uri_image2.toString(),downloadURI.toString());

                    boundaryDatabase.child(id).setValue(model);
                    boundaryDatabase.child(id).child("report_date").setValue(postDate);
                    if (manualLat!=null && manualLong!=null){
                        boundaryDatabase.child(id).child("manualLatitude").setValue(manualLat);
                        boundaryDatabase.child(id).child("manualLongitude").setValue(manualLong);
                    }
                    final DatabaseReference totalNo = FirebaseDatabase.getInstance().getReference().child("Total");

                    totalNo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            AllTotal allmodel = dataSnapshot.getValue(AllTotal.class);
                            int aff = allmodel.getAfforestation();
                            aff = aff+Integer.parseInt(noTrees);
                            totalNo.child("afforestation").setValue(aff);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    hud.dismiss();
                    try {
                        new MyAsyncClass().execute();
                    }catch (Exception e){

                    }

                    MDToast.makeText(getApplication(),"Dispute Successfully Reported",
                            MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();

                    Intent intent = new Intent(BoundaryDisputes.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

        } else{
            MDToast.makeText(getApplication(),"image Empty",
                    MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
        }

    }



    class MyAsyncClass extends AsyncTask<Void, Void, Void> {



        @Override

        protected void onPreExecute() {

            super.onPreExecute();


        }



        @Override

        protected Void doInBackground(Void... mApi) {

            try {

                // Add subject, Body, your mail Id, and receiver mail Id.


                sender.addAttachment(uri_image1.toString());

                sender.sendMail("Seldavine Boundary Dispute Resolution Agreement",
                        " This is the land dispute Resolution Agreement made at the border of the land of "+
                                disputerName1+" and " + disputerName2+" with "+witnessName+ " Serving as Witness in "+dispute_location+
                                " ", "treetrackingapp@gmail.com", disputerEmail1);
                sender.sendMail("Seldavine Boundary Dispute Resolution Agreement",
                        " This is the land dispute Resolution Agreement made at the border of the land of "+
                                disputerName1+" and " + disputerName2+" with "+witnessName+ " Serving as Witness in "+dispute_location+
                                " ", "treetrackingapp@gmail.com", disputerEmail2);
                sender.sendMail("Seldavine Boundary Dispute Resolution Agreement",
                        " This is the land dispute Resolution Agreement made at the border of the land of "+
                                disputerName1+" and " + disputerName2+" with "+witnessName+ " Serving as Witness in "+dispute_location+
                                " ", "treetrackingapp@gmail.com", witnessEmail);

            }



            catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;

        }



        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);


            MDToast.makeText(getApplicationContext(), "Email sent", MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();

        }

    }



}

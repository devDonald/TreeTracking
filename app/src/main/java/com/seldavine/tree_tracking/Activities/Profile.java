package com.seldavine.tree_tracking.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.seldavine.tree_tracking.Model.PlantingModel;
import com.seldavine.tree_tracking.Model.SignUpModel;
import com.seldavine.tree_tracking.R;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private CircleImageView userImage;
    private ImageView addImage;
    private TextView tvuserName,tvMuta,tvMadiba,tvGhandi;
    private TextView tvuserLocation,tvMutaPoints,tvMadibaPoints,tvGhandiPoints;
    private TextView tvuserPoints;
    private String username;

    private DatabaseReference userRef;
    private DatabaseReference treesPlanted, defTrees;
    private StorageReference userPics;
    private static final int GALLERY_REQUEST =78;
    private String uid;
    private FirebaseUser user;
    private String userId;
    private String noOfTree,economicTrees, nonEconsTree, deforestTree;
    private int totalTreesPlanted, totalEcons,totalNonEcons,totalDef;
    private Button goBack;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userImage = findViewById(R.id.iv_userImage);
        addImage = findViewById(R.id.iv_add_userimage);
        tvuserName = findViewById(R.id.userName);
        tvuserLocation = findViewById(R.id.userLocation);
        tvuserPoints = findViewById(R.id.userPoint);
        goBack = findViewById(R.id.back);
        tvMadiba = findViewById(R.id.madiba_necons);
        tvGhandi = findViewById(R.id.ghandi_def);
        tvMuta = findViewById(R.id.muta_econs);
        tvMutaPoints = findViewById(R.id.muta_points);
        tvMadibaPoints = findViewById(R.id.madiba_points);
        tvGhandiPoints = findViewById(R.id.ghandi_points);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userPics= FirebaseStorage.getInstance().getReference();
        uid = user.getUid();
        Log.d("uid",""+uid);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            country = bundle.getString("country");

        }

        defTrees = FirebaseDatabase.getInstance().getReference().child(country).child("Deforestation");
        treesPlanted = FirebaseDatabase.getInstance().getReference().child(country).child("Afforestation");

        treesPlanted.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalTreesPlanted=0;
                totalEcons = 0;
                totalNonEcons = 0;
                for (DataSnapshot ds :dataSnapshot.getChildren()){
                    PlantingModel model = ds.getValue(PlantingModel.class);
                    userId = model.getUid();
                    Log.d("userid",""+userId);
                    if (userId.matches(uid)){
                        noOfTree=model.getNoOfTrees();
                        totalTreesPlanted=totalTreesPlanted+Integer.parseInt(noOfTree);

                        if (model.getTypeOfTrees().matches("Economic")){
                            economicTrees = model.getNoOfTrees();
                            totalEcons = totalEcons + Integer.parseInt(economicTrees);
                        }
                        if (model.getTypeOfTrees().matches("Non Economics")){
                            nonEconsTree = model.getNoOfTrees();
                            totalNonEcons = totalNonEcons + Integer.parseInt(nonEconsTree);
                        }
                    }


                    Log.d("nooftree",""+noOfTree);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        defTrees.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalDef = 0;
                for (DataSnapshot ds :dataSnapshot.getChildren()){
                    PlantingModel model = ds.getValue(PlantingModel.class);
                    userId = model.getUid();
                    Log.d("userid",""+userId);
                    if (userId.matches(uid)){
                       deforestTree =  model.getNoOfTrees();
                       totalDef = totalDef + Integer.parseInt(deforestTree);
                    }


                    Log.d("nooftree",""+noOfTree);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SignUpModel userModel = dataSnapshot.getValue(SignUpModel.class);
                tvuserName.setText(userModel.getFirstName()+" "+userModel.getLastName());
                tvuserLocation.setText(userModel.getCountry());
                tvuserPoints.setText(""+totalTreesPlanted);
                tvMuta.setText(""+totalEcons);
                tvMadiba.setText(""+totalNonEcons);
                tvGhandi.setText(""+totalDef);
                tvMutaPoints.setText(""+totalEcons*2);
                tvMadibaPoints.setText(""+totalNonEcons);
                double gandi = Double.valueOf(totalDef);
                tvGhandiPoints.setText(""+gandi/2);

                String user_image = dataSnapshot.child("userImage").getValue(String.class);
                Picasso.get().load(user_image).into(userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galIntent, "Choose picture"), GALLERY_REQUEST);
            }
        });


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)

        {
           Uri imageUri = data.getData();

           final StorageReference file = userPics.child("ProfileImages").child(imageUri.getLastPathSegment());

            Task<Uri> uriTask = file.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return file.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri imagePath = task.getResult();

                    userRef.child(uid).child("userImage").setValue(imagePath.toString());
                    refreshImage();
                    MDToast.makeText(getApplication(),"image Added",
                            MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();
                }
            });


        }

    }
    private void refreshImage(){

        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_image = dataSnapshot.child("userImage").getValue(String.class);
                Picasso.get().load(user_image).into(userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

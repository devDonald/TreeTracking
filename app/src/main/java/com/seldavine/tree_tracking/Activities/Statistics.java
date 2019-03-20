package com.seldavine.tree_tracking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seldavine.tree_tracking.Model.AllTotal;
import com.seldavine.tree_tracking.Model.DeforestationModel;
import com.seldavine.tree_tracking.Model.PlantingModel;
import com.seldavine.tree_tracking.R;

public class Statistics extends AppCompatActivity {

    private Button btnRetrieve;
    private TextView tvAfforestEconomics,tvAllAfforestation,tvAllDeforestation;
    private TextView tvAfforestNonEco,tv_volumeOfCo2;

    private TextView tvDeforestEconomics,tvCountry;
    private TextView tvDeforestNonEco;
    private DatabaseReference treeRef, allAdditionRef;
    private DatabaseReference defRef;
    private String noEcoAffoTree;
    private String noNEcoAffoTree;
    private String noEcoDefTree;
    private String noNonEcoDefTree;
    private String treeType;
    private String treeTypeDef,st_volume_co2;
    private int treenoEco, totalAfforestation,totalDeforestation;
    private int treenoNonEco;
    private int defTreeNoEco;
    private int defTreeNoNonEco,int_volume_co2;
    private Button displayAfforestation;
    private DatabaseReference userRef;
    private String country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        tvAfforestEconomics = findViewById(R.id.tvNoOfTrees);
        tvAfforestNonEco =findViewById(R.id.tvNoOfTreesN);
        displayAfforestation = findViewById(R.id.displayAfforestation);
        tvDeforestEconomics = findViewById(R.id.tvNoOfEcoTreeReported);
        tvDeforestNonEco = findViewById(R.id.tvNoOfNonEcoReported);
        tvCountry = findViewById(R.id.stats_country);
        tvAllAfforestation = findViewById(R.id.tvNoAff);
        tvAllDeforestation = findViewById(R.id.tvNoDef);
        tv_volumeOfCo2 = findViewById(R.id.tv_volume_co2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

       try {
           if (bundle != null) {
               country = bundle.getString("country");
               tvCountry.setText(country);

           }

       }catch (Exception e){
           e.printStackTrace();
       }

       try {
           allAdditionRef = FirebaseDatabase.getInstance().getReference().child("Total");
           treeRef= FirebaseDatabase.getInstance().getReference().child(country).child("Afforestation");
           defRef = FirebaseDatabase.getInstance().getReference().child(country).child("Deforestation");


           allAdditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {

                   AllTotal allmodel = dataSnapshot.getValue(AllTotal.class);
                   totalAfforestation = allmodel.getAfforestation();
                   totalDeforestation = allmodel.getDeforestation();

                   int_volume_co2 = totalAfforestation * 25;
                   tv_volumeOfCo2.setText("Average volume of CO2 Absorbed by Reported Trees per year  = "+int_volume_co2);
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
           treeRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   try {
                       treenoEco=0;
                       treenoNonEco=0;
                       for (DataSnapshot ds : dataSnapshot.getChildren()) {
                           PlantingModel model = ds.getValue(PlantingModel.class);
                           treeType = model.getTypeOfTrees();
                           if (treeType.matches("Economic")) {
                               noEcoAffoTree = model.getNoOfTrees();
                               //Log.d("ecotree",""+noEcoAffoTree);
                               treenoEco = treenoEco + Integer.parseInt(noEcoAffoTree);
                           } else if (treeType.matches("Non Economics")) {
                               noNEcoAffoTree = model.getNoOfTrees();
                               treenoNonEco = treenoNonEco + Integer.parseInt(noNEcoAffoTree);

                           }
                       }
                   } catch (Exception e){

                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });

           defRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {

                   try {
                       for (DataSnapshot ds : dataSnapshot.getChildren()) {
                           DeforestationModel defmodel = ds.getValue(DeforestationModel.class);
                           treeTypeDef = defmodel.getTypeOfTrees();

                           if (treeTypeDef.matches("Economic")){
                               noEcoDefTree =defmodel.getNoOfTrees();
                               defTreeNoEco=defTreeNoEco+Integer.parseInt(noEcoDefTree);
                           }else  if (treeTypeDef.matches("Non Economics")){
                               noNonEcoDefTree =defmodel.getNoOfTrees();
                               defTreeNoNonEco=defTreeNoNonEco+Integer.parseInt(noNonEcoDefTree);
                           }

                       }
                   }catch (Exception e){
                       e.printStackTrace();
                   }

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });


           displayAfforestation.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //Log.d("treeNo",""+treenoEco);
                   tvAfforestEconomics.setText(""+treenoEco);
                   tvAfforestNonEco.setText(""+treenoNonEco);
                   tvDeforestEconomics.setText(""+defTreeNoEco);
                   tvDeforestNonEco.setText(""+defTreeNoNonEco);

                   allAdditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                           try {
                               AllTotal allmodel = dataSnapshot.getValue(AllTotal.class);
                               int aff = allmodel.getAfforestation();
                               int def = allmodel.getDeforestation();

                               tvAllAfforestation.setText(""+aff);
                               tvAllDeforestation.setText(""+def);
                           }catch (Exception e){
                               e.printStackTrace();
                           }

                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

               }
           });

       } catch (Exception e){
           e.printStackTrace();
       }


    }

}

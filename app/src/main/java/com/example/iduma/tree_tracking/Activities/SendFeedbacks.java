package com.example.iduma.tree_tracking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.iduma.tree_tracking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendFeedbacks extends AppCompatActivity {
    private EditText mFeedback;
    private Button mSendFeedback;
    private DatabaseReference mChats;
    private String firstname, lastname, message, country,date;
    private SimpleDateFormat sdf;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedbacks);

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        c = Calendar.getInstance();
        date = sdf.format(c.getTime());

        mFeedback = findViewById(R.id.feedback_message);
        mSendFeedback = findViewById(R.id.send_feedback);

        mChats= FirebaseDatabase.getInstance().getReference().child("Feedbacks");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            firstname = bundle.getString("firstname");
            lastname = bundle.getString("lastname");
            country = bundle.getString("country");

        }


        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = mFeedback.getText().toString().trim();

                if (TextUtils.isEmpty(message)){
                    mFeedback.setText("Please type feedback");
                } else{
                    FeedbackModel model = new FeedbackModel(message,firstname+" "+lastname,
                            country,date);
                    String id = mChats.push().getKey();
                    mChats.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFeedback.setText(" ");
                            if (task.isSuccessful()){
                                MDToast.makeText(getApplication(),"Feedback sent successfully",
                                        MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();

                                Intent intent = new Intent(SendFeedbacks.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                MDToast.makeText(getApplication(),"Error, try again",
                                        MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                            }
                        }
                    });


                }

            }
        });
    }

}

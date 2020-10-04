package com.example.foxykat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class userdet extends AppCompatActivity {
    Button sub;
    FirebaseFirestore db;//firestore database
    String pn,ph,UID;
    private static final int ACTIVITYuserdet = 2 ,ACTIVITYbotnav =3;
    EditText etpn , etph;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdet);
        setup();
        sub = findViewById(R.id.sub);
        etpn =  findViewById(R.id.etpn);
        etph = findViewById(R.id.etph);
        mAuth = FirebaseAuth.getInstance();
        UID  = getIntent().getStringExtra("UID");
        sub.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                pn = etpn.getText().toString();
                ph = etph.getText().toString();
                if(pn.isEmpty() || ph.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please fill the fields" , Toast.LENGTH_SHORT).show();
                }else {
                    basicReadWrite();
                    Intent intent = new Intent();
                    intent.putExtra("pn",pn);
                    intent.putExtra("ph",ph);
                    setResult(RESULT_OK,intent);
                    userdet.this.finish();
                }
            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void basicReadWrite() {
        if (mAuth != null) {

            // [START write_message]
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");

            String  email ;//  mAuth.getCurrentUser().getEmail();
            email =   FirebaseAuth.getInstance().getCurrentUser().getEmail();
            //myRef.setValue("Hello, World!"

//            myRef.child(UID).child("isActive").setValue("true");
            //          myRef.child(UID).child("uName").setValue("Lavanya sai kumar");
            //        myRef.child(UID).child("sMsg").setValue("Hey I'm Using FoxyKat");
            //      myRef.child(UID).child("email").setValue(email);
            User u1 = new User("true","Hey I'm Using FoxyKat",pn , email,ph);
            DatabaseReference myRef2 = database.getReference("mob");
            myRef2.child(ph).setValue(UID);
            myRef.child(UID).setValue(u1);
            // [END write_message]

            // [START read_message]
            // Read from the data
            //
            //
            userbio dat = new userbio(this);
            dat.save(ph,pn);

            BufferCreation();

            myRef.child(UID).child("isActive").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Toast.makeText(getApplicationContext(),"Welcome to Foxykat",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(userdet.this ,com.example.foxykat.botnav.class);
                    intent.putExtra("tok","login");
                    //   intent.putExtra("fm",people.get(index).getName());
                    startActivityForResult(intent,ACTIVITYbotnav);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
            // [END read_message]//
            //  setContentView(R.layout.actm2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void BufferCreation() {

        CollectionReference Inbox = db.collection("Inbox");
        String time;
        int h,m;
        h= LocalDateTime.now().getHour() ;
        m = LocalDateTime.now().getMinute();
        time = Integer.toString(h) + " : " + Integer.toString(m) ;
        Map<String, Object> data1 = new HashMap<>();
        data1.put("Name", pn);
        data1.put("Phone", ph);
        data1.put("Msg", " hEY I jOINED  fOXY kAT");
        data1.put("Time", time);

        Inbox.document(ph).collection("Buffer").document("root").set(data1);




    }

    public void setup() {
        // [START get_firestore_instance]
        db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }




}
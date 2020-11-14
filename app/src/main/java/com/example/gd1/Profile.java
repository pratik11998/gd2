package com.example.gd1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    Button signout;
    FirebaseAuth mAuth;
    TextView demail,dnum,dpass;
    FirebaseFirestore firestore;
    String userid;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bootom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        signout=findViewById(R.id.Signout);
       demail=findViewById(R.id.demail);
        dnum=findViewById(R.id.dnum);
        dpass=findViewById(R.id.dpass);
        signout.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        userid=mAuth.getCurrentUser().getUid();
        DocumentReference documentReference=firestore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
           demail.setText(value.getString("email"));
           dnum.setText(value.getString("Number"));
            dpass.setText(value.getString("password"));
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mitem) {

                switch (mitem.getItemId())
                {
                    case R.id.profile:

                        return  true;
                    case R.id.homee:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
     //   mAuth.signOut();
        Intent intent=new Intent(Profile.this,Login.class);
        startActivity(intent);
    }
}

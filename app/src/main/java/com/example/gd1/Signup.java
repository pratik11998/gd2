package com.example.gd1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    EditText email,pass,number;
    TextView alreadyuser;
    Button Signup;
    String em,pm,nu;
    private FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email=findViewById(R.id.txtEmail);
        pass=findViewById(R.id.txtPwd);
        number=findViewById(R.id.number);
        alreadyuser=findViewById(R.id.lnkLogin);
        Signup=findViewById(R.id.signup);
        Signup.setOnClickListener(this);
        alreadyuser.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signup) {
                        nu=number.getText().toString();
            createuser(email.getText().toString(), pass.getText().toString());

        }
        if (v.getId() == R.id.lnkLogin) {
            Intent intent=new Intent(Signup.this,Login.class);
            startActivity(intent);
        }
        }



public void createuser( String email, String password) {
         em=email;
         pm=password;
    auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String cureentuser=auth.getCurrentUser().getUid();
                        DocumentReference documentReference=firestore.collection("users").document(cureentuser);
                        Map<String,Object> user=new HashMap<>();
                             user.put("email",em);
                             user.put("password",pm);
                             user.put("Number",nu);
                             documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {
                                     Toast.makeText(Signup.this,"user store",Toast.LENGTH_LONG).show();
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     Toast.makeText(Signup.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                 }
                             });

                                     //   Toast.makeText(Signup.this,"done",Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(Signup.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }



                    else {
                        // If sign in fails, display a message to the user.


                        Toast.makeText(Signup.this, "Authentication failed."+  task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                }
            });
    }
}
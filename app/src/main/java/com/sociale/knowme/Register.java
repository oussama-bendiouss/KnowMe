package com.sociale.knowme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sociale.knowme.Models.User;

public class Register extends AppCompatActivity {
    private TextInputEditText Pseudo,Mail,Pass;
    private Button Register;
    private ProgressBar Progress;
    DatabaseReference reference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Pseudo = findViewById(R.id.inpseudo);
        Mail = findViewById(R.id.inmail);
        Pass = findViewById(R.id.inpass);
        Register = findViewById(R.id.register);
        Progress = findViewById(R.id.progress);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");


    }

    public void Registeruse(View view) {
        Progress.setVisibility(View.VISIBLE);
        final String mail = Mail.getText().toString();
        final String pass = Pass.getText().toString();
        final String pseudo = Pseudo.getText().toString();
        if (!mail.equals("")&&!pass.equals("")&&pseudo.equals("")&&pass.length()>8){
            auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseuser = auth.getCurrentUser();
                        User u = new User();
                        u.setName(pseudo);
                        u.setEmail(mail);
                        reference.child(firebaseuser.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Register.this,"User account is create",Toast.LENGTH_LONG).show();
                                    Progress.setVisibility(View.GONE);
                                    finish();
                                    Intent i = new Intent(Register.this,GroupeChat.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(Register.this,"User account is not create",Toast.LENGTH_LONG).show();
                                    Progress.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
package com.sociale.knowme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Autentification extends AppCompatActivity {
     private TextView NewAccount,ForgotPass;
     private Button Connect;
     private TextInputEditText Pass,Mail;
     private ProgressBar Progress;
     FirebaseAuth auth;
     DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            Intent i = new Intent(Autentification.this,GroupeChat.class);
            startActivity(i);
        }else{
            setContentView(R.layout.activity_autentification);
            NewAccount = findViewById(R.id.newaccount);
            ForgotPass = findViewById(R.id.forgetpass);
            Connect = findViewById(R.id.connect);
            Pass = findViewById(R.id.inpass);
            Mail = findViewById(R.id.inmail);
            Progress = findViewById(R.id.progress);
            reference = FirebaseDatabase.getInstance().getReference().child("Users");

        }


    }

    public void Loginuser(View view) {
        Progress.setVisibility(View.VISIBLE);
        String email = Mail.getText().toString();
        String pass = Pass.getText().toString();
        if(!email.equals("")&&!pass.equals("")){
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Progress.setVisibility(View.GONE);
                        Toast.makeText(Autentification.this,"Logged in",Toast.LENGTH_SHORT).show();
                        Intent i =new Intent(Autentification.this,GroupeChat.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(Autentification.this,"Wrong mail/password. Try again",Toast.LENGTH_SHORT).show();
                        Progress.setVisibility(View.GONE);
                    }

                }
            });
        }
    }

    public void gotoRegister(View view) {
        Intent reg = new Intent(Autentification.this,Register.class);
        startActivity(reg);
    }

    public void forgotpass(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Autentification.this);

        LinearLayout layout = new LinearLayout(Autentification.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ip.setMargins(50, 0, 0, 100);
        final EditText input = new EditText(Autentification.this);
        input.setLayoutParams(ip);
        input.setGravity(Gravity.TOP|Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setLines(1);
        input.setMaxLines(1);
        layout.addView(input,ip);

        alert.setMessage("Enter your regitered email adress?");
        alert.setTitle("Forgot Password ?");
        alert.setView(layout);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enter_mail = input.getText().toString();

                auth.sendPasswordResetEmail(enter_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            dialog.dismiss();
                            Toast.makeText(Autentification.this,"Email sent. Please chack your email", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }}
package com.example.crudfirebase.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crudfirebase.R;
import com.example.crudfirebase.session.SessionManagement;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText textEmail,textPass;
    private Button btLogin;
    private FirebaseAuth mAuth;
    private ImageView imgGoogle;
    private TextView textRegister;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth= FirebaseAuth.getInstance();

        textEmail = findViewById(R.id.textEmail);
        textPass = findViewById(R.id.textPassword);
        btLogin= findViewById(R.id.buttonLogin);

        textRegister = findViewById(R.id.textregister);



        imgGoogle= findViewById(R.id.imggoogle);
        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }

        });
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinwithGoogle();
            }
        });

    }
    private void signinwithGoogle(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                goMainActivity();
            }catch (ApiException e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void goMainActivity(){
        finish();
        Intent intent =  new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void login(){
        String email = textEmail.getText().toString();
        String pass = textPass.getText().toString();

        if(email.isEmpty()){
            return;
        }
        if(pass.isEmpty()){
            return;
        }
        if(email.equals("admin")&&pass.equals("admin")){
            goAdmin();
            finish();
        }
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                    SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                    sessionManagement.setUser(email.substring(0, email.lastIndexOf("@")));
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                    startActivity(intent);
                }
            }
        });

    }
    private void register(){
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);
    }
    private void goAdmin(){
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
    }
}

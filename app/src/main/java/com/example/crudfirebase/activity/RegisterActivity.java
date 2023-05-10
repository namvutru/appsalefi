package com.example.crudfirebase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crudfirebase.R;
import com.example.crudfirebase.model.Product;
import com.example.crudfirebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText textEmail,textPass,textPasscf,textTen;
    private Button btRegister;
    TextView textLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        mAuth= FirebaseAuth.getInstance();
        textTen=findViewById(R.id.textusename);
        textEmail = findViewById(R.id.textemail);
        textPass = findViewById(R.id.textpass);
        textPasscf= findViewById(R.id.textcfpass);
        btRegister= findViewById(R.id.btregister);
        textLogin = findViewById(R.id.textlogin);

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }

        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }
    private void register(){
        String email = textEmail.getText().toString();
        String pass = textPass.getText().toString();
        String passcf = textPasscf.getText().toString();
        String ten = textTen.getText().toString();
        if(email.isEmpty()){
            return;
        }
        if(pass.isEmpty()){
            return;
        }
        if(!pass.equals(passcf)){
            Toast.makeText(this, "password khong trung nhau", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Tao tai khoan thanh cong", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("User");
                    User user = new User(email,ten,"","");
                    ref.child(email.substring(0, email.lastIndexOf("@"))).setValue(user);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Tao tai khong thanh cong", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        });
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("User");
//        String username = email.substring(0, email.lastIndexOf("@"));
//        User newuser = new User(username,"","");
//        ref.child(username).setValue(newuser);

    }
    private void login(){
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
    }

}

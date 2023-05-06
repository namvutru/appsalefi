package com.example.crudfirebase.session;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.crudfirebase.activity.HomeActivity;
import com.example.crudfirebase.activity.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserNow {
    public String getUserNow(Context context){

        GoogleSignInAccount account =GoogleSignIn.getLastSignedInAccount(context);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mail="";
        if (user != null) {
            mail=user.getEmail();
            return mail.substring(0, mail.lastIndexOf("@"));

        }
        if(account !=null){
            mail=user.getEmail();
            return mail.substring(0, mail.lastIndexOf("@"));
        }
        return mail;
    }
}

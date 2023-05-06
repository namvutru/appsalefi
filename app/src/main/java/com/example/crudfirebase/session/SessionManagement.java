package com.example.crudfirebase.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_username";

    public SessionManagement (Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }
    public void setUser(String username){
        editor.putString(SESSION_KEY,username);
        editor.apply();
    }
    public String getUser(){
        return sharedPreferences.getString(SESSION_KEY,null);
    }
    public void removeAll(){
        editor.clear();
        editor.apply();
    }

}

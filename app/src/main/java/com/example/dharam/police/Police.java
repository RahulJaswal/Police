package com.example.dharam.police;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Dharam on 8/13/2018.
 */

public class Police extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);     //persistence is enabled here
    }
}

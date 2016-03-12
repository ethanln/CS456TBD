package com.tbd.appprototype;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by mitch10e on 12 March2016.
 */
public class TBDApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

    }

}

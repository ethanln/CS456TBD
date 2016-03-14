package com.tbd.appprototype;

import android.app.Application;

import com.firebase.client.Firebase;

import model.User;
import networking.NetworkManager;

/**
 * Created by mitch10e on 12 March2016.
 */
public class TBDApplication extends Application {

    private User currentUser;
    private NetworkManager network;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        network = NetworkManager.getInstance();
        network.application = this;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUserID() {
        if (currentUser != null) {
            return currentUser.getUserID();
        }
        return "";
    }
}

package com.dlkustovmylocatorgps.dmitry.mygpsone;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class User {
    public String phoneID;
    public String MyLatitude;
    public String MyLongitude;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String phoneID, String MyLatitude, String MyLongitude, String email) {
        this.phoneID = phoneID;
        this.MyLatitude = MyLatitude;
        this.MyLongitude = MyLongitude;
        this.email = email;
    }
}

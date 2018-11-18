package com.dlkustovmylocatorgps.dmitry.mygpsone;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class User {
    public String MyPhoneID;
    public String MyLatitude;
    public String MyLongitude;
    public String email;

    public String MyDirectorShip;
    public String MyNameShip;
    public String MyShortDescriptionShip;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String phoneID,
                String MyLatitude,
                String MyLongitude,
                String email,
                String MyDirectorShip,
                String MyNameShip,
                String MyShortDescriptionShip) {
        this.MyPhoneID = MyPhoneID;
        this.MyLatitude = MyLatitude;
        this.MyLongitude = MyLongitude;
        this.email = email;

        this.MyDirectorShip = MyDirectorShip;
        this.MyNameShip = MyNameShip;
        this.MyShortDescriptionShip = MyShortDescriptionShip;
    }
}

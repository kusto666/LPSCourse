package com.dlkustovmylocatorgps.dmitry.mygpsone;

import com.google.firebase.database.IgnoreExtraProperties;
import mygpsx.CStructUser;

@IgnoreExtraProperties
public class User /*extends  CStructUser*/{
    public String MyPhoneID;
    public String MyLatitude;
    public String MyLongitude;
    public String email;

    public String MyDirectorShip;
    public String MyNameShip;
    public String MyShortDescriptionShip;

    public User() {
        //CStructUser test__ = new CStructUser();
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

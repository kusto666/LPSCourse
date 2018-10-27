package com.dlkustovmylocatorgps.dmitry.mygpsone;

public class Upload
{
    public String MyName;
    public String MuUrlGs;
    private String MyUrlDownload;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String MyName,
                  String MuUrlGs,
                  String MyUrlDownload) {
        this.MyName = MyName;
        this.MuUrlGs = MuUrlGs;
        this.MyUrlDownload = MyUrlDownload;
    }

    public String getName() {
        return MyName;
    }

    public String getUrlGs() {
        return MuUrlGs;
    }

    public String getUrlDownload() {
        return MyUrlDownload;
    }
}

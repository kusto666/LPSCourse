package com.dlkustovmylocatorgps.dmitry.mygpsone;

public class Upload
{
    private String MyName;
    private String MyUrlGs;
    private String MyUrlDownload;

    public Upload() {
    }

    public Upload(String MyName,
                  String MyUrlGs,
                  String MyUrlDownload)
    {
        this.MyName = MyName;
        this.MyUrlGs = MyUrlGs;
        this.MyUrlDownload = MyUrlDownload;
    }

    public String getMyName()
    {
        return MyName;
    }

    public String getMyUrlGs()
    {
        return MyUrlGs;
    }

    public String getMyUrlDownload()
    {
        return MyUrlDownload;
    }
    public void setMyUrlDownload(String MyUrlDownload)
    {
        this.MyUrlDownload = MyUrlDownload;
    }
    public void setMyName(String MyName)
    {
        this.MyName = MyName;
    }
}

package com.dlkustovmylocatorgps.dmitry.mygpsone;

public class Upload
{
    private String MyIDFireBase;
    private String MyName;
    private String MyUrlGs;
    private String MyUrlDownload;
    private boolean MybChacked;

    public Upload() {
    }

    public Upload(String MyName,
                  String MyUrlGs,
                  String MyUrlDownload)
    {
        this.MyName = MyName;
        this.MyUrlGs = MyUrlGs;
        this.MyUrlDownload = MyUrlDownload;
        this.MybChacked = false;
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
    public boolean getMybChacked()
    {
        return MybChacked;
    }
    public void setMybChacked(boolean MybChacked)
    {
        MybChacked = MybChacked;
    }

    public String getMyIDFireBase()
    {
        return MyIDFireBase;
    }
    public void setMyIDFireBase(String MyIDFireBAse)
    {
        this.MyIDFireBase = MyIDFireBAse;
    }
    public void setMyName(String MyName)
    {
        this.MyName = MyName;
    }
}

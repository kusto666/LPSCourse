package com.dlkustovmylocatorgps.dmitry.mygpsone;

public class CMessages
{
    public String msg_title;
    public String msg_body;
    public String msg_time;
    public String msg_text_time;
    public String msg_url_file;
    public String msg_status;
    public String msg_from_user;
    public String msg_to_user;
    public Long msg_unix_time;
    public String msg_is_text;// Если = true - то текст и никакой ссылки для скачивания файла!!
    // Если = false - то наоборот!!!
    


    public CMessages()	{ }

    /*public CMessages(String stTitleMsg, String stTitleBody, String stTitleTime, String stStatus)
    {
        this.msg_title = stTitleMsg;
        this.msg_body = stTitleBody;
        this.msg_time = stTitleTime;
        this.msg_status = stStatus;
    }*/
    public String getMyFileUrl()
    {
        return msg_url_file;
    }
    public void setMyFileUrl(String MyFileUrl)
    {
        this.msg_url_file = MyFileUrl;
    }

    public void setUnixTime(Long msg_unix_time)
    {
        this.msg_unix_time = msg_unix_time;
    }
    public Long getUnixTime()
    {
        return this.msg_unix_time;
    }
}
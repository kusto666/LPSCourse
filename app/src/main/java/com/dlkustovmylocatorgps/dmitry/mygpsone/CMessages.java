package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

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

    // Функция отправки сообщения или ссылки на файл в сообщении!!!
    public static void SendingMsgOrFile(DatabaseReference mDatabaseTemp, CDateTime newCurrDate, String stMsgBody, String stMsgStatus,
                                  String stMsgTitle, Boolean bIsText, EditText editTextOutMsg)
    {
        // Формируем идентификатор сообщения!!!
        //m_stFINISH_ID_MSG = CCONSTANTS_EVENTS_JOB.MY_CURRENT_TEMP_USER_FOR_MSG +
        //        CCONSTANTS_EVENTS_JOB.MY_SEPARATOR_MSG + newCurrDate.GetCurrLongTime();
        //System.out.println("stFINISH_ID_MSG = " + m_stFINISH_ID_MSG);

        CMessages bMessss = new CMessages();
        bMessss.msg_body = stMsgBody;
        if(bIsText)
        {
            bMessss.msg_is_text = "true";
        }
        else
        {
            bMessss.msg_is_text = "false";
        }
        bMessss.msg_status = stMsgStatus;
        bMessss.msg_time = newCurrDate.GetPrintTime(newCurrDate.GetCurrLongTime());
        bMessss.msg_title = stMsgTitle;
        bMessss.msg_to_user = CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID;
        bMessss.msg_unix_time = newCurrDate.GetCurrLongTime();

		String uploadId = mDatabaseTemp.push().getKey();
        mDatabaseTemp.child(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID).child(uploadId).setValue(bMessss);

        // Здесь проверим, что поле для написания сообщения очищать не надо т.к. отправляем просто файл!!!
        if(editTextOutMsg != null)
        {
            editTextOutMsg.setText("");
        }
        else
        {
            // Ничего не чистим!!!
        }
//        editTextOutMsg.setText("");
        System.out.println("Типа послали сообщение!!!");
    }
}
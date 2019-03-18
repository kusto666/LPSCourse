package com.dlkustovmylocatorgps.dmitry.mygpsone;

import java.text.SimpleDateFormat;
import java.util.Date;

// Класс для работы со временем для отображения ПОКА в переписке!!!
public class CDateTime
{
    private Date m_MyDate;
    private String m_MyTime;
    private Long m_MyLongTime;
    //private String m_MyDate;


    public CDateTime()
    {
        m_MyDate = new Date();
        m_MyLongTime = System.currentTimeMillis()/* / 1000L*/;
        //System.out.println("UNIX m_MyLongTime = " + m_MyLongTime.toString());

        //m_MyDate = new Date(m_MyLongTime);
        //System.out.println(m_MyDate);

        //SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yy");
        //SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        //SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        //String dateText = format.format(m_MyDate);
        //System.out.println(dateText);

    }
    public void SetCurrLongTime(Long CurrLongTime)
    {
        this.m_MyLongTime = CurrLongTime;
    }
    public Long GetCurrLongTime()
    {
        return m_MyLongTime;
    }
    // Вызывается для конвертации текущего UNIX-времени в удобочитаемый формат
    public String GetPrintTime(Long lTime)
    {
        Date dDate = new Date(lTime);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        return format.format(dDate);
    }
}

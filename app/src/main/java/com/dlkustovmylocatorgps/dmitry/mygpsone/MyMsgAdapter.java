package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class MyMsgAdapter extends BaseAdapter
{
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<CMessages> objects;

        TextView tvTimeMsgSending;
        TextView tvIsRead;
        TextView tvBodyMsg;
        //Button btnLoadingFile;
        File localFile = null;
        CMessages mPosMesg;


        MyMsgAdapter(Context context, ArrayList<CMessages> products)
        {
                ctx = context;
                objects = products;
                lInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        //

        // кол-во элементов
        @Override
        public int getCount() {
            return objects.size();
        }

        // элемент по позиции
        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        // id по позиции
        @Override
        public long getItemId(int position) {
            return position;
        }

        // пункт списка
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // используем созданные, но не используемые view
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.activity_listview, parent, false);
            }


            tvTimeMsgSending = ((TextView) view.findViewById(R.id.tvTimeMsgSending));
            tvIsRead = ((TextView) view.findViewById(R.id.tvIsRead));
            tvBodyMsg = ((TextView) view.findViewById(R.id.tvBodyMsg));

            mPosMesg = getCMessage(position);// Наша структура сообщения со всеми данными из firebase!!!

            // Проверим входящее или нет и покажем это!!!! Блять - пишу второй раз!!!
            if(mPosMesg.msg_to_user.equals("disp777"))
            {
               // mPosMesg.msg_time = mPosMesg.msg_time + " >>>>>";
                tvIsRead.setText("ИСХОДЯЩЕЕ");
                tvIsRead.setBackgroundResource(R.color.color_green);
            }
            else
            {
               // mPosMesg.msg_time = mPosMesg.msg_time + " <<<<<";
            }

            ((TextView) view.findViewById(R.id.tvTimeMsgSending)).setText(mPosMesg.msg_time);

            if(mPosMesg.msg_is_text.equals("false"))// Проверим , что за сообщение: текст или ссылка на файл!!!
            {   // Это файл!!!!!!!!!!
                tvBodyMsg.setText("СКАЧАТЬ ФАЙЛ..."/* + mPosMesg.msg_title*/);
             }
            else
            {
               // ((TextView) view.findViewById(R.id.tvBodyMsg)).setText(p.msg_body);
                tvBodyMsg.setText(mPosMesg.msg_body);
                //btnLoadingFile.setEnabled(false);
            }



            return view;
        }

        // товар по позиции
        CMessages getCMessage(int position)
        {
            return ((CMessages) getItem(position));
        }

    }

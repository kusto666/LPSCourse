package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        /*TextView tvTimeMsgSending;
        TextView tvIsRead;
        TextView tvBodyMsg;
        */



        //Button btnLoadingFile;
        //File localFile = null;
        //final CMessages mPosMesg = null;


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
            //final TextView tvTimeMsgSending = null;
            ViewHolderItem viewHolder;
            // используем созданные, но не используемые view
            View view = convertView;
            if (view == null)
            {
                view = lInflater.inflate(R.layout.activity_listview, parent, false);
// well set up the ViewHolder
                viewHolder = new ViewHolderItem();
                viewHolder.tvTimeMsgSending = ((TextView) view.findViewById(R.id.tvTimeMsgSending));
                viewHolder.tvIsRead = ((TextView) view.findViewById(R.id.tvIsRead));
                viewHolder.tvBodyMsg = ((TextView) view.findViewById(R.id.tvBodyMsg));

                // store the holder with the view.
                view.setTag(viewHolder);
            }
            else{
                // we've just avoided calling findViewById() on resource everytime
                // just use the viewHolder
                viewHolder = (ViewHolderItem) view.getTag();
            }

            // object item based on the position
            CMessages mPosMesg = getCMessage(position);
// assign values if the object is not null
            if(mPosMesg != null) {
                // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
                //viewHolder.textViewItem.setText(objectItem.itemName);
               // viewHolder.textViewItem.setTag(objectItem.itemId);

                if(mPosMesg.msg_to_user.equals("disp777"))
                {
                    Log.i("mPosMesg.msg_to_user = ", mPosMesg.msg_to_user);
                    // mPosMesg.msg_time = mPosMesg.msg_time + " >>>>>";
                    //final TextView tvIsRead = ((TextView) view.findViewById(R.id.tvIsRead));
                    viewHolder.tvTimeMsgSending.setText(mPosMesg.msg_time + "  >>>>>");
                    viewHolder.tvIsRead.setText("ИСХОДЯЩЕЕ");
                    viewHolder.tvIsRead.setBackgroundResource(R.color.color_green);
                }
                else
                {
                    Log.i("mPosMesg.msg_to_user = ", mPosMesg.msg_to_user);
                    // mPosMesg.msg_time = mPosMesg.msg_time + " >>>>>";
                    viewHolder.tvTimeMsgSending.setText(mPosMesg.msg_time + "  <<<<<");
                    if(mPosMesg.msg_status.equals("no_read"))// Значит не прочитано!!!
                    {
                        viewHolder.tvIsRead.setText("НОВОЕ СООБЩЕНИЕ");
                        viewHolder.tvIsRead.setBackgroundResource(R.color.color_red);
                    }
                    else
                    {
                        viewHolder.tvIsRead.setText("ВХОДЯЩЕЕ");
                        viewHolder.tvIsRead.setBackgroundResource(R.color.color_red);
                    }
                   // viewHolder.tvIsRead.setText("ВХОДЯЩЕЕ");
                   // viewHolder.tvIsRead.setBackgroundResource(R.color.color_red);
                }
                //viewHolder.tvTimeMsgSending.setText(mPosMesg.msg_time);

                if(mPosMesg.msg_is_text.equals("false"))// Проверим , что за сообщение: текст или ссылка на файл!!!
                {   // Это файл!!!!!!!!!!
                    viewHolder.tvBodyMsg.setText("СКАЧАТЬ ФАЙЛ..."/* + mPosMesg.msg_title*/);
                }
                else
                {
                    // ((TextView) view.findViewById(R.id.tvBodyMsg)).setText(p.msg_body);
                    viewHolder.tvBodyMsg.setText(mPosMesg.msg_body);
                    //btnLoadingFile.setEnabled(false);
                }
            }
           /* Log.i("int position = ", Integer.toString(position));
            tvTimeMsgSending = ((TextView) view.findViewById(R.id.tvTimeMsgSending));
            tvIsRead = ((TextView) view.findViewById(R.id.tvIsRead));
            tvBodyMsg = ((TextView) view.findViewById(R.id.tvBodyMsg));*/

            //final CMessages mPosMesg = getCMessage(position);// Наша структура сообщения со всеми данными из firebase!!!
            //mPosMesg = MessagesFragmentMain.m_MyArrayMsg.get(position);
            // Проверим входящее или нет и покажем это!!!! Блять - пишу второй раз!!!
            /*if(mPosMesg.msg_to_user.equals("disp777"))
            {
                Log.i("mPosMesg.msg_to_user = ", mPosMesg.msg_to_user);
               // mPosMesg.msg_time = mPosMesg.msg_time + " >>>>>";
                tvIsRead.setText("ИСХОДЯЩЕЕ");
                tvIsRead.setBackgroundResource(R.color.color_green);
            }
            else
            {
               // mPosMesg.msg_time = mPosMesg.msg_time + " <<<<<";
            }*/

            //((TextView) view.findViewById(R.id.tvTimeMsgSending)).setText(mPosMesg.msg_time);

           /* if(mPosMesg.msg_is_text.equals("false"))// Проверим , что за сообщение: текст или ссылка на файл!!!
            {   // Это файл!!!!!!!!!!
                tvBodyMsg.setText("СКАЧАТЬ ФАЙЛ..."*//* + mPosMesg.msg_title*//*);
            }
            else
            {
               // ((TextView) view.findViewById(R.id.tvBodyMsg)).setText(p.msg_body);
                tvBodyMsg.setText(mPosMesg.msg_body);
                //btnLoadingFile.setEnabled(false);
            }*/



            return view;
        }

        // товар по позиции
        CMessages getCMessage(int position)
        {
            return ((CMessages) getItem(position));
        }

    // our ViewHolder.
// caches our TextView
    static class ViewHolderItem {
        TextView tvTimeMsgSending = null;
        TextView tvIsRead = null;
        TextView tvBodyMsg = null;
    }


}

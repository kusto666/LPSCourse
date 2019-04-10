package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;


public class MyMsgAdapter extends BaseAdapter
{
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<CMessages> objects;

        TextView tvTimeMsgSending;
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
            tvBodyMsg = ((TextView) view.findViewById(R.id.tvBodyMsg));

            mPosMesg = getCMessage(position);// Наша структура сообщения со всеми данными из firebase!!!

            ((TextView) view.findViewById(R.id.tvTimeMsgSending)).setText(mPosMesg.msg_time);
            if(mPosMesg.msg_is_text.equals("false"))// Проверим , что за сообщение: текст или ссылка на файл!!!
            {   // Это файл!!!!!!!!!!
               // ((TextView) view.findViewById(R.id.tvBodyMsg)).setText(p.msg_body);
                tvBodyMsg.setText("СКАЧАТЬ ФАЙЛ..."/* + mPosMesg.msg_title*/);
                //btnLoadingFile.setEnabled(true);
                // Здесь если кнопка активна , значит можем на нее нажать и скачать файл!!!
 /*               btnLoadingFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
//StorageReference storageRef = MessagesFragmentMain.m_myStorage.getReferenceFromUrl(
//                            "https://firebasestorage.googleapis.com/v0/b/mygpsone-kusto1.appspot.com/o/uploads%2FScreenshot_2017-10-03-09-38-30-734_com.dlkustovmindcleaner.dmitry.mindcleaner.jpg?alt=media");*//*
                        Log.i("mPosMesg.msg_body = ", mPosMesg.msg_body);
                        StorageReference storageRef = MessagesFragmentMain.m_myStorage.getReferenceFromUrl(mPosMesg.msg_body);
                        //StorageReference  islandRef = storageRef.child("file.txt");

                        File rootPath = new File(Environment.getExternalStorageDirectory(), "DCIM/MyFIREBASE");// Это пока временно
                        // Для проверки скачивания!!! Надо изменить на выбор через проводник(файловый менеджер)
                        if(!rootPath.exists()) {
                            rootPath.mkdirs();
                        }
                        localFile = new File(rootPath,"Screenshot_2017-10-03-09-38-30-734_com.dlkustovmindcleaner.dmitry.mindcleaner.jpg");
                        localFile = new File(rootPath,mPosMesg.msg_title);
                        try
                        {
                            //localFile = File.createTempFile("images5555555", "jpg");
                            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.e("firebase ",";local tem file created  created " +localFile.toString());
                                    //  updateDb(timestamp,localFile.toString(),position);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e("firebase ",";local tem file not created  created " +exception.toString());
                                }
                            });
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });*/
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

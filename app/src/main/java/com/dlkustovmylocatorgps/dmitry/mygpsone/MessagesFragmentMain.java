package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class MessagesFragmentMain extends Fragment {
    private Button btnClick;// Кнопка отправки сообщения!!!
    private Button buttonUploadFile;
    private EditText editTextOutMsg;
   // private TextView editTextIncomingMsg;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseIncoming;
    private ListView mListViewMsg;
    private MyMsgAdapter m_MyMsgAdapter;
    public static ArrayList<CMessages> m_MyArrayMsg = null;
            /*{"Java","C++","C#","CSS",
            "HTML","XML",".Net","VisualBasic", "SQL", "Python", "PHP"};*/

    //mDatabaseReference
    private DatabaseReference mDatabaseReference;

   //these are the views
  //  EditText editTextFilename;
    ProgressBar progressBar;
    TextView textViewStatus;

    // Для работы с облаком FirebaseStorage
    public static FirebaseStorage m_myStorage;
    private StorageReference m_myRootRef;
    //this is the pic pdf code used in file chooser
    final static int PICK_PDF_CODE = 2342;

    StorageReference mStorageReference;
    File localFile = null;
    private String m_TAG = "mmmmmm";// Для отслеживания событий - они мне ВАЖНЫ!!!

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        m_myStorage = FirebaseStorage.getInstance();
        m_myRootRef = m_myStorage.getReference();
        mStorageReference = m_myStorage.getReference();
        //getting firebase objects
        //mStorageReference = FirebaseStorage.getInstance().getReference();
       //mDatabaseReference = FirebaseDatabase.getInstance().getReference(com.dlkustovmylocatorgps.dmitry.mygpsone.CMAINCONSTANTS.DATABASE_PATH_UPLOADS);

       // mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        View retView = inflater.inflate(R.layout.fragment_massages_main, container, false);

        btnClick = (Button)retView.findViewById(R.id.buttonSendMsg);
        buttonUploadFile = (Button)retView.findViewById(R.id.buttonUploadFile);
        editTextOutMsg = (EditText)retView.findViewById(R.id.inputMsgText);
        //editTextIncomingMsg = (TextView)retView.findViewById(R.id.textViewIncoming);
       // editTextIncomingMsg.setMovementMethod(new ScrollingMovementMethod());
        mListViewMsg = (ListView)retView.findViewById(R.id.ListUsersMsg);
        mListViewMsg.setClickable(true);
       /* mListViewMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("mPosMesg.msg_body = ", "-------------------------");
            }
        } );*/

        progressBar = (ProgressBar)retView.findViewById(R.id.progressbar);

        //attaching listeners to views
       //retView.findViewById(R.id.buttonUploadFile).setOnClickListener();
        buttonUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Вызвале менеджер
                android.support.v4.app.Fragment tempFragment = new FileInOutFragment();
                MainActivity.replaceFragment(tempFragment,MainActivity.m_MainFragmentManager);
                Log.i(m_TAG, "Open FileInOutFragment!!!");
               /* StorageReference storageRef = m_myStorage.getReferenceFromUrl(
                        "https://firebasestorage.googleapis.com/v0/b/mygpsone-kusto1.appspot.com/o/uploads%2FScreenshot_2017-10-03-09-38-30-734_com.dlkustovmindcleaner.dmitry.mindcleaner.jpg?alt=media");
                //StorageReference  islandRef = storageRef.child("file.txt");

                File rootPath = new File(Environment.getExternalStorageDirectory(), "DCIM/MyFIREBASE");
                if(!rootPath.exists()) {
                    rootPath.mkdirs();
                }
                localFile = new File(rootPath,"Screenshot_2017-10-03-09-38-30-734_com.dlkustovmindcleaner.dmitry.mindcleaner.jpg");
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
                }*/




            }
        });


        btnClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDatabase = FirebaseDatabase.getInstance().getReference()
                        .child("message_to_android");
                try
                {
                    // Проверим, что сообщение не нулевой длины!!!
                    // Иначе не посылаем - ЗАЧЕМ???)))
                    if(editTextOutMsg.getText().toString().length() != 0)
                    {
                        //mDatabase.child("msg_555555").child("msg_body").setValue(editTextOutMsg.getText().toString()); // Старый вариант!!!
                        //mDatabase.child(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID).child("msg_body").setValue(editTextOutMsg.getText().toString());
                        CDateTime newCurrDate = new CDateTime(); // Берем текущее время для записи в базу!!!
                        CMessages.SendingMsgOrFile(mDatabase, newCurrDate,editTextOutMsg.getText().toString(),
                                "no_read",editTextOutMsg.getText().toString(), true, editTextOutMsg);
                        editTextOutMsg.setText("");
                        String url = "https://firebasestorage.googleapis.com/v0/b/mygpsone-kusto1.appspot.com/o/uploads%2FScreenshot_2017-10-03-09-38-30-734_com.dlkustovmindcleaner.dmitry.mindcleaner.jpg?alt=media&token=c0334bcd-c988-407d-8113-535eb83ef584";
                       /* Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(url), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Intent newIntent = Intent.createChooser(intent, "Open File");
                        try {
                            startActivity(newIntent);
                        } catch (ActivityNotFoundException e) {
                            // Instruct the user to install a PDF reader here, or something
                        }*/
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Сообщение не может быть пустым!", Toast.LENGTH_SHORT).show();
                    }


                }
                catch (Exception e)
                {
                    e.getMessage();
                }
                Log.i("MsgActivity = ", "Типа послали сообщение!!!");
            }
        });

        mDatabaseIncoming = FirebaseDatabase.getInstance()
                .getReference().child("message_to_android")
                .child(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID);

        mDatabaseIncoming.orderByChild("msg_unix_time").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot arg0)
            {
                // Выбираем , что слушать, какую ветку данных!!!
                DataSnapshot messagesSnapshot = arg0;
                Iterable<DataSnapshot> messageChildren = messagesSnapshot.getChildren();
                try
                {
                    if(m_MyArrayMsg == null)
                    {

                        m_MyArrayMsg = new ArrayList<CMessages>();
                    }
                    else
                    {
                        m_MyArrayMsg.clear();
                    }
                    for (DataSnapshot message : messageChildren)
                    {
                        CMessages MyMsg = message.getValue(CMessages.class);
                        Log.i("IncommingMsg = ", "Типа получили сообщение сообщение!!!");
                        Log.i("IncommingMsg = ", MyMsg.msg_body);
                        m_MyArrayMsg.add(MyMsg);
                       // editTextIncomingMsg.append(MyMsg.msg_body);
                        //editTextIncomingMsg.append("\n");
                    }
                    m_MyMsgAdapter = new MyMsgAdapter(mListViewMsg.getContext(), m_MyArrayMsg);
                    mListViewMsg.setAdapter(m_MyMsgAdapter);
                    m_MyMsgAdapter.notifyDataSetChanged();
                    mListViewMsg.setSelection(m_MyMsgAdapter.getCount() - 1);// Опускаемся сразу в самый низ списка сообщений!!!
                    //mListViewMsg.setClickable(true);
                    mListViewMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                            Log.i("mPosMesg.msg_body = ", "-------------------------");
                            final CMessages mPosMesg = (CMessages) adapter.getItemAtPosition(position);

                            ///////////////////////////////////////////////////////////////////////////
                            // Здесь для сообщения сразу изменим что он теперь прочитано при клике на его тело сообщения!
                            // И не важно файл это или просто текст, нам надо пометить его прочитанным!!!

                            ///////////////////////////////////////////////////////////////////////////

                        if(mPosMesg.msg_is_text.equals("false"))
                        {
                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("СКАЧИВАНИЕ...")
                                    .setMessage("СКАЧАТЬ ФАЙЛ?")
                                    .setPositiveButton("Да", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                            progressDialog.setTitle("Скачивание файла...");
                                            progressDialog.show();
                                                 /*StorageReference storageRef = MessagesFragmentMain.m_myStorage.getReferenceFromUrl(
                                                    "https://firebasestorage.googleapis.com/v0/b/mygpsone-kusto1.appspot.com/o/uploads%2FScreenshot_2017-10-03-09-38-30-734_com.dlkustovmindcleaner.dmitry.mindcleaner.jpg?alt=media");*/
                                            Log.i("mPosMesg.msg_body = ", mPosMesg.msg_body);
                                            StorageReference storageRef = MessagesFragmentMain.m_myStorage.getReferenceFromUrl(mPosMesg.msg_body);
                                            File rootPath = new File(Environment.getExternalStorageDirectory(), "DCIM/Download");// Это пока временно
                                            // Для проверки скачивания!!! Надо изменить на выбор через проводник(файловый менеджер)
                                            if(!rootPath.exists()) {
                                                rootPath.mkdirs();
                                            }
                                            /*localFile = new File(rootPath,"Screenshot_2017-10-03-09-38-30-734_com.dlkustovmindcleaner.dmitry.mindcleaner.jpg");*/
                                            localFile = new File(rootPath,mPosMesg.msg_title);
                                            try
                                            {
                                                //localFile = File.createTempFile("images5555555", "jpg");
                                                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                        Log.e("firebase ",";local tem file created  created " +localFile.toString());
                                                        //  updateDb(timestamp,localFile.toString(),position);
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getActivity().getApplicationContext(), "Файл скачан!", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent();
                                                        intent.setAction(Intent.ACTION_VIEW);
                                                        intent.setDataAndType(Uri.fromFile(new File(localFile.toString())), "image/*");
                                                        startActivity(intent);

                                                        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                        intent.setType("image/*");

                                                        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                                        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(chooser);*/
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        Log.e("firebase ",";local tem file not created  created " +exception.toString());
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getActivity().getApplicationContext(), "Ошибка скачивания файла!", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                            catch (Exception ex)
                                            {
                                                ex.printStackTrace();
                                            }
                                        }

                                    })
                                    .setNegativeButton("Нет", null)
                                    .show();


                        }
                    }


                        });
                }

                catch (Exception ex)
                {
                    Log.i("IncommingMsg = ", ex.getMessage());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        return retView;
    }
    private void MyUploadFile(String stMyPath)
    {
       /* m_myStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = m_myStorage.getReference();*/

        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/Downloads/"); // dir
       // File file = new File(sdPath, "test.txt"); //имя файла
        File file = new File(sdPath, stMyPath); //имя файла

        try
        {
            InputStream stream = new FileInputStream(file);
            StorageReference mountainsRef = m_myRootRef.child("test");
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                }
            });
        }
        catch (Exception ex)
        {
            System.out.print(ex.getMessage());
        }



    }
}
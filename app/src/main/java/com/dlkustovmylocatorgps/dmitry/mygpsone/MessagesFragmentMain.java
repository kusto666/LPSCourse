package com.dlkustovmylocatorgps.dmitry.mygpsone;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class MessagesFragmentMain extends Fragment {
    private Button btnClick;
    private Button buttonUploadFile;
    private EditText editTextOutMsg;
    private TextView editTextIncomingMsg;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseIncoming;
    private ListView mListViewMsg;
    private MyMsgAdapter m_MyMsgAdapter;
    private ArrayList<CMessages> m_MyArrayMsg = null;
            /*{"Java","C++","C#","CSS",
            "HTML","XML",".Net","VisualBasic", "SQL", "Python", "PHP"};*/

    //mDatabaseReference
    private DatabaseReference mDatabaseReference;

   //these are the views
  //  EditText editTextFilename;
    ProgressBar progressBar;
    TextView textViewStatus;

    // Для работы с облаком FirebaseStorage
    private FirebaseStorage m_myStorage;
    private StorageReference m_myRootRef;
    //this is the pic pdf code used in file chooser
    final static int PICK_PDF_CODE = 2342;

    StorageReference mStorageReference;

    private String m_TAG = "mmmmmm";// Для отслеживания событий - они мне ВАЖНЫ!!!

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        m_myStorage = FirebaseStorage.getInstance();
        m_myRootRef = m_myStorage.getReference();
        //getting firebase objects
        //mStorageReference = FirebaseStorage.getInstance().getReference();
       //mDatabaseReference = FirebaseDatabase.getInstance().getReference(com.dlkustovmylocatorgps.dmitry.mygpsone.CMAINCONSTANTS.DATABASE_PATH_UPLOADS);

       // mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        View retView = inflater.inflate(R.layout.fragment_massages_main, container, false);

        btnClick = (Button)retView.findViewById(R.id.buttonSendMsg);
        buttonUploadFile = (Button)retView.findViewById(R.id.buttonUploadFile);
        editTextOutMsg = (EditText)retView.findViewById(R.id.inputMsgText);
        editTextIncomingMsg = (TextView)retView.findViewById(R.id.textViewIncoming);
        editTextIncomingMsg.setMovementMethod(new ScrollingMovementMethod());
        mListViewMsg = (ListView)retView.findViewById(R.id.ListUsersMsg);
       /* m_AdapterMsg = new ArrayAdapter<String>(mListViewMsg.getContext(),R.layout.activity_listview, mobileArray);
        mListViewMsg.setAdapter(adapter);*/
        //getting the views
       // textViewStatus = (TextView)retView.findViewById(R.id.textViewStatus);
       // editTextFilename = (EditText)retView.findViewById(R.id.editTextFileName);
        progressBar = (ProgressBar)retView.findViewById(R.id.progressbar);

        //attaching listeners to views
       //retView.findViewById(R.id.buttonUploadFile).setOnClickListener();
        buttonUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Вызвале менеджер
                android.support.v4.app.Fragment tempFragment = new FileInOutFragment();
                MainActivity.replaceFragment(tempFragment,MainActivity.m_MainFragmentManager);
               /* if(m_mapFragment.isVisible())
                {
                    m_mapFragment.getView().setVisibility(View.GONE);
                }*/
                Log.i(m_TAG, "Open FileInOutFragment!!!");



               // System.out.println("Test.pdf");
               // MyUploadFile("Test.pdf");
                /*switch (view.getId()) {
                    case R.id.buttonUploadFile:
                        //getPDF();
                        break;
                    //case R.id.textViewUploads:
                       // startActivity(new Intent(this, ViewUploadsActivity.class));
                    //    break;
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
                    //mDatabase.child("msg_555555").child("msg_body").setValue(editTextOutMsg.getText().toString()); // Старый вариант!!!
                    //mDatabase.child(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID).child("msg_body").setValue(editTextOutMsg.getText().toString());
                    CDateTime newCurrDate = new CDateTime(); // Берем текущее время для записи в базу!!!
                    SendingMsgOrFile(mDatabase, newCurrDate,editTextOutMsg.getText().toString(),"no_read",editTextOutMsg.getText().toString(), true);
                    editTextOutMsg.setText("");
                }
                catch (Exception e)
                {
                    e.getMessage();
                }
                Log.i("MsgActivity = ", "Типа послали сообщение!!!");
            }
        });

        mDatabaseIncoming = FirebaseDatabase.getInstance().getReference().child("message_to_android").child(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID);

        mDatabaseIncoming.orderByChild("msg_unix_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot arg0)
            {
                // Выбираем , что слушать, какую ветку данных!!!
                DataSnapshot messagesSnapshot = arg0;
                Iterable<DataSnapshot> messageChildren = messagesSnapshot.getChildren();
                try
                {
                    for (DataSnapshot message : messageChildren)
                    {
                        if(m_MyArrayMsg == null)
                        {
                            m_MyArrayMsg = new ArrayList<CMessages>();
                        }
                        CMessages MyMsg = message.getValue(CMessages.class);
                        Log.i("IncommingMsg = ", "Типа получили сообщение сообщение!!!");
                        Log.i("IncommingMsg = ", MyMsg.msg_body);
                        m_MyArrayMsg.add(MyMsg);
                       // editTextIncomingMsg.append(MyMsg.msg_body);
                        //editTextIncomingMsg.append("\n");
                    }
                    m_MyMsgAdapter = new MyMsgAdapter(mListViewMsg.getContext(), m_MyArrayMsg);
                    mListViewMsg.setAdapter(m_MyMsgAdapter);
                }

                catch (Exception ex)
                {
                    Log.i("IncommingMsg = ", ex.getMessage());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
    //this method is uploading the file
    //the code is same as the previous tutorial
    //so we are not explaining it
/*    private void uploadFile(Uri data) {
        //progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child(com.dlkustovmylocatorgps.dmitry.mygpsone.CMAINCONSTANTS.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File Uploaded Successfully");

                        Upload upload = new Upload(editTextFilename.getText().toString(), taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                        mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        textViewStatus.setText((int) progress + "% Uploading...");
                    }
                });

    }*/
    // Функция отправки сообщения или ссылки на файл в сообщении!!!
    private void SendingMsgOrFile(DatabaseReference mDatabaseTemp,CDateTime newCurrDate, String stMsgBody, String stMsgStatus,
                                  String stMsgTitle, Boolean bIsText)
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



		/* mDatabaseTemp.child(m_stFINISH_ID_MSG).child("msg_body").setValueAsync(stMsgBody);
		 mDatabaseTemp.child(m_stFINISH_ID_MSG).child("msg_status").setValueAsync(stMsgStatus);
		 mDatabaseTemp.child(m_stFINISH_ID_MSG).child("msg_time").
		 setValueAsync(newCurrDate.GetPrintTime(newCurrDate.GetCurrLongTime()));
		 mDatabaseTemp.child(m_stFINISH_ID_MSG).child("msg_unix_time").setValueAsync(newCurrDate.GetCurrLongTime());
		 mDatabaseTemp.child(m_stFINISH_ID_MSG).child("msg_title").setValueAsync(stMsgBody);*/

		 /*if(bIsText)
		 {
			 mDatabaseTemp.child(m_stFINISH_ID_MSG).child("msg_is_text").setValueAsync("true");
		 }
		 else
		 {
			 mDatabaseTemp.child(m_stFINISH_ID_MSG).child("msg_is_text").setValueAsync("false");
		 }*/

        //mDatabaseTemp.child(m_stFINISH_ID_MSG).child("msg_to_user").setValueAsync(CCONSTANTS_EVENTS_JOB.MY_CURRENT_TEMP_USER_FOR_MSG);
        String uploadId = mDatabaseTemp.push().getKey();
        mDatabaseTemp.child(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID).child(uploadId).setValue(bMessss);

        editTextOutMsg.setText("");
        System.out.println("Типа послали сообщение!!!");
    }
}
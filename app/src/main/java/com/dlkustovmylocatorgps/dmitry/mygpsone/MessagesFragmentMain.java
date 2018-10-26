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
import android.widget.Button;
import android.widget.EditText;
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


public class MessagesFragmentMain extends Fragment {
    private Button btnClick;
    private Button buttonUploadFile;
    private EditText editTextOutMsg;
    private TextView editTextIncomingMsg;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseIncoming;
    //mDatabaseReference
    private DatabaseReference mDatabaseReference;

    //these are the views
    EditText editTextFilename;
    ProgressBar progressBar;
    TextView textViewStatus;

    // Для работы с облаком FirebaseStorage
    private FirebaseStorage m_myStorage;
    private StorageReference m_myRootRef;
    //this is the pic pdf code used in file chooser
    final static int PICK_PDF_CODE = 2342;

    StorageReference mStorageReference;

    private String m_TAG = "MyTag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        m_myStorage = FirebaseStorage.getInstance();
        m_myRootRef = m_myStorage.getReference();
        //getting firebase objects
        //mStorageReference = FirebaseStorage.getInstance().getReference();
       //mDatabaseReference = FirebaseDatabase.getInstance().getReference(com.dlkustovmylocatorgps.dmitry.mygpsone.Constants.DATABASE_PATH_UPLOADS);

       // mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        View retView = inflater.inflate(R.layout.fragment_massages_main, container, false);

        btnClick = (Button)retView.findViewById(R.id.buttonSendMsg);
        buttonUploadFile = (Button)retView.findViewById(R.id.buttonUploadFile);
        editTextOutMsg = (EditText)retView.findViewById(R.id.inputMsgText);
        editTextIncomingMsg = (TextView)retView.findViewById(R.id.textViewIncoming);
        editTextIncomingMsg.setMovementMethod(new ScrollingMovementMethod());


        //getting the views
        textViewStatus = (TextView)retView.findViewById(R.id.textViewStatus);
        editTextFilename = (EditText)retView.findViewById(R.id.editTextFileName);
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
                Log.i(m_TAG, "id == R.id.nav_go_points!!!");



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
                        .child("messages");
                try
                {
                    mDatabase.child("msg_555555").child("msg_body").setValue(editTextOutMsg.getText().toString());
                    editTextOutMsg.setText("");
                }
                catch (Exception e)
                {
                    e.getMessage();
                }
                Log.i("MsgActivity = ", "Типа послали сообщение!!!");
            }
        });

        mDatabaseIncoming = FirebaseDatabase.getInstance().getReference().child("message_to_android");

        mDatabaseIncoming.addValueEventListener(new ValueEventListener() {
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
                        CMessages MyMsg = message.getValue(CMessages.class);
                        Log.i("IncommingMsg = ", "Типа получили сообщение сообщение!!!");
                        Log.i("IncommingMsg = ", MyMsg.msg_body);
                        editTextIncomingMsg.append(MyMsg.msg_body);
                        editTextIncomingMsg.append("\n");
                        //textView.setText(MyMsg.msg_body);
                    }

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
    private void uploadFile(Uri data) {
        //progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child(com.dlkustovmylocatorgps.dmitry.mygpsone.Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf");
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

    }

}
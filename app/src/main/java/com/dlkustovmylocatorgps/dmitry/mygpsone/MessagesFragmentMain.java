package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MessagesFragmentMain extends Fragment {
    private Button btnClick;
    private EditText editTextOutMsg;
    private TextView editTextIncomingMsg;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseIncoming;

    StorageReference mStorageReference;

    private String m_TAG = "MyTag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View retView = inflater.inflate(R.layout.fragment_massages_main, container, false);

        btnClick = (Button)retView.findViewById(R.id.buttonSendMsg);
        editTextOutMsg = (EditText)retView.findViewById(R.id.inputMsgText);
        editTextIncomingMsg = (TextView)retView.findViewById(R.id.textViewIncoming);
        editTextIncomingMsg.setMovementMethod(new ScrollingMovementMethod());

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
   /* private void uploadFile(Uri data) {
        //progressBar.setVisibility(View.VISIBLE);
        // STORAGE_PATH_UPLOADS
        StorageReference sRef = mStorageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File Uploaded Successfully");

                        Upload upload = new Upload(editTextFilename.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                        mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
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

}
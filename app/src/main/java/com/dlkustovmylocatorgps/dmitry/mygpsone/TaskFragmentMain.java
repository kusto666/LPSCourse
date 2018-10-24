package com.dlkustovmylocatorgps.dmitry.mygpsone;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TaskFragmentMain extends Fragment {
    private Button btnClick;
    private EditText editTextOutMsg;
    private TextView editTextIncomingMsg;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseIncoming;
    private String m_TAG = "MyTag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View retView = inflater.inflate(R.layout.fragment_task_main, container, false);


        return retView;
    }

}
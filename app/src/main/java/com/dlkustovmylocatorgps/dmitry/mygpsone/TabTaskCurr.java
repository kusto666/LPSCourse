package com.dlkustovmylocatorgps.dmitry.mygpsone;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabTaskCurr extends Fragment
{
    private TextView mTvUid;
    private TextView tvTaskId;
    //private DatabaseReference mDatabase;

    public TabTaskCurr() {
       // Log.d(MainActivity.m_TAG, "new TabTask TabTaskCurr CONSTRUCTOR!!!" );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
       // Log.d(MainActivity.m_TAG, "new TabTask TabTaskCurr" );
        View retView = inflater.inflate(R.layout.fragment_tab_task_curr, container, false);

        mTvUid = (TextView)retView.findViewById(R.id.field_uid);
        tvTaskId = (TextView)retView.findViewById(R.id.tvTaskId);
        mTvUid.setText(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER);

        Query query = FirebaseDatabase.getInstance().getReference()
                                    .child("my_users_jobs")
                                    .child(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER)/*.equalTo("1","MyIsCurrTask")*/;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren())
                    {
                        try
                        {
                            Log.d(MainActivity.m_TAG, "ID_TASK = " + issue.getKey());
                            String stMyIsCurrTask = (String)issue.child("MyIsCurrTask").getValue();
                            Log.d(MainActivity.m_TAG, stMyIsCurrTask);
                            String stMyTemplateJob = (String)issue.child("MyTemplateJob").getValue();
                            tvTaskId.setText("MyTemplateJob = " + stMyTemplateJob);
                        }
                        catch (Exception ex)
                        {

                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return retView;
    }

}

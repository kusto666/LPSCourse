package com.dlkustovmylocatorgps.dmitry.mygpsone;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListOfFilesFragment extends Fragment {

    Button btnAdd, btnFind, btnDelete;
    private ListView list_data;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private List<Upload> list_users = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View retView = inflater.inflate(R.layout.fragment_list_of_files, container, false);

        btnAdd = (Button) retView.findViewById(R.id.btnAdd);
        btnFind = (Button)retView.findViewById(R.id.btnFind);
        btnDelete = (Button)retView.findViewById(R.id.btnDelete);
        list_data = (ListView)retView.findViewById(R.id.list_data);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("my_files");

        addEventFirebaseListener();

        return retView;
    }
    private void addEventFirebaseListener()
    {
        //показываем View загрузки
        // circular_progress.setVisibility(View.VISIBLE);
        list_data.setVisibility(View.INVISIBLE);

        dbRef.addValueEventListener(new ValueEventListener() {
            //если данные в БД меняются
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (list_users.size() > 0) {
                    list_users.clear();
                }
                //проходим по всем записям и помещаем их в list_users в виде класса User
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload user = postSnapshot.getValue(Upload.class);
                    list_users.add(user);
                }
                //публикуем данные в ListView
                ListViewAdapter adapter = new ListViewAdapter(ListOfFilesFragment.this, list_users);
                list_data.setAdapter(adapter);
                //убираем View загрузки
                // circular_progress.setVisibility(View.INVISIBLE);
                list_data.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

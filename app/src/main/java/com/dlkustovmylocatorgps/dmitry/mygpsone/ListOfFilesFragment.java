package com.dlkustovmylocatorgps.dmitry.mygpsone;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListOfFilesFragment extends Fragment {

    Button btnAdd, btnFind, btnDelete;
    private ListView list_data;
    private Upload selectedUser; //hold selected user

    //Firebase - потом обобщим  здесь не оставим!!!
    FirebaseStorage mFirebaseStorage;
    StorageReference storageReference;


    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private List<Upload> list_users = new ArrayList<>();
   // boolean checkState[];


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View retView = inflater.inflate(R.layout.fragment_list_of_files, container, false);

        btnAdd = (Button) retView.findViewById(R.id.btnAdd);
        btnFind = (Button)retView.findViewById(R.id.btnFind);
        btnDelete = (Button)retView.findViewById(R.id.btnDelete);
        list_data = (ListView)retView.findViewById(R.id.list_data);

        mFirebaseStorage = FirebaseStorage.getInstance();
        storageReference = mFirebaseStorage.getReference();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("my_files");
        //checkState = new boolean[list_data.getAdapter().getCount()];
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ///////////////////////////////////////////////////////////////////////
                // Это превратим позже в функцию!!!
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Удаление документа из базы!");
                builder.setMessage("Вы уверены, что хотите накосячить?");

                builder.setPositiveButton("Очень уверен!", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Log.d(CMAINCONSTANTS.MY_TAG, "btnDelete.setOnClickListener!");
                        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(selectedUser.getMyUrlDownload());

                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Log.d(CMAINCONSTANTS.MY_TAG, "onSuccess: deleted file");
                                dbRef.child(selectedUser.getMyIDFireBase()).setValue(null);
//                        list_data.notifyAll();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Log.d(CMAINCONSTANTS.MY_TAG, "onFailure: did not delete file");
                            }
                        });
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Не уверен(((", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.i(CMAINCONSTANTS.MY_TAG, "Передумал удалять!!!");
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                ////////////////////////////////////////////////////////////////////////

            }
        });
        list_data.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int position, long l)
            {
                Upload user = (Upload) adapterView.getItemAtPosition(position);
                selectedUser = user;
                Log.i(CMAINCONSTANTS.MY_TAG, "user.getMyName() = " + user.getMyName());
                Log.i(CMAINCONSTANTS.MY_TAG, "user.getMyUrlDownload() = " + user.getMyUrlDownload());
     /*           final CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);
                if(!itemCheckbox.isChecked())
                {
                    itemCheckbox.setChecked(true);
                   // user.setMybChacked(true);
                }
                else
                {
                    itemCheckbox.setChecked(false);
                   // user.setMybChacked(false);
                }*/
                // Get the checkbox.
                //final CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);
/*                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Log.d(CMAINCONSTANTS.MY_TAG, "btnDelete.setOnClickListener!");
                        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(selectedUser.getMyUrlDownload());

                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Log.d(CMAINCONSTANTS.MY_TAG, "onSuccess: deleted file");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Log.d(CMAINCONSTANTS.MY_TAG, "onFailure: did not delete file");
                            }
                        });
                    }
                });*/

            }

        });
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
                    Upload uploads = postSnapshot.getValue(Upload.class);
                    uploads.setMyIDFireBase(postSnapshot.getKey());
                    list_users.add(uploads);
                }
                //публикуем данные в ListView
                ListViewAdapter adapter = new ListViewAdapter(ListOfFilesFragment.this, list_users);
                list_data.setAdapter(adapter);
                Log.i(CMAINCONSTANTS.MY_TAG, "ListViewAdapter size = " + ListViewAdapter.checkState.length);

                list_data.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view,final int position, long l)
                    {
                        Upload user = (Upload) adapterView.getItemAtPosition(position);
                        selectedUser = user;
                        Log.i(CMAINCONSTANTS.MY_TAG, "user.MyIDFireBAse = " + user.getMyIDFireBase());
                        Log.i(CMAINCONSTANTS.MY_TAG, "user.getMyName() = " + user.getMyName());
                        Log.i(CMAINCONSTANTS.MY_TAG, "user.getMyUrlDownload() = " + user.getMyUrlDownload());
                        CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);
                        if(!user.getMybChacked())
                        {
                            itemCheckbox.setChecked(true);
                            list_users.get(position).setMybChacked(true);
                          //  user.setMybChacked(true);
                        }
                        else
                        {
                            itemCheckbox.setChecked(false);
                            list_users.get(position).setMybChacked(false);
                           // user.setMybChacked(false);
                        }
                        // Get the checkbox.
                        //final CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);
                        for(int i = 0; i < list_data.getCount(); i++)//
                        {
                            Upload upp = (Upload)list_users.get(i);
                            Log.i(CMAINCONSTANTS.MY_TAG, "upp[ " + i + "].getMybChacked() = " + upp.getMybChacked());
                        }
                    }

                });
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

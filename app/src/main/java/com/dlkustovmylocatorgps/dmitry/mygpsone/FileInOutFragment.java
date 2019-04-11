package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class FileInOutFragment extends Fragment
{
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //Firebase - потом обобщим  здесь не оставим!!!
    StorageReference storageReference;
    FirebaseStorage storage;


    private String PATH_NAME_UPLOADS_MAIN = "uploads/";

    private String m_stFileName;
    private Button btnChoose, btnUpload, btnGetListDocs;
    private EditText editTextName;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View retView = inflater.inflate(R.layout.fragment_file_in_out, container, false);

        //Initialize Views
        btnChoose = (Button)retView.findViewById(R.id.btnChoose);
        btnUpload = (Button)retView.findViewById(R.id.btnUpload);
        btnGetListDocs = (Button)retView.findViewById(R.id.btnGetListDocs);
        imageView = (ImageView)retView.findViewById(R.id.imgView);
        editTextName = (EditText) retView.findViewById(R.id.editTextName);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("chooseImage();");
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("uploadImage();");
                uploadImage();
            }
        });

        btnGetListDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment tempFragment = new ListOfFilesFragment();
                MainActivity.replaceFragment(tempFragment,MainActivity.m_MainFragmentManager);
            }
        });

        //storageReference = FirebaseStorage.getInstance().getReference().child("uploads");

        return retView;
    }

    private void chooseImage()
    {
        Intent intent = new Intent();
       /* intent.setType("image/*");*/
        intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите документ"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST /*&& resultCode == RESULT_OK */&& data != null && data.getData() != null )
        {
            filePath = data.getData();
            m_stFileName = data.getData().getLastPathSegment();
            editTextName.setText(m_stFileName);
            try
            {
                //Context applicationContext = MainActivity.getContextOfApplication();
                //applicationContext.getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {

        if(filePath != null)
        {
            /*editTextName.setText(filePath.toString());*/
            final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle("Отправка...");
            progressDialog.show();
            // Проверка на не пустое имя файла
            if(editTextName.getText().equals(""))
            {
                Toast.makeText(getActivity().getApplicationContext(), "Пустое имя файла", Toast.LENGTH_SHORT).show();
                return;
            }
            // Здесь позже рандом названия будем брать из textfield!!!
            /*StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());*/
            final StorageReference ref = storageReference.child(PATH_NAME_UPLOADS_MAIN + editTextName.getText());
            UploadTask uploadTask =  ref.putFile(filePath);



            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                                .child("message_to_android");
                        CDateTime newCurrDate = new CDateTime(); // Берем текущее время для записи в базу!!!
                        CMessages.SendingMsgOrFile(mDatabase, newCurrDate,downloadUri.toString(),
                                "no_read",editTextName.getText().toString(), false, null);



                       // Upload upload;// Объект для загрузки в realbase!!!
                       // downloadUri = task.getResult();
                        //upload = new Upload(editTextName.getText().toString(),
                        //        downloadUri.toString(),
                         //       downloadUri.toString());
                                   //* taskSnapshot.getUploadSessionUri().toString());*//*

                        //adding an upload to firebase database
                        //String uploadId = MainActivity.mDatabase.push().getKey();
                        //MainActivity.mDatabase.child("my_files").child(uploadId).setValue(upload);
                        progressDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Файл отправлен!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "Документ не выбран!", Toast.LENGTH_SHORT).show();
        }
    }
}
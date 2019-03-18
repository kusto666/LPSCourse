package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by For on 4/14/2017.
 */

public class AuthActivity extends BaseActivity implements
        View.OnClickListener
{

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]
    private DatabaseReference mDatabaseListenMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        mEmailField = (EditText) findViewById(R.id.field_email);
        /*mEmailField.setText("portkimry.commers@gmail.com");*/
        mEmailField.setText("test1@gmail.com");
        mPasswordField = (EditText) findViewById(R.id.field_password);
        /*mPasswordField.setText("761set31");*/
        mPasswordField.setText("111111");
        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    updateUI(null);
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else
                    {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful())
                        {
                            Intent intent =  new Intent(AuthActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                            CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER = task.getResult().getUser().getUid();
                            Log.d(TAG, "CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER == " + CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER);
                            CMAINCONSTANTS.MY_CURRENT_EMAIL_SYSUSER = task.getResult().getUser().getEmail();
                            Log.d(TAG, "CMAINCONSTANTS.MY_CURRENT_EMAIL_SYSUSER == " + CMAINCONSTANTS.MY_CURRENT_EMAIL_SYSUSER);

                            // Получим ветку из "message_to_android" для слушания сообщений!!!
                            mDatabaseListenMsg = FirebaseDatabase.getInstance().getReference().child("my_sys_users_binding")
                                    .child(CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER).child("myPhoneBinding");
                            mDatabaseListenMsg.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot arg0)
                                {
                                    // Выбираем , что слушать, какую ветку данных!!!

                                    //DataSnapshot messagesSnapshot = arg0;
                                    //Iterable<DataSnapshot> messageChildren = messagesSnapshot.getChildren();
                                    try
                                    {
                                        CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID = (String) arg0.getValue();
                                        /*for (DataSnapshot message : messageChildren)
                                        {
                                            CMessages MyMsg = message.getValue(CMessages.class);
                                            Log.i("IncommingMsg = ", "Типа получили сообщение сообщение!!!");
                                            Log.i("IncommingMsg = ", MyMsg.msg_body);
                                           // editTextIncomingMsg.append(MyMsg.msg_body);
                                            editTextIncomingMsg.append("\n");
                                            //textView.setText(MyMsg.msg_body);
                                        }*/
                                        Log.i("SYSUSER_MyPhoneID = ", CMAINCONSTANTS.MY_CURRENT_ID_SYSUSER_MyPhoneID);
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
                            /*Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(AuthActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();*/
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(AuthActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null)
        {
           // mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            // Была кнопка выйти из аккаунта, теперь ее уберем для правдоподобности!!! - Закомментим и потом выйдим !!!
           /* mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);*/

           // Это новый вариант пока!!!
            updateUI(null);
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
        else
        {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_out_button) {
            signOut();
        }
    }

}

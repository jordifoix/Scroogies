package com.jordifoix.scroogies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private Button signInButton,registerButton;
    private EditText phoneText,passwordText;
    private FirebaseAuth authentication;
    private FirebaseAuth.AuthStateListener authenticationListener;
    private boolean executingTask = false;
    private ProgressDialog dialog;


    public String TAG = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = FirebaseAuth.getInstance();
        authenticationListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("SIGN_IN", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("SIGN_IN", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        setContentView(R.layout.activity_login);
        phoneText = (EditText) findViewById(R.id.phoneText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executingTask = true;
                dialog = ProgressDialog.show(LoginActivity.this,null,"Loading...");
                dialog.setCancelable(true);
                if (phoneText.getText().toString().equals("") || passwordText.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Empty fields,\nwrite both number and password", Toast.LENGTH_SHORT).show();
                    executingTask = false;
                    dialog.dismiss();
                }
                else {
                    authentication.signInWithEmailAndPassword(phoneText.getText().toString()+"@mydomain.com",passwordText.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInUserWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            executingTask = false;
                            dialog.dismiss();
                            if (!task.isSuccessful()) {
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this,"Authentication failed:\n"+e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });


                    //FEM EL SIGN IN
                }
            }
        });
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        authentication.addAuthStateListener(authenticationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        phoneText.clearComposingText();
        passwordText.clearComposingText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authenticationListener != null) {
            authentication.removeAuthStateListener(authenticationListener);
        }
    }
}

package com.jordifoix.scroogies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private Button signUpButton,goBackButton;
    private EditText registerPhoneText,registerPasswordText;
    private ProgressDialog dialog;
    private FirebaseAuth authentication;
    private FirebaseAuth.AuthStateListener authenticationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dialog = ProgressDialog.show(RegisterActivity.this,null,"Registering User...");
        dialog.setCancelable(true);
        dialog.dismiss();
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
        registerPhoneText = (EditText) findViewById(R.id.registerPhoneText);
        registerPasswordText = (EditText) findViewById(R.id.registerPasswordText);
        registerPhoneText.clearComposingText();
        registerPasswordText.clearComposingText();
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (registerPhoneText.getText().toString().equals("") || registerPasswordText.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Empty fields,\nwrite both number and password", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else {
                    authentication.createUserWithEmailAndPassword(registerPhoneText.getText().toString()+"@mydomain.com",registerPasswordText.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Register", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            dialog.dismiss();

                            if (!task.isSuccessful()) {
                                if (registerPasswordText.getText().toString().length() < 6) {
                                    Toast.makeText(RegisterActivity.this, "Password must contain at least 6 characters",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(RegisterActivity.this,"Successfully registered!",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
        goBackButton = (Button) findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerPhoneText.clearComposingText();
        registerPasswordText.clearComposingText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authenticationListener != null) {
            authentication.removeAuthStateListener(authenticationListener);
        }
    }
}

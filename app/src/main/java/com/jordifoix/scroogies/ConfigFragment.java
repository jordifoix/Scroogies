package com.jordifoix.scroogies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by jordifoix on 3/3/17.
 */

public class ConfigFragment extends Fragment {

    private Button deleteAccountButton,signOutButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_config,container,false);
        deleteAccountButton = (Button) view.findViewById(R.id.deleteAccountButton);
        signOutButton = (Button) view.findViewById(R.id.signOutButton);
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),"User successfully deleted",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent((MainActivity)getContext(),LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user==null) {
                    Intent intent = new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(),"Not able to sign out",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}

package com.jordifoix.scroogies;

import android.os.Bundle;
import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jordifoix on 3/3/17.
 */

public class AddFragment extends Fragment {

    private DatabaseReference searchedDeuteReference,searchedDeuteReference2;
    private Button addButton;
    private EditText user2PhoneText,quantityText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add,container,false);
        user2PhoneText = (EditText) view.findViewById(R.id.user2PhoneText);
        quantityText = (EditText) view.findViewById(R.id.quantityText);
        addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String telefon1 = aux.substring(0,aux.indexOf("@"));
                //Toast.makeText(getActivity().getApplicationContext(),"El meu telefon: "+telefon1,Toast.LENGTH_SHORT).show();
                String telefon2 = user2PhoneText.getText().toString();
                //Toast.makeText(getActivity().getApplicationContext(),"El seu telefon: "+telefon2,Toast.LENGTH_SHORT).show();
                String quantitatString = quantityText.getText().toString();
                //Toast.makeText(getActivity().getApplicationContext(),"Quantitat: "+quantitat,Toast.LENGTH_SHORT).show();
                int i;
                Double quantitat;
                for (i = 0; i < quantitatString.length(); ++i) {
                    if (quantitatString.charAt(i)=='.') {
                        if (i+3 != quantitatString.length()) {
                            Toast.makeText(getContext(),"Maximum 2 decimals",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else {
                            quantitat = Double.parseDouble(quantitatString);
                            addDebt(telefon1,telefon2,quantitat);
                            Toast.makeText(getContext(),"Added new debt!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if (i==quantitatString.length()-1) {
                    quantitat = Double.parseDouble(quantitatString);
                    addDebt(telefon1,telefon2,quantitat);
                    Toast.makeText(getContext(),"Added new debt!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    public void addDebt(final String telefonUsuari1, final String telefonUsuari2, final Double quantitat) {
        searchedDeuteReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(telefonUsuari1+"/"+telefonUsuari2);
        searchedDeuteReference2 = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(telefonUsuari2+"/"+telefonUsuari1);
        searchedDeuteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getContext(),"data snapshot: "+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                Double antic = 0.0;
                    if (dataSnapshot.getValue()==null) {
                        Log.d("Quantitats","No hi havia cap deute");
                        // AFEGIM EL NOU
                    }
                    else {
                        String sAntic = dataSnapshot.getValue().toString();
                        //Toast.makeText(getActivity().getApplicationContext(),sAntic,Toast.LENGTH_SHORT).show();
                        antic = Double.valueOf(sAntic);
                        //Toast.makeText(getActivity().getApplicationContext(),"Antic double: "+antic,Toast.LENGTH_SHORT).show();
                    }
                    Double nova = antic + quantitat;
                    //Toast.makeText(getActivity().getApplicationContext(), "Quantitat nova: "+nova, Toast.LENGTH_SHORT).show();
                    Log.d("Quantitats","Quantitat nova: "+nova);
                    //searchedDeuteReference.push().setValue(nova);
                    searchedDeuteReference.setValue(nova);
                    //searchedDeuteReference.child(dataSnapshot.getKey()).setValue(nova);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Quantitats","Failed to read quantity", databaseError.toException());
                Toast.makeText(getContext(),"Failed to read database, couldn't add!",Toast.LENGTH_SHORT).show();
            }
        });
        searchedDeuteReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double antic = 0.0;
                if (dataSnapshot.getValue()==null) {
                    Log.d("Quantitats","No hi havia cap deute");
                    // AFEGIM EL NOU
                }
                else {
                    String sAntic = dataSnapshot.getValue().toString();
                    //Toast.makeText(getActivity().getApplicationContext(),sAntic,Toast.LENGTH_SHORT).show();
                    antic = Double.valueOf(sAntic);
                    //Toast.makeText(getActivity().getApplicationContext(),"Antic double: "+antic,Toast.LENGTH_SHORT).show();
                }
                Double nova = antic - quantitat;
                //Toast.makeText(getActivity().getApplicationContext(), "Quantitat nova: "+nova, Toast.LENGTH_SHORT).show();
                Log.d("Quantitats","Quantitat nova: "+nova);
                //searchedDeuteReference.push().setValue(nova);
                searchedDeuteReference2.setValue(nova);
                //searchedDeuteReference.child(dataSnapshot.getKey()).setValue(nova);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Quantitats","Failed to read quantity", databaseError.toException());
                Toast.makeText(getContext(),"Failed to read database, couldn't add!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

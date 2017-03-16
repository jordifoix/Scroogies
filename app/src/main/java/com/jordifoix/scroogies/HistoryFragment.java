package com.jordifoix.scroogies;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.TimeUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Time;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by jordifoix on 3/3/17.
 */

public class HistoryFragment extends Fragment {

    DatabaseReference user1DebtsReference;
    private ArrayList<Deute> deutes;
    private ArrayList<Deute> deutesBuscats;
    private ListView listView;
    private MyDebtAdapter adapter;
    private MyDebtAdapter adapter2;
    private String telefon1;
    private EditText searchText;
    private ProgressDialog dialog;
    //private Map<Integer,String> contacts;
    private Vector<Map.Entry<String,String>> contactes;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history,container,false);
        listView = (ListView) view.findViewById(R.id.historyListView);
        deutes = new ArrayList<Deute>();
        deutesBuscats = new ArrayList<Deute>();
        dialog = ProgressDialog.show(getActivity(),null,"Loading...");
        dialog.setCancelable(true);

        String aux = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        telefon1 = aux.substring(0,aux.indexOf("@"));

        //CADA COP QUE VOLEM BORRAR TOT:
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //ref.removeValue();

        user1DebtsReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(telefon1);

        searchText = (EditText) view.findViewById(R.id.searchText);

        user1DebtsReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deutes.clear();
                dialog.dismiss();
                Long value = dataSnapshot.getChildrenCount();
                Log.d("FOIX","dataSnapshot children count: "+value);
                //Toast.makeText(getContext(),"dataSnapshot children count: "+value,Toast.LENGTH_SHORT).show();

                // PILLEM NOMS DELS CONTACTES DEL MOBIL:
                getAllContacts();

                Toast.makeText(getContext(),"Mida de deutes: "+deutes.size(),Toast.LENGTH_SHORT).show();
                for (Deute debt : deutes) {
                    Log.d("antichill","telefon en els deutes, telefon: "+debt.getTelefonUsuari2()+", nom: "+debt.getNomUsuari2());
                }


                for (DataSnapshot debtSnapshot : dataSnapshot.getChildren()) {
                   // if (debtSnapshot.getValue()!=null) {
                        String telf2 = debtSnapshot.getKey();
                        Log.d("FOIX","key (telefon): "+telf2);
                        Double debt = Double.valueOf(debtSnapshot.getValue().toString());
                        Log.d("FOIX","debt: "+debt+" €");
                        if (debt!=0.0) {
                            deutes.add(new Deute(telefon1,"",telf2,"",debt));
                        }
                        //Toast.makeText(getContext(),"Child loaded",Toast.LENGTH_SHORT).show();
                   /* }
                    else {
                        Toast.makeText(getContext(),"Couldn't load some debts",Toast.LENGTH_SHORT).show();
                    }*/
                }
                // ASSIGNEM ELS NOMS TROBATS ALS DEUTES:
                for (Map.Entry<String,String> contact : contactes) {
                    int i = 0;
                    for (Deute debt : deutes) {
                        if (contact.getKey().equals(debt.getTelefonUsuari2())) {
                            Log.d("pollo","Primer nivell superat");
                            Log.d("pollo","contact.getKey()="+contact.getKey()+", debt.getTelefonUsuari2="+debt.getTelefonUsuari2());
                            Log.d("pollo","contact.getValue()="+contact.getValue());
                            Log.d("pollo","debt.getNomUsuari2()="+debt.getNomUsuari2());
                            if (debt.getNomUsuari2().equals("")) {
                                Log.d("pollo","Tercer nivell superat");
                                debt.setNomUsuari2(contact.getValue());
                                Log.d("pollo","Usuari guardat: "+debt.getNomUsuari2());
                                break;
                            }
                        }
                        ++i;
                    }
                }

                adapter = new MyDebtAdapter(getActivity().getApplicationContext(),deutes);
                listView.setAdapter(adapter);

                // LA BUSCA:
                searchText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        deutesBuscats.clear();
                        for (Deute deute : deutes) {
                            if (esPartDe(s.toString(),deute.getTelefonUsuari2())) {
                                deutesBuscats.add(deute);
                            }
                            else if (!(deute.getNomUsuari2().equals("")) && esPartDe(s.toString(),deute.getNomUsuari2())) {
                                deutesBuscats.add(deute);
                            }
                        }
                        adapter2 = new MyDebtAdapter(getActivity().getApplicationContext(),deutesBuscats);
                        if (listView!=null) {
                            adapter2.notifyDataSetChanged();
                            listView.setAdapter(adapter2);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                getAllContacts();
                for (Map.Entry<String,String> contact : contactes) {
                    int i = 0;
                    for (Deute debt : deutes) {
                        if (contact.getKey().equals(debt.getTelefonUsuari2())) {
                            Log.d("pollo","Primer nivell superat");
                            Log.d("pollo","contact.getKey()="+contact.getKey()+", debt.getTelefonUsuari2="+debt.getTelefonUsuari2());
                            Log.d("pollo","contact.getValue()="+contact.getValue());
                            Log.d("pollo","debt.getNomUsuari2()="+debt.getNomUsuari2());
                            if (debt.getNomUsuari2().equals("")) {
                                Log.d("pollo","Tercer nivell superat");
                                debt.setNomUsuari2(contact.getValue());
                                Log.d("pollo","Usuari guardat: "+debt.getNomUsuari2());
                                break;
                            }
                        }
                        ++i;
                    }
                }
                Toast.makeText(getContext(),"Mida de deutes: "+deutes.size(),Toast.LENGTH_SHORT).show();
                for (Deute debt : deutes) {
                    Log.d("antichill","telefon en els deutes, telefon: "+debt.getTelefonUsuari2()+", nom: "+debt.getNomUsuari2());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(getContext(),"Couldn't read from firebase: " + databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });

        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ((MainActivity)getActivity()).hideKeyboard(v);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    boolean esPartDe(String s1, String s2) {
        for (int i = 0; i < s1.length(); ++i) {
            if (s1.charAt(i)!=s2.charAt(i)) return false;
        }
        return true;
    }

    public void getAllContacts() {

        contactes = new Vector<Map.Entry<String, String> >();
        ContentResolver cr = ((MainActivity)getActivity()).getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    boolean chill = true;
                    while (pCur.moveToNext() && chill) {
                        chill = false;
                        String auxPhone = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("antichill","Nom i tal: " + name
                                + ", Phone No: " + auxPhone);

                        //TRACTAMENT DELS NUMEROS RAROS:
                        String auxPhoneNormalized = "";
                        for (int i=0; i<auxPhone.length(); ++i) {
                            if (auxPhone.charAt(i)!=')' && auxPhone.charAt(i)!='('
                            && auxPhone.charAt(i)!='-' && auxPhone.charAt(i)!=' '
                            && auxPhone.charAt(i)!='+') {
                                auxPhoneNormalized += Character.toString(auxPhone.charAt(i));
                            }
                        }
                        if (auxPhoneNormalized.charAt(0)=='3' && auxPhoneNormalized.charAt(1)=='4') {
                            auxPhoneNormalized = auxPhoneNormalized.substring(2,auxPhoneNormalized.length());
                        }
                        Log.d("antichill","Phone normalitzat: "+auxPhoneNormalized);
                        contactes.add(new AbstractMap.SimpleEntry<String, String>(auxPhoneNormalized,name));
                    }
                    pCur.close();
                }
            }
            Log.d("antichill","mida contacts: "+contactes.size());
        }
    }

    /*public String getContactNameByPhoneNumber(Integer phone) {

    }*/

}

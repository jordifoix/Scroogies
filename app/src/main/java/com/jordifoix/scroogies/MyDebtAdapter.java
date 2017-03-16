package com.jordifoix.scroogies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jordifoix on 5/3/17.
 */

public class MyDebtAdapter extends ArrayAdapter<Deute> implements Filterable {

    private View view = new View(this.getContext());
    private List<Deute> deutes;
    private TextView nameText,telephoneText,decDeuText,deuteTotalText;
    private Button whatsappButton,mesPastukiButton,pagarCobrarButton;
    private Context context;

    public MyDebtAdapter(Context context, List<Deute> deutes) {
        super(context, R.layout.deute_list_item, deutes);
        this.context = context;
        this.deutes = deutes;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.deute_list_item,parent,false);

        nameText = (TextView) view.findViewById(R.id.nameText);
        telephoneText = (TextView) view.findViewById(R.id.telephoneText);
        decDeuText = (TextView) view.findViewById(R.id.decDeuText);
        deuteTotalText = (TextView) view.findViewById(R.id.deuteTotalText);
        whatsappButton = (Button) view.findViewById(R.id.whatsappButton);
        mesPastukiButton = (Button) view.findViewById(R.id.mesPastukiButton);
        pagarCobrarButton = (Button) view.findViewById(R.id.pagarCobrarButton);

        nameText.setText(getItem(position).getTelefonUsuari2().toString());
        telephoneText.setText("");
        if (!(getItem(position).getNomUsuari2().toString().equals(""))) {
            nameText.setText(getItem(position).getNomUsuari2().toString());
            telephoneText.setText(getItem(position).getTelefonUsuari2().toString());
        }
        if (getItem(position).getQuantitat()<0.0) {
            decDeuText.setText("Em deu:");
            deuteTotalText.setText((getItem(position).getQuantitat())*(-1)+" €");
            view.setBackgroundColor(Color.parseColor("#10CE30"));
        }
        else {
            decDeuText.setText("Li dec:");
            deuteTotalText.setText(getItem(position).getQuantitat()+" €");
            view.setBackgroundColor(Color.parseColor("#1E90FF"));
        }

        pagarCobrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PagarActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }

}

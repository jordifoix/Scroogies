package com.jordifoix.scroogies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PagarActivity extends AppCompatActivity {

    private Button enrereButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar);

        enrereButton = (Button) findViewById(R.id.enrereButton);
        enrereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagarActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

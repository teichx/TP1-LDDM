package com.example.tp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnIntegracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        btnIntegracao = findViewById( R.id.btnIntegracao );
        btnIntegracao.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityIntegracao();
            }
        } );

    }

    public void openActivityIntegracao(){
        Intent intent = new Intent( this, Integracao.class );
        startActivity( intent );
    }
}

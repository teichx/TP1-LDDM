package com.example.tp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Integracao extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_integracao );

        Button btnMain = (Button) findViewById( R.id.btnMain );
        btnMain.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent( Integracao.this, MainActivity.class );
                startActivity( intent );
            }
        });
    }
}

package com.example.tp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private Button btnIntegracao, button_salvar;
    private EditText editText_nome, editText_apelido, editText_telefone, editText_empresa, editText_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        btnIntegracao = findViewById( R.id.btnIntegracao );
        btnIntegracao.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent( MainActivity.this, Integracao.class );
                startActivity( intent );
            }
        });

        button_salvar = findViewById( R.id.button_salvar );
        button_salvar.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getActivityElements();
            }
        });

    }

    public void getActivityElements(){
        editText_nome = findViewById( R.id.editText_nome );
        editText_apelido = findViewById( R.id.editText_apelido );
        editText_telefone = findViewById( R.id.editText_telefone );
        editText_empresa = findViewById( R.id.editText_empresa );
        editText_email = findViewById( R.id.editText_email );

        String nome = String.valueOf( editText_nome.getText() ),
                apelido = String.valueOf( editText_apelido.getText() ),
                telefone = String.valueOf( editText_telefone.getText() ),
                empresa = String.valueOf( editText_empresa.getText() ),
                email = String.valueOf( editText_email.getText() );

        saveContact(nome, apelido, telefone, empresa, email);
    }

    public void saveContact(String nome, String apelido, String telefone, String empresa, String email){
        Intent intent = new Intent( ContactsContract.Intents.Insert.ACTION );
        intent.setType( ContactsContract.RawContacts.CONTENT_TYPE );

        intent
            .putExtra( ContactsContract.Intents.Insert.NAME, nome )
            .putExtra( ContactsContract.Intents.Insert.PHONETIC_NAME, apelido )
            .putExtra( ContactsContract.Intents.Insert.PHONE, telefone )
            .putExtra( ContactsContract.Intents.Insert.EMAIL, email )
            .putExtra( ContactsContract.Intents.Insert.COMPANY, empresa )
            ;

        startActivity( intent );
    }


}

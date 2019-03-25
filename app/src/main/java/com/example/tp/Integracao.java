package com.example.tp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class Integracao extends AppCompatActivity{

    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private TextView txtNome, txtEmail;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_integracao );

        loginButton = findViewById( R.id.login_button );
        txtNome = findViewById( R.id.profile_name );
        txtEmail = findViewById( R.id.profile_email );
        circleImageView = findViewById( R.id.profile_pic );

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions( Arrays.asList("email", "public_profile") );

        loginButton.registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        } );

        Button btnMain = (Button) findViewById( R.id.btnMain );
        btnMain.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent( Integracao.this, MainActivity.class );
                startActivity( intent );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult( requestCode, resultCode, data );
        super.onActivityResult( requestCode, resultCode, data );
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null){
                txtEmail.setText( "" );
                txtNome.setText( "" );
                circleImageView.setImageResource( 0 );
                Toast.makeText(Integracao.this, "Usuario nao logado", Toast.LENGTH_LONG).show();
            }
            else{
                loadUserProfile( currentAccessToken );
            }
        }
    };

    private void loadUserProfile(AccessToken newAccesToken){
        GraphRequest request = GraphRequest.newMeRequest( newAccesToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString( "first_name" );
                    String last_name = object.getString( "last_name" );
                    String email = object.getString( "email" );
                    String id = object.getString( "id" );
                    String imageUrl = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    txtNome.setText( first_name + " " + last_name );
                    txtEmail.setText( email );
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with( Integracao.this).load( imageUrl ).into(circleImageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );

        Bundle parameters = new Bundle();
        parameters.putString( "fields", "first_name,last_name,email,id" );
        request.setParameters( parameters );
        request.executeAsync();
    }
}

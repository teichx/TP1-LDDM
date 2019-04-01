package com.example.tp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Integracao extends AppCompatActivity{

    //Facebook
    private LoginButton login_button;
    private CircleImageView fb_image;
    private String fb_image_url;
    private TextView txt_fb_nome, txt_fb_email;
    private CallbackManager callbackManager;
    private ConstraintLayout box_facebook;

    //Instagram
    private Button btnInstagram;
    private ConstraintLayout box_instagram;
    private String insta_image_url;
    private CircleImageView insta_image;
    private TextView txt_profile_instagram, txt_nome_insta;
    private InstagramApp mApp;
    private HashMap<String, String> userInfoHashMap = new HashMap<String, String>();
    private Handler handler = new Handler( new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if( msg.what == InstagramApp.WHAT_FINALIZE ){
                userInfoHashMap = mApp.getUserInfo();
            }
            else if( msg.what == InstagramApp.WHAT_FINALIZE ){
                Toast.makeText( Integracao.this, "Verifique sua conexão", Toast.LENGTH_LONG ).show();
            }
            printInstagram();
            return false;
        }
    } );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_integracao );

        //Facebook
        login_button = findViewById( R.id.login_button );
        txt_fb_nome = findViewById( R.id.profile_fb_name );
        txt_fb_email = findViewById( R.id.profile_fb_email );
        fb_image = findViewById( R.id.profile_pic );
        box_facebook = findViewById( R.id.box_facebook );

        //Instagram
        box_instagram = findViewById( R.id.box_instagram );
        insta_image = findViewById( R.id.profile_instagram );
        txt_profile_instagram = findViewById( R.id.profile_insta_user );
        txt_nome_insta = findViewById( R.id.profile_insta_nome );

        //Facebook
        callbackManager = CallbackManager.Factory.create();
        login_button.setReadPermissions( Arrays.asList("email", "public_profile") );

        //Facebook
        login_button.registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {}

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {}
        } );

        //Instagram
        btnInstagram = findViewById( R.id.btnInstagram );
        mApp = new InstagramApp( Integracao.this, AppConfig.CLIENT_ID, AppConfig.CLIENT_SECRET, AppConfig.CALLBACK_URL);

        mApp.setListener( new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                mApp.fetchUserName( handler );
            }

            @Override
            public void onFail(String error) {
                Toast.makeText( Integracao.this, error, Toast.LENGTH_LONG ).show();
            }
        } );

        btnInstagram.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verifica se o usuario esta logado vericando o texto
                if (txt_profile_instagram.getText().toString().equals( "" )) {
                    mApp.authorize();
                } else {
                    clearInstagram();
                    btnInstagram.setText( "INSTAGRAM" );
                }
            }
        } );

        //Voltar ao menu principal
        Button btnMain = findViewById( R.id.btnMain );
        btnMain.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent( Integracao.this, MainActivity.class );
                startActivity( intent );
            }
        });

        //Pegar estados
        if ( savedInstanceState != null ){
            //Estados do facebook
            if ( savedInstanceState.getString( "nome_facebook" ) != null ){
                txt_fb_nome.setText( savedInstanceState.getString( "nome_facebook" ) );
                txt_fb_email.setText( savedInstanceState.getString( "email_facebook" ) );
                fb_image_url = savedInstanceState.getString( "image_url_facebook" );
                Glide.with( Integracao.this).load( fb_image_url ).into(fb_image);
            }
            //Instagram
            if ( savedInstanceState.getString( "nome_insta" ) != null ){
                btnInstagram.setText( "Sair Instagram" );
                txt_nome_insta.setText( savedInstanceState.getString( "nome_insta" ) );
                txt_profile_instagram.setText( savedInstanceState.getString( "profile_insta" ) );
                insta_image_url = savedInstanceState.getString( "image_url_insta" );
                Glide.with( Integracao.this).load( insta_image_url ).into(insta_image);
            }
        }
       showFacebookData();
        showInstagramData();

    }

    private void clearInstagram(){
        txt_nome_insta.setText( "" );
        txt_profile_instagram.setText( "" );
        insta_image_url = "";
        Glide.with( Integracao.this).load( insta_image_url ).into(insta_image);
        showInstagramData();
        mApp.resetAccessToken();
    }

    private void printInstagram(){
        btnInstagram.setText( "Sair Instagram" );
        txt_nome_insta.setText( userInfoHashMap.get( InstagramApp.TAG_FULL_NAME ) );
        txt_profile_instagram.setText( userInfoHashMap.get( InstagramApp.TAG_USERNAME ) );
        insta_image_url = userInfoHashMap.get( InstagramApp.TAG_PROFILE_PICTURE );
        Glide.with( Integracao.this).load( insta_image_url ).into(insta_image);
        showInstagramData();
    }

    public void showInstagramData(){
        //Se o nome está vazio, não exibe o container dos dados do facebook
        box_instagram.setVisibility( txt_profile_instagram.getText().toString().equals( "" ) ? View.INVISIBLE : View.VISIBLE );
    }

    public void showFacebookData(){
        //Se o nome está vazio, não exibe o container dos dados do facebook
        box_facebook.setVisibility( txt_fb_nome.getText().toString().equals( "" ) ? View.INVISIBLE : View.VISIBLE );
    }

    //Metodos para girar tela e manter salvo as informações
    @Override
    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState( outstate );
        //Facebook
        outstate.putString( "nome_facebook", (String) txt_fb_nome.getText() );
        outstate.putString( "email_facebook", (String) txt_fb_email.getText() );
        outstate.putString( "image_url_facebook", fb_image_url );

        //Instagram
        outstate.putString( "nome_insta", (String) txt_nome_insta.getText()  );
        outstate.putString( "profile_insta", (String) txt_profile_instagram.getText()  );
        outstate.putString( "image_url_insta", insta_image_url );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState( savedInstanceState );
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
                txt_fb_email.setText( "" );
                txt_fb_nome.setText( "" );
                fb_image.setImageResource( 0 );
                Toast.makeText(Integracao.this, "Usuario nao logado", Toast.LENGTH_LONG).show();
                showFacebookData();
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

                    txt_fb_nome.setText( first_name + " " + last_name );
                    txt_fb_email.setText( email );
                    fb_image_url = imageUrl;
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with( Integracao.this).load( imageUrl ).into(fb_image);
                    showFacebookData();
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
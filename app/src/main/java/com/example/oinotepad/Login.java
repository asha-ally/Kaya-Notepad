package com.example.oinotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import static com.example.oinotepad.app.AppFunctions.*;

import static com.facebook.appevents.AppEventsLogger.activateApp;


public class Login extends AppCompatActivity {
    EditText editText_email;
    EditText editText_password;

    String user_email;
    String user_pass;

    Button button;
    LoginButton loginButton;
    Context context;
    private static final String EMAIL = "email";

    Boolean internetConnected = false;
    SharedPreferences notepad_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        internetConnected = isInternetConnected(this);
        Log.d("rage",String.valueOf(internetConnected));

        FacebookSdk.sdkInitialize(getApplicationContext());
 //       activateApp(context);
        setContentView(R.layout.activity_login);
        button=findViewById(R.id.btnlogIn);
       CallbackManager callbackManager = CallbackManager.Factory.create();
        loginButton =  findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        editText_email =findViewById(R.id.editText_email);
        editText_password =findViewById(R.id.editText_password);

        editText_email.setText("asha@mail.com");
        editText_password.setText("12345");

        /* function to check if session is set */
        notepad_pref = getApplicationContext().getSharedPreferences("MySession", 0);
        user_email = notepad_pref.getString("user_email", null);
        user_pass = notepad_pref.getString("user_pass", null);

        func_showToast(context, "user_email ni: "+user_email);

        /* sign out function */
        /*SharedPreferences.Editor session = notepad_pref.edit();
        session.clear().apply();*/


        if(user_email != null){
            displayMainActivity();
        }


        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("facebook", "facebook result "+loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Intent intent=new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String u_email = editText_email.getText().toString();
                String u_pass = editText_password.getText().toString();

                if(u_email.contains("@")){

                    SharedPreferences.Editor session = notepad_pref.edit();
                    session.putString("user_email", u_email);
                    session.putString("user_pass", u_pass);
                    session.apply();

                    displayMainActivity();
                }
                else {
                    func_showToast(context, "Weka password Aiseee!!");

                }

            }
        });


    }

    private void displayMainActivity(){
        Intent intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);

        finish();
    }
}

package com.example.oinotepad;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.example.oinotepad.app.AppFunctions.func_showToast;

public class FirebaseUtil {
    public static FirebaseDatabase mfirebasedatabase;
    public static DatabaseReference mdatabasereference;
    public  static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthlistener;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<Note> mnotes;
    private static final int RC_SIGN_IN=123;
    private static Activity caller;

    private FirebaseUtil(){}
    public static void openFbReference(String ref, final Activity callerActivivty){
        if (firebaseUtil==null){
            firebaseUtil=new FirebaseUtil();
            mfirebasedatabase=FirebaseDatabase.getInstance();
            mFirebaseAuth=FirebaseAuth.getInstance();
            caller= (Activity) callerActivivty;
            mAuthlistener=new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (mFirebaseAuth.getCurrentUser()==null){
                    FirebaseUtil.signIn();
                    }

                    func_showToast(caller.getBaseContext(),"Welcome Back");
                }
            };

        }

        mnotes=new ArrayList<Note>();
        mdatabasereference=mfirebasedatabase.getReference().child(ref);
    }
    private static void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
       caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }


    public static void attachListener(){
        mFirebaseAuth.addAuthStateListener(mAuthlistener);
    }
    public static void dettachListner(){
        mFirebaseAuth.removeAuthStateListener(mAuthlistener);
    }
}

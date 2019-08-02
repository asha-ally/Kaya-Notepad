package com.example.oinotepad;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase mfirebasedatabase;
    public static DatabaseReference mdatabasereference;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<Note> mnotes;

    private FirebaseUtil(){}
    public static void openFbReference(String ref){
        if (firebaseUtil==null){
            firebaseUtil=new FirebaseUtil();
            mfirebasedatabase=FirebaseDatabase.getInstance();
            mnotes=new ArrayList<Note>();
        }
        mdatabasereference=mfirebasedatabase.getReference().child(ref);
    }
}

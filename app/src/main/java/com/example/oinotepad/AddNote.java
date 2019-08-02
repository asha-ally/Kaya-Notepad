package com.example.oinotepad;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNote extends AppCompatActivity {
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mdatabasereference;
    EditText etTitle;
    EditText etNote;
    Button AddPhoto;
    Button AddVoiceNote;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseUtil.openFbReference("notes");
        mfirebasedatabase=FirebaseUtil.mfirebasedatabase;
        mdatabasereference= FirebaseUtil.mdatabasereference;
        etTitle=findViewById(R.id.etTitle);
        etNote=findViewById(R.id.etNote);
        AddPhoto=findViewById(R.id.btnAddPhoto);
        AddVoiceNote=findViewById(R.id.btnAddVoiceNote);
        btnSave=findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
                clean();

            }
        });
    }
    private void saveNote(){
        String title=etTitle.getText().toString();
        String noteText=etNote.getText().toString();
        Note note=new Note(title,noteText,"","");
        mdatabasereference.push().setValue(note); //.addOnFailureListener( AddNote.class,"hakuna");
        Log.d("fire", "etc.. "+note);
    }
    private void clean(){
        etTitle.setText("");
        etNote.setText("");
        etTitle.requestFocus();
    }

}

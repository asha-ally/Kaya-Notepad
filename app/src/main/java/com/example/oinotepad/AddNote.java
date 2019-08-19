package com.example.oinotepad;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

import static com.example.oinotepad.MainActivity.imageRoot;
import static com.example.oinotepad.app.AppFunctions.func_showToast;

public class AddNote extends AppCompatActivity {
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mdatabasereference;
    EditText etTitle;
    EditText etNote;
    Button AddPhoto;
    Button AddVoiceNote;
    Button btnSave;
    Note note;
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 600;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseUtil.openFbReference("notes", this);
        mfirebasedatabase = FirebaseUtil.mfirebasedatabase;
        mdatabasereference = FirebaseUtil.mdatabasereference;
        etTitle = findViewById(R.id.etTitle);
        etNote = findViewById(R.id.etNote);
        AddPhoto = findViewById(R.id.btnAddPhoto);
        AddVoiceNote = findViewById(R.id.btnAddVoiceNote);
        btnSave = findViewById(R.id.btnSave);
        Intent intent = getIntent();
        Note note = (Note) intent.getSerializableExtra("Note");
        if (note == null) {
            note = new Note();
        }
        this.note = note;
        etTitle.setText(note.getTitle());
        etNote.setText(note.getNotes());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
                clean();

            }
        });
        AddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
                imageRoot.mkdirs();
                final File image = new File(imageRoot, "image1.jpg");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
//            if (imageUri!=null){
//            StorageReference ref=FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());
//            ref.putFile(imageUri);}
            try {
                StorageReference ref = FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());
                ref.putFile(imageUri);
            } catch (NullPointerException e) {
                func_showToast(this, "error");
            }
        }

//       
        AddVoiceNote.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
//        MediaRecorder myAudioRecorder = new MediaRecorder();
//        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        myAudioRecorder.setOutputFile("mp3");
//        try {
//            myAudioRecorder.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        myAudioRecorder.start();

    }
    });
}

    private void saveNote(){
        note.setTitle(etTitle.getText().toString());
        note.setNotes(etNote.getText().toString());
        if (note.getId()==null){
            mdatabasereference.push().setValue(note); //.addOnFailureListener( AddNote.class,"hakuna");

        }
        else {
            mdatabasereference.child(note.getId()).setValue(note);
        }
        Log.d("fire", "etc.. "+note);
    }




    private void clean(){
        etTitle.setText("");
        etNote.setText("");
        etTitle.requestFocus();
    }


}

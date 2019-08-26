package com.example.oinotepad;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
    Context context;
    private static final int PICTURE_RESULT= 42;
    private static final int AUDIO_RESULT= 52;
    ImageView imageView;


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
        context=AddNote.this;
         imageView=findViewById(R.id.imageView);

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
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
               intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent.createChooser(intent,
                        "Insert Picture"),PICTURE_RESULT);
           }
        });
        AddVoiceNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/amr");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent.createChooser(intent,
                        "Insert Audio"),AUDIO_RESULT);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK && data!= null && data.getData() != null ) {
            Uri imageUri = data.getData();
                final StorageReference ref = FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());
            UploadTask task = ref.putFile(imageUri);
            Task<Uri> urlTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.d("pic",downloadUri.toString());
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
//            .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                   @Override
//                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                       String url=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                       Log.d("url",url);
//                       note.setImageUrl(url);
//                       saveNote();
//
//                   }
//               });



        }

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

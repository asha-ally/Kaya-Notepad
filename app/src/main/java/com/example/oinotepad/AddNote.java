package com.example.oinotepad;

import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.Inflater;

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
    ImageView imageView;
    private Uri filePath;
    String url;


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
        imageView= findViewById(R.id.imgView);

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
                func_showToast(context,"note saved successfully");

            }
        });
        AddPhoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
               intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,
                        "Insert Picture"),PICTURE_RESULT);
               uploadImage();

           }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK && data!= null && data.getData() != null ) {
            filePath = data.getData();
//            filePathUrl =filePath.getLastPathSegment();
            try {
                Intent intent= new Intent(getBaseContext(),ViewNote.class);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //MethodB for uploading image
//            Uri imageUri = data.getData();
//                final StorageReference ref = FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());
//                ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                   @Override
//                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                      url =taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                       Log.d("pic",url);
//                       note.setImageUrl(url);
//                   }
//
//               });



        }

}

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = FirebaseUtil.mStorageReference .child("note_images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            url =taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            Log.d("pic",url);
                            note.setImageUrl(url);
                            progressDialog.dismiss();
                            func_showToast(context,"uploaded");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            func_showToast(context,"upload failed");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }


    private void saveNote(){
        note.setTitle(etTitle.getText().toString());
        note.setNotes(etNote.getText().toString());
        note.setImageUrl(url);
        note.setAudioUrl("");
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
        imageView.setImageBitmap(null);
        etTitle.requestFocus();
    }


}

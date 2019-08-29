package com.example.oinotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.oinotepad.app.AppFunctions.func_showToast;

public class ViewNote extends AppCompatActivity {
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mdatabasereference;
    TextView tvTitle;
    TextView tvNoteText;
    Button btnDelete;
    ImageView imageView;
    EditText etTitle;
    EditText etNote;
    Button btnEdit;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        FirebaseUtil.openFbReference("notes",this);
        mfirebasedatabase=FirebaseUtil.mfirebasedatabase;
        mdatabasereference= FirebaseUtil.mdatabasereference;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tvTitle=findViewById(R.id.tvTitle);
        tvNoteText=findViewById(R.id.tvNoteText);
        etTitle=findViewById(R.id.etTitle);
        etNote=findViewById(R.id.etNote);
        btnDelete=findViewById(R.id.btnDelete);
        btnEdit=findViewById(R.id.btnEdit);
        imageView=findViewById(R.id.imageView);
        Intent intent=getIntent();Note note=(Note) intent.getSerializableExtra("Note");

                if (note==null){
                    note=new Note();
                }
                this.note=note;
                tvTitle.setText(note.getTitle());
                tvNoteText.setText(note.getNotes());
                String tvImage = note.getImageUrl();
                func_showToast(getBaseContext(), tvImage);

//                imageView.setImageBitmap(bm);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),AddNote.class);
                startActivity(intent);

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletenote();
                backToList();

            }
        });



    }
    private  void deletenote(){
        if (note==null){
            func_showToast(this,"Please save the note first");
            return;
        }

        else {
            mdatabasereference.child(note.getId()).removeValue();
        }
    }

    private void backToList(){
        Intent intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
    }
}

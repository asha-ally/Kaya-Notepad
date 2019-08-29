package com.example.oinotepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> notes;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mdatabasereference;
    private ChildEventListener mchildEventListener;
    private static Activity callerActivity;

    public NoteAdapter(){
        FirebaseUtil.openFbReference("notes",callerActivity);
        mfirebasedatabase=FirebaseUtil.mfirebasedatabase;
        mdatabasereference= FirebaseUtil.mdatabasereference;
        notes=FirebaseUtil.mnotes;
        mchildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Note note=dataSnapshot.getValue(Note.class);
                Log.d("fity",note.getTitle());
//                Log.d("pic",note.getImageUrl());

                note.setId(dataSnapshot.getKey());
                notes.add(note);
                notifyItemInserted(notes.size()-1);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mdatabasereference.addChildEventListener(mchildEventListener);

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        View itemView= LayoutInflater.from(context).
                inflate(R.layout.rv_row,parent,false);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note=notes.get(position);
        holder.bind(note);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvNote=itemView.findViewById(R.id.tvNote);
            itemView.setOnClickListener(this);

        }
        public void bind(Note note){
            tvTitle.setText(note.getTitle());
            tvNote.setText(note.getNotes());
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            Log.d("position",String.valueOf(position));
            Note clickednote=notes.get(position);
            Intent intent=new Intent(view.getContext(),ViewNote.class);
            intent.putExtra("Note", clickednote);
            view.getContext().startActivity(intent);



        }
    }
}

package com.example.oinotepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.oinotepad.app.AppFunctions.func_showToast;

public class MainActivity extends AppCompatActivity {
    Context context;
    SharedPreferences notepad_pref;
    String user_email;
    String user_pass;


    ArrayList<Note>notes;
    ImageView fab;
    private DrawerLayout drawer;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mdatabasereference;
    private ChildEventListener mchildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        notepad_pref = getApplicationContext().getSharedPreferences("MySession", 0);
        user_email = notepad_pref.getString("user_email", null);
        user_pass = notepad_pref.getString("user_pass", null);

        func_showToast(context, "user_email is: "+user_email);


        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);

        RecyclerView rvNotes=findViewById(R.id.rvNotes);
        final NoteAdapter adapter=new NoteAdapter();
        rvNotes.setAdapter(adapter);
        LinearLayoutManager noteslayoutmanager=
                new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rvNotes.setLayoutManager(noteslayoutmanager);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddNote.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
////
////        return super.onOptionsItemSelected(item);
////    }
//        return""
//    }
}

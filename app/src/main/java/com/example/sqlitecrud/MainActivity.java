package com.example.sqlitecrud;

import android.content.Intent;
import android.os.Bundle;

import com.example.sqlitecrud.helpers.DatabaseHandler;
import com.example.sqlitecrud.helpers.NoteAdapter;
import com.example.sqlitecrud.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "app_log";
    private static final int ADD_CODE = 1001;
    RecyclerView recyclerView;
    NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.main_recycler_view);

        initiateRecycler(false);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddNote.class), ADD_CODE);
            }
        });
    }

    private void initiateRecycler(boolean refresh) {
        final DatabaseHandler handler = new DatabaseHandler(this);
        final List<Note> notes = handler.getAllNote();
        adapter = new NoteAdapter(notes, this);
        recyclerView.setAdapter(adapter);
        if (!refresh) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: RequestCode: " + requestCode + " || Result Code: " + resultCode);
        switch (requestCode) {
            case ADD_CODE:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: Refreshing list!");
                    initiateRecycler(true);
                }
            case 1002:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: Refreshing list!");
                    initiateRecycler(true);
                }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            initiateRecycler(true);
            Toast.makeText(this, "Refreshed...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
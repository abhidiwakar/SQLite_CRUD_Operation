package com.example.sqlitecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlitecrud.helpers.DatabaseHandler;
import com.example.sqlitecrud.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;

public class AddNote extends AppCompatActivity {

    EditText etTitle, etDescription;
    FloatingActionButton btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        etTitle = findViewById(R.id.note_title);
        etDescription = findViewById(R.id.note_description);

        btnDone = findViewById(R.id.add_note_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    private void addNote() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (!(title.length() > 0)) {
            Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!(description.length() > 0)) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setDescription(description);
        note.setDate(new Date(new java.util.Date().getTime()));
        DatabaseHandler handler = new DatabaseHandler(this);
        handler.addNote(note);
        Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
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

public class EditNote extends AppCompatActivity {
    EditText etTitle, etDescription;
    FloatingActionButton btnDone;
    int noteId;
    Note currentNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        etTitle = findViewById(R.id.edit_note_title);
        etDescription = findViewById(R.id.edit_note_description);

        DatabaseHandler handler = new DatabaseHandler(this);
        currentNote = handler.getNoteById(this.getIntent().getIntExtra("noteId", 0));

        if(currentNote == null) {
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Can't update this note right now!", Toast.LENGTH_SHORT).show();
            finish();
        }

        etTitle.setText(currentNote.getTitle());
        etDescription.setText(currentNote.getDescription());

        btnDone = findViewById(R.id.edit_note_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
            }
        });
    }

    private void updateNote() {
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

        currentNote.setTitle(title);
        currentNote.setDescription(description);
        currentNote.setDate(new Date(new java.util.Date().getTime()));
        DatabaseHandler handler = new DatabaseHandler(this);
        handler.updateNote(currentNote);
        Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
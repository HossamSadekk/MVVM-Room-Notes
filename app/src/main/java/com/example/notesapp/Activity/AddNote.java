package com.example.notesapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.notesapp.R;

public class AddNote extends AppCompatActivity {

    public static final String EXTRA_ID ="com.example.notesapp.EXTRA_ID";
    public static final String EXTRA_TITLE ="com.example.notesapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION ="com.example.notesapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY="com.example.notesapp.EXTRA_PRIORITY";
private EditText et_title;
private EditText et_description;
private NumberPicker number_picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_description);
        number_picker = findViewById(R.id.number_picker);
        number_picker.setMinValue(0);
        number_picker.setMaxValue(20);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID))
        {
            setTitle("Edit Note");
            et_title.setText(intent.getStringExtra(EXTRA_TITLE));
            et_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            number_picker.setValue(intent.getIntExtra(EXTRA_PRIORITY,0));
        }
        else
        {
        setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void saveNote() {
        String title = et_title.getText().toString();
        String description = et_description.getText().toString();
        int priority = number_picker.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty())
        {
            Toast.makeText(this, "Empty fields!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE,title);
        data.putExtra(EXTRA_DESCRIPTION,description);
        data.putExtra(EXTRA_PRIORITY,priority);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if(id != -1)
        {
            data.putExtra(EXTRA_ID,id);
        }
        setResult(RESULT_OK,data);
        finish();
    }
}
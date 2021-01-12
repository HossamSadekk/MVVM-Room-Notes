package com.example.notesapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.Room.Note;
import com.example.notesapp.Adapter.NoteAdapter;
import com.example.notesapp.ViewModel.NoteViewModel;
import com.example.notesapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
private NoteViewModel noteViewModel;
FloatingActionButton floatingActionButton;
NoteAdapter noteAdapter;

public static final int ADD_NOTE_REQUEST=1;
public static final int Edit_NOTE_REQUEST=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ADDING NEW NOTE
        floatingActionButton = findViewById(R.id.add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNote.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        //RECYCLER VIEW
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);


        // VIEW MODEL
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.gettAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //Updating RecyclerView
                noteAdapter.submitList(notes);

            }
        });

        //DELETING NOTE BY SWIPE IT
        // adding here swipe to two directions , we can swipe it to only one direction
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
            // we should attach this to our recycler view object if we don't this won't work
        }).attachToRecyclerView(recyclerView);



        // handle click on note to update it
        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNote.class);
                intent.putExtra(AddNote.EXTRA_ID,note.getId());
                intent.putExtra(AddNote.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddNote.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddNote.EXTRA_PRIORITY,note.getPriority());
                startActivityForResult(intent,Edit_NOTE_REQUEST);

            }
        });




    }
    // this returns data from AddNote activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_NOTE_REQUEST && resultCode==RESULT_OK)
        {
            String title = data.getStringExtra(AddNote.EXTRA_TITLE);
            String description = data.getStringExtra(AddNote.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNote.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();

        }
        // return data from update the note
        else if(requestCode==Edit_NOTE_REQUEST && resultCode==RESULT_OK)
        {
            String title = data.getStringExtra(AddNote.EXTRA_TITLE);
            String description = data.getStringExtra(AddNote.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNote.EXTRA_PRIORITY,1);
            int id = data.getIntExtra(AddNote.EXTRA_ID,-1);
            if(id==-1){
                Toast.makeText(this, "note cannot be update", Toast.LENGTH_SHORT).show();
                return;
            }
            Note note = new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
        }
        else
        {
            Toast.makeText(this, "Note not saved !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.deleteAllNotes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
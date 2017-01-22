package com.kazuya.pluralsight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NoteList extends AppCompatActivity {

    private List<Note> notes = new ArrayList<Note>();
    private ListView notesListView;
    private int editingNoteId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        notesListView = (ListView)findViewById(R.id.list);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int itemNumber, long id) {
                Intent editNoteIntent = new Intent(view.getContext(),EditNote.class);
                editNoteIntent.putExtra("Note", notes.get(itemNumber));
                editingNoteId = itemNumber;
                startActivityForResult(editNoteIntent, 1);
            }
        });

        registerForContextMenu(notesListView);

        notes.add(new Note("1st Note", "Placeholder", new Date()));
        notes.add(new Note("Second Note", "Placeholder", new Date()));
        notes.add(new Note("Third Note", "Placeholder", new Date()));
        notes.add(new Note("Fourth Note", "Placeholder", new Date()));
        notes.add(new Note("Fifth Note", "Placeholder", new Date()));

        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent editNoteIntent = new Intent(this, EditNote.class);
        startActivityForResult(editNoteIntent, 1);

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        notes.remove(info.position);
        populateList();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED){
            return;
        }
        Serializable extra = data.getSerializableExtra("Note");
        if(extra != null){
            Note newNote = (Note)extra;
            if(editingNoteId>-1) {
                notes.set(editingNoteId, newNote);
                editingNoteId=-1;
            }
            else{
                notes.add(newNote);
            }

            populateList();
        }
    }

    private void populateList(){
        List<String> values = new ArrayList<String>();

        for (Note note : notes){
            values.add(note.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, values);

        notesListView.setAdapter(adapter);
    }
}

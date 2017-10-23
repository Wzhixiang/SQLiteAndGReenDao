package com.wzx.sqliteandgreendao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.anye.greendao.gen.DaoSession;
import com.anye.greendao.gen.NoteDao;

import org.greenrobot.greendao.query.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class SQLiteActivity extends AppCompatActivity {

    private EditText editText;
    private View addNoteButton;

    private NoteDao noteDao;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        setUpViews();
        // get the note DAO
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        noteDao = daoSession.getNoteDao();
        // query all notes, sorted a-z by their text
        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Text).build();
//        /*时间排序*/
//        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Date).build();
        updateNotes();
    }

    private void updateNotes() {
        List<Note> notes = notesQuery.list();
        notesAdapter.setNotes(notes);
    }

    protected void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesAdapter = new NotesAdapter(noteClickListener);
        recyclerView.setAdapter(notesAdapter);

        addNoteButton = findViewById(R.id.buttonAdd);
        //noinspection ConstantConditions
        addNoteButton.setEnabled(false);

        editText = (EditText) findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onAddButtonClick(View view) {
        addNote();
    }

    public void onQueryButtonClick(View view) {
        queryNote();
    }

    public void onLikeQueryButtonClick(View view){
        likeQueryNote();
    }


    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Note note = new Note();
        note.setText(noteText);
        note.setComment(comment);
        note.setDate(new Date());
        note.setType(NoteType.TEXT);
        noteDao.insert(note);

        Log.d("DaoExample", "Inserted new note, ID: " + note.getId());

        updateNotes();
    }

    private void queryNote() {
        /*具体查询*/
        Query query = noteDao.queryBuilder().where(
                NoteDao.Properties.Text.eq(""))
                .build();

        String noteText = editText.getText().toString();
        editText.setText("");

        List<Note> notes = query.setParameter(0, noteText).list();
        notesAdapter.setNotes(notes);
    }

    private void likeQueryNote(){
        /*模糊查询*/
        Query likeQuery = noteDao.queryBuilder().where(
                NoteDao.Properties.Text.like(""))
                .orderAsc(NoteDao.Properties.Text)
                .build();

        String noteText = "%"+editText.getText().toString()+"%";
        editText.setText("");

        List<Note> notes = likeQuery.setParameter(0, noteText).list();
        notesAdapter.setNotes(notes);
    }

    private void updateNote(Note note) {
        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Update on " + df.format(new Date());
        /*更新*/
        note.setText("更新" + note.getText());
        noteDao.update(note);
        note.setDate(new Date());
        note.setComment(comment);
        Log.d("DaoExample", "Update note, ID: " + note.getId());
        updateNotes();
    }

    NotesAdapter.NoteClickListener noteClickListener = new NotesAdapter.NoteClickListener() {
        @Override
        public void onNoteClick(int position) {
            Note note = notesAdapter.getNote(position);
            Long noteId = note.getId();
            /*删除*/
            noteDao.deleteByKey(noteId);
            Log.d("DaoExample", "Deleted note, ID: " + noteId);
            updateNotes();
        }

        @Override
        public void onNoteLongClick(int position) {
            updateNote(notesAdapter.getNote(position));
        }
    };
}

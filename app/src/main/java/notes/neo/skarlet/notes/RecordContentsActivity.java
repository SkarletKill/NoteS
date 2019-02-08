package notes.neo.skarlet.notes;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.entity.Record;
import notes.neo.skarlet.notes.entity.Constants;

public class RecordContentsActivity extends AppCompatActivity {
    private Integer recordId;
    private EditText noteText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_contents);

        recordId = getIntent().getExtras().getInt(Constants.RECORD);
        noteText = (EditText) findViewById(R.id.record_contents);
        saveButton = (Button) findViewById(R.id.btn_record_contents);

        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();
        Record record = db.recordDao().getById(recordId);
    }

    public void onSaveClick(View view) {

    }
}

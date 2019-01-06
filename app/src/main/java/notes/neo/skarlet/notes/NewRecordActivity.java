package notes.neo.skarlet.notes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import notes.neo.skarlet.notes.adapters.GenreItemAdapter;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Genre;
import notes.neo.skarlet.notes.entity.Constants;
import notes.neo.skarlet.notes.entity.GenreItem;

public class NewRecordActivity extends AppCompatActivity {
    private Integer categoryId;
    private EditText name;
    private Spinner spinnerGenres;
    private RatingBar ratingBar;
    private TextView ratingMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new);

        categoryId = getIntent().getExtras().getInt(Constants.CATEGORY_ID);

        name = (EditText) findViewById(R.id.newRecordName);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingMark = (TextView) findViewById(R.id.ratingMark);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingMark.setText(String.valueOf((int) (v * 2)));
            }
        });

        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);

        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();

        List<Genre> genreList = db.genreDao().getByCategoryId(categoryId);

        List<GenreItem> genreItems = new ArrayList<>();
        genreItems.add(new GenreItem("Select genres", false));
        for (Genre genre : genreList) {
            genreItems.add(new GenreItem(genre.getName(), false));
        }
        GenreItemAdapter myAdapter = new GenreItemAdapter(NewRecordActivity.this, 0,
                genreItems);
        spinnerGenres.setAdapter(myAdapter);
    }

    public void onCancelClick(View view) {
        onBackPressed();
    }

    public void onConfirmClick(View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.CATEGORY_ID, categoryId);
        startActivity(intent);
    }
}

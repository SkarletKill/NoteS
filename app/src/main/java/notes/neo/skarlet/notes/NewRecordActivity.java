package notes.neo.skarlet.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import notes.neo.skarlet.notes.adapters.GenreItemAdapter;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Genre;
import notes.neo.skarlet.notes.database.entity.RecCat;
import notes.neo.skarlet.notes.database.entity.Record;
import notes.neo.skarlet.notes.entity.Constants;
import notes.neo.skarlet.notes.entity.CreationType;
import notes.neo.skarlet.notes.entity.GenreItem;

public class NewRecordActivity extends AppCompatActivity {
    private Integer categoryId;
    private CreationType creationType;

    private TextView title;
    private EditText name;
    private Spinner spinnerGenres;
    private RatingBar ratingBar;
    private TextView ratingMark;

    private List<Genre> genreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new);

        categoryId = getIntent().getExtras().getInt(Constants.CATEGORY_ID);
        creationType = CreationType.get(getIntent().getExtras().getString(Constants.CREATION_TYPE));

        title = (TextView) findViewById(R.id.newRecord);
        if (creationType.equals(CreationType.CREATION))
            title.setText(Constants.ADD_RECORD);
        else if (creationType.equals(CreationType.EDIT))
            title.setText(Constants.EDIT_RECORD);
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

        genreList = db.genreDao().getByCategoryId(categoryId);

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

    @TargetApi(Build.VERSION_CODES.N)
    public void onConfirmClick(View view) {
        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, "notes_database")
                .allowMainThreadQueries().build();
        String name = String.valueOf(this.name.getText());
        Integer mark = Integer.parseInt(String.valueOf(this.ratingMark.getText()));
        Record record = new Record(categoryId, name, mark);
        if (creationType.equals(CreationType.CREATION))
            db.recordDao().insert(record);
        else if (creationType.equals(CreationType.EDIT)) {
            Record recordWithId = db.recordDao().getById(getIntent().getExtras().getInt(Constants.RECORD));
            recordWithId.setName(name);
            recordWithId.setRating(mark);
            db.recordDao().update(recordWithId);
        }

        Record recordWithId = db.recordDao().getByName(record.getName())
                .stream().filter(r -> record.getRating().equals(r.getRating()))
                .findAny().get();
        List<GenreItem> genreItems = retrieveAllItems(spinnerGenres)
                .stream().filter(GenreItem::isSelected).collect(Collectors.toCollection(ArrayList::new));

        // clear all genres for record
        if (creationType.equals(CreationType.EDIT)) {
            List<RecCat> recCats = db.recCatDao().getByRecordId(recordWithId.getId());
            for (RecCat recCat : recCats) {
                db.recCatDao().delete(recCat);
            }
        }

        // add genres for record
        for (GenreItem genreItem : genreItems) {
            Integer genreId = findGenreByName(genreItem.getTitle()).getId();

            RecCat recCat = new RecCat(genreId, recordWithId.getId());
            db.recCatDao().insert(recCat);
        }

        Intent intent = new Intent(this, RecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    private List<GenreItem> retrieveAllItems(Spinner theSpinner) {
        Adapter adapter = theSpinner.getAdapter();
        int n = adapter.getCount();
        List<GenreItem> genreItems = new ArrayList<GenreItem>(n);
        for (int i = 0; i < n; i++) {
            GenreItem user = (GenreItem) adapter.getItem(i);
            genreItems.add(user);
        }
        return genreItems;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Genre findGenreByName(String genreName) {
        return genreList.stream().filter(g -> g.getName().equals(genreName)).findFirst().get();
    }
}

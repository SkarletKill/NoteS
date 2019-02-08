package notes.neo.skarlet.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import notes.neo.skarlet.notes.adapters.GenreAdapter;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.entity.Genre;
import notes.neo.skarlet.notes.database.entity.Record;
import notes.neo.skarlet.notes.entity.Constants;
import notes.neo.skarlet.notes.entity.CreationType;

public class NewGenreActivity extends AppCompatActivity {
    private Bundle extras;
    private Integer categoryId;
    private TextView title;
    private EditText name;
    private ListView listOfGenres;
    private List<String> genres;

    private CreationType creationType;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_genre);

        extras = getIntent().getExtras();
        categoryId = extras.getInt(Constants.CATEGORY_ID);
        creationType = CreationType.get(extras.getString(Constants.CREATION_TYPE));

        title = (TextView) findViewById(R.id.newGenre);
        if (creationType.equals(CreationType.CREATION))
            title.setText(Constants.ADD_GENRE);
        else if (creationType.equals(CreationType.EDIT))
            title.setText(Constants.EDIT_GENRE);

        name = (EditText) findViewById(R.id.newGenreName);
        listOfGenres = (ListView) findViewById(R.id.listOfGenres);

        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();

        if (creationType.equals(CreationType.EDIT)) {
            Genre editableGenre = db.genreDao().getById(extras.getInt(Constants.GENRE));
            name.setText(editableGenre.getName());
        }

        List<Genre> genreList = new ArrayList<>(db.genreDao().getByCategoryId(categoryId));

        genres = genreList.stream().map(Genre::getName).collect(Collectors.toCollection(ArrayList::new));

        listOfGenres.setAdapter(new ArrayAdapter<>(this, R.layout.genre_listview_detail, genres));
    }

    public void onCancelClick(View view) {
        onBackPressed();
    }

    public void onConfirmClick(View view) {
        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, "notes_database")
                .allowMainThreadQueries().build();
        String name = String.valueOf(this.name.getText()).trim();
        Genre genre = new Genre(extras.getInt(Constants.CATEGORY_ID), name);

        if (creationType.equals(CreationType.CREATION))
            db.genreDao().insert(genre);
        else if (creationType.equals(CreationType.EDIT)) {
            Genre genreWithId = db.genreDao().getById(extras.getInt(Constants.GENRE));
            genreWithId.setName(name);
            db.genreDao().update(genreWithId);
        }


        Intent intent = new Intent(this, GenreActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.CATEGORY_ID, categoryId);
        startActivity(intent);
    }
}

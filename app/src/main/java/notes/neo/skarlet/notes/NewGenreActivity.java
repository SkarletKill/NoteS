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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.entity.Genre;

public class NewGenreActivity extends AppCompatActivity {
    private EditText name;
    private ListView listOfGenres;
    private List<String> genres;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_genre);

        name = (EditText) findViewById(R.id.newGenreName);
        listOfGenres = (ListView) findViewById(R.id.listOfGenres);

        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();

        List<Genre> genreList = new ArrayList<>();
        genreList.addAll(db.genreDao().getByCategoryId(getIntent().getExtras().getInt("categoryId")));

        genres = genreList.stream().map(Genre::getName).collect(Collectors.toCollection(ArrayList::new));

        listOfGenres.setAdapter(new ArrayAdapter<>(this, R.layout.genres_listview_detail, genres));
    }

    public void onCancelClick(View view) {
        onBackPressed();
    }

    public void onConfirmClick(View view) {
        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, "notes_database")
                .allowMainThreadQueries().build();
        String name = String.valueOf(this.name.getText());
        Genre genre = new Genre(getIntent().getExtras().getInt("categoryId"), name);

        db.genreDao().insert(genre);

        onBackPressed();
//        Intent intent = new Intent(this, RecordActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }
}

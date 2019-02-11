package notes.neo.skarlet.notes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import notes.neo.skarlet.notes.adapters.CategoryAdapter;
import notes.neo.skarlet.notes.adapters.GenreAdapter;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.entity.Genre;
import notes.neo.skarlet.notes.entity.Constants;
import notes.neo.skarlet.notes.entity.CreationType;
import notes.neo.skarlet.notes.swipe.SwipeController;
import notes.neo.skarlet.notes.swipe.SwipeControllerActions;

public class GenreActivity extends AppCompatActivity {
    private NotesDatabase db;

    private RecyclerView genresRecyclerView;
    private List<Genre> genres;
    private Integer categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryId = getIntent().getExtras().getInt(Constants.CATEGORY_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GenreActivity.this, NewGenreActivity.class);
                intent.putExtra(Constants.CATEGORY_ID, categoryId);
                intent.putExtra(Constants.CREATION_TYPE, CreationType.CREATION.name());
                startActivity(intent);
            }
        });

        db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();

        genresRecyclerView = (RecyclerView) findViewById(R.id.genres);
        genres = new ArrayList<>();
        genres.addAll(db.genreDao().getByCategoryId(categoryId));

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        genresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GenreAdapter genreAdapter = new GenreAdapter(genres);
        genresRecyclerView.setAdapter(genreAdapter);

        SwipeController swipeController = new SwipeController(getResources(), new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                Intent intent = new Intent(GenreActivity.this, NewGenreActivity.class);
                intent.putExtra(Constants.CATEGORY_ID, categoryId);
                intent.putExtra(Constants.CREATION_TYPE, CreationType.EDIT.name());
                intent.putExtra(Constants.GENRE, genres.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onRightClicked(int position) {
                db.genreDao().delete(genres.get(position));

                Intent intent = new Intent(GenreActivity.this, GenreActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.CATEGORY_ID, categoryId);
                startActivity(intent);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(genresRecyclerView);

        genresRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
}

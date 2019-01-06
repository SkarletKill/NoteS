package notes.neo.skarlet.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import notes.neo.skarlet.notes.adapters.RecordAdapter;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.RecCat;
import notes.neo.skarlet.notes.database.entity.Record;

public class RecordActivity extends AppCompatActivity {
    private Integer categoryId;
    private SwipeMenuListView recordsListView;
    private List<Record> records;
    private List<String> recordDescriptions;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryId = getIntent().getExtras().getInt("categoryId");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, NewRecordActivity.class);
                startActivity(intent);
            }
        });

        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();

        recordsListView = (SwipeMenuListView) findViewById(R.id.records);
        records = new ArrayList<>();
        records.addAll(db.recordDao().getAllInCategory(categoryId));
        recordDescriptions = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            List<RecCat> recCats = new ArrayList<>(db.recCatDao().getByRecordId(records.get(i).getId()));
            List<String> genres = new ArrayList<>();
            recCats.forEach(rc -> genres.add(db.genreDao().getById(rc.getGenreId()).getName()));
            String string = genres.spliterator().toString();
            recordDescriptions.add(string);
        }

        RecordAdapter recordAdapter = new RecordAdapter(this, records, recordDescriptions);
        recordsListView.setAdapter(recordAdapter);

        SwipeMenuCreator creator = getSwipeMenuCreator();
        // set creator
        recordsListView.setMenuCreator(creator);

        recordsListView.setOnMenuItemClickListener(getOnSwipeMenuItemClickListener());

    }

    @NonNull
    private SwipeMenuCreator getSwipeMenuCreator() {
        return menu -> {
            int buttonsWidth = 180;
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(R.color.green);
            // set item width
            openItem.setWidth(buttonsWidth);
            // set a icon
            openItem.setIcon(R.drawable.ic_edit);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(R.color.red);
            // set item width
            deleteItem.setWidth(buttonsWidth);
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        };
    }

    @NonNull
    private SwipeMenuListView.OnMenuItemClickListener getOnSwipeMenuItemClickListener() {
        return (position, menu, index) -> {
            switch (index) {
                case 0:
                    // open
                    System.out.println();
                    break;
                case 1:
                    // delete
                    System.out.println();
                    break;
            }
            // false : close the menu; true : not close the menu
            return false;
        };
    }
}

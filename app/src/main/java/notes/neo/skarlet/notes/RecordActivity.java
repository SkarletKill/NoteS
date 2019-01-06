package notes.neo.skarlet.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import notes.neo.skarlet.notes.adapters.RecordAdapter;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.RecCat;
import notes.neo.skarlet.notes.database.entity.Record;
import notes.neo.skarlet.notes.entity.Constants;

public class RecordActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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

        categoryId = getIntent().getExtras().getInt(Constants.CATEGORY_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, NewRecordActivity.class);
                intent.putExtra(Constants.CATEGORY_ID, categoryId);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            String description = stringJoiner(", ", genres);
            recordDescriptions.add(description);
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
                    NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                            .allowMainThreadQueries().build();
                    db.recordDao().delete(records.get(position));

                    Intent intent = new Intent(RecordActivity.this, RecordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("categoryId", categoryId);
                    startActivity(intent);
                    break;
            }
            // false : close the menu; true : not close the menu
            return false;
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sort_name) {
            // Handle the sorting action
        } else if (id == R.id.nav_sort_stars) {

        } else if (id == R.id.nav_add_genre) {
            Intent intent = new Intent(RecordActivity.this, NewGenreActivity.class);
            intent.putExtra(Constants.CATEGORY_ID, categoryId);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private String stringJoiner(String delimiter, List<String> array) {
        if (array.isEmpty()) return "";
        StringBuilder builder = new StringBuilder();
        array.forEach(str -> builder.append(str).append(delimiter));
        String joined = builder.substring(0, builder.length() - delimiter.length());
        return joined;
    }
}

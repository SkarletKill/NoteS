package notes.neo.skarlet.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import notes.neo.skarlet.notes.adapters.RecordAdapter;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.entity.Genre;
import notes.neo.skarlet.notes.database.entity.RecCat;
import notes.neo.skarlet.notes.database.entity.Record;
import notes.neo.skarlet.notes.entity.Constants;
import notes.neo.skarlet.notes.entity.CreationType;
import notes.neo.skarlet.notes.entity.RecordSort;
import notes.neo.skarlet.notes.entity.SessionSettings;
import notes.neo.skarlet.notes.swipe.SwipeController;
import notes.neo.skarlet.notes.swipe.SwipeControllerActions;

public class RecordActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NotesDatabase db;

    private Integer categoryId;

    private RecyclerView recordsRecyclerView;
    private List<Record> records;
    private List<String> recordDescriptions;

    private Menu optionMenu;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryId = getIntent().getExtras().getInt(Constants.CATEGORY_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            Intent intent = new Intent(RecordActivity.this, NewRecordActivity.class);
            intent.putExtra(Constants.CATEGORY_ID, categoryId);
            intent.putExtra(Constants.CREATION_TYPE, CreationType.CREATION.name());
            startActivity(intent);
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();
        Category category = db.categoryDao().getById(categoryId);
        View headView = navigationView.getHeaderView(0);
        TextView headerTitle = headView.findViewById(R.id.nav_header_title);
        headerTitle.setText(category.getName());    // null???
        TextView headerSubtitle = headView.findViewById(R.id.nav_header_subtitle);
        headerSubtitle.setText(category.getDescription());

        records = new ArrayList<>();
        records.addAll(db.recordDao().getAllInCategory(categoryId));
        if (SessionSettings.recordsSort.equals(RecordSort.BY_NAME)) {
            records.sort((r1, r2) -> r1.getName().toLowerCase().compareTo(r2.getName().toLowerCase()));
        } else if (SessionSettings.recordsSort.equals(RecordSort.BY_RATING)) {
            records.sort((r1, r2) -> r1.getRating().compareTo(r2.getRating()));
        }
        if (!SessionSettings.recordsSortingOrder) {
            records = reverseList(records);
        }

        createRecordDescriptions();

        recordsRecyclerView = (RecyclerView) findViewById(R.id.records);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecordAdapter recordAdapter = new RecordAdapter(this, records, recordDescriptions);
        recordsRecyclerView.setAdapter(recordAdapter);

        SwipeController swipeController = new SwipeController(getResources(), new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                Intent intent = new Intent(RecordActivity.this, NewRecordActivity.class);
                intent.putExtra(Constants.CATEGORY_ID, categoryId);
                intent.putExtra(Constants.CREATION_TYPE, CreationType.EDIT.name());
                intent.putExtra(Constants.RECORD, records.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onRightClicked(int position) {
                db.recordDao().delete(records.get(position));

                Intent intent = new Intent(RecordActivity.this, RecordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.CATEGORY_ID, categoryId);
                startActivity(intent);
//                recordAdapter.getRecords().remove(position);
//                recordAdapter.getDescriptions().remove(position);
//                recordAdapter.notifyItemRemoved(position);
//                recordAdapter.notifyItemRangeChanged(position, recordAdapter.getItemCount());
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recordsRecyclerView);

        recordsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void createRecordDescriptions() {
        recordDescriptions = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            List<RecCat> recCats = new ArrayList<>(db.recCatDao().getByRecordId(records.get(i).getId()));
            List<String> genres = new ArrayList<>();
            recCats.forEach(rc -> genres.add(db.genreDao().getById(rc.getGenreId()).getName()));
            String description = stringJoiner(", ", genres);
            recordDescriptions.add(description);
        }
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
        optionMenu = menu;
        SubMenu subMenu = menu.getItem(0).getSubMenu();// get my MenuItem with placeholder submenu
//        subMenu.clear(); // delete place holder

        List<Genre> genres = db.genreDao().getByCategoryId(categoryId);
        for (Genre genre : genres) {
            subMenu.add(R.id.record_filter_group, genre.getId(), 100, genre.getName());
        }

        subMenu.setGroupCheckable(R.id.record_filter_group, true, false);

        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<Genre> genres = db.genreDao().getByCategoryId(categoryId);

        Integer itemId = item.getItemId();
        for (Genre genre : genres) {
            if (itemId.equals(genre.getId())) {
                if (item.isChecked()) {
                    // If item already checked then unchecked it
                    item.setChecked(false);

                    records = db.recordDao().getAllInCategory(categoryId);
                } else {
                    // If item is unchecked then checked it
                    item.setChecked(true);
                }

                SubMenu menu = optionMenu.getItem(0).getSubMenu();

                // select all selected genres
                List<Integer> selectedGenreIds = new ArrayList<>();
                for (int i = 0; i < genres.size(); i++) {
                    if (menu.getItem(i).isChecked()) {
                        selectedGenreIds.add(genres.get(i).getId());
                    }
                }

                // create new array for filtered records
                List<Record> recordsNew = new ArrayList<>();
                nextRecord:
                for (Record record : records) {
                    List<Integer> recordGenreIds = db.recCatDao().getByRecordId(record.getId())
                            .stream().map(RecCat::getGenreId).collect(Collectors.toCollection(ArrayList::new));
                    for (Integer genreId : selectedGenreIds) {
                        if (!recordGenreIds.contains(genreId)) {
                            continue nextRecord;
                        }
                    }
                    recordsNew.add(record);
                }
                records = recordsNew;
                createRecordDescriptions();
                RecordAdapter recordAdapter = new RecordAdapter(this, records, recordDescriptions);
                recordsRecyclerView.setAdapter(recordAdapter);

                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sort_name) {
            if (SessionSettings.recordsSort.equals(RecordSort.BY_NAME)) {
                SessionSettings.recordsSortingOrder = !SessionSettings.recordsSortingOrder;

                records = reverseList(records);
                recordDescriptions = reverseList(recordDescriptions);
            } else {
                SessionSettings.recordsSort = RecordSort.BY_NAME;
                SessionSettings.recordsSortingOrder = true;

                records.sort((r1, r2) -> r1.getName().toLowerCase().compareTo(r2.getName().toLowerCase()));
                createRecordDescriptions();

                if (!SessionSettings.recordsSortingOrder) {
                    records = reverseList(records);
                    recordDescriptions = reverseList(recordDescriptions);
                }
            }

            RecordAdapter recordAdapter = new RecordAdapter(this, records, recordDescriptions);
            recordsRecyclerView.setAdapter(recordAdapter);

        } else if (id == R.id.nav_sort_stars) {
            if (SessionSettings.recordsSort.equals(RecordSort.BY_RATING)) {
                SessionSettings.recordsSortingOrder = !SessionSettings.recordsSortingOrder;

                records = reverseList(records);
                recordDescriptions = reverseList(recordDescriptions);
            } else {
                SessionSettings.recordsSort = RecordSort.BY_RATING;
                SessionSettings.recordsSortingOrder = false;

                records.sort((r1, r2) -> r1.getRating().compareTo(r2.getRating()));
                createRecordDescriptions();

                if (!SessionSettings.recordsSortingOrder) {
                    records = reverseList(records);
                    recordDescriptions = reverseList(recordDescriptions);
                }
            }
            RecordAdapter recordAdapter = new RecordAdapter(this, records, recordDescriptions);
            recordsRecyclerView.setAdapter(recordAdapter);

        } else if (id == R.id.nav_manage_genres) {
            Intent intent = new Intent(RecordActivity.this, GenreActivity.class);
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

    @TargetApi(Build.VERSION_CODES.N)
    public <T> List<T> reverseList(List<T> list) {
        return IntStream.range(0, list.size()).map(i -> list.size() - i - 1)
                .mapToObj(list::get).collect(Collectors.toCollection(ArrayList::new));
    }

    public void gotoRecordContents(Record record) {
        Intent intent = new Intent(RecordActivity.this, RecordContentsActivity.class);
//        intent.putExtra(Constants.CATEGORY_ID, category.getId());
        intent.putExtra(Constants.RECORD, record.getId());
        startActivity(intent);
    }
}
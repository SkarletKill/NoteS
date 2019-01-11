package notes.neo.skarlet.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import notes.neo.skarlet.notes.adapters.CategoryAdapter;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.entity.CategorySort;
import notes.neo.skarlet.notes.entity.Constants;
import notes.neo.skarlet.notes.entity.CreationType;
import notes.neo.skarlet.notes.entity.SessionSettings;
import notes.neo.skarlet.notes.swipe.SwipeController;
import notes.neo.skarlet.notes.swipe.SwipeControllerActions;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView categoriesRecyclerView;
    private List<Category> categories;

    private Menu optionMenu;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            Intent intent = new Intent(CategoryActivity.this, NewCategoryActivity.class);
            intent.putExtra(Constants.CREATION_TYPE, CreationType.CREATION.name());
            startActivity(intent);
        });

        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();

        categoriesRecyclerView = (RecyclerView) findViewById(R.id.categories);
        categories = new ArrayList<>();
        categories.addAll(db.categoryDao().getAll());

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                Intent intent = new Intent(CategoryActivity.this, NewCategoryActivity.class);
                intent.putExtra(Constants.CREATION_TYPE, CreationType.EDIT.name());
                intent.putExtra(Constants.CATEGORY, categories.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onRightClicked(int position) {
                NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                        .allowMainThreadQueries().build();
                db.categoryDao().delete(categories.get(position));

                Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.CATEGORY_ID, categories.get(position).getId());
                startActivity(intent);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(categoriesRecyclerView);

        categoriesRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category, menu);
        optionMenu = menu;

        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cat_action_sort_by_name) {
            if (SessionSettings.categoriesSort.equals(CategorySort.BY_NAME)) {
                SessionSettings.categoriesSortingOrder = !SessionSettings.categoriesSortingOrder;

                categories = reverseList(categories);
            } else {
                SessionSettings.categoriesSort = CategorySort.BY_NAME;
                SessionSettings.categoriesSortingOrder = true;

                categories.sort((c1, c2) -> c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase()));

                if (!SessionSettings.categoriesSortingOrder) {
                    categories = reverseList(categories);
                }
            }

            CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
            categoriesRecyclerView.setAdapter(categoryAdapter);
        } else if (id == R.id.cat_action_sort_by_priority) {
            if (SessionSettings.categoriesSort.equals(CategorySort.BY_PRIORITY)) {
                SessionSettings.categoriesSortingOrder = !SessionSettings.categoriesSortingOrder;

                categories = reverseList(categories);
            } else {
                SessionSettings.categoriesSort = CategorySort.BY_PRIORITY;
                SessionSettings.categoriesSortingOrder = true;

                categories.sort((c1, c2) -> c1.getPriority().compareTo(c2.getPriority()));

                if (!SessionSettings.categoriesSortingOrder) {
                    categories = reverseList(categories);
                }
            }

            CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
            categoriesRecyclerView.setAdapter(categoryAdapter);
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public <T> List<T> reverseList(List<T> list) {
        return IntStream.range(0, list.size()).map(i -> list.size() - i - 1)
                .mapToObj(list::get).collect(Collectors.toCollection(ArrayList::new));
    }

    public void gotoRecords(Category category){
        Intent intent = new Intent(CategoryActivity.this, RecordActivity.class);
        intent.putExtra(Constants.CATEGORY_ID, category.getId());
        startActivity(intent);
    }
}

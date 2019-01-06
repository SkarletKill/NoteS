package notes.neo.skarlet.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import notes.neo.skarlet.notes.adapters.CategoryAdapter;
import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.NotesDatabase;

public class CategoryActivity extends AppCompatActivity {
    private SwipeMenuListView categoriesListView;
    private List<Category> categories;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, NewCategoryActivity.class);
                startActivity(intent);
            }
        });

//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);

        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();

        categoriesListView = (SwipeMenuListView) findViewById(R.id.categories);
        categories = new ArrayList<>();
        categories.addAll(db.categoryDao().getAll());

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
        categoriesListView.setAdapter(categoryAdapter);
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CategoryActivity.this, RecordActivity.class);
                intent.putExtra("categoryId", categories.get(i).getId());
                startActivity(intent);
            }
        });

        SwipeMenuCreator creator = getSwipeMenuCreator();
        // set creator
        categoriesListView.setMenuCreator(creator);

        categoriesListView.setOnMenuItemClickListener(getOnSwipeMenuItemClickListener());
    }

    @NonNull
    private SwipeMenuCreator getSwipeMenuCreator() {
        return menu -> {
            int buttonsWidth = 220;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categoty, menu);
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
}

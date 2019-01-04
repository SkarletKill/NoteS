package notes.neo.skarlet.notes;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import notes.neo.skarlet.notes.adapters.CategoryAdapter;

public class CategoryActivity extends AppCompatActivity {
    private ListView categoriesListView;
    private List<String> categories;
    private List<String> prices;
    private List<String> descriptions;

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

        Resources res = getResources();
        categoriesListView = (ListView) findViewById(R.id.categories);
        categories = new ArrayList<>();
        categories.addAll(Arrays.asList(res.getStringArray(R.array.categories)));
        descriptions = new ArrayList<>();
        descriptions.addAll(Arrays.asList(res.getStringArray(R.array.descriptions)));
        prices = new ArrayList<>();
        prices.addAll(Arrays.asList(res.getStringArray(R.array.prices)));

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories, descriptions, prices);
        categoriesListView.setAdapter(categoryAdapter);
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CategoryActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
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

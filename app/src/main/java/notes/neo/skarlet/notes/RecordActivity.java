package notes.neo.skarlet.notes;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import notes.neo.skarlet.notes.adapters.RecordAdapter;

public class RecordActivity extends AppCompatActivity {
    private ListView recordsListView;
    private List<String> records;
    private List<String> descriptions;
    private List<String> marks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, NewRecordActivity.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Resources res = getResources();
        recordsListView = (ListView) findViewById(R.id.records);
        records = new ArrayList<>();
        records.addAll(Arrays.asList(res.getStringArray(R.array.categories)));
        descriptions = new ArrayList<>();
        descriptions.addAll(Arrays.asList(res.getStringArray(R.array.descriptions)));
        marks = new ArrayList<>();
        marks.addAll(Arrays.asList(res.getStringArray(R.array.prices)));

        RecordAdapter recordAdapter = new RecordAdapter(this, records, descriptions, marks);
        recordsListView.setAdapter(recordAdapter);
    }

}

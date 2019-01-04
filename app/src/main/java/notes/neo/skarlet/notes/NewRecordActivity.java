package notes.neo.skarlet.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import notes.neo.skarlet.notes.adapters.GenreItemAdapter;
import notes.neo.skarlet.notes.entity.GenreItem;

public class NewRecordActivity extends AppCompatActivity {
    private EditText name;
    private Spinner spinnerGenres;
    private RatingBar ratingBar;
    private TextView ratingMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new);

        name = (EditText) findViewById(R.id.newRecordName);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingMark = (TextView) findViewById(R.id.ratingMark);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingMark.setText(String.valueOf((int) (v * 2)));
            }
        });

        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        final String[] select_genres = {
                "Select Genres", "Action", "RPG", "Strategy", "Slasher",
                "Runner", "MMO"};

        List<GenreItem> genreItems = new ArrayList<>();
        for (String qualification : select_genres) {
            genreItems.add(new GenreItem(qualification, false));
        }
        GenreItemAdapter myAdapter = new GenreItemAdapter(NewRecordActivity.this, 0,
                genreItems);
        spinnerGenres.setAdapter(myAdapter);
    }

    public void onCancelClick(View view) {
        onBackPressed();
    }

    public void onConfirmClick(View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }
}

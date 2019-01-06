package notes.neo.skarlet.notes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.NotesDatabase;

public class NewCategoryActivity extends AppCompatActivity {
    private EditText name;
    private EditText description;
    private EditText priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_new);

        name = (EditText) findViewById(R.id.newCategoryName);
        description = (EditText) findViewById(R.id.newCategoryDescription);
        priority = (EditText) findViewById(R.id.newCategoryPriority);
    }

    public void onCancelClick(View view) {
        onBackPressed();
    }

    public void onConfirmClick(View view) {
        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, "notes_database")
                .allowMainThreadQueries().build();
        String name = String.valueOf(this.name.getText());
        String description = String.valueOf(this.description.getText());
        String priority = String.valueOf(this.priority.getText());
        Category category = new Category(name, description, priority);

        db.categoryDao().insert(category);

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

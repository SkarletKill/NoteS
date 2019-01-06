package notes.neo.skarlet.notes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import notes.neo.skarlet.notes.database.constants.DBTables;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.NotesDatabase;
import notes.neo.skarlet.notes.database.entity.Record;
import notes.neo.skarlet.notes.entity.Constants;
import notes.neo.skarlet.notes.entity.CreationType;

public class NewCategoryActivity extends AppCompatActivity {
    private Bundle extras;
    private CreationType creationType;

    private TextView title;
    private EditText name;
    private EditText description;
    private EditText priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_new);

        extras = getIntent().getExtras();
        creationType = CreationType.get(extras.getString(Constants.CREATION_TYPE));

        title = (TextView) findViewById(R.id.newCategory);
        if (creationType.equals(CreationType.CREATION))
            title.setText(Constants.ADD_CATEGORY);
        else if (creationType.equals(CreationType.EDIT))
            title.setText(Constants.EDIT_CATEGORY);

        name = (EditText) findViewById(R.id.newCategoryName);
        description = (EditText) findViewById(R.id.newCategoryDescription);
        priority = (EditText) findViewById(R.id.newCategoryPriority);

        if (creationType.equals(CreationType.EDIT)) {
            NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                    .allowMainThreadQueries().build();

            Category editableCategory = db.categoryDao().getById(extras.getInt(Constants.CATEGORY));
            name.setText(editableCategory.getName());
            description.setText(editableCategory.getDescription());
            priority.setText(String.valueOf(editableCategory.getPriority()));
        }
    }

    public void onCancelClick(View view) {
        onBackPressed();
    }

    public void onConfirmClick(View view) {
        NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DBTables.DB_NAME)
                .allowMainThreadQueries().build();
        String name = String.valueOf(this.name.getText());
        String description = String.valueOf(this.description.getText());
        Integer priority = Integer.parseInt(String.valueOf(this.priority.getText()));
        Category category = new Category(name, description, priority);

        if (creationType.equals(CreationType.CREATION))
            db.categoryDao().insert(category);
        else if (creationType.equals(CreationType.EDIT)) {
            Category categoryWithId = db.categoryDao().getById(extras.getInt(Constants.CATEGORY));
            categoryWithId.setName(name);
            categoryWithId.setDescription(description);
            categoryWithId.setPriority(priority);
            db.categoryDao().update(categoryWithId);
        }

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

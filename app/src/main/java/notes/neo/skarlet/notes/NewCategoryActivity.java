package notes.neo.skarlet.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NewCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_new);
    }

    public void onCancelClick(View view) {
        onBackPressed();
    }

    public void onConfirmClick(View view) {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }
}

package notes.neo.skarlet.notes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import notes.neo.skarlet.notes.R;
import notes.neo.skarlet.notes.database.entity.Category;

public class CategoryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Category> categories;

    public CategoryAdapter(Context c, List<Category> categories) {
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate(R.layout.categories_listview_datail, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.categoryName);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.categoryDescriprion);
        TextView priceTextView = (TextView) v.findViewById(R.id.categoryPrice);

        String name = categories.get(i).getName();
        String description = categories.get(i).getDescription();
        String priority = categories.get(i).getPriority();

        nameTextView.setText(name);
        descriptionTextView.setText(description);
        priceTextView.setText(priority);

        return v;
    }
}

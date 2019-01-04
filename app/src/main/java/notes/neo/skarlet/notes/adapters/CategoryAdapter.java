package notes.neo.skarlet.notes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import notes.neo.skarlet.notes.R;

public class CategoryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> categories;
    private List<String> prices;
    private List<String> descriptions;

    public CategoryAdapter(Context c, List<String> categories, List<String> descriptions, List<String> prices) {
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categories = categories;
        this.descriptions = descriptions;
        this.prices = prices;
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

        String name = categories.get(i);
        String desc = descriptions.get(i);
        String cost = prices.get(i);

        nameTextView.setText(name);
        descriptionTextView.setText(desc);
        priceTextView.setText(cost);

        return v;
    }
}

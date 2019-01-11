package notes.neo.skarlet.notes.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import notes.neo.skarlet.notes.CategoryActivity;
import notes.neo.skarlet.notes.R;
import notes.neo.skarlet.notes.database.entity.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    CategoryActivity activity;
    private List<Category> categories;

    public CategoryAdapter(CategoryActivity activity, List<Category> categories) {
        this.activity = activity;
        this.categories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_listview_datail, parent, false);
        ViewHolder viewHolder = new ViewHolder(activity, v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(categories.get(position));

        Category category = categories.get(position);

        holder.nameTextView.setText(category.getName());
        holder.descriptionTextView.setText(category.getDescription());
        holder.priceTextView.setText(String.valueOf(category.getPriority()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        AppCompatActivity activity;

        public ViewHolder(CategoryActivity activity, View itemView) {
            super(itemView);
            this.activity = activity;

            nameTextView = (TextView) itemView.findViewById(R.id.categoryName);
            descriptionTextView = (TextView) itemView.findViewById(R.id.categoryDescription);
            priceTextView = (TextView) itemView.findViewById(R.id.categoryPriority);

            itemView.setOnClickListener(view -> {
                Category category = (Category) view.getTag();
                activity.gotoRecords(category);
            });
        }
    }
}

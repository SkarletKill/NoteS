package notes.neo.skarlet.notes.adapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import notes.neo.skarlet.notes.CategoryActivity;
import notes.neo.skarlet.notes.R;
import notes.neo.skarlet.notes.RecordActivity;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.entity.Record;
import notes.neo.skarlet.notes.entity.Constants;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    RecordActivity activity;
    private List<Record> records;
    private List<String> descriptions;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecordAdapter(RecordActivity activity, List<Record> records, List<String> descriptions) {
        this.activity = activity;
        this.records = records;
        this.descriptions = descriptions;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.records_listview_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(activity, v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(records.get(position));

        Record record = records.get(position);
        String description = descriptions.get(position);

        holder.mRecordName.setText(record.getName());
        holder.mRecordDesctoption.setText(description);
        holder.mRecordMark.setText(String.valueOf(record.getRating()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return records.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mRecordName;
        TextView mRecordDesctoption;
        TextView mRecordMark;
        AppCompatActivity activity;

        public ViewHolder(RecordActivity activity, View itemView) {
            super(itemView);
            this.activity = activity;

            mRecordName = itemView.findViewById(R.id.recordName);
            mRecordDesctoption = itemView.findViewById(R.id.recordDescription);
            mRecordMark = itemView.findViewById(R.id.recordMark);

            itemView.setOnClickListener(view -> {
                Record record = (Record) view.getTag();
                activity.gotoRecordContents(record);
            });
        }
    }

    public List<Record> getRecords() {
        return records;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }
}

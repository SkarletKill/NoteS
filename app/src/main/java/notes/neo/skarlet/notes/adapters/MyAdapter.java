package notes.neo.skarlet.notes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import notes.neo.skarlet.notes.R;
import notes.neo.skarlet.notes.database.entity.Record;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mContext;

    private List<Record> records;
    private List<String> descriptions;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, List<Record> records, List<String> descriptions) {
        this.mContext = context;
        this.records = records;
        this.descriptions = descriptions;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.records_listview_detail, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mRecordName;
        TextView mRecordDesctoption;
        TextView mRecordMark;
        private Context mContext;

        public MyViewHolder(View itemView) {
            super(itemView);

            mRecordName = itemView.findViewById(R.id.recordName);
            mRecordDesctoption = itemView.findViewById(R.id.recordDescription);
            mRecordMark = itemView.findViewById(R.id.recordMark);
            mContext = itemView.getContext();

            itemView.setOnClickListener(view -> {
                Record record = (Record) view.getTag();
                Toast.makeText(view.getContext(), record.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}

package notes.neo.skarlet.notes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import notes.neo.skarlet.notes.R;
import notes.neo.skarlet.notes.database.entity.Record;

public class RecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Record> records;
    private List<String> descriptions;

    public RecordAdapter(Context c, List<Record> records, List<String> descriptions) {
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.records = records;
        this.descriptions = descriptions;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate(R.layout.records_listview_detail, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.recordName);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.recordDescription);
        TextView markTextView = (TextView) v.findViewById(R.id.recordMark);

        String name = records.get(i).getName();
        String desc = descriptions.get(i);
        String mark = String.valueOf(records.get(i).getRating());

        nameTextView.setText(name);
        descriptionTextView.setText(desc);
        markTextView.setText(mark);

        return v;
    }
}
package notes.neo.skarlet.notes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import notes.neo.skarlet.notes.R;
import notes.neo.skarlet.notes.database.entity.Genre;

public class GenreAdapter  extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private List<Genre> genres;

    public GenreAdapter(List<Genre> genres) {
        this.genres = genres;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genres_listview_detail, parent, false);
        GenreAdapter.ViewHolder viewHolder = new GenreAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(genres.get(position));

        Genre genre = genres.get(position);
        holder.mGenreName.setText(genre.getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mGenreName;

        public ViewHolder(View itemView) {
            super(itemView);

            mGenreName = itemView.findViewById(R.id.genreName);
        }
    }

    public List<Genre> getRecords() {
        return genres;
    }
}

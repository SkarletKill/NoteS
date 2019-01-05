package notes.neo.skarlet.notes.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import notes.neo.skarlet.notes.database.dao.CategoryDao;
import notes.neo.skarlet.notes.database.dao.GenreDao;
import notes.neo.skarlet.notes.database.dao.RecCatDao;
import notes.neo.skarlet.notes.database.dao.RecordDao;
import notes.neo.skarlet.notes.database.entity.Category;
import notes.neo.skarlet.notes.database.entity.Genre;
import notes.neo.skarlet.notes.database.entity.RecCat;
import notes.neo.skarlet.notes.database.entity.Record;

@Database(entities = {Category.class, Genre.class, Record.class, RecCat.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract GenreDao genreDao();
    public abstract RecordDao recordDao();
    public abstract RecCatDao recCatDao();
}

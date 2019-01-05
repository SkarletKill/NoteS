package notes.neo.skarlet.notes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import notes.neo.skarlet.notes.database.constants.DBQuery;
import notes.neo.skarlet.notes.database.entity.Genre;

@Dao
public interface GenreDao {
    @Query(DBQuery.SELECT_ALL_GENRES)
    List<Genre> getAll();

    @Query(DBQuery.SELECT_ALL_GENRES + " WHERE id = :id")
    Genre getById(Integer id);

    @Insert
    void insert(Genre genre);

    @Delete
    void delete(Genre genre);
}

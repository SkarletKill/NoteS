package notes.neo.skarlet.notes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import notes.neo.skarlet.notes.database.constants.DBQuery;
import notes.neo.skarlet.notes.database.entity.Genre;

@Dao
public interface GenreDao {
    @Query(DBQuery.SELECT_ALL_GENRES)
    List<Genre> getAll();

    @Query(DBQuery.SELECT_ALL_GENRES + " WHERE id = :id")
    Genre getById(Integer id);

    @Query(DBQuery.SELECT_ALL_GENRES + " WHERE cat_id = :categoryId")
    List<Genre> getByCategoryId(Integer categoryId);

    @Insert
    void insert(Genre genre);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Genre genre);

    @Delete
    void delete(Genre genre);
}

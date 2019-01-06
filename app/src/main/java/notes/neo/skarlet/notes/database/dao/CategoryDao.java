package notes.neo.skarlet.notes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import notes.neo.skarlet.notes.database.constants.DBQuery;
import notes.neo.skarlet.notes.database.entity.Category;

@Dao
public interface CategoryDao {
    @Query(DBQuery.SELECT_ALL_CATEGORIES)
    List<Category> getAll();

    @Query(DBQuery.SELECT_ALL_CATEGORIES + " WHERE id = :id")
    Category getById(Integer id);

    @Insert
    void insert(Category category);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Category category);

    @Delete
    void delete(Category category);
}

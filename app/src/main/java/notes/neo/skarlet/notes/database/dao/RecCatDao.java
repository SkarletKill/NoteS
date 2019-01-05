package notes.neo.skarlet.notes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import notes.neo.skarlet.notes.database.constants.DBQuery;
import notes.neo.skarlet.notes.database.entity.RecCat;

@Dao
public interface RecCatDao {
    @Query(DBQuery.SELECT_ALL_RECCAT)
    List<RecCat> getAll();

    @Query(DBQuery.SELECT_ALL_RECCAT + " WHERE id = :id")
    RecCat getById(Integer id);

    @Query(DBQuery.SELECT_ALL_RECCAT + " WHERE rec_id = :id")
    List<RecCat> getByRecordId(Integer id);

    @Insert
    void insert(RecCat rec_cat);

    @Delete
    void delete(RecCat rec_cat);
}

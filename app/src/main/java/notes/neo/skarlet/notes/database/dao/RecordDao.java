package notes.neo.skarlet.notes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import notes.neo.skarlet.notes.database.constants.DBQuery;
import notes.neo.skarlet.notes.database.entity.Record;

@Dao
public interface RecordDao {
    @Query(DBQuery.SELECT_ALL_RECORDS + " WHERE cat_id = :categoryId")
    List<Record> getAllInCategory(Integer categoryId);

    @Query(DBQuery.SELECT_ALL_RECORDS + " WHERE id = :id")
    Record getById(Integer id);

    @Insert
    void insert(Record record);

    @Delete
    void delete(Record record);
}

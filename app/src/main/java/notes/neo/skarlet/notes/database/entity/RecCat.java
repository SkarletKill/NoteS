package notes.neo.skarlet.notes.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import notes.neo.skarlet.notes.database.constants.DBFields;
import notes.neo.skarlet.notes.database.constants.DBTables;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = DBTables.REC_CAT, foreignKeys = {
        @ForeignKey(entity = Genre.class, parentColumns = DBFields.ID, childColumns = DBFields.GENRE_ID,
                onUpdate = CASCADE, onDelete = CASCADE),
        @ForeignKey(entity = Record.class, parentColumns = DBFields.ID, childColumns = DBFields.RECORD_ID,
                onUpdate = CASCADE, onDelete = CASCADE)})
public class RecCat {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBFields.ID)
    public Integer id;

    @ColumnInfo(name = DBFields.GENRE_ID)
    private Integer genreId;

    @ColumnInfo(name = DBFields.RECORD_ID)
    private Integer recordId;

    public RecCat(Integer genreId, Integer recordId) {
        this.genreId = genreId;
        this.recordId = recordId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public Integer getRecordId() {
        return recordId;
    }
}

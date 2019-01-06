package notes.neo.skarlet.notes.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import notes.neo.skarlet.notes.database.constants.DBFields;
import notes.neo.skarlet.notes.database.constants.DBTables;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = DBTables.GENRE,
        foreignKeys = @ForeignKey(entity = Category.class, parentColumns = DBFields.ID, childColumns = DBFields.CATEGORY_ID,
                onUpdate = CASCADE, onDelete = CASCADE),
        indices = @Index(value = DBFields.CATEGORY_ID))
public class Genre {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBFields.ID)
    public int id;

    @ColumnInfo(name = DBFields.CATEGORY_ID)
    private Integer categoryId;

    @ColumnInfo(name = DBFields.NAME)
    private String name;

    public Genre(Integer categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }
}

package notes.neo.skarlet.notes.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import notes.neo.skarlet.notes.database.constants.DBFields;
import notes.neo.skarlet.notes.database.constants.DBTables;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = DBTables.RECORD,
        foreignKeys = @ForeignKey(entity = Category.class, parentColumns = DBFields.ID, childColumns = DBFields.CATEGORY_ID,
                onUpdate = CASCADE, onDelete = CASCADE),
        indices = @Index(value = DBFields.CATEGORY_ID))
public class Record {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBFields.ID)
    public Integer id;

    @ColumnInfo(name = DBFields.CATEGORY_ID)
    private Integer categoryId;

    @ColumnInfo(name = DBFields.NAME)
    private String name;

    @ColumnInfo(name = DBFields.RARING)
    private Integer rating;

    @ColumnInfo(name = DBFields.TEXT)
    private String text;

    public Record(Integer categoryId, String name, Integer rating) {
        this.categoryId = categoryId;
        this.name = name;
        this.rating = rating;
        this.text = "";
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

    public Integer getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package notes.neo.skarlet.notes.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import notes.neo.skarlet.notes.database.constants.DBFields;
import notes.neo.skarlet.notes.database.constants.DBTables;

@Entity(tableName = DBTables.CATEGORY)
public class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBFields.ID)
    public Integer id;

    @ColumnInfo(name = DBFields.NAME)
    private String name;

    @ColumnInfo(name = DBFields.DESCRIPTION)
    private String description;

    @ColumnInfo(name = DBFields.PRIORITY)
    private String priority;

    public Category(String name, String description, String priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }
}

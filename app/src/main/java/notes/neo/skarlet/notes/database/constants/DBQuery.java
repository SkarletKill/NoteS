package notes.neo.skarlet.notes.database.constants;

public interface DBQuery {
    String SELECT_ALL_CATEGORIES = DBTables.SELECT_ALL + DBTables.CATEGORY;
    String SELECT_ALL_GENRES = DBTables.SELECT_ALL + DBTables.GENRE;
    String SELECT_ALL_RECORDS = DBTables.SELECT_ALL + DBTables.RECORD;
    String SELECT_ALL_RECCAT = DBTables.SELECT_ALL + DBTables.REC_CAT;
}

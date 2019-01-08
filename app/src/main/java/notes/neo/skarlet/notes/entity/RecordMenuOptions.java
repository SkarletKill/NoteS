package notes.neo.skarlet.notes.entity;

public enum RecordMenuOptions {
    FILTER(0);

    int index;

    RecordMenuOptions(int index) {
        this.index = index;
    }

    public int get() {
        return index;
    }
}

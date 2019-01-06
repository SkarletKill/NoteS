package notes.neo.skarlet.notes.entity;

public enum CreationType {
    CREATION,
    EDIT;

    public static CreationType get(String type) {
        type = type.toUpperCase();
        if (type.equals(CREATION.name())) return CREATION;
        else if (type.equals(EDIT.name())) return EDIT;
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}

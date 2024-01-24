package me.endermenskill.voreplugin.vore;

public enum VoreType {
    ORAL(0),
    ANAL(1),
    COCK(2),
    UNBIRTH(3),
    BREAST(4),
    TAIL(5),
    OTHER(99);

    private final int modelIndex;

    VoreType(int index) {
        this.modelIndex = index;
    }

    public int getIndex() {
        return modelIndex;
    }
}

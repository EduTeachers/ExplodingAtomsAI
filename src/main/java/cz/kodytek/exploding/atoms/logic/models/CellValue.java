package cz.kodytek.exploding.atoms.logic.models;

public enum CellValue {
    Empty(0), One(1), Two(2), Three(3);

    public final int value;

    CellValue(int value) {
        this.value = value;
    }

    public CellValue toggleValue() {
        return switch (value) {
            case 0 -> One;
            case 1 -> Two;
            case 2 -> Three;
            default -> Empty;
        };
    }
}

package cz.kodytek.exploding.atoms.logic.models;

public class Cell {
    private final PlayerType owner;
    private final CellValue value;

    public Cell(PlayerType owner, CellValue value) {
        this.owner = owner;
        this.value = value;
    }

    public PlayerType getOwner() {
        return owner;
    }

    public CellValue getValue() {
        return value;
    }
}

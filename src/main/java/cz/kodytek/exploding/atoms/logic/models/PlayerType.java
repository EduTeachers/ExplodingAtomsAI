package cz.kodytek.exploding.atoms.logic.models;

public enum PlayerType {
    None, Red, Blue;

    public PlayerType togglePlayer() {
        if (this == Red) return Blue;
        else return Red;
    }
}

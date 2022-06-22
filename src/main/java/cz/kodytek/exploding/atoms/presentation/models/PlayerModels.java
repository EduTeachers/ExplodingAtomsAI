package cz.kodytek.exploding.atoms.presentation.models;

import cz.kodytek.exploding.atoms.logic.ai.IExplodingAtomsPlayer;

public class PlayerModels {
    private final IExplodingAtomsPlayer redPlayer;
    private final IExplodingAtomsPlayer bluePlayer;

    public PlayerModels(IExplodingAtomsPlayer redPlayer, IExplodingAtomsPlayer bluePlayer) {
        this.redPlayer = redPlayer;
        this.bluePlayer = bluePlayer;
    }

    public IExplodingAtomsPlayer getRedPlayer() {
        return redPlayer;
    }

    public IExplodingAtomsPlayer getBluePlayer() {
        return bluePlayer;
    }
}

package cz.kodytek.exploding.atoms.logic.ai;

import cz.kodytek.exploding.atoms.logic.IExplodingAtomsGame;
import cz.kodytek.exploding.atoms.logic.models.Coordinate;
import cz.kodytek.exploding.atoms.logic.models.PlayerType;

public class AlwaysZeroAI implements IExplodingAtomsAI {
    @Override
    public Coordinate makeMove(IExplodingAtomsGame currentGameState, PlayerType playerType) {
        return new Coordinate(0, 0);
    }

    @Override
    public String getName() {
        return "Zero AI";
    }
}

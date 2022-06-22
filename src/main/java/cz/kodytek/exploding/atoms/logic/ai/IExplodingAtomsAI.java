package cz.kodytek.exploding.atoms.logic.ai;

import cz.kodytek.exploding.atoms.logic.IExplodingAtomsGame;
import cz.kodytek.exploding.atoms.logic.models.Coordinate;
import cz.kodytek.exploding.atoms.logic.models.PlayerType;

public interface IExplodingAtomsAI extends IExplodingAtomsPlayer {

    public Coordinate makeMove(IExplodingAtomsGame currentGameState, PlayerType playerType);

}

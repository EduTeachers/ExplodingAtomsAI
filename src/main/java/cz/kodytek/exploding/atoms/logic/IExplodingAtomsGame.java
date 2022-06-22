package cz.kodytek.exploding.atoms.logic;

import cz.kodytek.exploding.atoms.logic.exceptions.IllegalMoveException;
import cz.kodytek.exploding.atoms.logic.models.Board;
import cz.kodytek.exploding.atoms.logic.models.PlayerType;

public interface IExplodingAtomsGame {

    void move(int x, int y) throws IllegalMoveException;

    Board getBoard();

    PlayerType getPlayerOnMove();

    PlayerType getWinner();

    int getRound();

    float getRatio();

    float getRatio(PlayerType playerType);

}

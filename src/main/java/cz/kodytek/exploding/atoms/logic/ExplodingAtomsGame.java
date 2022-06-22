package cz.kodytek.exploding.atoms.logic;

import cz.kodytek.exploding.atoms.logic.exceptions.IllegalMoveException;
import cz.kodytek.exploding.atoms.logic.models.*;

public class ExplodingAtomsGame implements IExplodingAtomsGame {

    private Board board;
    private PlayerType playerOnMove;

    private int round;

    public ExplodingAtomsGame() {
        board = new Board();
        playerOnMove = PlayerType.Red;
    }

    public ExplodingAtomsGame(IExplodingAtomsGame explodingAtomsGame) {
        this.board = new Board(explodingAtomsGame.getBoard());
        this.playerOnMove = explodingAtomsGame.getPlayerOnMove();
        this.round = explodingAtomsGame.getRound();
    }

    @Override
    public void move(int x, int y) throws IllegalMoveException {
        Cell cell = board.getOrNull(x, y);
        if (cell == null || (!cell.getOwner().equals(getPlayerOnMove()) && !cell.getOwner().equals(PlayerType.None)))
            throw new IllegalMoveException();

        spread(x, y);
        playerOnMove = playerOnMove.togglePlayer();

        round++;
    }

    private void spread(int x, int y) {
        Coordinate[] surroundingCells = board.getSurroundingCoordinates(x, y);
        Cell currentCell = board.getOrNull(x, y);

        CellValue newValue = currentCell.getValue().toggleValue();
        board = board.setCell(x, y, new Cell(newValue != CellValue.Empty ? getPlayerOnMove() : PlayerType.None, newValue));
        if (currentCell.getValue().value == (surroundingCells.length - 1)) {
            board = board.setCell(x, y, new Cell(PlayerType.None, CellValue.Empty));
            for (Coordinate surroundingCell : surroundingCells) {
                spread(surroundingCell.getX(), surroundingCell.getY());
            }
        }
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public PlayerType getPlayerOnMove() {
        return playerOnMove;
    }

    @Override
    public PlayerType getWinner() {
        if (getRound() > 1)
            if (getRatio() <= 0)
                return PlayerType.Blue;
            else if (getRatio() >= 1)
                return PlayerType.Red;

        return PlayerType.None;
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public float getRatio() {
        int redCount = 0;
        int blueCount = 0;
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                Cell cell = board.getOrNull(i, j);
                if (cell.getOwner() == PlayerType.Red)
                    redCount++;
                else if (cell.getOwner() == PlayerType.Blue)
                    blueCount++;
            }
        }

        return (float) redCount / (redCount + blueCount);
    }

    @Override
    public float getRatio(PlayerType playerType) {
        if (playerType == PlayerType.Red)
            return getRatio();

        int redCount = 0;
        int blueCount = 0;
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                Cell cell = board.getOrNull(i, j);
                if (cell.getOwner() == PlayerType.Red)
                    redCount++;
                else if (cell.getOwner() == PlayerType.Blue)
                    blueCount++;
            }
        }

        return (float) blueCount / (redCount + blueCount);
    }
}

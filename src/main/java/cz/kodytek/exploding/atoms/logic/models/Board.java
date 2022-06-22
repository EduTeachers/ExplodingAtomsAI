package cz.kodytek.exploding.atoms.logic.models;

import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final Cell[][] board;

    public Board() {
        board = new Cell[10][10];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell(PlayerType.None, CellValue.Empty);
            }
        }
    }

    public Board(Board board) {
        this(board.board);
    }

    private Board(Cell[][] board) {
        this.board = board;
    }

    public Cell getOrNull(int x, int y) {
        if (isInField(x, y)) return board[x][y];
        return null;
    }

    public Board setCell(int x, int y, Cell newCell) {
        if (!isInField(x, y)) throw new IndexOutOfBoundsException();

        Cell[][] copiedBoard = copyCells(board);
        copiedBoard[x][y] = newCell;

        return new Board(copiedBoard);
    }

    public boolean isInField(int x, int y) {
        return x >= 0 && x < board.length && y >= 0 && y < board[x].length;
    }

    public int getRowCount() {
        return board.length;
    }

    public int getColumnCount() {
        return board[0].length;
    }

    public Coordinate[] getSurroundingCoordinates(int x, int y) {
        if (!isInField(x, y)) throw new IndexOutOfBoundsException();

        ArrayList<Coordinate> coords = new ArrayList<>();
        for (int i = (x - 1); i <= x + 1; i++)
            if (i != x && isInField(i, y))
                coords.add(new Coordinate(i, y));
        for (int i = (y - 1); i <= (y + 1); i++)
            if (i != y && isInField(x, i))
                coords.add(new Coordinate(x, i));

        return coords.toArray(new Coordinate[0]);
    }

    private Cell[][] copyCells(Cell[][] board) {
        Cell[][] newBoard = new Cell[10][10];
        for (int i = 0; i < board.length; i++) {
            newBoard[i] = Arrays.copyOf(board[i], board.length);
        }

        return newBoard;
    }
}

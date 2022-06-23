package cz.kodytek.exploding.atoms.presentation.controllers;

import cz.kodytek.exploding.atoms.logic.ExplodingAtomsGame;
import cz.kodytek.exploding.atoms.logic.IExplodingAtomsGame;
import cz.kodytek.exploding.atoms.logic.ai.IExplodingAtomsAI;
import cz.kodytek.exploding.atoms.logic.ai.IExplodingAtomsPlayer;
import cz.kodytek.exploding.atoms.logic.models.Cell;
import cz.kodytek.exploding.atoms.logic.models.Coordinate;
import cz.kodytek.exploding.atoms.logic.models.PlayerType;
import cz.kodytek.exploding.atoms.presentation.models.PlayerModels;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicReference;

public class GameController {

    /**
     * In seconds
     */
    private static final float TIME_BETWEEN_AI_PLAYS = 1;

    private PlayerModels playerModels;
    private final IExplodingAtomsGame game = new ExplodingAtomsGame();
    private final Button[][] buttons = new Button[game.getBoard().getRowCount()][game.getBoard().getColumnCount()];

    private boolean incorrectMove = false;

    @FXML
    public GridPane gpBoard;

    @FXML
    public Label lPlayerOnMove;

    @FXML
    public void initialize() {
        for (int i = 0; i < game.getBoard().getColumnCount(); i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / game.getBoard().getColumnCount());
            gpBoard.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < game.getBoard().getRowCount(); i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / game.getBoard().getRowCount());
            gpBoard.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < game.getBoard().getRowCount(); i++) {
            for (int j = 0; j < game.getBoard().getColumnCount(); j++) {
                Button button = new Button("");
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);
                buttons[i][j] = button;

                gpBoard.add(button, i, j);

                final int finalI = i;
                final int finalJ = j;

                if (isHumanPlayer())
                    button.setOnMouseClicked(event -> {
                        if (!incorrectMove)
                            move(finalI, finalJ);
                    });
            }
        }

        if (isHumanPlayer()) {
            moveIfAI(playerModels.getRedPlayer()); // Red moves first
            if (!incorrectMove)
                render();
        }

        if (!isHumanPlayer()) {
            AtomicReference<IExplodingAtomsPlayer> onMove = new AtomicReference<>(playerModels.getRedPlayer());
            AtomicReference<Timeline> timeline = new AtomicReference<>();
            timeline.set(
                    new Timeline(
                            new KeyFrame(Duration.seconds(TIME_BETWEEN_AI_PLAYS), event -> {
                                if (!incorrectMove)
                                    moveIfAI(onMove.get());
                                if (!incorrectMove)
                                    render();
                                else
                                    timeline.get().stop();

                                if (onMove.get() == playerModels.getRedPlayer())
                                    onMove.set(playerModels.getBluePlayer());
                                else
                                    onMove.set(playerModels.getRedPlayer());
                            })
                    ));
            timeline.get().setCycleCount(Timeline.INDEFINITE);
            timeline.get().play();
        }
    }

    public void setPlayerModels(PlayerModels playerModels) {
        this.playerModels = playerModels;
    }

    private void render() {
        for (int i = 0; i < game.getBoard().getRowCount(); i++) {
            for (int j = 0; j < game.getBoard().getColumnCount(); j++) {
                Button button = buttons[i][j];
                Cell cell = game.getBoard().getOrNull(i, j);

                if (cell.getOwner() == PlayerType.Blue)
                    button.setStyle("-fx-text-fill: blue");
                else if (cell.getOwner() == PlayerType.Red)
                    button.setStyle("-fx-text-fill: red");
                else
                    button.setStyle("-fx-text-fill: black");

                button.setText(cell.getValue().value + "");
            }
        }

        lPlayerOnMove.setText(game.getPlayerOnMove() == PlayerType.Red ? "Red move" : "Blue move");

        if (game.getWinner() != PlayerType.None) {
            lPlayerOnMove.setText(game.getWinner() == PlayerType.Red ? "Red wins!" : "Blue wins!");
        }
    }

    private void move(int x, int y) {
        if (game.getWinner() == PlayerType.None) {
            game.move(x, y);
            render();
        }

        if (game.getWinner() == PlayerType.None) {
            // AI Calls
            moveIfAI(playerModels.getBluePlayer());
            if (game.getWinner() == PlayerType.None)
                moveIfAI(playerModels.getRedPlayer());

            if (!incorrectMove)
                render();
        }
    }

    private void moveIfAI(IExplodingAtomsPlayer player) {
        if (player instanceof IExplodingAtomsAI) {
            try {
                Coordinate coordinate = ((IExplodingAtomsAI) player).makeMove(new ExplodingAtomsGame(game), game.getPlayerOnMove());
                try {
                    game.move(coordinate.getX(), coordinate.getY());
                } catch (Exception e) {
                    incorrectMove = true;
                    e.printStackTrace();
                    lPlayerOnMove.setText("Illegal move by \"" + player.getName() + "\" AI! [" + coordinate.getX() + ", " + coordinate.getY() + "]");
                }
            } catch (Exception e) {
                incorrectMove = true;
                e.printStackTrace();
                lPlayerOnMove.setText(player.getName() + " crashed! - see console");
            }
        }
    }

    private boolean isHumanPlayer() {
        return !((playerModels.getBluePlayer() instanceof IExplodingAtomsAI) && (playerModels.getRedPlayer() instanceof IExplodingAtomsAI));
    }
}

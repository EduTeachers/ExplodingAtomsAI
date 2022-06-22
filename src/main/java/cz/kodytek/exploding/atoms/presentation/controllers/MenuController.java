package cz.kodytek.exploding.atoms.presentation.controllers;

import cz.kodytek.exploding.atoms.logic.ai.*;
import cz.kodytek.exploding.atoms.logic.models.PlayerType;
import cz.kodytek.exploding.atoms.presentation.components.PlayerListViewCellFactory;
import cz.kodytek.exploding.atoms.presentation.models.PlayerModels;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class MenuController {

    @FXML
    public ListView<IExplodingAtomsPlayer> lvRedPlayer;

    @FXML
    public ListView<IExplodingAtomsPlayer> lvBluePlayer;


    @FXML
    public void initialize() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        lvRedPlayer.setCellFactory(new PlayerListViewCellFactory());
        lvBluePlayer.setCellFactory(new PlayerListViewCellFactory());

        Reflections reflections = new Reflections("cz.kodytek.exploding.atoms.logic.ai");


        Set<Class<?>> playerTypes = reflections.get(SubTypes.of(IExplodingAtomsPlayer.class).asClass());
        for (Class<?> player : playerTypes) {
            if (!player.isInterface() && !Modifier.isAbstract(player.getModifiers())) {
                lvRedPlayer.getItems().add((IExplodingAtomsPlayer) player.getDeclaredConstructor().newInstance());
                lvBluePlayer.getItems().add((IExplodingAtomsPlayer) player.getDeclaredConstructor().newInstance());
            }
        }
    }


    public void onPlayClicked(ActionEvent actionEvent) throws IOException {
        IExplodingAtomsPlayer redPlayer = lvRedPlayer.getSelectionModel().getSelectedItem();
        IExplodingAtomsPlayer bluePlayer = lvBluePlayer.getSelectionModel().getSelectedItem();

        if (redPlayer != null && bluePlayer != null) {
            Stage stage = (Stage) lvRedPlayer.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/game.fxml")));

            GameController gameController = new GameController();
            gameController.setPlayerModels(new PlayerModels(redPlayer, bluePlayer));
            loader.setController(gameController);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}

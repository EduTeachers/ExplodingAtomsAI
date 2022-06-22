package cz.kodytek.exploding.atoms.presentation.components;

import cz.kodytek.exploding.atoms.logic.ai.IExplodingAtomsPlayer;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PlayerListViewCellFactory implements Callback<ListView<IExplodingAtomsPlayer>, ListCell<IExplodingAtomsPlayer>> {
    @Override
    public ListCell<IExplodingAtomsPlayer> call(ListView<IExplodingAtomsPlayer> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(IExplodingAtomsPlayer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null)
                    setText(item.getName());
            }
        };
    }
}

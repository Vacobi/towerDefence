package gui;

import core.Game;
import core.Player;
import events.GameEvent;
import events.GameListener;
import events.PlayerEvent;
import events.PlayerListener;
import projectile.Projectile;
import tower.Tower;
import tower.TowersCatalogue;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CataloguePanel extends JPanel implements PlayerListener, GameListener {
    private final Map<Tower<? extends Projectile>, JButton> buttons = new HashMap<>();
    private final JButton cancelSelectionBtn;

    private final Player player;
    private Tower<? extends Projectile> selectedTower = null;

    public CataloguePanel(TowersCatalogue catalogue, Game game) {
        setLayout(new GridLayout(0, 1, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Каталог доступных башен"));
        catalogue.getAvailableTowersWithPrices().forEach((prototype, price) -> {
            String label = catalogue.getNameOfTower(prototype) + " ($" + price + ")";
            JButton btn = new JButton(label);

            btn.addActionListener(e -> select(prototype));

            buttons.put(prototype, btn);
            add(btn);
        });

        cancelSelectionBtn = createCancelBtn();
        add(cancelSelectionBtn);

        game.addGameListener(this);

        Player player = game.getPlayer();
        player.addListener(this);
        this.player = player;

        updateButtonsStates();
    }

    private JButton createCancelBtn() {
        JButton cancelSelectionBtn = new JButton("Отменить выбор");
        cancelSelectionBtn.addActionListener(e -> cancelSelection());
        return cancelSelectionBtn;
    }

    //------------------------------------------------------------------------------------------------------------------

    private void select(Tower<? extends Projectile> proto) {
        selectedTower = proto;
        updateButtonsStates();
        firePropertyChange("selectedPrototype", null, proto);
    }

    public void cancelSelection() {
        select(null);
    }

    public void updateButtonsStates() {
        for (Map.Entry<Tower<? extends Projectile>, JButton> entry : buttons.entrySet()) {
            Tower<? extends Projectile> tower = entry.getKey();
            JButton btn = entry.getValue();
            btn.setEnabled(selectedTower != tower && player.canBuildTower(tower));
        }

        cancelSelectionBtn.setEnabled(selectedTower != null);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onPlayerLostLive(PlayerEvent event) {
        ;
    }

    @Override
    public void onChangedPlayerGoldCount(PlayerEvent event) {
        updateButtonsStates();
    }

    @Override
    public void onPlayerWin(GameEvent event) {
        ;
    }

    @Override
    public void onPlayerLose(GameEvent event) {
        ;
    }

    @Override
    public void onWaveStart(GameEvent event) {
        updateButtonsStates();
        cancelSelection();
    }

    @Override
    public void onWaveEnd(GameEvent event) {
        ;
    }

    @Override
    public void onWaveChange(GameEvent event) {
        updateButtonsStates();
    }
}

package gui;

import projectile.Projectile;
import tower.Tower;
import tower.TowersCatalogue;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CataloguePanel extends JPanel {
    private final Map<Tower<? extends Projectile>, JButton> buttons = new HashMap<>();

    public CataloguePanel(TowersCatalogue catalogue) {
        setLayout(new GridLayout(0, 1, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Tower Catalogue"));
        catalogue.getAvailableTowersWithPrices().forEach((prototype, price) -> {
            String label = prototype.getClass().getSimpleName() + " ($" + price + ")";
            JButton btn = new JButton(label);

            btn.addActionListener(e -> select(prototype));

            buttons.put(prototype, btn);
            add(btn);
        });

        JButton cancelSelectionBtn = new JButton("Cancel");
        cancelSelectionBtn.addActionListener(e -> cancelSelection());
        add(cancelSelectionBtn);
    }

    private void select(Tower<? extends Projectile> proto) {
        buttons.forEach((p, btn) -> btn.setEnabled(p != proto));
        firePropertyChange("selectedPrototype", null, proto);
    }

    public void cancelSelection() {
        select(null);
    }

    public void activeButtons(boolean enabled) {
        if (!enabled) {
            cancelSelection();
        }

        for (JButton btn : buttons.values()) {
            btn.setEnabled(enabled);
        }
    }
}

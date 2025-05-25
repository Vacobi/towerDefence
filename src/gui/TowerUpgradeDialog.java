package gui;

import core.Player;
import tower.Tower;
import tower.TowerUpgradableCharacteristic;
import tower.TowersCatalogue;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class TowerUpgradeDialog extends JDialog {
    public TowerUpgradeDialog(JFrame parent, Tower<?> tower, Player player) {
        super(parent, "Улучшение башни", true);
        setLayout(new BorderLayout());
        JPanel upgradesPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        for (TowerUpgradableCharacteristic characteristic : TowerUpgradableCharacteristic.values()) {
            Optional<Integer> levelOpt = tower.getLevelOfCharacteristic(characteristic);
            if (levelOpt.isEmpty()) continue;

            int currentLevel = levelOpt.get();
            int maxLevel = tower.getLevelsUpgradeCount();
            if (currentLevel >= maxLevel) continue;

            TowersCatalogue catalogue = player.getBuilder().getTowersCatalogue();
            Optional<Integer> costOpt = catalogue.getUpgradePrice(characteristic, currentLevel + 1);

            if (costOpt.isEmpty()) continue;

            int cost = costOpt.get();

            JButton upgradeBtn = new JButton(
                String.format("%s (уровень %d/%d) - $%d", characteristic, currentLevel, maxLevel, cost)
            );

            upgradeBtn.addActionListener(e -> {
                boolean upgraded = player.upgradeTower(tower, characteristic);
                if (!upgraded) {
                    JOptionPane.showMessageDialog(this, "Невозможно улучшить: недостаточно золота или максимальный уровень");
                } else {
                    JOptionPane.showMessageDialog(this, "Башня успешно улучшена!");
                    dispose();
                }
            });

            upgradesPanel.add(upgradeBtn);
        }

        JButton closeBtn = new JButton("Закрыть");
        closeBtn.addActionListener(e -> dispose());

        add(upgradesPanel, BorderLayout.CENTER);
        add(closeBtn, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }
}

package gui;

import core.Player;
import projectile.Projectile;
import tower.Tower;
import tower.TowerUpgradableCharacteristic;
import tower.TowersCatalogue;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class TowerUpgradeDialog extends JDialog {
    public TowerUpgradeDialog(JFrame parent, Tower<? extends Projectile> tower, Player player) {
        super(parent, "Улучшение башни", true);
        setLayout(new BorderLayout());
        JPanel upgradesPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        TowersCatalogue catalogue = player.getBuilder().getTowersCatalogue();
        for (TowerUpgradableCharacteristic characteristic : TowerUpgradableCharacteristic.values()) {
            Optional<Integer> optionalLevel = tower.getLevelOfCharacteristic(characteristic);

            int currentLevel = optionalLevel.get();
            int maxLevel = tower.getLevelsUpgradeCount();

            Optional<Integer> optionalCost = catalogue.getUpgradePrice(characteristic, currentLevel + 1);
            JButton upgradeBtn;
            if (optionalCost.isPresent()) {
                int cost = optionalCost.get();
                upgradeBtn = new JButton(
                        String.format("%s (уровень %d/%d) - $%d", characteristic, currentLevel, maxLevel, cost)
                );
            } else {
                upgradeBtn = new JButton(
                        String.format("%s (уровень %d/%d)", characteristic, currentLevel, maxLevel)
                );
            }

            if (!player.enoughGoldTUpgrade(tower, characteristic)) {
                upgradeBtn.setEnabled(false);
            }

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

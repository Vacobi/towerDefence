package tower.utils;

import tower.TowerCharacteristicsValues;

public class TowerUpgradablePart {
    private final TowerCharacteristicsValues initialCharacteristicValues;
    private final int levelsCount;

    public TowerUpgradablePart(TowerCharacteristicsValues initialCharacteristicValues, int levelsCount) {
        this.initialCharacteristicValues = initialCharacteristicValues;
        this.levelsCount = levelsCount;
    }

    public TowerCharacteristicsValues initialCharacteristicValues() {
        return initialCharacteristicValues;
    }

    public int levelsCount() {
        return levelsCount;
    }
}

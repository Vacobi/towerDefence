package economic;

import core.Wave;

public class Accountant {
    private final BankAccount account;
    private final int defaultCreditBeforeWave;
    private final int defaultCreditForMonsterKill;
    private final double defaultCreditForMonsterKillChance;

    public Accountant(BankAccount account) {
        this.account = account;
        defaultCreditBeforeWave = 20;
        defaultCreditForMonsterKill = 2;
        defaultCreditForMonsterKillChance = 0.15;
    }

    public void creditGoldBeforeWave(Wave wave) {
        int gold;
        if (wave.getNumber() == 1) {
            gold = defaultCreditBeforeWave * 5;
        } else {
            gold = defaultCreditBeforeWave * wave.getNumber();
        }
        account.creditGold(gold);
    }

    public void creditGoldForMonsterKill() {
        if (Math.random() < defaultCreditForMonsterKillChance) {
            account.creditGold(defaultCreditForMonsterKill);
        }
    }
}

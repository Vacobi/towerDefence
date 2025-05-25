package economic;

import core.Field;
import core.Wave;
import factory.WaveFactory;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class AccountantTest {

    Path path = Paths.get("test", "monster", "resources", "several_segments.txt")
            .toAbsolutePath()
            .normalize();
    private final Field field = new Field(path.toString());

    private final WaveFactory waveFactory = new WaveFactory();

    private final int waveModifierOfGoldCount = 20;

    @Test
    void creditGoldBeforeFirstWave() {
        BankAccount bankAccount = new BankAccount();
        Accountant accountant = new Accountant(bankAccount);
        int waveNumber = 1;
        Wave wave = waveFactory.createWave(waveNumber, field);

        int MODIFIER_FOR_FIRST_WAVE = 5;
        int expectedGoldCount = bankAccount.getGold() + waveModifierOfGoldCount * MODIFIER_FOR_FIRST_WAVE;

        accountant.creditGoldBeforeWave(wave);

        assertEquals(expectedGoldCount, bankAccount.getGold());
    }

    @Test
    void creditGoldAfterMonsterDeath() {
        int initialGoldCount = 50;
        BankAccount bankAccount = new BankAccount(initialGoldCount);
        Accountant accountant = new Accountant(bankAccount);

        for(int i = 0; i < 1000; i++) {
            accountant.creditGoldForMonsterKill();
        }

        assertTrue(bankAccount.getGold() > initialGoldCount);
    }


    @Test
    void creditGoldBeforeNotFirstWave() {
        BankAccount bankAccount = new BankAccount();
        Accountant accountant = new Accountant(bankAccount);
        int waveNumber = 4;
        Wave wave = waveFactory.createWave(waveNumber, field);

        int expectedGoldCount = bankAccount.getGold() + waveModifierOfGoldCount * waveNumber;

        accountant.creditGoldBeforeWave(wave);

        assertEquals(expectedGoldCount, bankAccount.getGold());
    }
}
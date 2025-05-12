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

    private final int waveModifierOfGoldCount = 50;

    @Test
    void creditGoldBeforeFirstWave() {
        int initialGoldCount = 50;
        BankAccount bankAccount = new BankAccount(initialGoldCount);
        Accountant accountant = new Accountant(bankAccount);
        int waveNumber = 1;
        Wave wave = waveFactory.createWave(waveNumber, field);

        int expectedGoldCount = bankAccount.getGold() + waveModifierOfGoldCount * waveNumber;

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
}
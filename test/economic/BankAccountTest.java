package economic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {
    @Test
    void writeOffGold() {
        BankAccount bankAccount = new BankAccount(100);

        bankAccount.writeOffGold(50);

        int expectedGold = 50;
        assertEquals(expectedGold, bankAccount.getGold());
    }

    @Test
    void writeOffGoldMoreThanHave() {
        BankAccount bankAccount = new BankAccount(0);

        assertThrows(IllegalArgumentException.class, () -> bankAccount.writeOffGold(50));
    }

    @Test
    void writeOffNegativeGold() {
        BankAccount bankAccount = new BankAccount(0);

        assertThrows(IllegalArgumentException.class, () -> bankAccount.writeOffGold(-50));
    }

    @Test
    void creditGold() {
        BankAccount bankAccount = new BankAccount();

        bankAccount.creditGold(50);

        int expectedGold = 50;
        assertEquals(expectedGold, bankAccount.getGold());
    }

    @Test
    void creditNegativeGold() {
        BankAccount bankAccount = new BankAccount(0);

        assertThrows(IllegalArgumentException.class, () -> bankAccount.writeOffGold(-50));
    }

    @Test
    void creatingNegativeGoldCountBankAccount() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(-50));
    }
}

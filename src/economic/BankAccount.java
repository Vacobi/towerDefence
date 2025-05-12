package economic;

public class BankAccount {
    public int gold;

    public BankAccount() {
        this.gold = 0;
    }

    public BankAccount(int gold) {
        if (gold < 0) {
            throw new IllegalArgumentException("Gold count cannot be negative");
        }

        this.gold = gold;
    }

    public void creditGold(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Credit count cannot be negative");
        }

        this.gold += count;
    }

    public void writeOffGold(int count) {
        if (gold < count) {
            throw new IllegalArgumentException("Not enough gold");
        }

        if (count < 0) {
            throw new IllegalArgumentException("Credit count cannot be negative");
        }

        this.gold -= count;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return this.gold;
    }
}

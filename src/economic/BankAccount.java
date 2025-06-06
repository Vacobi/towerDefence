package economic;

import events.BankAccountEvent;
import events.BankAccountListener;

import java.util.LinkedList;
import java.util.List;

public class BankAccount {
    private int gold;

    private final List<BankAccountListener> listeners;

    public BankAccount() {
        this(0);
    }

    public BankAccount(int gold) {
        if (gold < 0) {
            throw new IllegalArgumentException("Gold count cannot be negative");
        }

        this.gold = gold;

        listeners = new LinkedList<>();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void creditGold(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Credit count cannot be negative");
        }

        this.gold += count;

        fireGoldCountChanged();
    }

    public void writeOffGold(int count) {
        if (gold < count) {
            throw new IllegalArgumentException("Not enough gold");
        }

        if (count < 0) {
            throw new IllegalArgumentException("Credit count cannot be negative");
        }

        this.gold -= count;

        fireGoldCountChanged();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void setGold(int gold) {
        this.gold = gold;

        fireGoldCountChanged();
    }

    public int getGold() {
        return this.gold;
    }

    public void addListener(BankAccountListener listener) {
        listeners.add(listener);
    }

    public void removeListener(BankAccountListener listener) {
        listeners.remove(listener);
    }

    //------------------------------------------------------------------------------------------------------------------

    private void fireGoldCountChanged() {
        BankAccountEvent event = new BankAccountEvent(this);
        event.setGold(gold);

        for (BankAccountListener listener : listeners) {
            listener.onGoldCountChange(event);
        }
    }
}

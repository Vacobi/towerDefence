package events;

import java.util.EventObject;

public class BankAccountEvent extends EventObject {

    int gold;

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return gold;
    }

    public BankAccountEvent(Object source) {
        super(source);
    }
}

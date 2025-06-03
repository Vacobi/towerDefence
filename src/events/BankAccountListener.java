package events;

public interface BankAccountListener {
    void onGoldCountChange(BankAccountEvent event);
}

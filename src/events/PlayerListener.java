package events;

public interface PlayerListener {
    void onPlayerLostLive(PlayerEvent event);

    void onChangedPlayerGoldCount(PlayerEvent event);
}

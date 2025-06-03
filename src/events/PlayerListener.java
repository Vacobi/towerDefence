package events;

import core.Player;

public interface PlayerListener {
    void onPlayerLostLive(PlayerEvent event);

    void onChangedPlayerGoldCount(PlayerEvent event);
}

package events;

import core.Player;

public interface PlayerListener {
    void onPlayerLostLive(Player player);

    void onChangedPlayerGoldCount(Player player);
}

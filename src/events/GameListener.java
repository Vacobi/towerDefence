package events;

import core.Player;

public interface GameListener {
    void onPlayerWin(Player player);

    void onPlayerLose(Player player);
}

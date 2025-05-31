package events;

import core.Player;
import core.Wave;

public interface GameListener {
    void onPlayerWin(Player player);

    void onPlayerLose(Player player);

    void onWaveStart(Wave wave);

    void onWaveEnd(Wave wave);

    void onWaveChange(Wave wave);
}

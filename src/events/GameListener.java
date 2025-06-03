package events;

public interface GameListener {
    void onPlayerWin(GameEvent event);

    void onPlayerLose(GameEvent event);

    void onWaveStart(GameEvent event);

    void onWaveEnd(GameEvent event);

    void onWaveChange(GameEvent event);
}

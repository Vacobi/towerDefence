package events;

public interface WaveListener {
    void onMonsterDeath(WaveEvent event);

    void onMonsterReachedEnd(WaveEvent event);

    void onWaveEnd(WaveEvent event);
}

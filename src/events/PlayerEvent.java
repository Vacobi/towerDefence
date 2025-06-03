package events;

import core.Player;

import java.util.EventObject;

public class PlayerEvent extends EventObject {

    Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerEvent(Object source) {
        super(source);
    }
}

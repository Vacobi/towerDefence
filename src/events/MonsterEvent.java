package events;

import monster.Monster;

import java.util.EventObject;

public class MonsterEvent extends EventObject {

    Monster monster;

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public Monster getMonster() {
        return monster;
    }

    public MonsterEvent(Object source) {
        super(source);
    }
}

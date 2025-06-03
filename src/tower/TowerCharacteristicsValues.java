package tower;

public class TowerCharacteristicsValues implements Cloneable{
    private Integer damage;
    private Integer range;
    private Long shootingDelay;

    public TowerCharacteristicsValues(int damage, int range, long delay) {
        this.damage = damage;
        this.range = range;
        this.shootingDelay = delay;
    }

    //------------------------------------------------------------------------------------------------------------------

    void setDamage(int damage) {
        this.damage = damage;
    }

    void setRange(int range) {
        this.range = range;
    }

    void setShootingDelay(Long delay) {
        this.shootingDelay = delay;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public long shootingDelay() {
        return shootingDelay;
    }

    @Override
    public TowerCharacteristicsValues clone() {
        return new TowerCharacteristicsValues(damage, range, shootingDelay);
    }
}

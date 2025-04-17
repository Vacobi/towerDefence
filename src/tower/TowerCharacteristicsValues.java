package tower;


public class TowerCharacteristicsValues {
    private Double damage;
    private Double range;
    private Long shootingDelay;

    public TowerCharacteristicsValues() {}

    public TowerCharacteristicsValues(Double damage, Double range, Long delay) {
        this.damage = damage;
        this.range = range;
        this.shootingDelay = delay;
    }

    public void setDamage(Double damage) {
        this.damage = damage;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    public void setShootingDelay(Long delay) {
        this.shootingDelay = delay;
    }

    public Double getDamage() {
        return damage;
    }

    public Double getRange() {
        return range;
    }

    public Long shootingDelay() {
        return shootingDelay;
    }
}

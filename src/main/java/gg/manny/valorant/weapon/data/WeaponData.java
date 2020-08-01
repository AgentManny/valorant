package gg.manny.valorant.weapon.data;

import lombok.Getter;
import lombok.Setter;

@Getter
public class WeaponData implements Cloneable {

    /** Returns the mode of a weapon */
    private WeaponMode mode;

    private WeaponPenetration penetration;

    /** Returns the fire rate of a weapon */
    private double fireRate;

    /** Returns the min range of a weapon */
    @Setter private int minRange = 0;

    /** Returns the maximum range of a weapon */
    private int maxRange;

    private boolean burst = false;
    private double burstRate = -1;

    public WeaponData(WeaponMode mode, WeaponPenetration penetration, double fireRate, int maxRange) {
        this.mode = mode;
        this.penetration = penetration;
        this.fireRate = fireRate;
        this.maxRange = maxRange;
    }

    public WeaponData(WeaponPenetration penetration, double fireRate, int maxRange) {
        this(WeaponMode.FULL_AUTOMATIC, penetration, fireRate, maxRange);
    }

    public WeaponData(double fireRate) {
        this(WeaponMode.FULL_AUTOMATIC, WeaponPenetration.LOW, fireRate, 50);
    }

    public WeaponData burstRate(double burstRate) {
        this.burst = burstRate > 0;
        this.burstRate = burstRate;
        return this;
    }

    @Override
    public WeaponData clone() {
        return new WeaponData(mode, penetration, fireRate, maxRange)
                .burstRate(burstRate);
    }
}

package gg.manny.valorant.weapon;

import com.google.common.collect.Table;
import gg.manny.valorant.game.data.Damage;
import gg.manny.valorant.weapon.data.WeaponData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class Weapon {

    /** Returns the name of a weapon */
    private final String name;

    /** Returns the type of a weapon */
    private final WeaponType type;

    protected WeaponData primaryFire;
    protected WeaponData alternateFire;

    private double runSpeed;
    private double equipSpeed;

    private double firstShotSpread;

    private double reloadSpeed;

    /** Returns the damage data of a weapon
     * Distance, Damage Type, Damage Amount
     */
    private Table<Integer, Damage, Integer> damage;

    /** Returns the icon of a weapon */
    public abstract Material getIcon();

    public abstract int getCredits();

    /**
     * Returns the amount of ammo is inside a magazine
     */
    public abstract int getMagazineCapacity();

    public int getMagazineMultiplier() {
        return 3;
    }

    /**
     * Returns the maximum of ammo in the weapon
     */
    public int getMaxMagazine() {
        return getMagazineCapacity() * getMagazineMultiplier();
    }

    public abstract double getRunSpeed();
    public abstract double getEquipSpeed();
    public abstract double getFirstShotSpread();
    public abstract double getReloadSpeed();

    public void addDamage(int distance, Map<Damage, Integer> damageMap) {
        damageMap.forEach((damageType, damageAmount) -> this.damage.put(distance, damageType, damageAmount));
    }

}
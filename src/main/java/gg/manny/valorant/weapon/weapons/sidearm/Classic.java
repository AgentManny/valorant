package gg.manny.valorant.weapon.weapons.sidearm;

import com.google.common.collect.ImmutableMap;
import gg.manny.valorant.game.data.Damage;
import gg.manny.valorant.weapon.Weapon;
import gg.manny.valorant.weapon.WeaponType;
import gg.manny.valorant.weapon.data.WeaponData;
import org.bukkit.Material;

public class Classic extends Weapon {

    public Classic() {
        super("Classic", WeaponType.SIDEARM);

        primaryFire = new WeaponData(6.75);
        alternateFire = new WeaponData(2.22)
                .burstRate(3.0); // Pellet count

        // Add damage data for distance
        addDamage(30, ImmutableMap.of(
                Damage.HEAD, 78,
                Damage.BODY, 26,
                Damage.LEG, 22
        ));

        addDamage(50, ImmutableMap.of(
                Damage.HEAD, 66,
                Damage.BODY, 22,
                Damage.LEG, 18));
    }

    @Override
    public Material getIcon() {
        return Material.WOODEN_HOE;
    }

    @Override
    public int getCredits() {
        return 0;
    }

    @Override
    public int getMagazineCapacity() {
        return 12;
    }

    @Override
    public int getMagazineMultiplier() {
        return 3;
    }

    @Override
    public double getRunSpeed() {
        return 5.73;
    }

    @Override
    public double getEquipSpeed() {
        return 0.75;
    }

    @Override
    public double getFirstShotSpread() {
        return 0.4;
    }

    @Override
    public double getReloadSpeed() {
        return 1.75;
    }
}

package gg.manny.valorant.weapon;

import gg.manny.valorant.weapon.weapons.sidearm.Classic;

import java.util.HashMap;
import java.util.Map;

public class WeaponManager {

    private Map<String, Weapon> weapons = new HashMap<>();

    public WeaponManager() {
        addWeapon(new Classic());
    }

    public void addWeapon(Weapon weapon) {
        weapons.put(weapon.getName(), weapon);
    }

}

package gg.manny.valorant.player;

import java.util.HashMap;
import java.util.Map;

public final class PlayerMeta {

    private Map<String, Object> metaData = new HashMap<>();

    public boolean contains(String key) {
        return metaData.containsKey(key);
    }

    public Object get(String key) {
        return metaData.get(key);
    }

    public int getInt(String key) {
        return (int) metaData.get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) metaData.get(key);
    }

    public void put(String key, Object object) {
        metaData.put(key, object);
    }

    public void putIfAbsent(String key, Object object) {
        if (!contains(key)) {
            metaData.put(key, object);
        }
    }

    public void remove(String key) {
        metaData.remove(key);
    }

    public void reset() {
        metaData.clear();
    }
}

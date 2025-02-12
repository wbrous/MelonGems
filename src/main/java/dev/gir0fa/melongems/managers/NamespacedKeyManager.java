package dev.gir0fa.melongems.managers;

import dev.gir0fa.melongems.MelonGems;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class NamespacedKeyManager {

    private final HashMap<String, NamespacedKey> namespacedKeyLookupMap = new HashMap<>();

    private void addKeyOrIgnore(String name){
        if (namespacedKeyLookupMap.containsKey(name)){
            return;
        }
        namespacedKeyLookupMap.put(name, NamespacedKey.fromString(name, MelonGems.getPlugin()));
    }

    public NamespacedKey getKey(String name) {
        addKeyOrIgnore(name);
        return namespacedKeyLookupMap.get(name);
    }

}

package dev.gir0fa.melongems.managers;

import dev.gir0fa.melongems.gems.powerClasses.tasks.FireballPowerDecay;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class TempDataManager {

    // Fire players in fireball
    public HashMap<Player, FireballPowerDecay> chargingFireball = new HashMap<>(1);

    // Players not able to use gems
    public HashMap<Player, Long> cantUseGems = new HashMap<>(1);

    // Iron shift players that left
    public LinkedList<UUID> ironShiftLeft = new LinkedList<>();

    // Iron right players that left
    public LinkedList<UUID> ironRightLeft = new LinkedList<>();
}

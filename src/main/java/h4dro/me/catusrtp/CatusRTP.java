package h4dro.me.catusrtp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CatusRTP extends JavaPlugin {

    // Settings
    public static final int RADIUS = 1000;
    public static final int TIME_COOLDOWN = 10;
    public static final int RTP_COOLDOWN = 5;
    public static final boolean ON_MOVE = true;

    // Message
    public static final String MESSAGE_NO_PLAYER = "&cThis command can only be run by a player.";
    public static final String MESSAGE_WORLD_NOT_FOUND = "&cWorld not found.";
    public static final String MESSAGE_COOLDOWN = "&cWait &e{time} &cseconds to use /rtp again!";
    public static final String MESSAGE_TELEPORTING = "&aTeleporting in &e{time} &aseconds...";
    public static final String MESSAGE_TELEPORT_SUCCESS = "&#9eff00✔ Teleport successful";
    public static final String MESSAGE_TELEPORT_COORDS = "&7Coordinates: &#9eff00X: {x}, &#9eff00Y: {y}, &#9eff00Z: {z}";
    public static final String MESSAGE_TELEPORT_CANCELLED = "&cTeleportation cancelled because you moved!";
    public static final String MESSAGE_FAIL_FIND_LOCATION = "&cFailed to find a suitable location. Please try again.";

    // Sound
    public static final String SOUND_COUNTDOWN = "ENTITY_EXPERIENCE_ORB_PICKUP";
    public static final float SOUND_COUNTDOWN_VOLUME = 1.0f;
    public static final float SOUND_COUNTDOWN_PITCH = 1.0f;
    public static final String SOUND_SUCCESS = "ENTITY_PLAYER_LEVELUP";
    public static final float SOUND_SUCCESS_VOLUME = 1.0f;
    public static final float SOUND_SUCCESS_PITCH = 1.0f;
    public static final String SOUND_CANCEL = "BLOCK_ANVIL_LAND";
    public static final float SOUND_CANCEL_VOLUME = 1.0f;
    public static final float SOUND_CANCEL_PITCH = 1.0f;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        this.getCommand("rtp").setExecutor(new RTPCommand(this));

        getLogger().info("");
        getLogger().info("     ______      __             ____  __________ ");
        getLogger().info("    / ____/___ _/ /___  _______/ __ \\/_  __/ __ \\");
        getLogger().info("   / /   / __ `/ __/ / / / ___/ /_/ / / / / /_/ /");
        getLogger().info("  / /___/ /_/ / /_/ /_/ (__  ) _, _/ / / / ____/ ");
        getLogger().info("  \\____/\\__,_/\\__/\\__,_/____/_/ |_| /_/ /_/      ");
        getLogger().info("                                               ");
        getLogger().info("");
        getLogger().info("  By TiDe - Version: 1.0  ");
        getLogger().info("");

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        getLogger().info("  Plugin enabled in " + duration + " ms");
    }

    @Override
    public void onDisable() {
    }
}
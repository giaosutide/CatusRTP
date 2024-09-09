package h4dro.me.catusrtp;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RTPCommand implements CommandExecutor {
    private final CatusRTP plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final int radius;
    private final List<String> disabledWorlds;
    private final int timeCooldown;
    private final int rtpCooldown;
    private final boolean onMove;
    private final String noPlayerMsg;
    private final String worldNotFoundMsg;
    private final String cooldownMsg;
    private final String teleportingMsg;
    private final String teleportSuccessMsg;
    private final String teleportCoordsMsg;
    private final String teleportCancelledMsg;
    private final String failFindLocationMsg;
    private final Sound countdownSound;
    private final float countdownSoundVolume;
    private final float countdownSoundPitch;
    private final Sound successSound;
    private final float successSoundVolume;
    private final float successSoundPitch;
    private final Sound cancelSound;
    private final float cancelSoundVolume;
    private final float cancelSoundPitch;

    public RTPCommand(CatusRTP plugin) {
        this.plugin = plugin;

        this.radius = CatusRTP.RADIUS;
        this.disabledWorlds = Arrays.asList("world_the_end");
        this.timeCooldown = CatusRTP.TIME_COOLDOWN;
        this.rtpCooldown = CatusRTP.RTP_COOLDOWN;
        this.onMove = CatusRTP.ON_MOVE;

        this.countdownSound = Sound.valueOf(CatusRTP.SOUND_COUNTDOWN);
        this.countdownSoundVolume = CatusRTP.SOUND_COUNTDOWN_VOLUME;
        this.countdownSoundPitch = CatusRTP.SOUND_COUNTDOWN_PITCH;

        this.successSound = Sound.valueOf(CatusRTP.SOUND_SUCCESS);
        this.successSoundVolume = CatusRTP.SOUND_SUCCESS_VOLUME;
        this.successSoundPitch = CatusRTP.SOUND_SUCCESS_PITCH;

        this.cancelSound = Sound.valueOf(CatusRTP.SOUND_CANCEL);
        this.cancelSoundVolume = CatusRTP.SOUND_CANCEL_VOLUME;
        this.cancelSoundPitch = CatusRTP.SOUND_CANCEL_PITCH;

        this.noPlayerMsg = translateColorCodes(CatusRTP.MESSAGE_NO_PLAYER);
        this.worldNotFoundMsg = translateColorCodes(CatusRTP.MESSAGE_WORLD_NOT_FOUND);
        this.cooldownMsg = translateColorCodes(CatusRTP.MESSAGE_COOLDOWN);
        this.teleportingMsg = translateColorCodes(CatusRTP.MESSAGE_TELEPORTING);
        this.teleportSuccessMsg = translateColorCodes(CatusRTP.MESSAGE_TELEPORT_SUCCESS);
        this.teleportCoordsMsg = translateColorCodes(CatusRTP.MESSAGE_TELEPORT_COORDS);
        this.teleportCancelledMsg = translateColorCodes(CatusRTP.MESSAGE_TELEPORT_CANCELLED);
        this.failFindLocationMsg = translateColorCodes(CatusRTP.MESSAGE_FAIL_FIND_LOCATION);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(noPlayerMsg);
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        if (cooldowns.containsKey(playerId)) {
            long timeLeft = ((cooldowns.get(playerId) / 1000) + timeCooldown) - (System.currentTimeMillis() / 1000);
            if (timeLeft > 0) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(cooldownMsg.replace("{time}", String.valueOf(timeLeft))));
                return true;
            }
        }

        World world;

        if (args.length > 0) {
            world = Bukkit.getWorld(args[0]);
            if (world == null) {
                player.sendMessage(worldNotFoundMsg);
                return true;
            }
        } else {
            world = player.getWorld();
        }

        if (disabledWorlds.contains(world.getName())) {
            player.sendMessage(worldNotFoundMsg);
            return true;
        }

        cooldowns.put(playerId, System.currentTimeMillis());
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskAsynchronously(plugin, () -> {
            Location randomLocation = getRandomLocation(world);
            if (randomLocation != null) {
                scheduler.runTask(plugin, () -> startTeleportCountdown(player, randomLocation));
            } else {
                player.sendMessage(failFindLocationMsg);
            }
        });

        return true;
    }

    private void startTeleportCountdown(Player player, Location randomLocation) {
        new BukkitRunnable() {
            int countdown = rtpCooldown;
            Location initialLocation = player.getLocation();

            @Override
            public void run() {
                if (onMove && player.getLocation().distance(initialLocation) > 0.1) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(teleportCancelledMsg));
                    player.sendMessage(teleportCancelledMsg);
                    player.playSound(player.getLocation(), cancelSound, cancelSoundVolume, cancelSoundPitch);
                    this.cancel();
                    return;
                }

                if (countdown > 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(teleportingMsg.replace("{time}", String.valueOf(countdown))));
                    player.playSound(player.getLocation(), countdownSound, countdownSoundVolume, countdownSoundPitch);
                    countdown--;
                } else {
                    player.teleport(randomLocation);
                    player.sendMessage(teleportCoordsMsg.replace("{x}", String.valueOf(randomLocation.getBlockX()))
                            .replace("{y}", String.valueOf(randomLocation.getBlockY()))
                            .replace("{z}", String.valueOf(randomLocation.getBlockZ())));

                    player.playSound(player.getLocation(), successSound, successSoundVolume, successSoundPitch);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(teleportSuccessMsg));

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    private Location getRandomLocation(World world) {
        Random random = new Random();
        int minY = 64;
        int maxY = 118;

        for (int attempt = 0; attempt < 10; attempt++) {
            int x = random.nextInt(radius * 2) - radius;
            int z = random.nextInt(radius * 2) - radius;

            if (world.getEnvironment() == Environment.NETHER) {
                for (int y = maxY; y >= minY; y--) {
                    Location location = new Location(world, x, y, z);
                    if (isSafeNetherLocation(location)) {
                        return location;
                    }
                }
            } else {
                for (int y = minY; y < world.getMaxHeight(); y++) {
                    Location location = new Location(world, x, y, z);
                    if (isSafeLocation(location)) {
                        return location;
                    }
                }
            }
        }

        return null;
    }

    private boolean isSafeNetherLocation(Location location) {
        Material block = location.getBlock().getType();
        Material belowBlock = location.clone().add(new Vector(0, -1, 0)).getBlock().getType();

        return block == Material.AIR &&
                belowBlock.isSolid() &&
                belowBlock != Material.LAVA &&
                belowBlock != Material.WATER &&
                belowBlock != Material.BEDROCK;
    }

    private boolean isSafeLocation(Location location) {
        Material block = location.getBlock().getType();
        Material belowBlock = location.clone().add(new Vector(0, -1, 0)).getBlock().getType();

        return block == Material.AIR &&
                belowBlock.isSolid() &&
                belowBlock != Material.LAVA &&
                belowBlock != Material.WATER;
    }

    private String translateColorCodes(String message) {
        return ColorUtils.translateColors(message);
    }
}
package gg.manny.valorant.command;

import gg.manny.valorant.Locale;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.sage.SlowOrb;
import gg.manny.valorant.agent.menu.AgentSelector;
import gg.manny.valorant.game.Game;
import gg.manny.valorant.game.GameLobby;
import gg.manny.valorant.game.GameState;
import gg.manny.valorant.game.TeamType;
import gg.manny.valorant.listener.TestListener;
import gg.manny.valorant.team.GameTeam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleEffect;

@RequiredArgsConstructor
public class DebugCommand implements CommandExecutor {

    private final Valorant plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Locale.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/debug game");
            sender.sendMessage(ChatColor.RED + "/debug bossbar");
            sender.sendMessage(ChatColor.RED + "/debug lobby");
            sender.sendMessage(ChatColor.RED + "/debug winners");
            sender.sendMessage(ChatColor.RED + "/debug forcestart");
            sender.sendMessage(ChatColor.RED + "/debug agent");
            return true;
        }
        Player player = (Player) sender;
        Game game = plugin.getGame();
        if (args[0].equalsIgnoreCase("speed")) {
            if (args.length == 1) return true;

            double speed = Double.parseDouble(args[1]);
            TestListener.SPEED_REDUCER = speed;
            sender.sendMessage(ChatColor.GREEN + "Speed set to " + speed + " for projectiles.");
        } else if (args[0].equalsIgnoreCase("effect")) {
            if (args.length == 1) return true;

            ParticleEffect effect;
            try {
                effect = ParticleEffect.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Particle " + args[1] + " not found.");
                return true;
            }

            Location location = player.getLocation().add(0, 1, 0);
            sender.sendMessage(ChatColor.GOLD + "Particle " + ChatColor.WHITE + WordUtils.capitalizeFully(effect.name().toLowerCase().replace("_", " ")) + ChatColor.GOLD + " was sent to your eye location.");
            effect.display(location, 0f, 0f, 0f, 0f, 15, null);
        } else  if (args[0].equalsIgnoreCase("task")) {
            if (TestListener.ACTIVE_TASK == null || !Bukkit.getScheduler().isCurrentlyRunning(TestListener.ACTIVE_TASK.getTaskId())) {
                sender.sendMessage(ChatColor.RED + "Task not active.");
                return true;
            }
            TestListener.ACTIVE_TASK.cancel();
            sender.sendMessage("Cancelled task");
        } else  if (args[0].equalsIgnoreCase("volume")) {
            if (args.length == 1) return true;

            float volume = Float.parseFloat(args[1]);
            SlowOrb.volume = volume;
            sender.sendMessage(ChatColor.GREEN + "Volume set to " + volume + " for Slow Orb sound.");
        } else  if (args[0].equalsIgnoreCase("pitch")) {
            if (args.length == 1) return true;

            float volume = Float.parseFloat(args[1]);
            SlowOrb.pitch = volume;
            sender.sendMessage(ChatColor.GREEN + "Pitch set to " + volume + " for Slow Orb sound.");

        } else if (args[0].equalsIgnoreCase("agent")) {
            new AgentSelector().openMenu((Player) sender);
        } else if (args[0].equalsIgnoreCase("game")) {
            sender.sendMessage(ChatColor.RED + "--- Game ---");
            sender.sendMessage("Map: " + (game.getMap() == null ? "None" : game.getMap().getName()));
            sender.sendMessage("State: " + game.getState().getFriendlyName() + " [" + game.getState().name() + "]");
            sender.sendMessage("Round State: " + (game.getRoundState() == null ? "N/A" : game.getRoundState().getFriendlyName() + " [" + game.getRoundState().name() + "]"));
            sender.sendMessage("Timer: " + game.getTimer() + "s");
            sender.sendMessage("Task id: " + game.getTask().getTaskId() + " (sync: " + game.getTask().isSync() + ") (active: " + plugin.getServer().getScheduler().isCurrentlyRunning(game.getTask().getTaskId()) + ")");
            sender.sendMessage("Started at: " + (System.currentTimeMillis() - game.getStartedAt()) + "ms");
            sender.sendMessage("Starting in: " + (System.currentTimeMillis() - game.getStartingIn()) + "ms");
        } else if (args[0].equalsIgnoreCase("winners")) {
            sender.sendMessage(ChatColor.GRAY + "--- Winners ---");
            Pair<TeamType, GameTeam> winners = game.getWinners();
            sender.sendMessage("Winners: " + (winners == null ? "None" : winners.getKey().getDisplayName() + " [" + winners.getKey().name() + "]"));
            if (winners != null) {
                GameTeam value = winners.getValue();
                sender.sendMessage("Team: " + value.getAliveCount() + "/" + value.getSize());
            }
        } else if (args[0].equalsIgnoreCase("bossbar")) {
            sender.sendMessage(ChatColor.GRAY + "--- Bossbar (timer) info ---");
            BossBar timerBar = game.getTimerBar();
            sender.sendMessage("Title: " + timerBar.getTitle());
            sender.sendMessage("Color: " + timerBar.getColor().name());
            sender.sendMessage("Style: " + timerBar.getStyle().name());
            sender.sendMessage("Loaded players: " + timerBar.getPlayers().size());
            timerBar.getPlayers().forEach(online -> sender.sendMessage(" - " + online.getName()));
            sender.sendMessage("Progress: " + timerBar.getProgress());
        } else if (args[0].equalsIgnoreCase("lobby")) {
            sender.sendMessage(ChatColor.YELLOW + "--- Lobby ---");
            sender.sendMessage("Required players: " + GameLobby.REQUIRED_PLAYERS + " (reached: " + (GameLobby.REQUIRED_PLAYERS <= Bukkit.getOnlinePlayers().size()));
        } else if (args[0].equalsIgnoreCase("forcestart")) {
            sender.sendMessage("Forcing...");
            if (game.getState() != GameState.STARTED) {
                game.start();
            } else {
                sender.sendMessage("Already started.");
            }

        }
        return true;
    }
}

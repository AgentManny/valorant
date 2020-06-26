package gg.manny.valorant.util.scoreboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.util.packet.ScoreboardTeamPacketMod;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_15_R1.ScoreboardServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Field;
import java.util.*;

final class PlayerScoreboard {

    private Player player;
    private Objective objective;

    private Map<String, Integer> displayedScores = new HashMap<>();
    private Map<String, String> scorePrefixes = new HashMap<>();
    private Map<String, String> scoreSuffixes = new HashMap<>();
    private Set<String> sentTeamCreates = new HashSet<>();

    private final StringBuilder separateScoreBuilder = new StringBuilder();
    private final List<String> separateScores = new ArrayList<>();
    private final Set<String> recentlyUpdatedScores = new HashSet<>();
    private final Set<String> usedBaseScores = new HashSet<>();
    private final String[] prefixScoreSuffix = new String[3];
    private final ThreadLocal<LinkedList<String>> localList = ThreadLocal.withInitial(LinkedList::new);

    public PlayerScoreboard(Player player) {
        this.player = player;

        Scoreboard board = Valorant.getInstance().getServer().getScoreboardManager().getNewScoreboard();

        this.objective = board.registerNewObjective("Construct", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (MScoreboardHandler.isShowHealthBelowPlayer()) {
            Objective health = board.registerNewObjective("health", "health");
            health.setDisplayName(ChatColor.DARK_RED + "\u2764");
            health.getScore(player).setScore((int) player.getHealth());

            for(Player online : Bukkit.getOnlinePlayers()) {
                if(online.isDead()) continue;

                health.getScore(online).setScore((int) online.getHealth());

                Scoreboard otherBoard = online.getScoreboard();
                if (otherBoard != null && otherBoard.getObjective("health") != null) {
                    Objective otherHealth = otherBoard.getObjective("health");
                    if (!player.isDead()) {
                        otherHealth.getScore(player).setScore((int) player.getHealth());
                    }
                }
            }


            health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }

        player.setScoreboard(board);
    }

    public void update() {
        String untranslatedTitle = MScoreboardHandler.getAdapter().getTitle(this.player);
        String title = ChatColor.translateAlternateColorCodes('&', untranslatedTitle);

        List<String> lines = this.localList.get();
        if (!lines.isEmpty()) {
            lines.clear();
        }

        MScoreboardHandler.getAdapter().getLines(this.localList.get(), this.player);
        this.recentlyUpdatedScores.clear();
        this.usedBaseScores.clear();
        int nextValue = lines.size();
        Preconditions.checkArgument((lines.size() < 16), "Too many lines passed!");
        Preconditions.checkArgument((title.length() < 32), "Title is too long!");
        if (!this.objective.getDisplayName().equals(title)) {
            this.objective.setDisplayName(title);
        }

        for (String line : lines) {

            // Do all validation before we apply any logic.
            if (48 <= line.length()) {
                throw new IllegalArgumentException("Line is too long! Offending line: " + line);
            }
            String[] separated = this.separate(line, this.usedBaseScores);
            String prefix = separated[0];
            String score = separated[1];
            String suffix = separated[2];
            this.recentlyUpdatedScores.add(score);
            if (!this.sentTeamCreates.contains(score)) {
                this.createAndAddMember(score);
            }
            if (!this.displayedScores.containsKey(score) || this.displayedScores.get(score) != nextValue) {
                this.setScore(score, nextValue);
            }
            if (!(this.scorePrefixes.containsKey(score) && this.scorePrefixes.get(score).equals(prefix) && this.scoreSuffixes.get(score).equals(suffix))) {
                this.updateScore(score, prefix, suffix);
            }
            --nextValue;
        }
        for (String displayedScore : ImmutableSet.copyOf(this.displayedScores.keySet())) {
            if (this.recentlyUpdatedScores.contains(displayedScore)) continue;
            this.removeScore(displayedScore);
        }
    }

    private void setField(Packet packet, String field, Object value) {
        try {
            Field fieldObject = packet.getClass().getDeclaredField(field);
            fieldObject.setAccessible(true);
            fieldObject.set(packet, value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This is here so that the score joins itself, this way
    // #updateScore will work as it should (that works on a 'player'), which technically we are adding to ourselves
    private void createAndAddMember(String scoreTitle) {
        ScoreboardTeamPacketMod scoreboardTeamAdd = new ScoreboardTeamPacketMod(scoreTitle, "_", "_", ImmutableList.of(), 0);
        ScoreboardTeamPacketMod scoreboardTeamAddMember = new ScoreboardTeamPacketMod(scoreTitle, ImmutableList.of((Object)scoreTitle), 3);
        scoreboardTeamAdd.sendToPlayer(this.player);
        scoreboardTeamAddMember.sendToPlayer(this.player);
        this.sentTeamCreates.add(scoreTitle);
    }

    private void setScore(String score, int value) {
        PacketPlayOutScoreboardScore scoreboardScorePacket = new PacketPlayOutScoreboardScore();
        this.setField(scoreboardScorePacket, "a", score);
        this.setField(scoreboardScorePacket, "b", this.objective.getName());
        this.setField(scoreboardScorePacket, "c", value);
        this.setField(scoreboardScorePacket, "d", ScoreboardServer.Action.CHANGE);
        this.displayedScores.put(score, value);
        ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket(scoreboardScorePacket);
    }

    private void removeScore(String score) {
        this.displayedScores.remove(score);
        this.scorePrefixes.remove(score);
        this.scoreSuffixes.remove(score);
        ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, this.objective.getName(), score, 0));
    }

    private void updateScore(String score, String prefix, String suffix) {
        this.scorePrefixes.put(score, prefix);
        this.scoreSuffixes.put(score, suffix);
        (new ScoreboardTeamPacketMod(score, prefix, suffix, null, 2)).sendToPlayer(this.player);
    }

    // Here be dragons.
    // Good luck maintaining this code.
    private String[] separate(String line, Collection<String> usedBaseScores) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        String prefix = "";
        String score = "";
        String suffix = "";
        this.separateScores.clear();
        this.separateScoreBuilder.setLength(0);
        for (int i = 0; i < line.length(); ++i) {
            int c = line.charAt(i);
            if (c == 42 || this.separateScoreBuilder.length() == 16 && this.separateScores.size() < 3) {
                this.separateScores.add(this.separateScoreBuilder.toString());
                this.separateScoreBuilder.setLength(0);
                if (c == 42) continue;
            }
            this.separateScoreBuilder.append((char)c);
        }
        this.separateScores.add(this.separateScoreBuilder.toString());
        switch (this.separateScores.size()) {
            case 1: {
                score = this.separateScores.get(0);
                break;
            }
            case 2: {
                score = this.separateScores.get(0);
                suffix = this.separateScores.get(1);
                break;
            }
            case 3: {
                prefix = this.separateScores.get(0);
                score = this.separateScores.get(1);
                suffix = this.separateScores.get(2);
                break;
            }
            default: {
                Valorant.getInstance().getLogger().warning("Failed to separate scoreboard line. Input: " + line);
            }
        }
        if (usedBaseScores.contains(score)) {
            if (score.length() <= 14) {
                for (ChatColor chatColor : ChatColor.values()) {
                    String possibleScore = chatColor + score;
                    if (usedBaseScores.contains(possibleScore)) continue;
                    score = possibleScore;
                    break;
                }
                if (usedBaseScores.contains(score)) {
                    Valorant.getInstance().getLogger().warning("Failed to find alternate color code for: " + score);
                }
            } else {
                Valorant.getInstance().getLogger().warning("Found a scoreboard base collision to shift: " + score);
            }
        }
        if (prefix.length() > 16) {
            prefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }
        if (score.length() > 16) {
            score = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }
        if (suffix.length() > 16) {
            suffix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }
        usedBaseScores.add(score);
        this.prefixScoreSuffix[0] = prefix;
        this.prefixScoreSuffix[1] = score;
        this.prefixScoreSuffix[2] = suffix;
        return (this.prefixScoreSuffix);
    }
}


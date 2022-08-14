package net.maed.cinematictool.util;

import net.maed.cinematictool.CinematicTool;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;

public class ModTeam {
    public static Team getTeam(MinecraftServer server) {
        Scoreboard scoreboard = server.getScoreboard();
        Team team = scoreboard.getTeam(CinematicTool.MOD_ID);
        if (team == null) {
            team = scoreboard.addTeam(CinematicTool.MOD_ID);
            loadSettings(team);
            return team;
        }
        return team;
    }
    public static void loadSettings(Team team) {
        team.setCollisionRule(AbstractTeam.CollisionRule.NEVER);
        team.setNameTagVisibilityRule(AbstractTeam.VisibilityRule.NEVER);
        team.setShowFriendlyInvisibles(false);
    }
}

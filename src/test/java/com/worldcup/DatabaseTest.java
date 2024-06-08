package com.worldcup;

import static org.junit.Assert.*;

import org.junit.Test;

public class DatabaseTest {

    public WorldCup worldCup;
    @Test
    public void testTeam() { 
        Team team = worldCup.getTeams().get(0);
        assertEquals("Team 1", team.getName());
        assertEquals(0, team.getPoints());
        team.addPoints(9);
        assertEquals(9, team.getPoints());
        assertEquals(3, team.getMatchesPlayed());
        team.incrementMatchesPlayed();
        assertEquals(4, team.getMatchesPlayed());
        Player player = new Player("Player 1");
        team.addPlayer(player);
        assertEquals(1, team.players.size());
        Team supportCoach = new Team("Support Coach");
        team.addSupportCoach(supportCoach);
        assertEquals(1, team.supportCoach);
        assertFalse(team.hasForfeited());
    }
}

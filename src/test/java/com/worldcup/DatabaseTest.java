package com.worldcup;

import static org.junit.Assert.*;

// import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
// import org.junit.Test;
import org.junit.Test;

public class DatabaseTest {
    public Region region;
    public Match match;
    public WorldCup worldCup;

    @Before
    public void setUp() {
        worldCup = new WorldCup();
        worldCup.initializeTeams();
        for (int i = 0; i < 8; i++) {
            worldCup.groups.add(new Group("Group " + (i + 1), null));
        }
        region = new Region();
        match = new Match(null, null);
        region.addTeamsInAsia();
        region.addTeamsInAfrica();
        region.addTeamsInEurope();
        region.addTeamsInNorthCentralAmericaAndCaribbean();
        region.addTeamsInOceania();
        region.addTeamsInSouthAmerica();

        int i = 0;

        for (int j = 0; j < worldCup.groups.size(); j++) {
            worldCup.groups.get(j).teams = new ArrayList<Team>();
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 1)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 2)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 3)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 4)));
        }
    }

    @Test
    public void testDatabaseConnection() {
        Database db = new Database();
        assertTrue(db.connect());
    }

    @Test
    public void testTeamDataPersistence() {
        Database db = new Database();
        Team team = new Team("Persisted Team");
        db.saveTeam(team);
        Team loadedTeam = db.loadTeam("Persisted Team");
        assertEquals(team.getName(), loadedTeam.getName());
    }

    @Test
    public void testMatchResultPersistence() {
        Database db = new Database();
        Team teamA = worldCup.getTeams().get(0);
        Team teamB = worldCup.getTeams().get(1);
        Match match = new Match(teamA, teamB);
        match.play();
        db.saveMatchResult(match);
        Match loadedMatch = db.loadMatchResult(match.getId());
        assertEquals(match.getResult(), loadedMatch.getResult());
    }

    // // 14
    // @Test
    // public void testMatchDataPersistence() { // Kiểm tra dữ liệu trận đấu
    // Database db = new Database();
    // Team teamA = new Team("Team A");
    // Team teamB = new Team("Team B");
    // Match match = new Match(teamA, teamB);
    // db.saveMatch(match);
    // Match loadedMatch = db.loadMatch(teamA, teamB);
    // assertEquals(match.getTeamA().getName(), loadedMatch.getTeamA().getName());
    // assertEquals(match.getTeamB().getName(), loadedMatch.getTeamB().getName());
    // }
}

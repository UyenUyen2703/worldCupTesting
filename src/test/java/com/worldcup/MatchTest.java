package com.worldcup;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class MatchTest {

    public WorldCup worldCup;
    public Region region;
    public Group group;
    public Match match;

    @Before
    public void setUp() {
        worldCup = new WorldCup();
        worldCup.initializeTeams();
        // worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();

        for (int i = 0; i < 8; i++) {
            worldCup.groups.add(new Group("Group " + (i + 1), null));
        }
        int i = 0;

        for (int j = 0; j < worldCup.groups.size(); j++) {
            worldCup.groups.get(j).teams = new ArrayList<Team>();
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 1)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 2)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 3)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 4)));
        }

        // Chơi vòng bảng và đưa các đội vào vòng knock-out
        worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
    }

    @Test
    public void testTopTwoTeamsAdvanceToKnockoutStage() {
        // worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
        assertEquals(16, worldCup.getKnockoutStageTeams().size());
    }

    @Test
    public void testPenaltyShootout() { 
        Team teamA = worldCup.getTeams().get(0);
        Team teamB = worldCup.getTeams().get(1);
        Match match = new Match(teamA, teamB);
        match.setDraw(true);
        match.playPenaltyShootout();
        assertNotNull(match.getWinner());
    }

    @Test
    public void testExtraTime() {
        Team teamA = worldCup.getTeams().get(0);
        Team teamB = worldCup.getTeams().get(1);
        Match match = new Match(teamA, teamB);
        match.setDraw(true);
        match.playExtraTime();
        assertNotNull(match.getWinner());
    }

    @Test
    public void testHaftTime() {
        int expected = 45;
        int actual = match.haftTime;
        assertEquals(expected, actual);
    }

    // 42
    @Test
    public void testHaveInjuryTime() {
        boolean expected = true;
        boolean actual = match.injuryTime();
        if (match.draw = true) {
            assertEquals(expected, actual);
        }
    }

    // 43
    @Test
    public void testHaveExtraTime() {
        boolean expected = true;
        boolean actual = match.extraTime;
        assertEquals(expected, actual);
    }

        // 53 // Kiểm tra thời gian của hiệp đầu
        @Test
        public void testFirstHalfDuration() {
            assertEquals("Thời gian của hiệp đầu không đúng", 45, match.getFirstHalfDuration());
        }
    
        // 54 // Kiểm tra thời gian của hiệp hai
        @Test
        public void testSecondHalfDuration() {
            assertEquals("Thời gian của hiệp hai không đúng", 45, match.getSecondHalfDuration());
        }
    
        // 55 // Kiểm tra thời gian nghỉ giữa hai hiệp
        @Test
        public void testHalfTimeBreak() {
            assertEquals("Thời gian nghỉ giữa hai hiệp không đúng", 15, match.getHalfTimeBreakDuration());
        }
    
        // 56 // Kiểm tra tổng thời gian của trận đấu
        @Test
        public void testTotalDuration() {
            assertEquals("Tổng thời gian của trận đấu không đúng", 105, match.getTotalDuration());
        }
    
        @Test
        public void testPlayMatchTeam3Team4() {
            String expectedMessage = "Trận đấu đã diễn ra giữa Team 3 và Team 4 trong 105 phút.";
            assertEquals(expectedMessage, match.playMatch("Team 3", "Team 4"),
                    "Trận đấu đã diễn ra giữa Team 3 và Team 4 trong 105 phút.");
        }
    
}

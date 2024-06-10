package com.worldcup;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TeamTest {
    public WorldCup worldCup;
    public Region region;
    public Group group;
    public Match match;
    public Card card;
    public Player player;
    public Team TeamA;
    public Team TeamB;

    @Before
    public void setUp() {
        worldCup = new WorldCup();
        worldCup.playGroupStage();
        worldCup.initializeTeams();
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
    }

    @Test
    public void testGoldenGoalRule() {
        Team teamA = worldCup.getTeams().get(0);
        Team teamB = worldCup.getTeams().get(1);
        assertNotEquals(teamA.name, teamB.name);
    }

    @Test
    public void testNameCoachTeamNotEquals() {
        int expected = 1;
        Team teamA = worldCup.getTeams().get(0);
        Team teamB = worldCup.getTeams().get(1);
        teamA.coach = 1;
        teamB.coach = 1;
        assertEquals(expected, teamA.coach, teamB.coach);
    }

    @Test
    public void testAddPoints() {
        Team team = worldCup.groups.get(0).teams.get(0);
        team.addPoints(3);
        assertEquals(3, team.getPoints());
    }

    @Test
    public void testIncrementMatchesPlayed() {
        worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
        Team team = worldCup.groups.get(0).teams.get(0);
        team.incrementMatchesPlayed();
        assertEquals(4, team.getMatchesPlayed());
    }

    @Test
    public void testAddPlayer() {
        Team team = worldCup.groups.get(0).teams.get(0);
        Player player = new Player("Player 1");
        team.addPlayer(player);
        assertEquals(1, team.players.size());
    }

    @Test
    public void testTeamWithLessThanSevenPlayersForfeits() { // Đội bóng còn dưới 7 cầu thủ trên sân
        Team team = new Team("Test Team");
        for (int i = 0; i < 5; i++) {
            Player player = new Player("Player " + i);
            player.receiveCard(Card.RED);
            team.addPlayer(player);
        }
        assertTrue(team.hasForfeited());

    }

    @Test
    public void testTop1GroupA() {
        String expected = "Team 4";

        Team team1 = worldCup.groups.get(0).teams.get(0);
        team1.points = 6;
        Team team2 = worldCup.groups.get(0).teams.get(1);
        team2.points = 3;
        Team team3 = worldCup.groups.get(0).teams.get(2);
        team3.points = 0;
        Team team4 = worldCup.groups.get(0).teams.get(3);
        team4.points = 9;
        assertEquals(expected, worldCup.groups.get(0).getRankedTeams().get(0).name);
    }

    @Test
    public void testTop2GroupA() {
        String expected = "Team 1";

        Team team1 = worldCup.groups.get(0).teams.get(0);
        team1.points = 6;
        Team team2 = worldCup.groups.get(0).teams.get(1);
        team2.points = 3;
        Team team3 = worldCup.groups.get(0).teams.get(2);
        team3.points = 0;
        Team team4 = worldCup.groups.get(0).teams.get(3);
        team4.points = 9;
        assertEquals(expected, worldCup.groups.get(0).getRankedTeams().get(1).name);
    }

    @Test
    public void testTop1GroupB() {
        String expected = "Team 4";

        Team team1 = worldCup.groups.get(1).teams.get(0);
        team1.points = 1;
        Team team2 = worldCup.groups.get(1).teams.get(1);
        team2.points = 4;
        Team team3 = worldCup.groups.get(1).teams.get(2);
        team3.points = 5;
        Team team4 = worldCup.groups.get(1).teams.get(3);
        team4.points = 6;
        assertEquals(expected, worldCup.groups.get(1).getRankedTeams().get(0).name);
    }

    @Test
    public void testTop2GroupB() {
        String expected = "Team 3";
        Team team1 = worldCup.groups.get(1).teams.get(0);
        team1.points = 1;
        Team team2 = worldCup.groups.get(1).teams.get(1);
        team2.points = 4;
        Team team3 = worldCup.groups.get(1).teams.get(2);
        team3.points = 5;
        Team team4 = worldCup.groups.get(1).teams.get(3);
        team4.points = 6;
        assertEquals(expected, worldCup.groups.get(1).getRankedTeams().get(1).name);
    }

    @Test
    public void testTop1GroupC() {
        String expected = "Team 4";
        Team team1 = worldCup.groups.get(2).teams.get(0);
        team1.points = 1;
        Team team2 = worldCup.groups.get(2).teams.get(1);
        team2.points = 4;
        Team team3 = worldCup.groups.get(2).teams.get(2);
        team3.points = 5;
        Team team4 = worldCup.groups.get(2).teams.get(3);
        team4.points = 6;
        assertEquals(expected, worldCup.groups.get(2).getRankedTeams().get(0).name);
    }

    @Test
    public void testTop2GroupC() {
        String expected = "Team 3";
        Team team1 = worldCup.groups.get(2).teams.get(0);
        team1.points = 1;
        Team team2 = worldCup.groups.get(2).teams.get(1);
        team2.points = 4;
        Team team3 = worldCup.groups.get(2).teams.get(2);
        team3.points = 5;
        Team team4 = worldCup.groups.get(2).teams.get(3);
        team4.points = 6;

        assertEquals(expected, worldCup.groups.get(2).getRankedTeams().get(1).name);
    }

    @Test
    public void testTop1GroupD() {
        String expected = "Team 3";
        Team team1 = worldCup.groups.get(3).teams.get(0);
        team1.points = 1;
        Team team2 = worldCup.groups.get(3).teams.get(1);
        team2.points = 2;
        Team team3 = worldCup.groups.get(3).teams.get(2);
        team3.points = 9;
        Team team4 = worldCup.groups.get(3).teams.get(3);
        team4.points = 3;
        assertEquals(expected, worldCup.groups.get(3).getRankedTeams().get(0).name);
    }

    @Test
    public void testTop2GroupD() {
        String expected = "Team 4";
        Team team1 = worldCup.groups.get(3).teams.get(0);
        team1.points = 1;
        Team team2 = worldCup.groups.get(3).teams.get(1);
        team2.points = 2;
        Team team3 = worldCup.groups.get(3).teams.get(2);
        team3.points = 9;
        Team team4 = worldCup.groups.get(3).teams.get(3);
        team4.points = 3;
        assertEquals(expected, worldCup.groups.get(3).getRankedTeams().get(1).name);
    }

    @Test
    public void testTop1GroupE() {
        String expected = "Team 3";
        Team team1 = worldCup.groups.get(4).teams.get(0);
        team1.points = 1;
        Team team2 = worldCup.groups.get(4).teams.get(1);
        team2.points = 3;
        Team team3 = worldCup.groups.get(4).teams.get(2);
        team3.points = 7;
        Team team4 = worldCup.groups.get(4).teams.get(3);
        team4.points = 5;

        assertEquals(expected, worldCup.groups.get(4).getRankedTeams().get(0).name);
    }

    @Test
    public void testTop2GroupE() {
        String expected = "Team 4";
        Team team1 = worldCup.groups.get(4).teams.get(0);
        team1.points = 1;
        Team team2 = worldCup.groups.get(4).teams.get(1);
        team2.points = 3;
        Team team3 = worldCup.groups.get(4).teams.get(2);
        team3.points = 7;
        Team team4 = worldCup.groups.get(4).teams.get(3);
        team4.points = 5;
        assertEquals(expected, worldCup.groups.get(4).getRankedTeams().get(1).name);
    }

    @Test
    public void testTop1GroupF() {
        String expected = "Team 3";
        Team team1 = worldCup.groups.get(5).teams.get(0);
        team1.points = 3;
        Team team2 = worldCup.groups.get(5).teams.get(1);
        team2.points = 6;
        Team team3 = worldCup.groups.get(5).teams.get(2);
        team3.points = 7;
        Team team4 = worldCup.groups.get(5).teams.get(3);
        team4.points = 4;

        assertEquals(expected, worldCup.groups.get(5).getRankedTeams().get(0).name);
    }

    @Test
    public void testTop2GroupF() {
        String expected = "Team 2";
        Team team1 = worldCup.groups.get(5).teams.get(0);
        team1.points = 3;
        Team team2 = worldCup.groups.get(5).teams.get(1);
        team2.points = 6;
        Team team3 = worldCup.groups.get(5).teams.get(2);
        team3.points = 7;
        Team team4 = worldCup.groups.get(5).teams.get(3);
        team4.points = 4;
        assertEquals(expected, worldCup.groups.get(5).getRankedTeams().get(1).name);
    }

    @Test
    public void testTop1GroupG() {
        String expected = "Team 1";
        Team team1 = worldCup.groups.get(6).teams.get(0);
        team1.points = 6;
        Team team2 = worldCup.groups.get(6).teams.get(1);
        team2.points = 0;
        Team team3 = worldCup.groups.get(6).teams.get(2);
        team3.points = 1;
        Team team4 = worldCup.groups.get(6).teams.get(3);
        team4.points = 3;

        assertEquals(expected, worldCup.groups.get(6).getRankedTeams().get(0).name);
    }

    @Test
    public void testTop2GroupG() {
        String expected = "Team 4";
        Team team1 = worldCup.groups.get(6).teams.get(0);
        team1.points = 6;
        Team team2 = worldCup.groups.get(6).teams.get(1);
        team2.points = 0;
        Team team3 = worldCup.groups.get(6).teams.get(2);
        team3.points = 1;
        Team team4 = worldCup.groups.get(6).teams.get(3);
        team4.points = 3;
        assertEquals(expected, worldCup.groups.get(6).getRankedTeams().get(1).name);
    }

    @Test
    public void testTop1GroupH() {
        String expected = "Team 2";
        Team team1 = worldCup.groups.get(7).teams.get(0);
        team1.points = 4;
        Team team2 = worldCup.groups.get(7).teams.get(1);
        team2.points = 6;
        Team team3 = worldCup.groups.get(7).teams.get(2);
        team3.points = 1;
        Team team4 = worldCup.groups.get(7).teams.get(3);
        team4.points = 5;

        assertEquals(expected, worldCup.groups.get(7).getRankedTeams().get(0).name);
    }

    @Test
    public void testTop2GroupH() {
        String expected = "Team 4";
        Team team1 = worldCup.groups.get(7).teams.get(0);
        team1.points = 4;
        Team team2 = worldCup.groups.get(7).teams.get(1);
        team2.points = 6;
        Team team3 = worldCup.groups.get(7).teams.get(2);
        team3.points = 1;
        Team team4 = worldCup.groups.get(7).teams.get(3);
        team4.points = 5;

        assertEquals(expected, worldCup.groups.get(7).getRankedTeams().get(1).name);
    }

    @Test
    public void testTeamWin() {

        int expected = 3;
        Team team = worldCup.getTeams().get(0);
        team.addPointsForWin();
        assertEquals(expected, team.getPoints());
    }

    @Test
    public void testTeamDraw() {
        Team team = worldCup.getTeams().get(0);
        team.addPointsForDraw();
        assertEquals(1, team.getPoints());
    }

    @Test
    public void testTeamLoss() {
        Team team = worldCup.getTeams().get(0);
        team.addPointsForLoss();
        assertEquals(0, team.getPoints());
    }

    public void testTeamPoints() {

        Team team = new Team("Team A");
        assertEquals("Điểm ban đầu của đội phải là 0", 0, team.getPoints());

        team.updatePoints(3);
        team.updatePoints(0);
        team.updatePoints(0);

        assertEquals("Điểm cuối cùng của đội phải là 3", 3, team.getPoints());
    }

    @Test
    public void testTeamPointsAfterThreeMatch() {
        Team team = new Team("Team A");
        assertEquals("Điểm ban đầu của đội phải là 0", 0, team.getPoints());

        team.updatePoints(3);
        team.updatePoints(3);
        team.updatePoints(0);

        assertEquals("Điểm cuối cùng của đội phải là 6", 6, team.getPoints());
    }

    @Test
    public void testTeamPointsAfterThreeMatch2() {
        Team team = new Team("Team A");
        assertEquals("Điểm ban đầu của đội phải là 0", 0, team.getPoints());

        team.updatePoints(3);
        team.updatePoints(0);
        team.updatePoints(1);

        assertEquals("Điểm cuối cùng của đội phải là 4", 4, team.getPoints());
    }

    @Test
    public void testTeamsWinThreeMatches() {
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                team.addPoints(9);
            }
        }
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                assertEquals("Điểm của đội " + team.getName() + " đúng", 9, team.getPoints());
            }
        }
    }

    @Test
    public void testTeamsDrawThreeMatches() {
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                for (int i = 0; i < 3; i++) {
                    team.addPointsForDraw();
                }
            }
        }

        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                assertEquals("Điểm của đội " + team.getName() + " đúng", 3, team.getPoints());
            }
        }
    }

    public void testTeamsLoseThreeMatches() {
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                for (int i = 0; i < 3; i++) {
                    team.addPointsForLoss();
                }
            }
        }

        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                assertEquals("Điểm của đội " + team.getName() + " đúng", 0, team.getPoints());
            }
        }
    }

    @Test
    public void testTeam1Points() {
        Team team1 = worldCup.getTeams().get(0);
        team1.addPoints(9);
        assertEquals(9, team1.getPoints());
    }

    @Test
    public void testTeam2Points() {
        Team team2 = worldCup.getTeams().get(1);
        team2.addPoints(9);
        assertEquals(9, team2.getPoints());
    }

    @Test
    public void testTeam3Points() {
        Team team3 = worldCup.getTeams().get(2);
        team3.addPoints(9);
        assertEquals(9, team3.getPoints());
    }

    @Test
    public void testTeam4Points() {
        Team team4 = worldCup.getTeams().get(3);
        team4.addPoints(9);
        assertEquals(9, team4.getPoints());
    }

    @Test
    public void testTeam5Points() {
        Team team5 = worldCup.getTeams().get(4);
        team5.addPoints(9);
        assertEquals(9, team5.getPoints());
    }

    @Test
    public void testTeam6Points() {
        Team team6 = worldCup.getTeams().get(5);
        team6.addPoints(9);
        assertEquals(9, team6.getPoints());
    }

    @Test
    public void testTeam7Points() {
        Team team7 = worldCup.getTeams().get(6);
        team7.addPoints(9);
        assertEquals(9, team7.getPoints());
    }

    @Test
    public void testTeam8Points() {
        Team team8 = worldCup.getTeams().get(7);
        team8.addPoints(9);
        assertEquals(9, team8.getPoints());
    }

    @Test
    public void testTeam9Points() {
        Team team9 = worldCup.getTeams().get(8);
        team9.addPoints(9);
        assertEquals(9, team9.getPoints());
    }

    @Test
    public void testTeam10Points() {
        Team team10 = worldCup.getTeams().get(9);
        team10.addPoints(9);
        assertEquals(9, team10.getPoints());
    }

    @Test
    public void testTeam11Points() {
        Team team11 = worldCup.getTeams().get(10);
        team11.addPoints(9);
        assertEquals(9, team11.getPoints());
    }

    @Test
    public void testTeam12Points() {
        Team team12 = worldCup.getTeams().get(11);
        team12.addPoints(9);
        assertEquals(9, team12.getPoints());
    }

    @Test
    public void testTeam13Points() {
        Team team13 = worldCup.getTeams().get(12);
        team13.addPoints(9);
        assertEquals(9, team13.getPoints());
    }

    @Test
    public void testTeam14Points() {
        Team team14 = worldCup.getTeams().get(13);
        team14.addPoints(9);
        assertEquals(9, team14.getPoints());
    }

    @Test
    public void testTeam15Points() {
        Team team15 = worldCup.getTeams().get(14);
        team15.addPoints(9);
        assertEquals(9, team15.getPoints());
    }

    @Test
    public void testTeam16Points() {
        Team team16 = worldCup.getTeams().get(15);
        team16.addPoints(9);
        assertEquals(9, team16.getPoints());
    }

    @Test
    public void testTeam17Points() {
        Team team17 = worldCup.getTeams().get(16);
        team17.addPoints(9);
        assertEquals(9, team17.getPoints());
    }

    @Test
    public void testTeam18Points() {
        Team team18 = worldCup.getTeams().get(17);
        team18.addPoints(9);
        assertEquals(9, team18.getPoints());
    }

    @Test
    public void testTeam19Points() {
        Team team19 = worldCup.getTeams().get(18);
        team19.addPoints(9);
        assertEquals(9, team19.getPoints());
    }

    @Test
    public void testTeam20Points() {
        Team team20 = worldCup.getTeams().get(19);
        team20.addPoints(9);
        assertEquals(9, team20.getPoints());
    }

    @Test
    public void testTeam21Points() {
        Team team21 = worldCup.getTeams().get(20);
        team21.addPoints(9);
        assertEquals(9, team21.getPoints());
    }

    @Test
    public void testTeam22Points() {
        Team team22 = worldCup.getTeams().get(21);
        team22.addPoints(9);
        assertEquals(9, team22.getPoints());
    }

    @Test
    public void testTeam23Points() {
        Team team23 = worldCup.getTeams().get(22);
        team23.addPoints(9);
        assertEquals(9, team23.getPoints());
    }

    @Test
    public void testTeam24Points() {
        Team team24 = worldCup.getTeams().get(23);
        team24.addPoints(9);
        assertEquals(9, team24.getPoints());
    }

    @Test
    public void testTeam25Points() {
        Team team25 = worldCup.getTeams().get(24);
        team25.addPoints(9);
        assertEquals(9, team25.getPoints());
    }

    @Test
    public void testTeam26Points() {
        Team team26 = worldCup.getTeams().get(25);
        team26.addPoints(9);
        assertEquals(9, team26.getPoints());
    }

    @Test
    public void testTeam27Points() {
        Team team27 = worldCup.getTeams().get(26);
        team27.addPoints(9);
        assertEquals(9, team27.getPoints());
    }

    @Test
    public void testTeam28Points() {
        Team team28 = worldCup.getTeams().get(27);
        team28.addPoints(9);
        assertEquals(9, team28.getPoints());
    }

    @Test
    public void testTeam29Points() {
        Team team29 = worldCup.getTeams().get(28);
        team29.addPoints(9);
        assertEquals(9, team29.getPoints());
    }

    @Test
    public void testTeam30Points() {
        Team team30 = worldCup.getTeams().get(29);
        team30.addPoints(9);
        assertEquals(9, team30.getPoints());
    }

    @Test
    public void testTeam31Points() {
        Team team31 = worldCup.getTeams().get(30);
        team31.addPoints(9);
        assertEquals(9, team31.getPoints());
    }

    @Test
    public void testTeam32Points() {
        Team team32 = worldCup.getTeams().get(31);
        team32.addPoints(9);
        assertEquals(9, team32.getPoints());
    }

    @Test
    public void testaddSupportCoach() {
        Team team = worldCup.getTeams().get(0);
        team.supportCoach = 1;
        team.addSupportCoach();
        team.addSupportCoach();
        assertEquals(3, team.supportCoach);
        if (team.supportCoach >= 3) {
            try {
                team.addSupportCoach();
            } catch (IllegalStateException e) {
                assertEquals("Cannot add more than 3 support coaches", e.getMessage());
            }
        }
    }

    @Test
    public void testPlayRoundOf16TeamsQualified() {
        worldCup.playRoundOf16();
        List<Team> qualifiedTeams = worldCup.getKnockoutStageTeams();
        assertEquals(8, qualifiedTeams.size());
    }

}

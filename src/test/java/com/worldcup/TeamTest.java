package com.worldcup;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TeamTest {
    public WorldCup worldCup;
    public Region region;
    // public Group group;
    public Match match;
    public Card card;
    public Player player;
    public Team TeamA;
    public Team TeamB;

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
    public void testGoldenGoalRule() { // Tên 2 đội phải khác nhau
        Team teamA = worldCup.getTeams().get(0);
        Team teamB = worldCup.getTeams().get(1);
        assertNotEquals(teamA.name, teamB.name);
    }

    @Test
    public void testNameCoachTeamNotEquals() { // Kiểm tra số lượng hlv không được quá 1 người
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

        // 44 // Giả sử đội thứ nhất thắng 1 trận cộng đúng 3 điểm
        @Test
        public void testTeamWin() {
    
            int expected = 3;
            Team team = worldCup.getTeams().get(0); // Lấy đội thứ nhất
            team.addPointsForWin(); // Thắng 1 trận
            assertEquals(expected, team.getPoints());
        }
    
        // 45 // Giả sử đội thứ nhất hòa 1 trận cộng đúng 1 điểm
        @Test
        public void testTeamDraw() {
            Team team = worldCup.getTeams().get(0); // Lấy đội thứ nhất
            team.addPointsForDraw(); // Hòa 1 trận
            assertEquals(1, team.getPoints());
        }
    
        // 46 // Giả sử đội thứ nhất thua 1 trận không cộng điểm
        @Test
        public void testTeamLoss() {
            Team team = worldCup.getTeams().get(0); // Lấy đội thứ nhất
            team.addPointsForLoss(); // Thua 1 trận
            assertEquals(0, team.getPoints());
        }
    
        // 47 // Kiểm tra một đội đá 3 trận và thắng 1, thua 2
        @Test
        public void testTeamPoints() {
            // Tạo đội và thiết lập điểm ban đầu là 0
            Team team = new Team("Team A");
            assertEquals("Điểm ban đầu của đội phải là 0", 0, team.getPoints());
    
            // Giả sử đội đá 3 trận và thắng 1, thua 2
            team.updatePoints(3); // Thắng trận đầu tiên
            team.updatePoints(0); // Thua trận thứ hai
            team.updatePoints(0); // Thua trận thứ ba
    
            // Điểm cuối cùng sẽ là 3 điểm từ trận thắng
            assertEquals("Điểm cuối cùng của đội phải là 3", 3, team.getPoints());
        }
    
        // 48 // Kiểm tra một đội đá 3 trận và thắng 2, thua 1
        @Test
        public void testTeamPointsAfterThreeMatch() {
            // Tạo đội và thiết lập điểm ban đầu là 0
            Team team = new Team("Team A");
            assertEquals("Điểm ban đầu của đội phải là 0", 0, team.getPoints());
    
            // Giả sử đội đá 3 trận và thắng 2, thua 1
            team.updatePoints(3); // Thắng trận đầu tiên
            team.updatePoints(3); // Thắng trận thứ hai
            team.updatePoints(0); // Thua trận thứ ba
    
            // Điểm cuối cùng sẽ là 6 điểm từ trận thắng
            assertEquals("Điểm cuối cùng của đội phải là 6", 6, team.getPoints());
        }
    
        // 49 // Kiểm tra một đội đá 3 trận và thắng 1 thua 1 hòa 1
        @Test
        public void testTeamPointsAfterThreeMatch2() {
            // Tạo đội và thiết lập điểm ban đầu là 0
            Team team = new Team("Team A");
            assertEquals("Điểm ban đầu của đội phải là 0", 0, team.getPoints());
    
            // Giả sử đội đá 3 trận và thắng 1, thua 1, hòa 1
            team.updatePoints(3); // Thắng trận đầu tiên
            team.updatePoints(0); // Thua trận thứ hai
            team.updatePoints(1); // Hòa trận thứ ba
    
            // Điểm cuối cùng sẽ là 4 điểm từ trận thắng và hòa
            assertEquals("Điểm cuối cùng của đội phải là 4", 4, team.getPoints());
        }

          // 50 // Kiểm tra một đội đá 3 trận thắng 3 trận cộng đúng 9 điểm
    @Test
    public void testTeamsWinThreeMatches() {
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                team.addPoints(9); // Thắng 3 trận, mỗi trận 3 điểm
            }
        }

        // Kiểm tra điểm của từng đội
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                assertEquals("Điểm của đội " + team.getName() + " đúng", 9, team.getPoints());
            }
        }
    }

    // 51 // Kiểm tra một đội đá 3 trận hòa 3 trận cộng đúng 3 điểm
    @Test
    public void testTeamsDrawThreeMatches() {
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                for (int i = 0; i < 3; i++) {
                    team.addPointsForDraw(); // Hòa 3 trận, mỗi trận 1 điểm
                }
            }
        }

        // Kiểm tra điểm của từng đội
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                assertEquals("Điểm của đội " + team.getName() + " đúng", 3, team.getPoints());
            }
        }
    }

    // 52 // Kiểm tra một đội đá 3 trận thua 3 trận không cộng điểm
    @Test
    public void testTeamsLoseThreeMatches() {
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                for (int i = 0; i < 3; i++) {
                    team.addPointsForLoss(); // Thua 3 trận, không được cộng điểm
                }
            }
        }

        // Kiểm tra điểm của từng đội
        for (Group group : worldCup.getGroups()) {
            for (Team team : group.getTeams()) {
                assertEquals("Điểm của đội " + team.getName() + " đúng", 0, team.getPoints());
            }
        }
    }
        // 58 // Kiểm tra điểm của đội 1
        @Test
        public void testTeam1Points() {
            Team team1 = worldCup.getTeams().get(0);
            team1.addPoints(9); // Thắng 3 trận
            assertEquals(9, team1.getPoints());
        }
    
        // 59 // Kiểm tra điểm của đội 2
        @Test
        public void testTeam2Points() {
            Team team2 = worldCup.getTeams().get(1);
            team2.addPoints(9); // Thắng 3 trận
            assertEquals(9, team2.getPoints());
        }
    
        // 60
        @Test
        public void testTeam3Points() {
            Team team3 = worldCup.getTeams().get(2);
            team3.addPoints(9); // Thắng 3 trận
            assertEquals(9, team3.getPoints());
        }
    
        // 61
        @Test
        public void testTeam4Points() {
            Team team4 = worldCup.getTeams().get(3);
            team4.addPoints(9); // Thắng 3 trận
            assertEquals(9, team4.getPoints());
        }
    
        // 62
        @Test
        public void testTeam5Points() {
            Team team5 = worldCup.getTeams().get(4);
            team5.addPoints(9); // Thắng 3 trận
            assertEquals(9, team5.getPoints());
        }
    
        // 63
        @Test
        public void testTeam6Points() {
            Team team6 = worldCup.getTeams().get(5);
            team6.addPoints(9); // Thắng 3 trận
            assertEquals(9, team6.getPoints());
        }
    
        // 64
        @Test
        public void testTeam7Points() {
            Team team7 = worldCup.getTeams().get(6);
            team7.addPoints(9); // Thắng 3 trận
            assertEquals(9, team7.getPoints());
        }
    
        // 65
        @Test
        public void testTeam8Points() {
            Team team8 = worldCup.getTeams().get(7);
            team8.addPoints(9); // Thắng 3 trận
            assertEquals(9, team8.getPoints());
        }
    
        // 66
        @Test
        public void testTeam9Points() {
            Team team9 = worldCup.getTeams().get(8);
            team9.addPoints(9); // Thắng 3 trận
            assertEquals(9, team9.getPoints());
        }
    
        // 67
        @Test
        public void testTeam10Points() {
            Team team10 = worldCup.getTeams().get(9);
            team10.addPoints(9); // Thắng 3 trận
            assertEquals(9, team10.getPoints());
        }
    
        // 68
        @Test
        public void testTeam11Points() {
            Team team11 = worldCup.getTeams().get(10);
            team11.addPoints(9); // Tháng 3 trận
            assertEquals(9, team11.getPoints());
        }
    
        // 69
        @Test
        public void testTeam12Points() {
            Team team12 = worldCup.getTeams().get(11);
            team12.addPoints(9); // Thắng 3 trận
            assertEquals(9, team12.getPoints());
        }
    
        // 70
        @Test
        public void testTeam13Points() {
            Team team13 = worldCup.getTeams().get(12);
            team13.addPoints(9); // Thắng 3 trận
            assertEquals(9, team13.getPoints());
        }
    
        // 71
        @Test
        public void testTeam14Points() {
            Team team14 = worldCup.getTeams().get(13);
            team14.addPoints(9); // Thắng 3 trận
            assertEquals(9, team14.getPoints());
        }
    
        // 72
        @Test
        public void testTeam15Points() {
            Team team15 = worldCup.getTeams().get(14);
            team15.addPoints(9); // Thắng 3 trận
            assertEquals(9, team15.getPoints());
        }
    
        // 73
        @Test
        public void testTeam16Points() {
            Team team16 = worldCup.getTeams().get(15);
            team16.addPoints(9); // Thắng 3 trận
            assertEquals(9, team16.getPoints());
        }
    
        // 74
        @Test
        public void testTeam17Points() {
            Team team17 = worldCup.getTeams().get(16);
            team17.addPoints(9); // Thắng 3 trận
            assertEquals(9, team17.getPoints());
        }
    
        // 75
        @Test
        public void testTeam18Points() {
            Team team18 = worldCup.getTeams().get(17);
            team18.addPoints(9); // Thắng 3 trận
            assertEquals(9, team18.getPoints());
        }
    
        // 76
        @Test
        public void testTeam19Points() {
            Team team19 = worldCup.getTeams().get(18);
            team19.addPoints(9); // Thắng 3 trận
            assertEquals(9, team19.getPoints());
        }
    
        // 77
        @Test
        public void testTeam20Points() {
            Team team20 = worldCup.getTeams().get(19);
            team20.addPoints(9); // Thắng 3 trận
            assertEquals(9, team20.getPoints());
        }
    
        // 78
        @Test
        public void testTeam21Points() {
            Team team21 = worldCup.getTeams().get(20);
            team21.addPoints(9); // Thắng 3 trận
            assertEquals(9, team21.getPoints());
        }
    
        // 79
        @Test
        public void testTeam22Points() {
            Team team22 = worldCup.getTeams().get(21);
            team22.addPoints(9); // Thắng 3 trận
            assertEquals(9, team22.getPoints());
        }
    
        // 80
        @Test
        public void testTeam23Points() {
            Team team23 = worldCup.getTeams().get(22);
            team23.addPoints(9); // Thắng 3 trận
            assertEquals(9, team23.getPoints());
        }
    
        // 81
        @Test
        public void testTeam24Points() {
            Team team24 = worldCup.getTeams().get(23);
            team24.addPoints(9); // Thắng 3 trận
            assertEquals(9, team24.getPoints());
        }
    
        // 82
        @Test
        public void testTeam25Points() {
            Team team25 = worldCup.getTeams().get(24);
            team25.addPoints(9); // Thắng 3 trận
            assertEquals(9, team25.getPoints());
        }
    
        // 83
        @Test
        public void testTeam26Points() {
            Team team26 = worldCup.getTeams().get(25);
            team26.addPoints(9); // Thắng 3 trận
            assertEquals(9, team26.getPoints());
        }
    
        // 84
        @Test
        public void testTeam27Points() {
            Team team27 = worldCup.getTeams().get(26);
            team27.addPoints(9); // Thắng 3 trận
            assertEquals(9, team27.getPoints());
        }
    
        // 85
        @Test
        public void testTeam28Points() {
            Team team28 = worldCup.getTeams().get(27);
            team28.addPoints(9); // Thắng 3 trận
            assertEquals(9, team28.getPoints());
        }
    
        // 86
        @Test
        public void testTeam29Points() {
            Team team29 = worldCup.getTeams().get(28);
            team29.addPoints(9); // Thắng 3 trận
            assertEquals(9, team29.getPoints());
        }
    
        // 87
        @Test
        public void testTeam30Points() {
            Team team30 = worldCup.getTeams().get(29);
            team30.addPoints(9); // Thắng 3 trận
            assertEquals(9, team30.getPoints());
        }
    
        // 88
        @Test
        public void testTeam31Points() {
            Team team31 = worldCup.getTeams().get(30);
            team31.addPoints(9); // Thắng 3 trận
            assertEquals(9, team31.getPoints());
        }
    
        // 89
        @Test
        public void testTeam32Points() {
            Team team32 = worldCup.getTeams().get(31);
            team32.addPoints(9); // Thắng 3 trận
            assertEquals(9, team32.getPoints());
        }

        
}

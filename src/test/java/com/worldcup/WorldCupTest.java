package com.worldcup;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WorldCupTest {

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

        region = new Region();
        match = new Match(null, null);
        region.addTeamsInAsia();
        region.addTeamsInAfrica();
        region.addTeamsInEurope();
        region.addTeamsInNorthCentralAmericaAndCaribbean();
        region.addTeamsInOceania();
        region.addTeamsInSouthAmerica();

        worldCup.groups.add(new Group("Group A", null));
        worldCup.groups.add(new Group("Group B", null));
        worldCup.groups.add(new Group("Group C", null));
        worldCup.groups.add(new Group("Group D", null));
        worldCup.groups.add(new Group("Group E", null));
        worldCup.groups.add(new Group("Group F", null));
        worldCup.groups.add(new Group("Group G", null));
        worldCup.groups.add(new Group("Group H", null));

        int i = 0;

        for (int j = 0; j < worldCup.groups.size(); j++) {
            worldCup.groups.get(j).teams = new ArrayList<Team>();
            worldCup.groups.get(j).teams.add(new Team("Team" + (i + 1), "Coach " + (i + 1)));
            worldCup.groups.get(j).teams.add(new Team("Team" + (i + 2), "Coach " + (i + 2)));
            worldCup.groups.get(j).teams.add(new Team("Team" + (i + 3), "Coach " + (i + 3)));
            worldCup.groups.get(j).teams.add(new Team("Team" + (i + 4), "Coach " + (i + 4)));
        }

    }

    // 1
    @Test
    public void testInitializeTeams() { // test số lượng đội thi đấu
        assertEquals(32, worldCup.getTeams().size());
    }

    // 2
    @Test
    public void testPlayGroupStageMatches() { // đấu vòng bảng

        // worldCup.playGroupStage();
        worldCup.getGroups().forEach(group -> {
            group.getTeams().forEach(team -> {
                assertTrue("Mỗi đội sẽ đấu với 3 đội còn trong cùng bảng đấu", team.getMatchesPlayed() == 3);
            });
        });
    }

    // 3
    @Test
    public void testGroupStageRanking() { // kiểm tra xếp hạng trong bảng
        // worldCup.playGroupStage();
        boolean expected = true;
        worldCup.getGroups().forEach(group -> {
            List<Team> rankedTeams = group.getRankedTeams();
            for (int i = 1; i < rankedTeams.size(); i++) {
                assertEquals(expected, rankedTeams.get(i - 1).getPoints() >= rankedTeams.get(i).getPoints());
            }
        });
    }

    // 4
    @Test
    public void testTopTwoTeamsAdvanceToKnockoutStage() { // kiểm tra hai đội xếp thứ nhất và thứ hai của mỗi bảng đấu
                                                          // được tham dự tiếp vòng 1/16.
        // worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
        assertEquals(16, worldCup.getKnockoutStageTeams().size());
    }

    // 5
    @Test
    public void testKnockoutStageMatches() { // kiểm tra chỉ được một đội thắng ở mỗi bảng

        // worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
        worldCup.playKnockoutStage();
        assertNotNull(worldCup.getChampion());
    }

    // 6
    @Test
    public void testFinalMatch() { // kiểm tra cuối trận đấu
        // worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
        worldCup.playKnockoutStage();
        // assertNotNull(worldCup.getFinalMatch());
    }

    // 7
    @Test
    public void testRedCardLeadsToPlayerEjection() { // Kiểm tra thẻ đỏ thì đuổi khỏi sân
        boolean expected = true;
        Player player = new Player("Test Player");
        player.receiveCard(Card.RED);
        assertEquals(expected, player.isEjected());
    }

    // 8
    @Test
    public void testTwoYellowCardsLeadToRedCard() { // kiểm tra cầu thủ bị 2 thẻ vàng = 1 thẻ đỏ và bị đuổi khỏi sân
        Player player = new Player("Test Player");
        player.receiveCard(Card.YELLOW);
        player.receiveCard(Card.YELLOW);
        player.receiveCard(Card.RED);
        assertTrue(player.isEjected());
    }

    // 9
    @Test
    public void testTeamWithLessThanSevenPlayersForfeits() { // Đội bóng còn dưới 7 cầu thủ trên sân
        Team team = new Team("Test Team", "CoachTeam");
        for (int i = 0; i < 5; i++) {
            Player player = new Player("Player " + i);
            player.receiveCard(Card.RED);
            team.addPlayer(player);
        }
        assertTrue(team.hasForfeited());
    }

    // 10
    @Test
    public void testPenaltyShootout() { // đá luân lưu
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match match = new Match(teamA, teamB);
        match.setDraw(true);
        match.playPenaltyShootout();
        assertNotNull(match.getWinner());
    }

    // 11
    @Test
    public void testGoldenGoalRule() { // Tên 2 đội phải khác nhau
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        assertNotEquals(teamA.name, teamB.name);
    }

    // 12
    @Test
    public void testDatabaseConnection() { // Kiểm tra kết nối database
        Database db = new Database();
        assertTrue(db.connect());
    }

    // 13
    @Test
    public void testTeamDataPersistence() {
        Database db = new Database();
        Team team = new Team("Persisted Team", "CoachTeam");
        db.saveTeam(team);
        Team loadedTeam = db.loadTeam("Persisted Team");
        assertEquals(team.getName(), loadedTeam.getName());
    }

    // 14
    @Test
    public void testMatchResultPersistence() { // Kiểm tra kết quả cuối trận đấu
        Database db = new Database();
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match match = new Match(teamA, teamB);
        match.play();
        db.saveMatchResult(match);
        Match loadedMatch = db.loadMatchResult(match.getId());
        assertEquals(match.getResult(), loadedMatch.getResult());
    }

    // 15
    @Test
    public void testNameCoachTeamNotEquals() { // Kiểm tra tên hlv của các đội không được giống nhau
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        teamA.coach = "Coach A";
        teamB.coach = "Coach B";
        assertNotEquals(teamA.coach, teamB.coach);
    }

    // 16
    @Test
    public void testRegion() { // kiểm tra khu vực
        int expected = 6;
        int actual = Region.numOfRegion;
        assertEquals(expected, actual);
    }

    // 17
    @Test
    public void test6TeamsAsia() {
        int expected = 6;
        int actual = region.asia.size();
        assertEquals(expected, actual);
    }

    // 18
    @Test
    public void test5TeamsAfrica() {
        int expected = 5;
        int actual = region.africa.size();
        assertEquals(expected, actual);
    }

    // 19
    @Test
    public void test4TeamsNorthCentralAmericaAndCaribbean() {
        int expected = 4;
        int actual = region.northCentralAmericaAndCaribbean.size();
        assertEquals(expected, actual);
    }

    // 20
    @Test
    public void test4TeamsSouthAmerica() {
        int expected = 4;
        int actual = region.southAmerica.size();
        assertEquals(expected, actual);
    }

    // 21
    @Test
    public void test1TeamsOceania() {
        int expected = 1;
        int actual = region.oceania.size();
        assertEquals(expected, actual);
    }

    // 22
    @Test
    public void test13TeamsEurope() {
        int expected = 13;
        int actual = region.europe.size();
        assertEquals(expected, actual);
    }

    // 23
    @Test
    public void testNumbersOfGroup() { // kiểm tra số lượng group
        int expected = 8;
        int actual = worldCup.groups.size();
        assertEquals(expected, actual);
    }

    // 24
    @Test
    public void testNumbersTeamsOfGroup() { // kiểm tra số lượng team mỗi group
        boolean expected = true;
        boolean isEnough = true;
        for (int i = 0; i < worldCup.groups.size(); i++) {
            if (worldCup.groups.get(i).teams.size() == 4) {
                continue;
            } else {
                isEnough = false;
                break;
            }
        }
        assertEquals(expected, isEnough);
    }

    // 25
    @Test
    public void testTop1GroupA() {
        String expected = "Team4";

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
    // 26

    @Test
    public void testTop2GroupA() {
        String expected = "Team1";

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

    // 27
    @Test
    public void testTop1GroupB() {
        String expected = "Team4";

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

    // 28
    @Test
    public void testTop2GroupB() {
        String expected = "Team3";
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

    // 29
    @Test
    public void testTop1GroupC() {
        String expected = "Team4";
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

    // 30
    @Test
    public void testTop2GroupC() {
        String expected = "Team3";
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

    // 31
    @Test
    public void testTop1GroupD() {
        String expected = "Team3";
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

    // 32
    @Test
    public void testTop2GroupD() {
        String expected = "Team4";
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

    // 33
    @Test
    public void testTop1GroupE() {
        String expected = "Team3";
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

    // 34
    @Test
    public void testTop2GroupE() {
        String expected = "Team4";
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

    // 35
    @Test
    public void testTop1GroupF() {
        String expected = "Team3";
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

    // 36
    @Test
    public void testTop2GroupF() {
        String expected = "Team2";
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

    // 37
    @Test
    public void testTop1GroupG() {
        String expected = "Team1";
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

    // 38
    @Test
    public void testTop2GroupG() {
        String expected = "Team4";
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

    // 39
    @Test
    public void testTop1GroupH() {
        String expected = "Team2";
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

    // 40
    @Test
    public void testTop2GroupH() {
        String expected = "Team4";
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

    // 41
    @Test
    public void testHaftTime() {
        int expected = 45;
        int actual = match.haftTime();
        assertEquals(expected, actual);
    }

    // 42
    @Test
    public void testHaveInjuryTime() { // xét xem có phút bù giờ không
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
        Team team = new Team("Team A", "Coach A");
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
        Team team = new Team("Team A", "Coach A");
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
        Team team = new Team("Team A", "Coach A");
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
                team.addPoints(3 * 3); // Thắng 3 trận, mỗi trận 3 điểm
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

    // 57 // Kiểm tra thông báo của trận đấu
    // @Test
    // public void testPlayMatch() {
    // String expectedMessage = "Trận đấu đã diễn ra giữa Team A và Team B trong 105
    // phút.";
    // assertEquals(expectedMessage, match.playMatch(), "Trận đấu đã diễn ra giữa
    // Team A và Team B trong 105 phút.");
    // }

    @Test
    public void testPlayMatchTeam3Team4() {
        String expectedMessage = "Trận đấu đã diễn ra giữa Team3 và Team4 trong 105 phút.";
        assertEquals(expectedMessage, match.playMatch("Team3", "Team4"),
                "Trận đấu đã diễn ra giữa Team3 và Team4 trong 105 phút.");
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

    // 90 // Kiểm tra số lượng đội vào vòng 1/16
    @Test
    public void testNumberOfTeamsInKnockoutStage() {
        List<Team> knockoutStageTeams = worldCup.getKnockoutStageTeams();
        assertEquals(16, knockoutStageTeams.size());
    }

    // 91 // Kiểm tra số lượng đội vào vòng 1/8
    @Test
    public void testQuarterFinals() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        assertEquals(4, quarterFinalWinners.size());
    }

    // 92 // Kiểm tra số lượng đội vào bán kết
    @Test
    public void testSemiFinals() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        assertEquals(2, semiFinalWinners.size());
    }

    // 93 // Kiểm tra số lượng đội vào chung kết
    // @Test
    // public void testFinal() {
    // List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
    // List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
    // worldCup.playFinal(semiFinalWinners);
    // assertEquals(2, semiFinalWinners.size());
    // assertEquals(worldCup.getChampion(), semiFinalWinners.get(0));
    // }

    // Chuyên (Vòng chung kết)
    // 91 Test khi có một đội thắng trong trận chung kết:
    @Test
    public void testPlayFinalOneWinner() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        worldCup.playFinal(semiFinalWinners);
        Team champion = worldCup.getChampion();
        assertNotNull(champion);
        assertTrue(semiFinalWinners.contains(champion));
    }

    // 92 Test khi có trận chung kết hòa và phải chơi thêm hiệp phụ:
    @Test
    public void testPlayFinalDrawWithExtraTime() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        // Kết quả hòa sau 90 phút
        Match match = new Match(semiFinalWinners.get(0), semiFinalWinners.get(1));
        match.setDraw(true);
        match.play();
        // Chơi hiệp phụ
        match.playExtraTime();
        worldCup.playFinal(semiFinalWinners);
        Team champion = worldCup.getChampion();
        assertNotNull(champion);
        assertTrue(semiFinalWinners.contains(champion));
    }

    // 93 Test khi có trận chung kết hòa và phải chơi loạt đá penalty:
    @Test
    public void testPlayFinalDrawWithPenaltyShootout() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        // Kết quả hòa sau 90 phút
        Match match = new Match(semiFinalWinners.get(0), semiFinalWinners.get(1));
        match.setDraw(true);
        match.play();
        // Chơi loạt đá penalty
        match.playPenaltyShootout();
        worldCup.playFinal(semiFinalWinners);
        Team champion = worldCup.getChampion();
        assertNotNull(champion);
        assertTrue(semiFinalWinners.contains(champion));
    }

    // 94 Test cho trường hợp giải đấu với số lượng đội là 8:
    @Test
    public void testPlayFinalWithEightTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        semiFinalWinners.add(new Team("Team E", "Coach E"));
        semiFinalWinners.add(new Team("Team F", "Coach F"));
        semiFinalWinners.add(new Team("Team G", "Coach G"));
        semiFinalWinners.add(new Team("Team H", "Coach H"));
        // Danh sách đội tham gia vòng chung kết có đúng 8 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 95 Test cho trường hợp giành chiến thắng trong trận chung kết:
    @Test
    public void testPlayFinalWithWinner() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        // Danh sách đội tham gia vòng chung kết có đúng 2 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 96 Test cho trường hợp giải đấu với số lượng đội là 2:
    @Test
    public void testPlayFinalWithTwoTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        // Danh sách đội tham gia vòng chung kết có đúng 2 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 97 Test cho trường hợp giải đấu với số lượng đội là 4:
    @Test
    public void testPlayFinalWithFourTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        // Danh sách đội tham gia vòng chung kết có đúng 4 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 98 Test cho trường hợp giải đấu với số lượng đội là 16:
    @Test
    public void testPlayFinalWithSixteenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        semiFinalWinners.add(new Team("Team E", "Coach E"));
        semiFinalWinners.add(new Team("Team F", "Coach F"));
        semiFinalWinners.add(new Team("Team G", "Coach G"));
        semiFinalWinners.add(new Team("Team H", "Coach H"));
        semiFinalWinners.add(new Team("Team I", "Coach I"));
        semiFinalWinners.add(new Team("Team J", "Coach J"));
        semiFinalWinners.add(new Team("Team K", "Coach K"));
        semiFinalWinners.add(new Team("Team L", "Coach L"));
        semiFinalWinners.add(new Team("Team M", "Coach M"));
        semiFinalWinners.add(new Team("Team N", "Coach N"));
        semiFinalWinners.add(new Team("Team O", "Coach O"));
        semiFinalWinners.add(new Team("Team P", "Coach P"));
        // Danh sách đội tham gia vòng chung kết có đúng 16 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 99 Test cho trường hợp giải đấu với số lượng đội lớn hơn 16:
    @Test
    public void testPlayFinalWithMoreThanSixteenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        // Tạo danh sách 17 đội
        for (int i = 0; i < 17; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1), "Coach " + (i + 1)));
        }
        // Danh sách đội tham gia vòng chung kết có 17 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 100 Test cho trường hợp giải đấu với số lượng đội là 3:
    @Test
    public void testPlayFinalWithThreeTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        // Danh sách đội tham gia vòng chung kết có 3 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 101 Test cho trường hợp giải đấu với số lượng đội là 5:
    @Test
    public void testPlayFinalWithFiveTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        semiFinalWinners.add(new Team("Team E", "Coach E"));
        // Danh sách đội tham gia vòng chung kết có 5 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 102 Test cho trường hợp giải đấu với số lượng đội là 6:
    @Test
    public void testPlayFinalWithSixTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        semiFinalWinners.add(new Team("Team E", "Coach E"));
        semiFinalWinners.add(new Team("Team F", "Coach F"));
        // Danh sách đội tham gia vòng chung kết có 6 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 103Test cho trường hợp giải đấu với số lượng đội là 7:
    @Test
    public void testPlayFinalWithSevenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        semiFinalWinners.add(new Team("Team E", "Coach E"));
        semiFinalWinners.add(new Team("Team F", "Coach F"));
        semiFinalWinners.add(new Team("Team G", "Coach G"));
        // Danh sách đội tham gia vòng chung kết có 7 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 104 Test cho trường hợp giải đấu với số lượng đội là 9:
    @Test
    public void testPlayFinalWithNineTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        semiFinalWinners.add(new Team("Team E", "Coach E"));
        semiFinalWinners.add(new Team("Team F", "Coach F"));
        semiFinalWinners.add(new Team("Team G", "Coach G"));
        semiFinalWinners.add(new Team("Team H", "Coach H"));
        semiFinalWinners.add(new Team("Team I", "Coach I"));
        // Danh sách đội tham gia vòng chung kết có 9 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 105 Test cho trường hợp giải đấu với số lượng đội là 10:
    @Test
    public void testPlayFinalWithTenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        semiFinalWinners.add(new Team("Team E", "Coach E"));
        semiFinalWinners.add(new Team("Team F", "Coach F"));
        semiFinalWinners.add(new Team("Team G", "Coach G"));
        semiFinalWinners.add(new Team("Team H", "Coach H"));
        semiFinalWinners.add(new Team("Team I", "Coach I"));
        semiFinalWinners.add(new Team("Team J", "Coach J"));
        // Danh sách đội tham gia vòng chung kết có 10 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 106 Test cho trường hợp giải đấu với số lượng đội là 15:
    @Test
    public void testPlayFinalWithFifteenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        semiFinalWinners.add(new Team("Team C", "Coach C"));
        semiFinalWinners.add(new Team("Team D", "Coach D"));
        semiFinalWinners.add(new Team("Team E", "Coach E"));
        semiFinalWinners.add(new Team("Team F", "Coach F"));
        semiFinalWinners.add(new Team("Team G", "Coach G"));
        semiFinalWinners.add(new Team("Team H", "Coach H"));
        semiFinalWinners.add(new Team("Team I", "Coach I"));
        semiFinalWinners.add(new Team("Team J", "Coach J"));
        semiFinalWinners.add(new Team("Team K", "Coach K"));
        semiFinalWinners.add(new Team("Team L", "Coach L"));
        semiFinalWinners.add(new Team("Team M", "Coach M"));
        semiFinalWinners.add(new Team("Team N", "Coach N"));
        // Danh sách đội tham gia vòng chung kết có 15 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 107 Test khi có lỗi xảy ra trong trận chung kết:
    @Test
    public void testPlayFinalWithException() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A", "Coach A"));
        semiFinalWinners.add(new Team("Team B", "Coach B"));
        // Khi có lỗi xảy ra trong trận chung kết
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 108
    @Test
    public void testFinalWithTwoTeamsAndSimulateMatch() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(teamA);
        semiFinalWinners.add(teamB);
        worldCup.playFinal(semiFinalWinners);
        assertTrue(teamA.getPoints() > 0 || teamB.getPoints() > 0);
    }

    // 109
    @Test
    public void testFinalWithWinnerTeamA() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        worldCup.champion = finalMatch.getWinner();
        assertEquals("Team A", worldCup.getChampion().getName());
    }

    // 110
    @Test
    public void testFinalWithWinnerTeamB() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        worldCup.champion = finalMatch.getWinner();
        assertEquals("Team B", worldCup.getChampion().getName());
    }

    // 111
    @Test
    public void testFinalMatchDuration() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        assertEquals(45, finalMatch.getFirstHalfDuration());
    }

    // 112
    @Test
    public void testFinalWithTwoTeams() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(teamA);
        semiFinalWinners.add(teamB);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 113
    @Test
    public void testFinalMatchScoreNonNegative() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        assertTrue(finalMatch.getScore1() >= 0 && finalMatch.getScore2() >= 0);
    }

    // 114
    @Test
    public void testFinalMatchResultWithExtraTime() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.playExtraTime();
        assertNotNull(finalMatch.getWinner());
    }

    // 115
    @Test
    public void testFinalMatchResultWithPenaltyShootout() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.playPenaltyShootout();
        assertNotNull(finalMatch.getWinner());
    }

    // 116
    @Test
    public void testFinalMatchResultWithoutGoals() {
        Team teamA = new Team("Team A", "Coach A");
        Team teamB = new Team("Team B", "Coach B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        assertEquals("Team A wins", finalMatch.getResult());
    }

    // 117
    @Test
    public void testChampion() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 118
    @Test
    public void testKnockoutStageTeams() {
        assertNotNull(worldCup.getKnockoutStageTeams());
    }

    // 119
    @Test
    public void testAdvanceToKnockoutStage() {
        worldCup.advanceToKnockoutStage();
        assertEquals(32, worldCup.getKnockoutStageTeams().size());
    }

    // 120
    @Test
    public void testPlaySemiFinalsReturnsWinners() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        assertNotNull(semiFinalWinners);
    }

    // 121
    @Test
    public void testPlayFinalReturnsChampion() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }
    // 91
    // 92
    // 93
    @Test
    public void testPlayRoundOf16NotNull() {
        assertNotNull(worldCup.getRoundOf16Matches());
    }

    // 94 // Kiểm tra xem tất cả các đội trong vòng 1/16 đều từ vòng bảng
    @Test
    public void testTeamsInRoundOf16FromGroupStage() {
        List<Team> roundOf16Teams = worldCup.getKnockoutStageTeams();
        List<Team> groupStageTeams = worldCup.getAllTeamsFromGroupStage();
        assertTrue("All teams in the round of 16 should be from the group stage.",
                groupStageTeams.containsAll(roundOf16Teams));
    }

    // 95// Kiểm tra xem tất cả các trận đấu trong vòng 1/16 đều có kết quả
    @Test
    public void testAllMatchesInRoundOf16HaveResult() {
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        for (Match match : roundOf16Matches) {
            assertNotNull("Each match should have a result.", match.getResult());
        }
    }

    // 96 // Số đội được chọn vào vòng 1/8 đúng bằng nửa số đội của vòng trước
    @Test
    public void testPlayRoundOf16TeamsQualified() {
        List<Team> roundOf16Teams = worldCup.getKnockoutStageTeams();
        worldCup.playRoundOf16();
        List<Team> qualifiedTeams = worldCup.getKnockoutStageTeams();
        assertEquals(roundOf16Teams.size() / 2, qualifiedTeams.size());
    }

    // 97 // Kiểm tra kết quả của từng trận đấu trong vòng 16 đội
    @Test
    public void testRoundOf16MatchResults() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getWinner());
        }
    }

    // 98 // Kiểm tra tính hợp lệ của các đội tiến vào vòng 16 đội
    @Test
    public void testRoundOf16Teams() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        List<Team> roundOf16Teams = new ArrayList<>();
        for (Match match : roundOf16Matches) {
            roundOf16Teams.add(match.getWinner());
        }
        assertEquals(8, roundOf16Teams.size());
    }

    // 99 //Kiểm tra đội bóng được giữa và chơi trong vòng 16 đội
    @Test
    public void testRoundOf16TeamsParticipation() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getTeamA());
            assertNotNull(match.getTeamB());
        }
    }

    // 100 //Kiểm tra kết quả chính xác của từng trận đấu
    @Test
    public void testRoundOf16MatchDetails() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertTrue(match.getScoreTeamA() >= 0);
            assertTrue(match.getScoreTeamB() >= 0);
        }
    }

    // 101 //Kiểm tra tính hợp lệ của kết quả cuối cùng (người chiến thắng) của từng
    // trận đấu
    @Test
    public void testRoundOf16MatchWinners() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getWinner());
        }
    }

    // 101 //Kiểm tra tính hợp lệ của việc chơi các trận đấu
    @Test
    public void testRoundOf16MatchPlay() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertTrue(match.getScoreTeamA() >= 0);
            assertTrue(match.getScoreTeamB() >= 0);
            assertNotNull(match.getWinner());
        }
    }

    // 102 //Kiểm tra tính hợp lệ của việc cập nhật kết quả vào danh sách đội
    @Test
    public void testRoundOf16TeamsUpdate() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getWinner());
            assertTrue(worldCup.getKnockoutStageTeams().contains(match.getWinner()));
        }
    }

    // 103 //Kiểm tra tính đa dạng của các đội chia vào các trận đấu
    @Test
    public void testRoundOf16TeamsDiversity() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        List<Team> allTeams = new ArrayList<>();
        for (Match match : roundOf16Matches) {
            allTeams.add(match.getTeamA());
            allTeams.add(match.getTeamB());
        }
        long distinctTeams = allTeams.stream().distinct().count();
        assertEquals(16, distinctTeams);
    }

    // 104 //Kiểm tra tỷ số cuối cùng của các trận đấu không âm
    @Test
    public void testRoundOf16ScoresNonNegative() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertTrue(match.getScoreTeamA() >= 0);
            assertTrue(match.getScoreTeamB() >= 0);
        }
    }

    // 105 //Kiểm tra tổng số trận đấu trong vòng 16 đội
    @Test
    public void testRoundOf16TotalMatches() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        assertEquals(8, roundOf16Matches.size());
    }

    // 106 //Kiểm tra không có đội nào thi đấu với chính mình
    @Test
    public void testRoundOf16NoSelfMatch() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotEquals(match.getTeamA(), match.getTeamB());
        }
    }

    // 107 //Kiểm tra tổng điểm của tất cả các trận đấu vòng 1/16
    @Test
    public void testRoundOf16TotalPoints() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        int totalPoints = 0;
        for (Match match : roundOf16Matches) {
            totalPoints += match.getScoreTeamA() + match.getScoreTeamB();
        }
        assertTrue(totalPoints >= 0);
    }

    // 108 //Kiểm tra các trận đấu đã được chơi hay chưa
    @Test
    public void testRoundOf16MatchPlayed() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertTrue(match.isPlayed());
        }
    }

    // 109 //Kiểm tra xem tất cả các đội tham gia vòng 1/16 đều là các đội đúng theo
    // kỳ vọng
    @Test
    public void testRoundOf16ParticipatingTeams() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        List<Team> roundOf16Teams = new ArrayList<>();
        for (Match match : roundOf16Matches) {
            roundOf16Teams.add(match.getTeamA());
            roundOf16Teams.add(match.getTeamB());
        }
        assertEquals(16, roundOf16Teams.size());
        assertTrue(worldCup.getAllTeamsFromGroupStage().containsAll(roundOf16Teams));
    }

    // 110 //Kiểm tra xem các trận đấu không có đội nào thi đấu trên sân nhà
    @Test
    public void testRoundOf16NoHomeAdvantage() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotEquals(match.getTeamA(), match.getTeamB());
        }
    }

    // 111

    // 112 //Kiểm tra xem tất cả các đội tham gia vòng 1/16 đều là các đội thực sự
    // tồn tại
    @Test
    public void testRoundOf16TeamsExistence() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getTeamA());
            assertNotNull(match.getTeamB());
        }
    }

    // 113 //Kiểm tra xem người thắng mỗi trận đấu đã được xác định đúng
    @Test
    public void testRoundOf16WinnersDetermined() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getWinner());
        }
    }

    // 114 //Kiểm tra kết quả cuối cùng của từng trận đấu không âm
    @Test
    public void testRoundOf16MatchResultsNonNegative() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertTrue(match.getScoreTeamA() >= 0);
            assertTrue(match.getScoreTeamB() >= 0);
        }
    }

    // 115

    // 116 //Kiểm tra xem tất cả các đội tham gia vòng 1/16 đều là các đội duy nhất
    @Test
    public void testRoundOf16UniqueTeams() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        List<Team> allTeams = new ArrayList<>();
        for (Match match : roundOf16Matches) {
            allTeams.add(match.getTeamA());
            allTeams.add(match.getTeamB());
        }
        long distinctTeams = allTeams.stream().distinct().count();
        assertEquals(16, distinctTeams);
    }

    // 117 //Kiểm tra xem không có hai trận đấu nào giống nhau
    @Test
    public void testRoundOf16UniqueMatchPairs() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        List<String> matchPairs = new ArrayList<>();
        for (Match match : roundOf16Matches) {
            String pair = match.getTeamA().getName() + " vs " + match.getTeamB().getName();
            assertFalse(matchPairs.contains(pair));
            matchPairs.add(pair);
        }
    }

    // 118 //Kiểm tra xem tất cả các đội tham gia vòng 1/16 đều là các đội có điểm
    // từ vòng bảng
    @Test
    public void testRoundOf16TeamsFromGroupStage() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        List<Team> allTeams = new ArrayList<>();
        for (Match match : roundOf16Matches) {
            allTeams.add(match.getTeamA());
            allTeams.add(match.getTeamB());
        }
        List<Team> groupStageTeams = worldCup.getAllTeamsFromGroupStage();
        assertTrue(groupStageTeams.containsAll(allTeams));
    }

    // 119 //Kiểm tra xem có đủ số trận đấu được chơi trong vòng 1/16 không
    @Test
    public void testRoundOf16MatchesPlayedCount() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        long playedMatchesCount = roundOf16Matches.stream().filter(Match::isPlayed).count();
        assertEquals(8, playedMatchesCount);
    }

    // 120 //Kiểm tra xem kết quả của từng trận đấu đã được cập nhật đúng trong danh
    // sách đội
    @Test
    public void testRoundOf16TeamResultsUpdate() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertTrue(worldCup.getKnockoutStageTeams().contains(match.getWinner())); // Đội thắng được thêm vào danh
                                                                                      // sách đội
        }
    }

    // 121 //Kiểm tra xem có đủ 8 đội thắng để tiến vào tứ kết không:
    @Test
    public void testRoundOf16WinnersCount() {
        Team teamA = new Team("Team A", "");
        Team teamB = new Team("Team B", "");
        Match match = new Match(teamA, teamB);
        match.setScore(4, 5);
        match.play();
        assertEquals(teamB, match.getWinner());
    }

    // 122
    @Test
    public void testPlayKnockoutStageDeterminesChampion() {
        worldCup.playKnockoutStage();
        assertNotNull(worldCup.getChampion());
    }
    //
    public void testRoundOf16Match1() {
        Team teamA = new Team("Team A", "");
        Team teamB = new Team("Team B", "");
        Match match = new Match(teamA, teamB);
        match.setScore(3, 0);
        match.play();
        assertEquals(teamA, match.getWinner());
    }

    // 123
    @Test
    public void testChampionInKnockoutStage() {
        worldCup.playKnockoutStage();
        assertTrue(worldCup.getKnockoutStageTeams().contains(worldCup.getChampion()));
    }
    //
    public void testRoundOf16Match2() {
        Team teamA = new Team("Team A", "");
        Team teamB = new Team("Team B", "");
        Match match = new Match(teamA, teamB);
        match.setScore(0, 3);
        match.play();
        assertEquals(teamB, match.getWinner());
    }

    // 124
    @Test
    public void testFinalMatchHasWinner() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getFinalMatch().getWinner());
    }
    //
    public void testRoundOf16Match3() {
        Team teamA = new Team("đội 1", "");
        Team teamB = new Team("đội 2", "");
        Match match = new Match(teamA, teamB);
        match.setScore(2, 1);
        match.play();
        assertEquals(teamA, match.getWinner());
    }

    // 125
    @Test
    public void testFinalMatchResult() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getFinalMatch().getResult());
    }
    //
    public void testRoundOf16Match4() {
        Team teamA = new Team("đội 3", "");
        Team teamB = new Team("đội 4", "");
        Match match = new Match(teamA, teamB);
        match.setScore(0, 3);
        match.play();
        assertEquals(teamA, match.getWinner());
    }

    // 126
    @Test
    public void testFinalMatchScore() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertTrue(worldCup.getFinalMatch().getScore1() >= 0);
        assertTrue(worldCup.getFinalMatch().getScore2() >= 0);
    }
    //
    public void testRoundOf16Match5() {
        Team teamA = new Team("đội 5", "");
        Team teamB = new Team("đội 6", "");
        Match match = new Match(teamA, teamB);
        match.setScore(3, 0);
        match.play();
        assertEquals(teamB, match.getWinner());
    }

    // 127
    @Test
    public void testFinalMatchHasDuration() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertTrue(worldCup.getFinalMatch().getTotalDuration() > 0);
    }
    //
    public void testRoundOf16Match6() {
        Team teamA = new Team("đội 7", "");
        Team teamB = new Team("đội 8", "");
        Match match = new Match(teamA, teamB);
        match.setScore(2, 3);
        match.play();
        assertEquals(teamA, match.getWinner());
    }

    // 128
    @Test
    public void testKnockoutStageTeamsNotNull() {
        assertNotNull(worldCup.getKnockoutStageTeams());
    }
    //
    public void testRoundOf16Match7() {
        Team teamA = new Team("đội 9", "");
        Team teamB = new Team("đội 10", "");
        Match match = new Match(teamA, teamB);
        match.setScore(1, 3);
        match.play();
        assertEquals(teamB, match.getWinner());
    }

    // 129
    @Test
    public void testPlayKnockoutStage() {
        worldCup.playKnockoutStage();
        assertTrue(worldCup.getKnockoutStageTeams().size() > 0);
    }

    // 130
    @Test
    public void testChampionNotNull() {
        assertNull(worldCup.getChampion());
    }

    // 131
    @Test
    public void testFinalMatchNotNull() {
        assertNull(worldCup.getFinalMatch());
    }
    //
    public void testRoundOf16Match8() {
        Team teamA = new Team("đội 11", "");
        Team teamB = new Team("đội 12", "");
        Match match = new Match(teamA, teamB);
        match.setScore(2, 1);
        match.play();
        assertEquals(teamA, match.getWinner());
    }
    // 130

    // 131

    // 132

    // 133

    // 134

    // 135

    // 136

    // 137

    // 138

    // 139

    // 140

    // 141
    // 142
    // 143
    // 144
    // 145
    // 146
    // 147
    // 148
    // 149
    // 150
    // 151
    // 152
    // 153
    // 154
    // 155
    // 156
    // 157
    // 158
    // 159
    // 160
    // 161
    // 162
    // 163
    // 164
    // 165
    // 166
    // 167
    // 168
    // 169
    // 170
    // 171
    // 172
    // 173
    // 174
    // 175
    // 176
    // 177
    // 178
    // 179
    // 180
    // 181
    // 182
    // 183
    // 184
    // 185
    // 186
    // 187
    // 188
    // 189
    // 190
    // 191
    // 192
    // 193
    // 194
    // 195
    // 196
    // 197
    // 198
    // 199
    // 200

}
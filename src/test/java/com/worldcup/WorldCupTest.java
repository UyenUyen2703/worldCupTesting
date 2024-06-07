package com.worldcup;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WorldCupTest {

    public WorldCup worldCup;
    public Region region;
    public Group group;
    public Match match;
    public Card card;
    public Player player;
    public Team teamA;
    public Team teamB;  


    @Before
    public void setUp() {
        worldCup = new WorldCup();
        worldCup.initializeTeams();
        worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
        region = new Region();
        match = new Match(null, null);
        region.addTeamsInAsia();
        region.addTeamsInAfrica();
        region.addTeamsInEurope();
        region.addTeamsInNorthCentralAmericaAndCaribbean();
        region.addTeamsInOceania();
        region.addTeamsInSouthAmerica();

    }

    // @Before
    // public void setUpRegion() {

    // }

    // 1
    @Test
    public void testInitializeTeams() { // test số lượng đội thi đấu
        assertEquals(32, worldCup.getTeams().size());
    }

    // 2
    @Test
    public void testPlayGroupStageMatches() { // đấu vòng bảng
        worldCup.playGroupStage();
        worldCup.getGroups().forEach(group -> {
            group.getTeams().forEach(team -> {
                assertTrue("Mỗi đội sẽ đấu với 3 đội còn trong cùng bảng đấu", team.getMatchesPlayed() == 3);
            });
        });
    }

    // 3
    @Test
    public void testGroupStageRanking() { // kiểm tra xếp hạng trong bảng
        worldCup.playGroupStage();
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
        worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
        assertEquals(16, worldCup.getKnockoutStageTeams().size());
    }

    // 5
    @Test
    public void testKnockoutStageMatches() { // kiểm tra chỉ được một đội thắng ở mỗi bảng

        worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();
        worldCup.playKnockoutStage();
        assertNotNull(worldCup.getChampion());
    }

    // 6
    @Test
    public void testFinalMatch() { // kiểm tra cuối trận đấu
        worldCup.playGroupStage();
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
        String expected = "Đội 4";

        Team team1 = new Team("Đội 1", "1");
        team1.points = 6;
        Team team2 = new Team("Đội 2", "2");
        team2.points = 3;
        Team team3 = new Team("Đội 3", "3");
        team3.points = 0;
        Team team4 = new Team("Đội 4", "4");
        team4.points = 9;

        group = new Group("Group A", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        // group.findTop1InGroup();
        assertEquals(expected, group.getRankedTeams().get(0).name);
    }
    // 26

    @Test
    public void testTop2GroupA() {
        String expected = "Đội 1";

        Team team1 = new Team("Đội 1", "1");
        team1.points = 6;
        Team team2 = new Team("Đội 2", "2");
        team2.points = 3;
        Team team3 = new Team("Đội 3", "3");
        team3.points = 0;
        Team team4 = new Team("Đội 4", "4");
        team4.points = 9;

        group = new Group("Group A", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        // group.findTop1InGroup();
        assertEquals(expected, group.getRankedTeams().get(1).name);
    }

    // 27
    @Test
    public void testTop1GroupB() {
        String expected = "Đội 8";

        Team team1 = new Team("Đội 5", "1");
        team1.points = 1;
        Team team2 = new Team("Đội 6", "2");
        team2.points = 4;
        Team team3 = new Team("Đội 7", "3");
        team3.points = 5;
        Team team4 = new Team("Đội 8", "4");
        team4.points = 6;

        group = new Group("Group B", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        // group.findTop1InGroup();
        assertEquals(expected, group.getRankedTeams().get(0).name);
    }

    // 28
    @Test
    public void testTop2GroupB() {
        String expected = "Đội 7";
        Team team1 = new Team("Đội 5", "1");
        team1.points = 1;
        Team team2 = new Team("Đội 6", "2");
        team2.points = 4;
        Team team3 = new Team("Đội 7", "3");
        team3.points = 5;
        Team team4 = new Team("Đội 8", "4");
        team4.points = 6;

        group = new Group("Group B", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(1).name);
    }

    // 29
    @Test
    public void testTop1GroupC() {
        String expected = "Đội 9";
        Team team1 = new Team("Đội 9", "1");
        team1.points = 7;
        Team team2 = new Team("Đội 10", "2");
        team2.points = 1;
        Team team3 = new Team("Đội 11", "3");
        team3.points = 4;
        Team team4 = new Team("Đội 12", "4");
        team4.points = 2;

        group = new Group("Group C", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(0).name);
    }

    // 30
    @Test
    public void testTop2GroupC() {
        String expected = "Đội 11";
        Team team1 = new Team("Đội 9", "1");
        team1.points = 7;
        Team team2 = new Team("Đội 10", "2");
        team2.points = 1;
        Team team3 = new Team("Đội 11", "3");
        team3.points = 4;
        Team team4 = new Team("Đội 12", "4");
        team4.points = 2;

        group = new Group("Group C", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(1).name);
    }

    // 31
    @Test
    public void testTop1GroupD() {
        String expected = "Đội 15";
        Team team1 = new Team("Đội 13", "1");
        team1.points = 1;
        Team team2 = new Team("Đội 14", "2");
        team2.points = 2;
        Team team3 = new Team("Đội 15", "3");
        team3.points = 9;
        Team team4 = new Team("Đội 16", "4");
        team4.points = 3;

        group = new Group("Group D", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(0).name);
    }

    // 32
    @Test
    public void testTop2GroupD() {
        String expected = "Đội 16";
        Team team1 = new Team("Đội 13", "1");
        team1.points = 1;
        Team team2 = new Team("Đội 14", "2");
        team2.points = 2;
        Team team3 = new Team("Đội 15", "3");
        team3.points = 9;
        Team team4 = new Team("Đội 16", "4");
        team4.points = 3;

        group = new Group("Group D", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(1).name);
    }

    // 33
    @Test
    public void testTop1GroupE() {
        String expected = "Đội 19";
        Team team1 = new Team("Đội 17", "1");
        team1.points = 1;
        Team team2 = new Team("Đội 18", "2");
        team2.points = 3;
        Team team3 = new Team("Đội 19", "3");
        team3.points = 7;
        Team team4 = new Team("Đội 20", "4");
        team4.points = 5;

        group = new Group("Group E", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(0).name);
    }

    // 34
    @Test
    public void testTop2GroupE() {
        String expected = "Đội 20";
        Team team1 = new Team("Đội 17", "1");
        team1.points = 1;
        Team team2 = new Team("Đội 18", "2");
        team2.points = 3;
        Team team3 = new Team("Đội 19", "3");
        team3.points = 7;
        Team team4 = new Team("Đội 20", "4");
        team4.points = 5;

        group = new Group("Group E", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(1).name);
    }

    // 35
    @Test
    public void testTop1GroupF() {
        String expected = "Đội 23";
        Team team1 = new Team("Đội 21", "1");
        team1.points = 3;
        Team team2 = new Team("Đội 22", "2");
        team2.points = 6;
        Team team3 = new Team("Đội 23", "3");
        team3.points = 7;
        Team team4 = new Team("Đội 24", "4");
        team4.points = 3;

        group = new Group("Group F", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(0).name);
    }

    // 36
    @Test
    public void testTop2GroupF() {
        String expected = "Đội 22";
        Team team1 = new Team("Đội 21", "1");
        team1.points = 3;
        Team team2 = new Team("Đội 22", "2");
        team2.points = 6;
        Team team3 = new Team("Đội 23", "3");
        team3.points = 7;
        Team team4 = new Team("Đội 24", "4");
        team4.points = 3;

        group = new Group("Group F", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(1).name);
    }

    // 37
    @Test
    public void testTop1GroupG() {
        String expected = "Đội 25";
        Team team1 = new Team("Đội 25", "1");
        team1.points = 6;
        Team team2 = new Team("Đội 26", "2");
        team2.points = 0;
        Team team3 = new Team("Đội 27", "3");
        team3.points = 1;
        Team team4 = new Team("Đội 28", "4");
        team4.points = 3;

        group = new Group("Group G", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(0).name);
    }

    // 38
    @Test
    public void testTop2GroupG() {
        String expected = "Đội 28";
        Team team1 = new Team("Đội 25", "1");
        team1.points = 6;
        Team team2 = new Team("Đội 26", "2");
        team2.points = 0;
        Team team3 = new Team("Đội 27", "3");
        team3.points = 1;
        Team team4 = new Team("Đội 28", "4");
        team4.points = 3;

        group = new Group("Group G", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(1).name);
    }

    // 39
    @Test
    public void testTop1GroupH() {
        String expected = "Đội 30";
        Team team1 = new Team("Đội 29", "1");
        team1.points = 4;
        Team team2 = new Team("Đội 30", "2");
        team2.points = 6;
        Team team3 = new Team("Đội 31", "3");
        team3.points = 1;
        Team team4 = new Team("Đội 32", "4");
        team4.points = 5;

        group = new Group("Group H", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(0).name);
    }

    // 40
    @Test
    public void testTop2GroupH() {
        String expected = "Đội 32";
        Team team1 = new Team("Đội 29", "1");
        team1.points = 4;
        Team team2 = new Team("Đội 30", "2");
        team2.points = 6;
        Team team3 = new Team("Đội 31", "3");
        team3.points = 1;
        Team team4 = new Team("Đội 32", "4");
        team4.points = 5;

        group = new Group("Group H", null);
        group.teams = new ArrayList<Team>();
        group.teams.add(team1);
        group.teams.add(team2);
        group.teams.add(team3);
        group.teams.add(team4);
        assertEquals(expected, group.getRankedTeams().get(1).name);
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

    //44 // Giả sử đội thứ nhất thắng 1 trận cộng đúng 3 điểm
    @Test
    public void testTeamWin() { 
        Team team = worldCup.getTeams().get(0); // Lấy đội thứ nhất
        team.addPointsForWin(); // Thắng 1 trận
        assertEquals(3, team.getPoints());
    }

    //45 // Giả sử đội thứ nhất hòa 1 trận cộng đúng 1 điểm
    @Test
    public void testTeamDraw() {
        Team team = worldCup.getTeams().get(0); // Lấy đội thứ nhất
        team.addPointsForDraw(); // Hòa 1 trận
        assertEquals(1, team.getPoints());
    }

    //46 // Giả sử đội thứ nhất thua 1 trận không cộng điểm
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
        assertEquals( "Điểm ban đầu của đội phải là 0",0, team.getPoints());

        // Giả sử đội đá 3 trận và thắng 1, thua 2
        team.updatePoints(3); // Thắng trận đầu tiên
        team.updatePoints(0); // Thua trận thứ hai
        team.updatePoints(0); // Thua trận thứ ba

        // Điểm cuối cùng sẽ là 3 điểm từ trận thắng
        assertEquals("Điểm cuối cùng của đội phải là 3",3, team.getPoints());
    }

    // 48 // Kiểm tra một đội đá 3 trận và thắng 2, thua 1
    @Test
    public void testTeamPointsAfterThreeMatch() {
        // Tạo đội và thiết lập điểm ban đầu là 0
        Team team = new Team("Team A", "Coach A");
        assertEquals( "Điểm ban đầu của đội phải là 0",0, team.getPoints());

        // Giả sử đội đá 3 trận và thắng 2, thua 1
        team.updatePoints(3); // Thắng trận đầu tiên
        team.updatePoints(3); // Thắng trận thứ hai
        team.updatePoints(0); // Thua trận thứ ba

        // Điểm cuối cùng sẽ là 6 điểm từ trận thắng
        assertEquals("Điểm cuối cùng của đội phải là 6",6, team.getPoints());
    }

    // 49 // Kiểm tra một đội đá 3 trận và thắng 1 thua 1 hòa 1
    @Test
    public void testTeamPointsAfterThreeMatch2() {
        // Tạo đội và thiết lập điểm ban đầu là 0
        Team team = new Team("Team A", "Coach A");
        assertEquals( "Điểm ban đầu của đội phải là 0",0, team.getPoints());

        // Giả sử đội đá 3 trận và thắng 1, thua 1, hòa 1
        team.updatePoints(3); // Thắng trận đầu tiên
        team.updatePoints(0); // Thua trận thứ hai
        team.updatePoints(1); // Hòa trận thứ ba

        // Điểm cuối cùng sẽ là 4 điểm từ trận thắng và hòa
        assertEquals("Điểm cuối cùng của đội phải là 4",4, team.getPoints());
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
                assertEquals( "Điểm của đội " + team.getName() + " đúng",3, team.getPoints());
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
                assertEquals( "Điểm của đội " + team.getName() + " đúng",0, team.getPoints());
            }
        }
    }

    // 53 // Kiểm tra thời gian của hiệp đầu
    @Test
    public void testFirstHalfDuration() {
        assertEquals("Thời gian của hiệp đầu không đúng",45, match.getFirstHalfDuration());
    }

    // 54 // Kiểm tra thời gian của hiệp hai
    @Test
    public void testSecondHalfDuration() {
        assertEquals("Thời gian của hiệp hai không đúng",45, match.getSecondHalfDuration());
    }

    // 55 // Kiểm tra thời gian nghỉ giữa hai hiệp
    @Test
    public void testHalfTimeBreak() {
        assertEquals("Thời gian nghỉ giữa hai hiệp không đúng",15, match.getHalfTimeBreakDuration());
    }

    // 56 // Kiểm tra tổng thời gian của trận đấu
    @Test
    public void testTotalDuration() {
        assertEquals("Tổng thời gian của trận đấu không đúng",105, match.getTotalDuration());
    }

    // 57 // Kiểm tra thông báo của trận đấu
    @Test
    public void testPlayMatch() {
        String expectedMessage = "Trận đấu đã diễn ra giữa Team A và Team B trong 105 phút.";
        assertEquals(expectedMessage, match.playMatchReturn(), "Trận đấu đã diễn ra giữa Team A và Team B trong 105 phút.");
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
        assertEquals("The number of teams in the knockout stage should be 16.",16, knockoutStageTeams.size());
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
    //     List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
    //     List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
    //     worldCup.playFinal(semiFinalWinners);
    //     assertEquals(2, semiFinalWinners.size());
    //     assertEquals(worldCup.getChampion(), semiFinalWinners.get(0));
    // }


    // 91
    // 92
    // 93
    // 94

    // 95
    // 96
    // 97
    // 98
    // 99
    // 100
}

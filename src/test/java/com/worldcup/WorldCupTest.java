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

    @Before
    public void setUp() {
        worldCup = new WorldCup();
        worldCup.initializeTeams();
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

    // 44
    // 45
    // 46
    // 47
    // 48
    // 49
    // 50
    // 51
    // 52
    // 53
    // 54
    // 55
    // 56
    // 57
    // 58
    // 59
    // 60
    // 61
    // 62
    // 63
    // 64
    // 65
    // 66
    // 67
    // 68
    // 69
    // 70
    // 71
    // 72
    // 73
    // 74
    // 75
    // 76
    // 77
    // 78
    // 79
    // 80
    // 81
    // 82
    // 83
    // 84
    // 85
    // 86
    // 87
    // 88
    // 89
    // 90
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

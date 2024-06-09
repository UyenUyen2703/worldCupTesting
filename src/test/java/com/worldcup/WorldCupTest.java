package com.worldcup;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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

        for (int i = 0; i < 8; i++) {
            worldCup.groups.add(new Group("Group " + (i + 1), null));
        }
        // worldCup.groups.add(new Group("Group A", null));
        // worldCup.groups.add(new Group("Group B", null));
        // worldCup.groups.add(new Group("Group C", null));
        // worldCup.groups.add(new Group("Group D", null));
        // worldCup.groups.add(new Group("Group E", null));
        // worldCup.groups.add(new Group("Group F", null));
        // worldCup.groups.add(new Group("Group G", null));
        // worldCup.groups.add(new Group("Group H", null));

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
            // group.playMatches();
            group.getTeams().forEach(team -> {
                assertEquals(3, team.getMatchesPlayed());
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

    // 90 // Kiểm tra số lượng đội vào vòng 1/16
    @Test
    public void testNumberOfTeamsInRoundOf16() {
        // Lấy danh sách các đội tham gia vòng 1/16
        List<Team> knockoutStageTeams = worldCup.getKnockoutStageTeams();
        // Kiểm tra số lượng đội là 16
        assertEquals("The number of teams in the knockout stage should be 16.", 16, knockoutStageTeams.size());
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
    @Test
    public void testNumberOfTeamsInFinal() {
        // Play quarter-finals
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        // Play semi-finals
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        // Check the number of teams in the final
        assertEquals("The number of teams in the Final should be 2.", 2, semiFinalWinners.size());
    }

    // 91 Test khi có một đội thắng trong trận chung kết:
    @Test
    public void testPlayFinalOneWinner() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        worldCup.playFinal(semiFinalWinners);
        Team champion = worldCup.getChampion();
        assertNotNull(champion);
        assertTrue(semiFinalWinners.contains(champion));
    }

    // 92 Test khi có trận chung kết hòa và phải chơi thêm hiệp phụ:
    @Test
    public void testPlayFinalDrawWithExtraTime() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
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
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
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
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        semiFinalWinners.add(new Team("Team E"));
        semiFinalWinners.add(new Team("Team F"));
        semiFinalWinners.add(new Team("Team G"));
        semiFinalWinners.add(new Team("Team H"));
        // Danh sách đội tham gia vòng chung kết có đúng 8 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 95 Test cho trường hợp giành chiến thắng trong trận chung kết:
    @Test
    public void testPlayFinalWithWinner() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        // Danh sách đội tham gia vòng chung kết có đúng 2 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 96 Test cho trường hợp giải đấu với số lượng đội là 2:
    @Test
    public void testPlayFinalWithTwoTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        // Danh sách đội tham gia vòng chung kết có đúng 2 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 97 Test cho trường hợp giải đấu với số lượng đội là 4:
    @Test
    public void testPlayFinalWithFourTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        // Danh sách đội tham gia vòng chung kết có đúng 4 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 98 Test cho trường hợp giải đấu với số lượng đội là 16:
    @Test
    public void testPlayFinalWithSixteenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        semiFinalWinners.add(new Team("Team E"));
        semiFinalWinners.add(new Team("Team F"));
        semiFinalWinners.add(new Team("Team G"));
        semiFinalWinners.add(new Team("Team H"));
        semiFinalWinners.add(new Team("Team I"));
        semiFinalWinners.add(new Team("Team J"));
        semiFinalWinners.add(new Team("Team K"));
        semiFinalWinners.add(new Team("Team L"));
        semiFinalWinners.add(new Team("Team M"));
        semiFinalWinners.add(new Team("Team N"));
        semiFinalWinners.add(new Team("Team O"));
        semiFinalWinners.add(new Team("Team P"));
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
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        // Danh sách đội tham gia vòng chung kết có 17 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 100 Test cho trường hợp giải đấu với số lượng đội là 3:
    @Test
    public void testPlayFinalWithThreeTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        // Danh sách đội tham gia vòng chung kết có 3 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 101 Test cho trường hợp giải đấu với số lượng đội là 5:
    @Test
    public void testPlayFinalWithFiveTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        semiFinalWinners.add(new Team("Team E"));
        // Danh sách đội tham gia vòng chung kết có 5 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 102 Test cho trường hợp giải đấu với số lượng đội là 6:
    @Test
    public void testPlayFinalWithSixTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        semiFinalWinners.add(new Team("Team E"));
        semiFinalWinners.add(new Team("Team F"));
        // Danh sách đội tham gia vòng chung kết có 6 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 103Test cho trường hợp giải đấu với số lượng đội là 7:
    @Test
    public void testPlayFinalWithSevenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        semiFinalWinners.add(new Team("Team E"));
        semiFinalWinners.add(new Team("Team F"));
        semiFinalWinners.add(new Team("Team G"));
        // Danh sách đội tham gia vòng chung kết có 7 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 104 Test cho trường hợp giải đấu với số lượng đội là 9:
    @Test
    public void testPlayFinalWithNineTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        semiFinalWinners.add(new Team("Team E"));
        semiFinalWinners.add(new Team("Team F"));
        semiFinalWinners.add(new Team("Team G"));
        semiFinalWinners.add(new Team("Team H"));
        semiFinalWinners.add(new Team("Team I"));
        // Danh sách đội tham gia vòng chung kết có 9 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 105 Test cho trường hợp giải đấu với số lượng đội là 10:
    @Test
    public void testPlayFinalWithTenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        semiFinalWinners.add(new Team("Team E"));
        semiFinalWinners.add(new Team("Team F"));
        semiFinalWinners.add(new Team("Team G"));
        semiFinalWinners.add(new Team("Team H"));
        semiFinalWinners.add(new Team("Team I"));
        semiFinalWinners.add(new Team("Team J"));
        // Danh sách đội tham gia vòng chung kết có 10 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 106 Test cho trường hợp giải đấu với số lượng đội là 15:
    @Test
    public void testPlayFinalWithFifteenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        semiFinalWinners.add(new Team("Team C"));
        semiFinalWinners.add(new Team("Team D"));
        semiFinalWinners.add(new Team("Team E"));
        semiFinalWinners.add(new Team("Team F"));
        semiFinalWinners.add(new Team("Team G"));
        semiFinalWinners.add(new Team("Team H"));
        semiFinalWinners.add(new Team("Team I"));
        semiFinalWinners.add(new Team("Team J"));
        semiFinalWinners.add(new Team("Team K"));
        semiFinalWinners.add(new Team("Team L"));
        semiFinalWinners.add(new Team("Team M"));
        semiFinalWinners.add(new Team("Team N"));
        // Danh sách đội tham gia vòng chung kết có 15 đội
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 107 Test khi có lỗi xảy ra trong trận chung kết:
    @Test
    public void testPlayFinalWithException() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(new Team("Team A"));
        semiFinalWinners.add(new Team("Team B"));
        // Khi có lỗi xảy ra trong trận chung kết
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 108
    @Test
    public void testFinalWithTwoTeamsAndSimulateMatch() {
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(teamA);
        semiFinalWinners.add(teamB);
        worldCup.playFinal(semiFinalWinners);
        assertTrue(teamA.getPoints() > 0 || teamB.getPoints() > 0);
    }

    // 109
    @Test
    public void testFinalWithWinnerTeamA() {
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        worldCup.champion = finalMatch.getWinner();
        assertEquals("Team A", worldCup.getChampion().getName());
    }

    // 110
    @Test
    public void testFinalWithWinnerTeamB() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        worldCup.champion = finalMatch.getWinner();
        assertEquals("Team 4", worldCup.getChampion().getName());
    }

    // 111
    @Test
    public void testFinalMatchDuration() {
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        assertEquals(45, finalMatch.getFirstHalfDuration());
    }

    // 112
    @Test
    public void testFinalWithTwoTeams() {
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(teamA);
        semiFinalWinners.add(teamB);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    // 113
    @Test
    public void testFinalMatchScoreNonNegative() {
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        assertTrue(finalMatch.getScore1() >= 0 && finalMatch.getScore2() >= 0);
    }

    // 114
    @Test
    public void testFinalMatchResultWithExtraTime() { // Kiểm tra kết quả trận chung kết với thời gian thi đấu thêm
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.playExtraTime();
        if (finalMatch.getScore1() > finalMatch.getScore2()) {
            finalMatch.setWinner(teamA);
        } else if (finalMatch.getScore1() < finalMatch.getScore2()) {
            finalMatch.setWinner(teamB);

        }
        assertEquals("Team 1 wins", finalMatch.getResult());
    }

    // 115
    @Test
    public void testFinalMatchResultWithPenaltyShootout() {
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.playPenaltyShootout();
        assertNotNull(finalMatch.getWinner());
    }

    // 116
    @Test
    public void testFinalMatchResultWithoutGoals() {
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
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
        assertEquals(16, worldCup.getKnockoutStageTeams().size());
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
        worldCup.playRoundOf16(); // Chơi vòng 1/16
        List<Team> qualifiedTeams = worldCup.getKnockoutStageTeams();
        assertEquals(8, qualifiedTeams.size());
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
        assertNotNull(roundOf16Matches); // Kiểm tra danh sách trận đấu không rỗng
        List<String> matchPairs = new ArrayList<>(); // Danh sách các cặp trận đấu
        for (Match match : roundOf16Matches) {
            String expected = match.getTeamA().name + " vs " + match.getTeamB().name;
            String pair = match.getTeamA().name + " vs " + match.getTeamB().name;
            assertEquals(expected, pair);
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
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match match = new Match(teamA, teamB);
        match.setScore(4, 5);
        match.play();
        assertEquals(teamB.name, match.getWinner().name);
    }

    // 122
    @Test
    public void testPlayKnockoutStageDeterminesChampion() {
        worldCup.playKnockoutStage();
        assertNotNull(worldCup.getChampion());
    }

    //
    public void testRoundOf16Match1() {
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
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
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
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
        Team teamA = new Team("đội 1");
        Team teamB = new Team("đội 2");
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
        Team teamA = new Team("đội 3");
        Team teamB = new Team("đội 4");
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
        Team teamA = new Team("đội 5");
        Team teamB = new Team("đội 6");
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
        Team teamA = new Team("đội 7");
        Team teamB = new Team("đội 8");
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
        Team teamA = new Team("đội 9");
        Team teamB = new Team("đội 10");
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
    @Test
    public void testRoundOf16Match8() {
        Team teamA = worldCup.getRoundOf16Matches().get(0).getTeamA();
        Team teamB = worldCup.getRoundOf16Matches().get(0).getTeamB();
        Match match = new Match(teamA, teamB);
        match.setScore(teamA.points, teamB.points);
        match.play();
        assertEquals(teamA, match.getWinner().name);
    }

    @Test
    public void testPlaySemiFinals() {
        // Tạo các đội tham gia vòng bán kết
        Team team1 = new Team("Team1");
        Team team2 = new Team("Team2");
        Team team3 = new Team("Team3");
        Team team4 = new Team("Team4");

        List<Team> quarterFinalWinners = new ArrayList<>();
        quarterFinalWinners.add(team1);
        quarterFinalWinners.add(team2);
        quarterFinalWinners.add(team3);
        quarterFinalWinners.add(team4);

        // Chơi bán kết
        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);

        // Kiểm tra rằng có 2 đội chiến thắng
        assertEquals(2, semiFinalWinners.size());

        // Kiểm tra rằng các đội không trùng lặp
        assertNotEquals(semiFinalWinners.get(0), semiFinalWinners.get(1));
    }

    // 151
    @Test
    public void testPlaySemiFinalsWithDuplicateWinningTeams() {
        List<Team> duplicateTeams = new ArrayList<>();
        Team team1 = new Team("Team 1");
        Team team2 = new Team("Team 2");
        duplicateTeams.add(team1);
        duplicateTeams.add(team2);
        duplicateTeams.add(team1); // Thêm đội thắng trùng lặp
        duplicateTeams.add(team2); // Thêm đội thắng trùng lặp
        List<Team> semiFinalWinners = worldCup.playSemiFinals(duplicateTeams);
        assertEquals(2, semiFinalWinners.size()); // Chỉ nên có 2 đội thắng không trùng lặp
    }

    // 152
    @Test
    public void testSemiFinalMatch1() {
        // Arrange
        Team team1 = new Team("Team 1");
        Team team2 = new Team("Team 2");
        Team team3 = new Team("Team 3");
        Team team4 = new Team("Team 4");

        // Set points for teams to simulate advancing from the quarter-finals
        team1.addPoints(3);
        team2.addPoints(3);
        team3.addPoints(3);
        team4.addPoints(3);

        List<Team> quarterFinalWinners = new ArrayList<>();
        quarterFinalWinners.add(team1);
        quarterFinalWinners.add(team2);
        quarterFinalWinners.add(team3);
        quarterFinalWinners.add(team4);

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);

        // Assert
        assertTrue(semiFinalWinners.size() == 2);
        assertTrue(semiFinalWinners.contains(team1) || semiFinalWinners.contains(team2));
        assertTrue(semiFinalWinners.contains(team3) || semiFinalWinners.contains(team4));
    }

    // 153
    @Test
    public void testSemiFinalsAllWinners() {
        // Arrange
        WorldCup worldCup = new WorldCup();
        List<Team> quarterFinalWinners = new ArrayList<>();
        Team team1 = new Team("Team 1");
        Team team2 = new Team("Team 2");
        Team team3 = new Team("Team 3");
        Team team4 = new Team("Team 4");
        quarterFinalWinners.add(team1);
        quarterFinalWinners.add(team2);
        quarterFinalWinners.add(team3);
        quarterFinalWinners.add(team4);

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 154
    @Test
    public void testPlaySemiFinalsWithRandomWinners() {

        List<Team> randomWinners = new ArrayList<>();
        Team winner1 = new Team("Winner 1");
        Team winner2 = new Team("Winner 2");
        Team winner3 = new Team("Winner 3");
        Team winner4 = new Team("Winner 4");
        randomWinners.add(winner1);
        randomWinners.add(winner2);
        randomWinners.add(winner3);
        randomWinners.add(winner4);
        List<Team> semiFinalWinners = worldCup.playSemiFinals(randomWinners);
        assertEquals(2, semiFinalWinners.size()); // Chỉ nên có hai đội thắng (vì chỉ có hai trận bán kết)
    }

    // 155
    @Test
    public void testPlaySemiFinalsWithMoreThanFourWinners() {
        // Trường hợp có nhiều hơn bốn đội thắng trong bán kết
        List<Team> moreThanFourWinners = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            moreThanFourWinners.add(new Team("Winner " + (i + 1)));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(moreThanFourWinners);
        assertEquals(2, semiFinalWinners.size()); // Chỉ nên có hai đội thắng (vì chỉ có bốn đội tham gia bán kết)
    }

    // 156
    @Test
    public void testSemiFinalMatchS1_TeamQ2WinsQ1Q2AndTeamQ4WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(1, 2); // Đội Q2 thắng đội Q1 1-2

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(3, 0); // Đội Q4 thắng đội Q3 3-0

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        // Kiểm tra xem đội thắng của trận bán kết S1 có phải là đội Q2 không
        assertTrue(semiFinalWinners.contains(teamQ2));
    }

    // 157
    @Test
    public void testPlaySemiFinalsWithAllDraws() {
        List<Team> fourDraws = new ArrayList<>();
        Team draw1 = new Team("Draw 1");
        Team draw2 = new Team("Draw 2");
        Team draw3 = new Team("Draw 3");
        Team draw4 = new Team("Draw 4");
        fourDraws.add(draw1);
        fourDraws.add(draw2);
        fourDraws.add(draw3);
        fourDraws.add(draw4);

        List<Team> semiFinalWinners = worldCup.playSemiFinals(fourDraws);
        assertEquals(2, semiFinalWinners.size()); // Chỉ nên có hai đội thắng (vì chỉ có hai trận bán kết)
    }

    // 158
    @Test
    public void testPlaySemiFinalsWithAllResults() {
        List<Team> fourTeams = new ArrayList<>();
        Team team1 = new Team("Team 1");
        Team team2 = new Team("Team 2");
        Team team3 = new Team("Team 3");
        Team team4 = new Team("Team 4");
        fourTeams.add(team1);
        fourTeams.add(team2);
        fourTeams.add(team3);
        fourTeams.add(team4);

        List<Match> semiFinalMatches = new ArrayList<>();
        semiFinalMatches.add(new Match(team1, team2));
        semiFinalMatches.add(new Match(team3, team4));

        for (Match match : semiFinalMatches) {
            match.play(); // Chơi tất cả các trận đấu
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(fourTeams);
        assertEquals(2, semiFinalWinners.size()); // Chỉ nên có hai đội thắng (vì chỉ có hai trận bán kết)
    }

    // 159
    @Test
    public void testPlaySemiFinalsWithNoWinners() {
        List<Team> fourLosers = new ArrayList<>();
        Team loser1 = new Team("Loser 1");
        Team loser2 = new Team("Loser 2");
        Team loser3 = new Team("Loser 3");
        Team loser4 = new Team("Loser 4");
        fourLosers.add(loser1);
        fourLosers.add(loser2);
        fourLosers.add(loser3);
        fourLosers.add(loser4);

        List<Team> semiFinalWinners = worldCup.playSemiFinals(fourLosers);
        assertEquals(2, semiFinalWinners.size()); // Không có đội nào thắng, vì không có trận đấu nào
    }

    // 160
    @Test
    public void testPlaySemiFinalsWithOneDrawAndOneWin() {
        List<Team> semiFinalists = new ArrayList<>();
        Team team1 = new Team("Team 1");
        Team team2 = new Team("Team 2");
        Team team3 = new Team("Team 3");
        Team team4 = new Team("Team 4");
        semiFinalists.add(team1);
        semiFinalists.add(team2);
        semiFinalists.add(team3);
        semiFinalists.add(team4);

        Match drawMatch = new Match(team1, team2);
        drawMatch.play(); // Trận đấu kết thúc hòa

        Match winMatch = new Match(team3, team4);
        winMatch.play(); // Một đội thắng trong trận này

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size()); // Chỉ nên có hai đội thắng (vì chỉ có hai trận bán kết)
    }

    // 161
    @Test
    public void testPlaySemiFinalsWithOneWinnerAndThreeDraws() {
        List<Team> semiFinalists = new ArrayList<>();
        Team winner = new Team("Winner");
        Team draw1 = new Team("Draw 1");
        Team draw2 = new Team("Draw 2");
        Team draw3 = new Team("Draw 3");
        semiFinalists.add(winner);
        semiFinalists.add(draw1);
        semiFinalists.add(draw2);
        semiFinalists.add(draw3);

        Match drawMatch1 = new Match(draw1, draw2);
        drawMatch1.play(); // Trận đấu 1 kết thúc hòa

        Match drawMatch2 = new Match(draw2, draw3);
        drawMatch2.play(); // Trận đấu 2 kết thúc hòa

        Match drawMatch3 = new Match(draw3, winner);
        drawMatch3.play(); // Trận đấu 3 đội winner thắng

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size()); // Chỉ nên có hai đội thắng (vì chỉ có hai trận bán kết)
    }

    // 162
    @Test
    public void testPlaySemiFinalsWithOneWinnerAndThreeDraws_2() {
        List<Team> semiFinalists = new ArrayList<>();
        Team winner = new Team("Winner");
        Team draw1 = new Team("Draw 1");
        Team draw2 = new Team("Draw 2");
        Team draw3 = new Team("Draw 3");
        semiFinalists.add(winner);
        semiFinalists.add(draw1);
        semiFinalists.add(draw2);
        semiFinalists.add(draw3);

        Match winMatch = new Match(winner, draw1);
        winMatch.play(); // Trận đấu winner thắng

        Match drawMatch1 = new Match(draw1, draw2);
        drawMatch1.play(); // Trận đấu 1 kết thúc hòa

        Match drawMatch2 = new Match(draw2, draw3);
        drawMatch2.play(); // Trận đấu 2 kết thúc hòa

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size()); // Chỉ nên có hai độ
    }
    // 163

    @Test
    public void testPlaySemiFinalsWithEvenNumberOfWinners() {
        // Arrange
        List<Team> fourWinners = new ArrayList<>();
        Team winner1 = new Team("Winner 1");
        Team winner2 = new Team("Winner 2");
        Team winner3 = new Team("Winner 3");
        Team winner4 = new Team("Winner 4");
        fourWinners.add(winner1);
        fourWinners.add(winner2);
        fourWinners.add(winner3);
        fourWinners.add(winner4);

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(fourWinners);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 164
    @Test
    public void testPlaySemiFinalsWithOddNumberOfWinners() {
        // Arrange
        List<Team> fiveWinners = new ArrayList<>();
        Team winner1 = new Team("Winner 1");
        Team winner2 = new Team("Winner 2");
        Team winner3 = new Team("Winner 3");
        Team winner4 = new Team("Winner 4");
        Team winner5 = new Team("Winner 5");
        fiveWinners.add(winner1);
        fiveWinners.add(winner2);
        fiveWinners.add(winner3);
        fiveWinners.add(winner4);
        fiveWinners.add(winner5);

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(fiveWinners);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 165
    @Test
    public void testSemiFinalMatchS2_TeamQ2WinsQ1Q2AndTeamQ3WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(0, 2); // Đội Q2 thắng đội Q1 0-2

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");
        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(2, 1); // Đội Q3 thắng đội Q4 2-1

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        // Kiểm tra xem đội thắng của trận bán kết S2 có phải là đội Q3 không
        assertTrue(semiFinalWinners.contains(teamQ3));
    }

    // 166
    // Test case kiểm tra kết quả của trận bán kết S1 khi đội Q4 thắng trận tứ kết
    // Q1-Q2 và đội Q1 thắng trận tứ kết Q3-Q4
    @Test
    public void testSemiFinalMatchS1_TeamQ4WinsQ1Q2AndTeamQ1WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(0, 2); // Đội Q4 thắng đội Q2 0-2

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(1, 0); // Đội Q1 thắng đội Q3 1-0

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        // Kiểm tra xem đội thắng của trận bán kết S1 có phải là đội Q4 không
        assertTrue(semiFinalWinners.contains(teamQ4));
    }

    // 167
    // Test case kiểm tra kết quả của trận bán kết S2 khi đội Q2 thắng trận tứ kết
    // Q1-Q2 và đội Q4 thắng trận tứ kết Q3-Q4
    // Test case kiểm tra kết quả của trận bán kết S1 khi đội Q2 thắng trận tứ kết
    // Q1-Q2 và đội Q3 thắng trận tứ kết Q3-Q4
    @Test
    public void testSemiFinalMatchS2_TeamQ2WinsQ1Q2AndTeamQ4WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(1, 2); // Đội Q2 thắng đội Q1 1-2

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(1, 0); // Đội Q4 thắng đội Q3 1-0

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        // Kiểm tra xem đội thắng của trận bán kết S2 có phải là đội Q4 không
        assertTrue(semiFinalWinners.contains(teamQ4));
    }
    // 168

    @Test
    public void testPlaySemiFinalsWithFourTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 169
    @Test
    public void testPlaySemiFinalsWithFiveTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));
        semiFinalists.add(new Team("Team 5"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 170
    @Test
    public void testPlaySemiFinalsWithSixTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));
        semiFinalists.add(new Team("Team 5"));
        semiFinalists.add(new Team("Team 6"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 171
    @Test
    public void testPlaySemiFinalsWithSevenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));
        semiFinalists.add(new Team("Team 5"));
        semiFinalists.add(new Team("Team 6"));
        semiFinalists.add(new Team("Team 7"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 172
    @Test
    public void testPlaySemiFinalsWithEightTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));
        semiFinalists.add(new Team("Team 5"));
        semiFinalists.add(new Team("Team 6"));
        semiFinalists.add(new Team("Team 7"));
        semiFinalists.add(new Team("Team 8"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 173
    @Test
    public void testPlaySemiFinalsWithNineTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));
        semiFinalists.add(new Team("Team 5"));
        semiFinalists.add(new Team("Team 6"));
        semiFinalists.add(new Team("Team 7"));
        semiFinalists.add(new Team("Team 8"));
        semiFinalists.add(new Team("Team 9"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 174
    @Test
    public void testPlaySemiFinalsWithTenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));
        semiFinalists.add(new Team("Team 5"));
        semiFinalists.add(new Team("Team 6"));
        semiFinalists.add(new Team("Team 7"));
        semiFinalists.add(new Team("Team 8"));
        semiFinalists.add(new Team("Team 9"));
        semiFinalists.add(new Team("Team 10"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 175
    @Test
    public void testPlaySemiFinalsWithElevenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));
        semiFinalists.add(new Team("Team 5"));
        semiFinalists.add(new Team("Team 6"));
        semiFinalists.add(new Team("Team 7"));
        semiFinalists.add(new Team("Team 8"));
        semiFinalists.add(new Team("Team 9"));
        semiFinalists.add(new Team("Team 10"));
        semiFinalists.add(new Team("Team 11"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 176
    @Test
    public void testPlaySemiFinalsWithTwelveTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));
        semiFinalists.add(new Team("Team 5"));
        semiFinalists.add(new Team("Team 6"));
        semiFinalists.add(new Team("Team 7"));
        semiFinalists.add(new Team("Team 8"));
        semiFinalists.add(new Team("Team 9"));
        semiFinalists.add(new Team("Team 10"));
        semiFinalists.add(new Team("Team 11"));
        semiFinalists.add(new Team("Team 12"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 177
    @Test
    public void testPlaySemiFinalsWithThirteenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 178
    @Test
    public void testPlaySemiFinalsWithFourteenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 14; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }
    // 179

    @Test
    public void testPlaySemiFinalsWithFifteenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }
    // 180

    @Test
    public void testPlaySemiFinalsWithSixteenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }
    // 181

    @Test
    public void testPlaySemiFinalsWithSeventeenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 17; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 181
    public void testPlaySemiFinalsWithEighteenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 182
    @Test
    public void testPlaySemiFinalsWithNineteenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 19; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }
    // 183

    @Test
    public void testPlaySemiFinalsWithTwentyTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 184
    @Test
    public void testPlaySemiFinalsWithTwentyOneTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 21; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 184
    @Test
    public void testPlaySemiFinalsWithTwentyTwoTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 22; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    // 185

    @Test
    public void testPlaySemiFinalsWithTwentyThreeTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 23; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 186
    // Test case for playing semi-finals with twenty-four teams
    @Test
    public void testPlaySemiFinalsWithTwentyFourTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 187
    // Test case for playing semi-finals with twenty-five teams
    @Test
    public void testPlaySemiFinalsWithTwentyFiveTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 188
    // Test case for playing semi-finals with twenty-six teams
    @Test
    public void testPlaySemiFinalsWithTwentySixTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 26; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }// 189
     // Test case for playing semi-finals with twenty-seven teams

    @Test
    public void testPlaySemiFinalsWithTwentySevenTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 27; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 190
    // Test case for playing semi-finals with twenty-eight teams
    @Test
    public void testPlaySemiFinalsWithTwentyEightTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 28; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 191
    // Test case for playing semi-finals with twenty-nine teams
    @Test
    public void testPlaySemiFinalsWithTwentyNineTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 29; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 192
    // Test case for playing semi-finals with thirty teams
    @Test
    public void testPlaySemiFinalsWithThirtyTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 193
    // Test case for playing semi-finals with thirty-one teams
    @Test
    public void testPlaySemiFinalsWithThirtyOneTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 194
    // Test case for playing semi-finals with thirty-two teams
    @Test
    public void testPlaySemiFinalsWithThirtyTwoTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
    }

    // 195

    // Test case for playing semi-finals with four teams
    @Test
    public void testSemiFinalsWithFourTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team A"));
        semiFinalists.add(new Team("Team B"));
        semiFinalists.add(new Team("Team C"));
        semiFinalists.add(new Team("Team D"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
        assertNotNull(semiFinalWinners.get(0));
        assertNotNull(semiFinalWinners.get(1));
        assertNotEquals(semiFinalWinners.get(0), semiFinalWinners.get(1));
    }

    // 196
    // Test case for playing semi-finals with eight teams
    @Test
    public void testSemiFinalsWithEightTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team A"));
        semiFinalists.add(new Team("Team B"));
        semiFinalists.add(new Team("Team C"));
        semiFinalists.add(new Team("Team D"));
        semiFinalists.add(new Team("Team E"));
        semiFinalists.add(new Team("Team F"));
        semiFinalists.add(new Team("Team G"));
        semiFinalists.add(new Team("Team H"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
        assertNotNull(semiFinalWinners.get(0));
        assertNotNull(semiFinalWinners.get(1));
        assertNotEquals(semiFinalWinners.get(0), semiFinalWinners.get(1));
    }

    // 197
    // Test case for playing semi-finals with odd number of teams
    @Test
    public void testSemiFinalsWithOddNumberOfTeams() {
        // Arrange
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team A"));
        semiFinalists.add(new Team("Team B"));
        semiFinalists.add(new Team("Team C"));
        semiFinalists.add(new Team("Team D"));
        semiFinalists.add(new Team("Team E"));
        semiFinalists.add(new Team("Team F"));
        semiFinalists.add(new Team("Team G"));

        // Act
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        // Assert
        assertEquals(2, semiFinalWinners.size());
        assertNotNull(semiFinalWinners.get(0));
        assertNotNull(semiFinalWinners.get(1));
        assertNotEquals(semiFinalWinners.get(0), semiFinalWinners.get(1));
    }

    // 198
    @Test
    public void testSemiFinalMatchS1() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(2, 1); // Đội Q1 thắng đội Q2 2-1

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(3, 0); // Đội Q3 thắng đội Q4 3-0

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        // Kiểm tra xem đội thắng của trận bán kết S1 có phải là đội Q1 không
        assertTrue(semiFinalWinners.contains(teamQ1));
    }

    // 199

    // Test case kiểm tra kết quả của trận bán kết S1 khi cả hai trận tứ kết đều hòa
    // và phải xác định đội thắng bằng loạt sút luân lưu, nhưng không có đội nào
    // thắng trong loạt sút luân lưu
    @Test
    public void testSemiFinalMatchS1_DrawAndNoWinnerInPenaltyShootout() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(1, 1); // Đội Q1 hòa đội Q2 1-1

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(2, 2); // Đội Q3 hòa đội Q4 2-2

        // Không thiết lập đội thắng trong loạt sút luân lưu cho bất kỳ trận tứ kết nào

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        // Không có đội nào thắng trong trận bán kết S1
        assertEquals(2, semiFinalWinners.size());
    }

    // 200

    @Test
    public void testSemiFinalMatchS1_TeamQ2WinsQ1Q2AndTeamQ3WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(1, 2); // Đội Q2 thắng đội Q1 1-2

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(2, 1); // Đội Q3 thắng đội Q4 2-1

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        // Kiểm tra xem đội thắng của trận bán kết S1 có phải là đội Q2 không
        assertTrue(semiFinalWinners.contains(teamQ2));
    }

}
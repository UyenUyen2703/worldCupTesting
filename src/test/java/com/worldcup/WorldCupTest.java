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
    public void testInitializeTeams() {
        assertEquals(32, worldCup.getTeams().size());
    }

    @Test
    public void testPlayGroupStageMatches() {
        worldCup.getGroups().forEach(group -> {
            // group.playMatches();
            group.getTeams().forEach(team -> {
                assertEquals(3, team.getMatchesPlayed());
            });
        });
    }

    @Test
    public void testGroupStageRanking() {
        boolean expected = true;
        worldCup.getGroups().forEach(group -> {
            List<Team> rankedTeams = group.getRankedTeams();
            for (int i = 1; i < rankedTeams.size(); i++) {
                assertEquals(expected, rankedTeams.get(i - 1).getPoints() >= rankedTeams.get(i).getPoints());
            }
        });
    }

    @Test
    public void testKnockoutStageMatches() {
        worldCup.advanceToKnockoutStage();
        worldCup.playKnockoutStage();
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testFinalMatch() {
        worldCup.advanceToKnockoutStage();
        worldCup.playKnockoutStage();
        assertNotNull(worldCup.getFinalMatch());
    }

    @Test
    public void testNumberOfTeamsInRoundOf16() {
        List<Team> knockoutStageTeams = worldCup.getKnockoutStageTeams();
        assertEquals("The number of teams in the knockout stage should be 16.", 16, knockoutStageTeams.size());
    }

    @Test
    public void testQuarterFinals() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        assertEquals(4, quarterFinalWinners.size());
    }

    @Test
    public void testSemiFinals() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testNumberOfTeamsInFinal() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        assertEquals("The number of teams in the Final should be 2.", 2, semiFinalWinners.size());
    }

    @Test
    public void testPlayFinalOneWinner() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(worldCup.groups.get(0).getTeams().get(0));
        semiFinalWinners.add(worldCup.groups.get(1).getTeams().get(1));
        worldCup.playFinal(semiFinalWinners);
        Team champion = worldCup.getChampion();
        assertNotNull(champion);
        assertTrue(semiFinalWinners.contains(champion));
    }

    @Test
    public void testPlayFinalDrawWithExtraTime() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        Match match = new Match(semiFinalWinners.get(0), semiFinalWinners.get(1));
        match.setDraw(true);
        match.play();
        match.playExtraTime();
        worldCup.playFinal(semiFinalWinners);
        Team champion = worldCup.getChampion();
        assertNotNull(champion);
        assertTrue(semiFinalWinners.contains(champion));
    }

    @Test
    public void testPlayFinalDrawWithPenaltyShootout() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        Match match = new Match(semiFinalWinners.get(0), semiFinalWinners.get(1));
        match.setDraw(true);
        match.play();
        match.playPenaltyShootout();
        worldCup.playFinal(semiFinalWinners);
        Team champion = worldCup.getChampion();
        assertNotNull(champion);
        assertTrue(semiFinalWinners.contains(champion));
    }

    @Test
    public void testPlayFinalWithEightTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithWinner() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithTwoTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithFourTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithSixteenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithMoreThanSixteenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithThreeTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithFiveTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithSixTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithSevenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithNineTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithTenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithFifteenTeams() {
        List<Team> semiFinalWinners = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            semiFinalWinners.add(new Team("Team " + (i + 1)));
        }
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayFinalWithException() {
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(worldCup.groups.get(0).getTeams().get(0));
        semiFinalWinners.add(worldCup.groups.get(1).getTeams().get(1));
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testFinalWithTwoTeamsAndSimulateMatch() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        ;
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(teamA);
        semiFinalWinners.add(teamB);
        worldCup.playFinal(semiFinalWinners);
        assertTrue(teamA.getPoints() > 0 || teamB.getPoints() > 0);
    }

    @Test
    public void testFinalWithWinnerTeamA() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        worldCup.champion = finalMatch.getWinner();
        assertTrue(worldCup.getChampion().getName().equals(teamA.name)
                || worldCup.getChampion().getName().equals(teamB.name));
    }

    @Test
    public void testFinalWithWinnerTeamB() {
        Team teamA = worldCup.groups.get(1).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        worldCup.champion = finalMatch.getWinner();
        assertTrue(worldCup.getChampion().getName().equals(teamA.name)
                || worldCup.getChampion().getName().equals(teamB.name));
    }

    @Test
    public void testFinalMatchDuration() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        assertEquals(45, finalMatch.getFirstHalfDuration());
    }

    @Test
    public void testFinalWithTwoTeams() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        ;
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        List<Team> semiFinalWinners = new ArrayList<>();
        semiFinalWinners.add(teamA);
        semiFinalWinners.add(teamB);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testFinalMatchScoreNonNegative() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        ;
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        assertTrue(finalMatch.getScore1() >= 0 && finalMatch.getScore2() >= 0);
    }

    @Test
    public void testFinalMatchResultWithExtraTime() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.playExtraTime();
        if (finalMatch.getScore1() > finalMatch.getScore2()) {
            finalMatch.setWinner(teamA);
        } else if (finalMatch.getScore1() < finalMatch.getScore2()) {
            finalMatch.setWinner(teamB);

        }
        assertEquals("Draw", finalMatch.getResult());
    }

    @Test
    public void testFinalMatchResultWithPenaltyShootout() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        ;
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.playPenaltyShootout();
        assertNotNull(finalMatch.getWinner());
    }

    @Test
    public void testFinalMatchResultWithoutGoals() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match finalMatch = new Match(teamA, teamB);
        finalMatch.play();
        assertEquals("Team 2 wins", finalMatch.getResult());
    }

    @Test
    public void testChampion() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testKnockoutStageTeams() {
        assertNotNull(worldCup.getKnockoutStageTeams());
    }

    @Test
    public void testPlaySemiFinalsReturnsWinners() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        assertNotNull(semiFinalWinners);
    }

    @Test
    public void testPlayFinalReturnsChampion() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testPlayRoundOf16NotNull() {
        assertNotNull(worldCup.getRoundOf16Matches());
    }

    @Test
    public void testTeamsInRoundOf16FromGroupStage() {
        List<Team> roundOf16Teams = worldCup.getKnockoutStageTeams();
        List<Team> groupStageTeams = worldCup.getAllTeamsFromGroupStage();
        assertTrue("All teams in the round of 16 should be from the group stage.",
                groupStageTeams.containsAll(roundOf16Teams));
    }

    @Test
    public void testAllMatchesInRoundOf16HaveResult() {
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        for (Match match : roundOf16Matches) {
            assertNotNull("Each match should have a result.", match.getResult());
        }
    }

    @Test
    public void testRoundOf16MatchResults() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getWinner());
        }
    }

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

    @Test
    public void testRoundOf16MatchWinners() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getWinner());
        }
    }

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

    @Test
    public void testRoundOf16TotalMatches() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        assertEquals(8, roundOf16Matches.size());
    }

    @Test
    public void testRoundOf16NoSelfMatch() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotEquals(match.getTeamA(), match.getTeamB());
        }
    }

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

    @Test
    public void testRoundOf16MatchPlayed() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertTrue(match.isPlayed());
        }
    }

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

    @Test
    public void testRoundOf16NoHomeAdvantage() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotEquals(match.getTeamA(), match.getTeamB());
        }
    }

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

    @Test
    public void testRoundOf16WinnersDetermined() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertNotNull(match.getWinner());
        }
    }

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

    @Test
    public void testRoundOf16UniqueMatchPairs() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        List<String> matchPairs = new ArrayList<>();
        for (Match match : roundOf16Matches) {
            String expected = match.getTeamA().name + " vs " + match.getTeamB().name;
            String pair = match.getTeamA().name + " vs " + match.getTeamB().name;
            assertEquals(expected, pair);
            matchPairs.add(pair);
        }
    }

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

    @Test
    public void testRoundOf16MatchesPlayedCount() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        long playedMatchesCount = roundOf16Matches.stream().filter(Match::isPlayed).count();
        assertEquals(8, playedMatchesCount);
    }

    @Test
    public void testRoundOf16TeamResultsUpdate() {
        worldCup.playRoundOf16();
        List<Match> roundOf16Matches = worldCup.getRoundOf16Matches();
        assertNotNull(roundOf16Matches);
        for (Match match : roundOf16Matches) {
            assertTrue(worldCup.getKnockoutStageTeams().contains(match.getWinner()));
        }
    }

    @Test
    public void testRoundOf16WinnersCount() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match match = new Match(teamA, teamB);
        match.setScore(4, 5);
        match.play();
        assertTrue(match.getWinner().name.equals(teamA.name) || match.getWinner().name.equals(teamB.name));
    }

    @Test
    public void testPlayKnockoutStageDeterminesChampion() {
        worldCup.playKnockoutStage();
        assertNotNull(worldCup.getChampion());
    }

    @Test
    public void testRoundOf16Match1() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match match = new Match(teamA, teamB);
        match.scoreTeamA = 3;
        match.scoreTeamB = 0;
        match.play();
        assertTrue(match.getWinner().name.equals(teamA.name));
    }

    @Test
    public void testChampionInKnockoutStage() {
        worldCup.playKnockoutStage();
        assertTrue(worldCup.getKnockoutStageTeams().contains(worldCup.getChampion()));
    }

    @Test
    public void testRoundOf16Match2() {
        Team teamA = worldCup.groups.get(0).getTeams().get(0);
        Team teamB = worldCup.groups.get(1).getTeams().get(1);
        Match match = new Match(teamA, teamB);
        match.scoreTeamA = 0;
        match.scoreTeamB = 3;
        match.play();
        assertTrue(match.getWinner().name.equals(teamB.name));
    }

    @Test
    public void testFinalMatchHasWinner() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getFinalMatch().getWinner());
    }

    public void testRoundOf16Match3() {
        Team teamA = worldCup.getRoundOf16Matches().get(0).getTeamA();
        Team teamB = worldCup.getRoundOf16Matches().get(0).getTeamB();
        Match match = new Match(teamA, teamB);
        match.setScore(2, 1);
        match.play();
        assertEquals(teamA, match.getWinner());
    }

    @Test
    public void testFinalMatchResult() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertNotNull(worldCup.getFinalMatch().getResult());
    }

    public void testRoundOf16Match4() {
        Team teamA = worldCup.getRoundOf16Matches().get(2).getTeamA();
        Team teamB = worldCup.getRoundOf16Matches().get(3).getTeamB();
        Match match = new Match(teamA, teamB);
        match.setScore(0, 3);
        match.play();
        assertEquals(teamA, match.getWinner());
    }

    @Test
    public void testFinalMatchScore() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertTrue(worldCup.getFinalMatch().getScore1() >= 0);
        assertTrue(worldCup.getFinalMatch().getScore2() >= 0);
    }

    public void testRoundOf16Match5() {
        Team teamA = new Team("đội 5");
        Team teamB = new Team("đội 6");
        Match match = new Match(teamA, teamB);
        match.setScore(3, 0);
        match.play();
        assertEquals(teamB, match.getWinner());
    }

    @Test
    public void testFinalMatchHasDuration() {
        List<Team> quarterFinalWinners = worldCup.playQuarterFinals();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);
        worldCup.playFinal(semiFinalWinners);
        assertTrue(worldCup.getFinalMatch().getTotalDuration() > 0);
    }

    public void testRoundOf16Match6() {
        Team teamA = new Team("đội 7");
        Team teamB = new Team("đội 8");
        Match match = new Match(teamA, teamB);
        match.setScore(2, 3);
        match.play();
        assertEquals(teamA, match.getWinner());
    }

    @Test
    public void testKnockoutStageTeamsNotNull() {
        assertNotNull(worldCup.getKnockoutStageTeams());
    }

    public void testRoundOf16Match7() {
        Team teamA = new Team("đội 9");
        Team teamB = new Team("đội 10");
        Match match = new Match(teamA, teamB);
        match.setScore(1, 3);
        match.play();
        assertEquals(teamB, match.getWinner());
    }

    @Test
    public void testPlayKnockoutStage() {
        worldCup.playKnockoutStage();
        assertTrue(worldCup.getKnockoutStageTeams().size() > 0);
    }

    @Test
    public void testChampionNotNull() {
        assertNull(worldCup.getChampion());
    }

    @Test
    public void testFinalMatchNotNull() {
        assertNull(worldCup.getFinalMatch());
    }

    @Test
    public void testRoundOf16Match8() {
        Team teamA = worldCup.getRoundOf16Matches().get(0).getTeamA();
        Team teamB = worldCup.getRoundOf16Matches().get(0).getTeamB();
        Match match = new Match(teamA, teamB);
        match.setScore(teamA.points, teamB.points);
        match.play();
        assertEquals(teamA.name, match.getWinner().name);
    }

    @Test
    public void testPlaySemiFinals() {
        Team team1 = new Team("Team1");
        Team team2 = new Team("Team2");
        Team team3 = new Team("Team3");
        Team team4 = new Team("Team4");

        List<Team> quarterFinalWinners = new ArrayList<>();
        quarterFinalWinners.add(team1);
        quarterFinalWinners.add(team2);
        quarterFinalWinners.add(team3);
        quarterFinalWinners.add(team4);

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);

        assertEquals(2, semiFinalWinners.size());

        assertNotEquals(semiFinalWinners.get(0), semiFinalWinners.get(1));
    }

    @Test
    public void testPlaySemiFinalsWithDuplicateWinningTeams() {
        List<Team> duplicateTeams = new ArrayList<>();
        Team team1 = new Team("Team 1");
        Team team2 = new Team("Team 2");
        duplicateTeams.add(team1);
        duplicateTeams.add(team2);
        // duplicateTeams.add(team1);
        List<Team> semiFinalWinners = worldCup.playSemiFinals(duplicateTeams);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testSemiFinalMatch1() {

        Team team1 = new Team("Team 1");
        Team team2 = new Team("Team 2");
        Team team3 = new Team("Team 3");
        Team team4 = new Team("Team 4");

        team1.addPoints(3);
        team2.addPoints(3);
        team3.addPoints(3);
        team4.addPoints(3);

        List<Team> quarterFinalWinners = new ArrayList<>();
        quarterFinalWinners.add(team1);
        quarterFinalWinners.add(team2);
        quarterFinalWinners.add(team3);
        quarterFinalWinners.add(team4);

        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);

        assertTrue(semiFinalWinners.size() == 2);
        assertTrue(semiFinalWinners.contains(team1) || semiFinalWinners.contains(team2));
        assertTrue(semiFinalWinners.contains(team3) || semiFinalWinners.contains(team4));
    }

    @Test
    public void testSemiFinalsAllWinners() {

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

        List<Team> semiFinalWinners = worldCup.playSemiFinals(quarterFinalWinners);

        assertEquals(2, semiFinalWinners.size());
    }

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
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithMoreThanFourWinners() {
        List<Team> moreThanFourWinners = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            moreThanFourWinners.add(new Team("Winner " + (i + 1)));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(moreThanFourWinners);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testSemiFinalMatchS1_TeamQ2WinsQ1Q2AndTeamQ4WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(1, 2);

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(3, 0);

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        assertTrue(semiFinalWinners.contains(teamQ2) || semiFinalWinners.contains(teamQ2));
    }

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
        assertEquals(2, semiFinalWinners.size());
    }

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
            match.play();
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(fourTeams);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithNoWinners() {
        List<Team> fourLosers = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            fourLosers.add(new Team("Loser " + i));
        }
        fourLosers.add(fourLosers.get(0));
        fourLosers.add(fourLosers.get(1));
        fourLosers.add(fourLosers.get(2));
        fourLosers.add(fourLosers.get(3));

        List<Team> semiFinalWinners = worldCup.playSemiFinals(fourLosers);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithOneDrawAndOneWin() {
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            semiFinalists.add(new Team("Team " + i));
        }
        semiFinalists.add(semiFinalists.get(0));
        semiFinalists.add(semiFinalists.get(1));
        semiFinalists.add(semiFinalists.get(2));
        semiFinalists.add(semiFinalists.get(3));

        Match drawMatch = new Match(semiFinalists.get(0), semiFinalists.get(1));
        drawMatch.play();

        Match winMatch = new Match(semiFinalists.get(2), semiFinalists.get(3));
        winMatch.play();

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithOneWinnerAndThreeDraws() {
        List<Team> semiFinalists = new ArrayList<>();
        Team winner = new Team("Winner");
        semiFinalists.add(winner);
        for (int i = 1; i <= 3; i++) {
            semiFinalists.add(new Team("Draw " + i));
        }
        semiFinalists.add(semiFinalists.get(0));
        semiFinalists.add(semiFinalists.get(1));
        semiFinalists.add(semiFinalists.get(2));

        Match drawMatch1 = new Match(semiFinalists.get(0), semiFinalists.get(1));
        drawMatch1.play();
        Match drawMatch2 = new Match(semiFinalists.get(1), semiFinalists.get(2));
        drawMatch2.play();
        Match drawMatch3 = new Match(semiFinalists.get(2), winner);
        drawMatch3.play();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithOneWinnerAndThreeDraws_2() {
        List<Team> semiFinalists = new ArrayList<>();
        Team winner = new Team("Winner");
        for (int i = 1; i <= 3; i++) {
            semiFinalists.add(new Team("Draw " + i));
        }
        semiFinalists.add(winner);
        semiFinalists.add(semiFinalists.get(0));
        semiFinalists.add(semiFinalists.get(1));
        semiFinalists.add(semiFinalists.get(2));

        Match winMatch = new Match(winner, semiFinalists.get(0));
        winMatch.play();

        Match drawMatch1 = new Match(semiFinalists.get(0), semiFinalists.get(1));
        drawMatch1.play();

        Match drawMatch2 = new Match(semiFinalists.get(1), semiFinalists.get(2));
        drawMatch2.play();

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithEvenNumberOfWinners() {

        List<Team> fourWinners = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            fourWinners.add(new Team("Winner " + i));
        }
        fourWinners.add(fourWinners.get(0));
        fourWinners.add(fourWinners.get(1));
        fourWinners.add(fourWinners.get(2));
        fourWinners.add(fourWinners.get(3));

        List<Team> semiFinalWinners = worldCup.playSemiFinals(fourWinners);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithOddNumberOfWinners() {

        List<Team> fiveWinners = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            fiveWinners.add(new Team("Winner " + i));
        }
        fiveWinners.add(fiveWinners.get(0));
        fiveWinners.add(fiveWinners.get(1));
        fiveWinners.add(fiveWinners.get(2));
        fiveWinners.add(fiveWinners.get(3));
        fiveWinners.add(fiveWinners.get(4));

        List<Team> semiFinalWinners = worldCup.playSemiFinals(fiveWinners);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testSemiFinalMatchS2_TeamQ2WinsQ1Q2AndTeamQ3WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(0, 2);

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");
        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(2, 1);

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        assertTrue(semiFinalWinners.contains(teamQ2) || semiFinalWinners.contains(teamQ3));
    }

    @Test
    public void testSemiFinalMatchS1_TeamQ4WinsQ1Q2AndTeamQ1WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(0, 2);

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(1, 0);
        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        assertTrue(semiFinalWinners.contains(teamQ4));
    }

    @Test
    public void testSemiFinalMatchS2_TeamQ2WinsQ1Q2AndTeamQ4WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(1, 2);

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(1, 0);

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        assertTrue(semiFinalWinners.contains(teamQ4));
    }

    @Test
    public void testPlaySemiFinalsWithFourTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(new Team("Team 1"));
        semiFinalists.add(new Team("Team 2"));
        semiFinalists.add(new Team("Team 3"));
        semiFinalists.add(new Team("Team 4"));

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithFiveTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithSixTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithSevenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithEightTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithNineTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithElevenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwelveTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithThirteenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithFourteenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 14; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithFifteenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithSixteenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithSeventeenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 17; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    public void testPlaySemiFinalsWithEighteenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithNineteenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 19; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentyTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentyOneTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 21; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentyTwoTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 22; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentyThreeTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 23; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentyFourTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentyFiveTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentySixTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 26; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentySevenTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 27; i++) {
            semiFinalists.add(new Team("Team " + i));
        }

        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);

        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentyEightTeams() {
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 28; i++) {
            semiFinalists.add(new Team("Team " + i));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithTwentyNineTeams() {
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 29; i++) {
            semiFinalists.add(new Team("Team " + i));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithThirtyTeams() {
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            semiFinalists.add(new Team("Team " + i));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
    }

    public void testPlaySemiFinalsWithThirtyOneTeams() {
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            semiFinalists.add(new Team("Team " + i));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testPlaySemiFinalsWithThirtyTwoTeams() {
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            semiFinalists.add(new Team("Team " + i));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testSemiFinalsWithFourTeams() {
        List<Team> semiFinalists = new ArrayList<>();
        semiFinalists.add(worldCup.groups.get(0).getTeams().get(0));
        semiFinalists.add(worldCup.groups.get(1).getTeams().get(1));
        semiFinalists.add(new Team("Team C"));
        semiFinalists.add(new Team("Team D"));
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
        assertNotNull(semiFinalWinners.get(0));
        assertNotNull(semiFinalWinners.get(1));
        assertNotEquals(semiFinalWinners.get(0), semiFinalWinners.get(1));
    }

    @Test
    public void testSemiFinalsWithEightTeams() {
        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            semiFinalists.add(new Team("Team " + i));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
        assertNotNull(semiFinalWinners.get(0));
        assertNotNull(semiFinalWinners.get(1));
        assertNotEquals(semiFinalWinners.get(0), semiFinalWinners.get(1));
    }

    @Test
    public void testSemiFinalsWithOddNumberOfTeams() {

        List<Team> semiFinalists = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            semiFinalists.add(new Team("Team " + i));
        }
        List<Team> semiFinalWinners = worldCup.playSemiFinals(semiFinalists);
        assertEquals(2, semiFinalWinners.size());
        assertNotNull(semiFinalWinners.get(0));
        assertNotNull(semiFinalWinners.get(1));
        assertNotEquals(semiFinalWinners.get(0), semiFinalWinners.get(1));
    }

    @Test
    public void testSemiFinalMatchS1() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(2, 1);
        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(3, 0);

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        assertTrue(semiFinalWinners.contains(teamQ1));
    }

    @Test
    public void testSemiFinalMatchS1_DrawAndNoWinnerInPenaltyShootout() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(1, 1);

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(2, 2);

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));
        assertEquals(2, semiFinalWinners.size());
    }

    @Test
    public void testSemiFinalMatchS1_TeamQ2WinsQ1Q2AndTeamQ3WinsQ3Q4() {
        Team teamQ1 = new Team("Team Q1");
        Team teamQ2 = new Team("Team Q2");

        Match matchQ1 = new Match(teamQ1, teamQ2);
        matchQ1.setScore(1, 2);

        Team teamQ3 = new Team("Team Q3");
        Team teamQ4 = new Team("Team Q4");

        Match matchQ2 = new Match(teamQ3, teamQ4);
        matchQ2.setScore(2, 1);

        WorldCup worldCup = new WorldCup();
        List<Team> semiFinalWinners = worldCup.playSemiFinals(Arrays.asList(teamQ1, teamQ2, teamQ3, teamQ4));

        assertTrue(semiFinalWinners.contains(teamQ2));
    }

}
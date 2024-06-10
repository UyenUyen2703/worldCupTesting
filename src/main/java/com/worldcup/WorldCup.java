package com.worldcup;

import java.util.ArrayList;
import java.util.List;

public class WorldCup {
    public List<Team> teams;
    public List<Group> groups;
    public List<Team> knockoutStageTeams;
    public Team champion;
    public Match finalMatch;
    public List<Region> region;
    public List<Match> roundOf16Matches;

    public WorldCup() {
        this.teams = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.knockoutStageTeams = new ArrayList<>();
        this.region = new ArrayList<>();
        this.roundOf16Matches = new ArrayList<>();
    }

    public void initializeTeams() {
        for (int i = 0; i < 32; i++) {
            teams.add(new Team("Team " + (i + 1)));
        }
        // // Chia đội thành 8 bảng
        // for (int i = 0; i < 8; i++) {
        // groups.add(new Group("Group " + (char) ('A' + i), teams.subList(i * 4, (i +
        // 1) * 4)));
        // Group.numTeamsOfGroup++;
        // }
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Team> getKnockoutStageTeams() {
        return knockoutStageTeams;
    }

    public void playGroupStage() {
        for (Group group : groups) {
            group.playMatches();
        }
    }

    public void advanceToKnockoutStage() {
        for (Group group : groups) {
            List<Team> rankedTeams = group.getRankedTeams();
            knockoutStageTeams.add(rankedTeams.get(0));
            knockoutStageTeams.add(rankedTeams.get(1));
        }
    }

    // chơi vòng loại 1/16
    public void playRoundOf16() {
        List<Team> roundOf16Teams = getKnockoutStageTeams();
        for (int i = 0; i < roundOf16Teams.size(); i += 2) {
            Team team1 = roundOf16Teams.get(i);
            Team team2 = roundOf16Teams.get(i + 1);
            Match match = new Match(team1, team2);
            match.play();
            roundOf16Matches.add(match);
        }
    }

    public List<Match> getRoundOf16Matches() {
        return roundOf16Matches;
    }

    public List<Team> getAllTeamsFromGroupStage() {
        List<Team> allGroupStageTeams = new ArrayList<>();
        for (Group group : groups) {
            allGroupStageTeams.addAll(group.getTeams());
        }
        return allGroupStageTeams;
    }

    // Chơi tứ kết
    public List<Team> playQuarterFinals() {
        List<Team> quarterFinalWinners = new ArrayList<>();
        for (int i = 0; i < 8; i += 2) { // i+=2 để chọn ra 2 đội đầu tiên trong danh sách các đội tham gia tứ kết
            Match match = new Match(knockoutStageTeams.get(i), knockoutStageTeams.get(i + 1));
            match.play(); // Chơi trận đấu
            quarterFinalWinners.add(match.getWinner());
        }
        return quarterFinalWinners;
    }

    // Chơi bán kết
    public List<Team> playSemiFinals(List<Team> quarterFinalWinners) {
        List<Team> semiFinalWinners = new ArrayList<>();

        Match semiFinal1 = new Match(quarterFinalWinners.get(0), quarterFinalWinners.get(1));
        semiFinal1.play();
        semiFinalWinners.add(semiFinal1.getWinner());
        Match semiFinal2 = new Match(quarterFinalWinners.get(2), quarterFinalWinners.get(3));
        semiFinal2.play();
        semiFinalWinners.add(semiFinal2.getWinner());

        return semiFinalWinners;
    }

    // Choi chung ket
    public void playFinal(List<Team> semiFinalWinners) {
        finalMatch = new Match(semiFinalWinners.get(0), semiFinalWinners.get(1));
        finalMatch.play();
        champion = finalMatch.getWinner();
    }

    public void playKnockoutStage() {
        List<Team> currentStageTeams = new ArrayList<>(knockoutStageTeams);
        while (currentStageTeams.size() > 1) {
            List<Team> nextStageTeams = new ArrayList<Team>();
            for (int i = 0; i < currentStageTeams.size(); i += 2) {
                Match match = new Match(currentStageTeams.get(i), currentStageTeams.get(i + 1));
                match.play();
                nextStageTeams.add(match.getWinner());
            }
            currentStageTeams = nextStageTeams;
        }
        champion = currentStageTeams.get(0);
    }

    public Team getChampion() {
        return champion;
    }

    public Match getFinalMatch() {
        return finalMatch;
    }

}

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

    public WorldCup() {
        this.teams = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.knockoutStageTeams = new ArrayList<>();
        this.region = new ArrayList<>();
    }

    public void initializeTeams() {
        for (int i = 0; i < 32; i++) {
            teams.add(new Team("Team " + (i + 1), "Coach" + (i + 1)));
        }
        // Chia đội thành 8 bảng
        for (int i = 0; i < 8; i++) {
            groups.add(new Group("Group " + (char) ('A' + i), teams.subList(i * 4, (i + 1) * 4)));
            Group.numTeamsOfGroup++;
        }
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
            knockoutStageTeams.add(rankedTeams.get(0)); // đội đứng đầu
            knockoutStageTeams.add(rankedTeams.get(1)); // đội đứng thứ hai
        }
    }

    public void playKnockoutStage() {
        // Mô phỏng các trận đấu vòng loại trực tiếp
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
        // finalMatch = new Match(currentStageTeams.get(0), currentStageTeams.get(1));
    }

    public Team getChampion() {
        return champion;
    }

    public Match getFinalMatch() {
        return finalMatch;
    }

    public static void main(String[] args) {
        // Team team1 = new Team("Đội 1", "1");
        // team1.points = 6;
        // Team team2 = new Team("Đội 2", "2");
        // team2.points = 3;
        // Team team3 = new Team("Đội 3", "3");
        // team3.points = 0;
        // Team team4 = new Team("Đội 4", "4");
        // team4.points = 9;

        // ArrayList<Team> teams = new ArrayList<>();
        // teams.add(team1);
        // teams.add(team2);
        // teams.add(team3);
        // teams.add(team4);
        // Group group = new Group("Group A",teams);

        // System.out.println(group.getRankedTeams().get(0).name);

        // group.printTeamsinGroup();
        WorldCup wc = new WorldCup();
        Region region = new Region();
        region.addTeamsInAsia();
        region.addTeamsInAfrica();
        region.addTeamsInEurope();
        region.addTeamsInNorthCentralAmericaAndCaribbean();
        region.addTeamsInOceania();
        region.addTeamsInSouthAmerica();
        wc.region.add(region);

        // Playoff matches
        Team asiaPlayoffWinner = region.playAsiaVsNorthCentralAmericaPlayoff();
        System.out.println("Winner of Asia vs North Central America playoff: " + asiaPlayoffWinner.getName());

        Team southAmericaPlayoffWinner = region.playSouthAmericaVsOceaniaPlayoff();
        System.out.println("Winner of South America vs Oceania playoff: " + southAmericaPlayoffWinner.getName());

        List<Group> groups = region.distributeTeamsIntoGroups();

        for (Group group : groups) {
            System.out.println(group.getName() + ":");
            group.printTeamsinGroup();
            System.out.println();
        }
        // System.out.println(wc.region);
        // wc.initializeTeams();.

        // for (int i = 0; i < wc.teams.size(); i++) {
        // System.out.println(wc.teams.get(i).name);
        // System.out.println(wc.teams.get(i).coach);
        // }
        // for (int i = 0; i < wc.groups.size(); i++) {
        // System.out.println(wc.groups.get(i).name);
        // }
        // for (int i = 0; i < wc.groups.size(); i++) {
        // System.out.println(wc.groups.get(i).name);

        // for (int j = 0; j < wc.groups.get(i).teams.size(); j++) {
        // System.out.println(wc.groups.get(i).teams.get(j).name);
        // }
        // // System.out.println(wc.groups.get(i).name);
        // }

        // for (int i = 0; i < wc.groups.size(); i++) {
        // wc.groups.get(i).playMatches();

        // for(int j =0; j<wc.groups.get(i).teams.size();j++){
        // wc.groups.get(i).teams.get(j)
        // }
        // }
        // System.out.println(wc.teams.get(0).name);
        // System.out.println(wc.teams.get(0).points);
        // System.out.println(wc.teams.get(0).coach);
        // System.out.println(wc.teams.get(0).matchesPlayed);

        // System.out.println(wc.teams.get(1).name);
        // System.out.println(wc.teams.get(1).points);
        // System.out.println(wc.teams.get(1).coach);
        // System.out.println(wc.teams.get(1).matchesPlayed);
        // System.out.println(wc.teams.get(0).name);
        // for (int i = 0; i < wc.region.size(); i++) {
        // // Region region_ = wc.region.get(i);
        // for (int j = 0; j < wc.region.get(i).asia.size(); j++) {
        // System.out.println(wc.region.get(i).asia.get(j).name);
        // }
        // }
    }
}

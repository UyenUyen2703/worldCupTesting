package com.worldcup;

import java.util.Comparator;
import java.util.List;

public class Group {
    public String name;
    public static int numTeamsOfGroup = 0;
    public List<Team> teams;

    public Group(String name, List<Team> teams) {
        this.name = name;
        this.teams = teams;
    }

    public String getName() {
        return name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void playMatches() {
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Match match = new Match(teams.get(i), teams.get(j));
                match.play();
            }
        }
    }

    public List<Team> getRankedTeams() {
        teams.sort(Comparator.comparing(Team::getPoints).reversed());
        return teams;
    }

    public void printTeamsinGroup() {
        for (int i = 0; i < this.teams.size(); i++) {
            System.out.println(this.teams.get(i).name);
        }
    }
}

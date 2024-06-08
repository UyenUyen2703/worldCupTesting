package com.worldcup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Region {
    public static int numOfRegion = 6;
    public ArrayList<Team> asia;
    public ArrayList<Team> africa;
    public ArrayList<Team> northCentralAmericaAndCaribbean;
    public ArrayList<Team> southAmerica;
    public ArrayList<Team> oceania;
    public ArrayList<Team> europe;
    public Team hostTeam;

    public Region() {
        this.asia = new ArrayList<>();
        this.africa = new ArrayList<>();
        this.northCentralAmericaAndCaribbean = new ArrayList<>();
        this.southAmerica = new ArrayList<>();
        this.oceania = new ArrayList<>();
        this.europe = new ArrayList<>();
        this.hostTeam = new Team("Quatar - Host Team");
    }

    public ArrayList<Team> getAsia() {
        return asia;
    }

    public ArrayList<Team> getAfrica() {
        return africa;
    }

    public ArrayList<Team> getNorthCentralAmericaAndCaribbean() {
        return northCentralAmericaAndCaribbean;
    }

    public ArrayList<Team> getSouthAmerica() {
        return southAmerica;
    }

    public ArrayList<Team> getOceania() {
        return oceania;
    }

    public ArrayList<Team> getEurope() {
        return europe;
    }

    public Team getHostTeam() {
        return hostTeam;
    }

    public void addTeamsInAsia() {
        this.asia.add(new Team("Sourth Korea"));
        this.asia.add(new Team("Saudi Arabia"));
        this.asia.add(new Team("Japan"));
        this.asia.add(new Team("Iran"));
        this.asia.add(new Team("Australia"));
        this.asia.add(new Team("VietNam"));
    }

    public void addTeamsInAfrica() {
        this.africa.add(new Team("Cameroon"));
        this.africa.add(new Team("Ghana"));
        this.africa.add(new Team("Maroc"));
        this.africa.add(new Team("Senegal"));
        this.africa.add(new Team("Tunisia"));
    }

    public void addTeamsInNorthCentralAmericaAndCaribbean() {
        this.northCentralAmericaAndCaribbean.add(new Team("Canada"));
        this.northCentralAmericaAndCaribbean.add(new Team("Costa Rica"));
        this.northCentralAmericaAndCaribbean.add(new Team("Mexico"));
        this.northCentralAmericaAndCaribbean.add(new Team("USA"));
    }

    public void addTeamsInSouthAmerica() {
        this.southAmerica.add(new Team("Argentina"));
        this.southAmerica.add(new Team("Brasil"));
        this.southAmerica.add(new Team("Ecuador"));
        this.southAmerica.add(new Team("Uruguay"));
    }

    public void addTeamsInOceania() {
        this.oceania.add(new Team("New Zealand"));
    }

    public void addTeamsInEurope() {
        this.europe.add(new Team("Belgium"));
        this.europe.add(new Team("Croatia"));
        this.europe.add(new Team("Denmark"));
        this.europe.add(new Team("England"));
        this.europe.add(new Team("France"));
        this.europe.add(new Team("Gremany"));
        this.europe.add(new Team("Netherlands"));

        this.europe.add(new Team("Poland"));
        this.europe.add(new Team("Croatia"));
        this.europe.add(new Team("Portugal"));
        this.europe.add(new Team("Serbia"));
        this.europe.add(new Team("Spain"));
        this.europe.add(new Team("Switzerland "));
    }

    public List<Group> distributeTeamsIntoGroups() {
        List<Team> allTeams = new ArrayList<>();
        allTeams.addAll(asia);
        allTeams.addAll(africa);
        allTeams.addAll(northCentralAmericaAndCaribbean);
        allTeams.addAll(southAmerica);
        allTeams.addAll(oceania);
        allTeams.addAll(europe);
        allTeams.add(hostTeam); // Add the host team

        Collections.shuffle(allTeams);

        List<Group> groups = new ArrayList<>();
        int numGroups = 8;
        for (int i = 0; i < numGroups; i++) {
            groups.add(new Group("Group " + (char) ('A' + i), new ArrayList<>()));
        }

        for (int i = 0; i < allTeams.size(); i++) {
            groups.get(i % numGroups).getTeams().add(allTeams.get(i));
        }

        return groups;
    }

    public Team playAsiaVsNorthCentralAmericaPlayoff() {
        Team asiaTeam = asia.get(5); // 6th team in Asia (index 5)
        Team northCentralAmericaTeam = northCentralAmericaAndCaribbean.get(3); // 4th team in North Central America
                                                                               // (index 3)

        // Play two matches
        Match match1 = new Match(asiaTeam, northCentralAmericaTeam);
        match1.play();
        Match match2 = new Match(northCentralAmericaTeam, asiaTeam);
        match2.play();

        // Determine the winner based on aggregate score
        int aggregateAsia = match1.getScore1() + match2.getScore2();
        int aggregateNorthCentralAmerica = match1.getScore2() + match2.getScore1();

        Team winner;
        if (aggregateAsia > aggregateNorthCentralAmerica) {
            winner = asiaTeam;
            northCentralAmericaAndCaribbean.remove(northCentralAmericaTeam);
        } else if (aggregateAsia < aggregateNorthCentralAmerica) {
            winner = northCentralAmericaTeam;
            asia.remove(asiaTeam);
        } else {
            // In case of a tie, choose the winner randomly or use other rules like away
            // goals
            winner = Math.random() > 0.5 ? asiaTeam : northCentralAmericaTeam;
            if (winner == asiaTeam) {
                northCentralAmericaAndCaribbean.remove(northCentralAmericaTeam);
            } else {
                asia.remove(asiaTeam);
            }
        }
        return winner;
    }

    public Team playSouthAmericaVsOceaniaPlayoff() {
        Team southAmericaTeam = southAmerica.get(3); // 4th team in South America (index 3)
        Team oceaniaTeam = oceania.get(0); // 1st team in Oceania (index 0)

        // Play two matches
        Match match1 = new Match(southAmericaTeam, oceaniaTeam);
        match1.play();
        Match match2 = new Match(oceaniaTeam, southAmericaTeam);
        match2.play();

        // Determine the winner based on aggregate score
        int aggregateSouthAmerica = match1.getScore1() + match2.getScore2();
        int aggregateOceania = match1.getScore2() + match2.getScore1();

        Team winner;
        if (aggregateSouthAmerica > aggregateOceania) {
            winner = southAmericaTeam;
            oceania.remove(oceaniaTeam);
        } else if (aggregateSouthAmerica < aggregateOceania) {
            winner = oceaniaTeam;
            southAmerica.remove(southAmericaTeam);
        } else {
            // In case of a tie, choose the winner randomly or use other rules like away
            // goals
            winner = Math.random() > 0.5 ? southAmericaTeam : oceaniaTeam;
            if (winner == southAmericaTeam) {
                oceania.remove(oceaniaTeam);
            } else {
                southAmerica.remove(southAmericaTeam);
            }
        }
        return winner;
    }
}
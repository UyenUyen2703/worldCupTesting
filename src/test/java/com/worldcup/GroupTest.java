package com.worldcup;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class GroupTest {
    public Region region;
    public Match match;
    public WorldCup worldCup;

    @Before
    public void setUp() {
        worldCup = new WorldCup();
        worldCup.initializeTeams();
        worldCup.groups.add(new Group("Group A", null));
        worldCup.groups.add(new Group("Group B", null));
        worldCup.groups.add(new Group("Group C", null));
        worldCup.groups.add(new Group("Group D", null));
        worldCup.groups.add(new Group("Group E", null));
        worldCup.groups.add(new Group("Group F", null));
        worldCup.groups.add(new Group("Group G", null));
        worldCup.groups.add(new Group("Group H", null));
        region = new Region();
        match = new Match(null, null);
        region.addTeamsInAsia();
        region.addTeamsInAfrica();
        region.addTeamsInEurope();
        region.addTeamsInNorthCentralAmericaAndCaribbean();
        region.addTeamsInOceania();
        region.addTeamsInSouthAmerica();

        int i = 0;

        for (int j = 0; j < worldCup.groups.size(); j++) {
            worldCup.groups.get(j).teams = new ArrayList<Team>();
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 1)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 2)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 3)));
            worldCup.groups.get(j).teams.add(new Team("Team " + (i + 4)));
        }
    }

}

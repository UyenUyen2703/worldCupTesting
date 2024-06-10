package com.worldcup;

import static org.junit.Assert.*;

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
        for (int i = 0; i < 8; i++) {
            worldCup.groups.add(new Group("Group " + (i + 1), null));
        }
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

    @Test
    public void testGroup() {
        assertEquals(8, worldCup.groups.size());
    }

    @Test
    public void testNumbersTeamsOfGroup() {
        boolean isEnough = true;
        for (int i = 0; i < worldCup.groups.size(); i++) {
            if (worldCup.groups.get(i).teams.size() == 4) {
                continue;
            } else {
                isEnough = false;
                break;
            }
        }
        assertEquals(true, isEnough);
    }

    @Test
    public void testPlayMatches() {
        worldCup.playGroupStage();
        for (int i = 0; i < worldCup.groups.size(); i++) {
            worldCup.groups.get(i).playMatches();
        }
        assertEquals(6, worldCup.groups.get(0).teams.get(0).getMatchesPlayed());
    }
}

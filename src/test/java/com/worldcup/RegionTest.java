package com.worldcup;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RegionTest {
    public WorldCup worldCup;
    public Region region;

    @Before
    public void setUp() {
        worldCup = new WorldCup();
        worldCup.initializeTeams();
        // worldCup.playGroupStage();
        worldCup.advanceToKnockoutStage();

        region = new Region();
        region.addTeamsInAsia();
        region.addTeamsInAfrica();
        region.addTeamsInEurope();
        region.addTeamsInNorthCentralAmericaAndCaribbean();
        region.addTeamsInOceania();
        region.addTeamsInSouthAmerica();
    }

    @Test
    public void testRegion() { // kiểm tra khu vực
        int expected = 6;
        int actual = Region.numOfRegion;
        assertEquals(expected, actual);
    }

    @Test
    public void test6TeamsAsia() {
        int expected = 6;
        int actual = region.asia.size();
        assertEquals(expected, actual);
    }

    @Test
    public void test5TeamsAfrica() {
        int expected = 5;
        int actual = region.africa.size();
        assertEquals(expected, actual);
    }

    @Test
    public void test4TeamsNorthCentralAmericaAndCaribbean() {
        int expected = 4;
        int actual = region.northCentralAmericaAndCaribbean.size();
        assertEquals(expected, actual);
    }

    @Test
    public void test4TeamsSouthAmerica() {
        int expected = 4;
        int actual = region.southAmerica.size();
        assertEquals(expected, actual);
    }

    @Test
    public void test1TeamsOceania() {
        int expected = 1;
        int actual = region.oceania.size();
        assertEquals(expected, actual);
    }

    @Test
    public void test13TeamsEurope() {
        int expected = 13;
        int actual = region.europe.size();
        assertEquals(expected, actual);
    }
}

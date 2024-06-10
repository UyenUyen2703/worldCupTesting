package com.worldcup;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public String name;
    public List<Player> players;
    public int points;
    public int matchesPlayed;
    public int coach;
    public int supportCoach;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.points = 0;
        this.matchesPlayed = 0;
        this.coach = 0;
        this.supportCoach = 0;
    }

    public int getCoach() {
        return coach;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void incrementMatchesPlayed() {
        this.matchesPlayed++;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addSupportCoach() {
        if (supportCoach >= 3) {
            throw new IllegalStateException("Cannot add more than 3 support coaches");
        }
        supportCoach++;
    }

    public boolean hasForfeited() {
        return players.stream().filter(p -> !p.isEjected()).count() < 7; // filter(p -> !p.isEjected()).count() < 7;: để
                                                                         // lọc ra những cầu thủ chưa bị đuổi khỏi sân.
                                                                         // stream: để tạo 1 dòng trong dữ liệu
                                                                         // count(): để đếm số lượng cầu thủ chưa bị
                                                                         // đuổi khỏi sân.
    }

    public void addPointsForWin() {
        this.points += 3;
    }

    public void addPointsForDraw() {
        points += 1;
    }

    public void addPointsForLoss() {
        points += 0;
    }

    public void updatePoints(int matchResult) {
        if (matchResult == 3) {
            points += 3;
        } else if (matchResult == 1) {
            points += 1;
        }
    }

}

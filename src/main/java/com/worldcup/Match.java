package com.worldcup;

import java.util.Random;
import java.util.UUID;

public class Match {
    public Team teamA;
    public Team teamB;
    public boolean draw;
    public Team winner;
    public UUID id;
    public int scoreTeamA;
    public int scoreTeamB;
    public int haftTime = 45;
    public boolean injuryTime;
    public boolean extraTime;
    public int totalDuration;
    public int secondHalfDuration;
    public int halfTimeBreak;

    public Match(Team teamA, Team teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.id = UUID.randomUUID();
        this.scoreTeamA = 0;
        this.scoreTeamB = 0;
        this.extraTime = true;
        this.haftTime = 45;
        this.secondHalfDuration = 45;
        this.halfTimeBreak = 15;
        this.totalDuration = haftTime + secondHalfDuration + halfTimeBreak;
    }

    public void play() {
        // Mô phỏng một trận đấu, quyết định người thắng ngẫu nhiên cho đơn giản
        if (new Random().nextBoolean()) {
            teamA.addPoints(3);
            winner = teamA;
        } else {
            teamB.addPoints(3);
            winner = teamB;
        }
        teamA.incrementMatchesPlayed();
        teamB.incrementMatchesPlayed();
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public boolean isDraw() {
        return draw;
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public int getScoreTeamA() {
        return scoreTeamA;
    }

    public int getScoreTeamB() {
        return scoreTeamB;
    }

    public boolean isPlayed() {
        return winner != null;
    }

    public Team getWinner() {
        return winner;
    }

    public Team getLoser() {
        if (winner != null) {
            return (winner == teamA) ? teamB : teamA;
        }
        return null;
    }

    public UUID getId() {
        return id;
    }

    public String getResult() {
        return winner != null ? winner.getName() + " wins" : "Draw";
    }

    public void playPenaltyShootout() {
        // Mô phỏng đá luân lưu
        this.play();
    }

    public int getScore1() {
        return scoreTeamA;
    }

    public int getScore2() {
        return scoreTeamB;
    }

    public int haftTime() {
        int haftTime1 = 45;
        return haftTime1;
    }

    public boolean injuryTime() {
        boolean injuryTime = true;
        return injuryTime;
    }

    public int getFirstHalfDuration() {
        return 45;
    }

    public int getSecondHalfDuration() {
        return 45;
    }

    public int getHalfTimeBreakDuration() {
        return halfTimeBreak;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public String playMatch(String TeamA, String TeamB) { // Mô phỏng trận đấu
        return "Trận đấu đã diễn ra giữa " + TeamA + " và " + TeamB + " trong " + totalDuration + " phút.";
    }

    public void setScore(int scoreTeamA, int scoreTeamB) {
        this.scoreTeamA = scoreTeamA;
        this.scoreTeamB = scoreTeamB;
    }

    public String playMatchReturn() { // Mô phỏng trận đấu
        return "Trận đấu đã diễn ra giữa " + teamA.getName() + " và " + teamB.getName() + " trong " + totalDuration
                + " phút.";
    }

    public void playExtraTime() {
        if (draw) {
            this.play();
        } else {
            System.out.println("Trận đấu không hòa, không cần đá hiệp phụ.");
        }
    }

    public Team setWinner(Team teamA2) {
        return teamA2;
    }

}

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
    public int haftTime; // hiệp 1
    public boolean injuryTime; // bù giờ
    public boolean extraTime = true; // hiệp phụ
    public int totalDuration; // tổng thời gian
    public int secondHalfDuration; // hiệp 2
    public int halfTimeBreak; // thời gian nghỉ giữa hiệp

    public Match(Team teamA, Team teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.id = UUID.randomUUID();
        this.scoreTeamA = 0;
        this.scoreTeamB = 0;
        this.haftTime = 45; // 45 phút cho hiệp đầu
        this.secondHalfDuration = 45; // 45 phút cho hiệp hai
        this.halfTimeBreak = 15; // 15 phút nghỉ giải lao
        this.totalDuration = haftTime + secondHalfDuration + halfTimeBreak; // Tổng thời gian 90 phút
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
        teamA.incrementMatchesPlayed(); // incrementMatchesPlayed() để tăng số trận đã chơi của teamA teamB lên 1.
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
        return winner != null ? winner.getName()+ " wins" : "Draw";
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
        return 45; // 45 phút cho hiệp đầu
    }

    public int getSecondHalfDuration() {
        return 45; // 45 phút cho hiệp hai
    }

    public int getHalfTimeBreakDuration() {
        return halfTimeBreak; // 15 phút nghỉ giải lao
    }

    public int getTotalDuration() {
        return totalDuration; // Tổng thời gian 90 phút
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

    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }
}

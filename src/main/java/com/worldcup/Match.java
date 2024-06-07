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
    public boolean extraTime = false; // hiệp phụ
    public int totalDuration;   // tổng thời gian
    public int secondHalfDuration;      // hiệp 2
    public int halfTimeBreak;       // thời gian nghỉ giữa hiệp


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

    public Team getWinner() {
        return winner;
    }

    public UUID getId() {
        return id;
    }

    public String getResult() {
        return winner != null ? winner.getName() + " wins" : "Draw";
    }

    public void playExtraTime() {
        // Mô phỏng thêm giờ với luật bàn thắng vàng
        this.play();
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

    public String playMatch() { // Mô phỏng trận đấu
        return "Trận đấu đã diễn ra giữa " + teamA.getName() + " và " + teamB.getName() + " trong " + totalDuration + " phút.";
    }
}

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

    public Match(Team teamA, Team teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.id = UUID.randomUUID();
        this.scoreTeamA = 0;
        this.scoreTeamB = 0;
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
        teamA.incrementMatchesPlayed(); // incrementMatchesPlayed() để tăng số trận đã chơi của teamA teamBlên 1.
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
}

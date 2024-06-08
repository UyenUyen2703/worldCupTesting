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
        this.points = 0; // 9
        this.matchesPlayed = 3; // 3
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
        this.points += points; // tính điểm
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void incrementMatchesPlayed() {
        this.matchesPlayed++;
    }

    public void addPlayer(Player player) { // Thêm cầu thủ
        players.add(player);
    }

    public void addSupportCoach(Team supportCoach) {
        supportCoach.addSupportCoach(supportCoach);
    }

    public boolean hasForfeited() { // số lượng cầu thủ không đc dưới 7
        return players.stream().filter(p -> !p.isEjected()).count() < 7; // filter(p -> !p.isEjected()).count() < 7;: để
                                                                         // lọc ra những cầu thủ chưa bị đuổi khỏi sân.
                                                                         // stream: để tạo 1 dòng trong dữ liệu
                                                                         // count(): để đếm số lượng cầu thủ chưa bị
                                                                         // đuổi khỏi sân.
    }

    public void addPointsForWin() {
        points += 3;
    }

    public void addPointsForDraw() {
        points += 1;
    }

    public void addPointsForLoss() {
        // Không cần thêm điểm nếu thua
        points += 0;
    }

    // Phương thức cập nhật điểm số của đội sau mỗi trận đấu
    public void updatePoints(int matchResult) {
        // Cập nhật điểm số dựa trên kết quả của trận đấu
        if (matchResult == 3) {
            points += 3; // Thắng
        } else if (matchResult == 1) {
            points += 1; // Hòa
        }
        // Điểm không thay đổi nếu thua
    }

}

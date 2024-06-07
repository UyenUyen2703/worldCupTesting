package com.worldcup;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public String name;
    public List<Player> players;
    public int points;
    public int matchesPlayed;
    public String coach;
    public int supportCoach;
    public int goalDifference;

    public Team(String name, String coach) {
        this.name = name;
        this.players = new ArrayList<>();
        this.points = 0; // 9
        this.matchesPlayed = 0; // 3
        this.coach = coach;
        this.supportCoach = 0;
        this.goalDifference = 0;
    }

    public String getCoach() {
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
    public void updateGoalDifference(int difference) { //Hiệu số bàn thắng bại
        this.goalDifference += difference;
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

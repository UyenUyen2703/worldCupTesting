package com.worldcup;

import java.util.ArrayList;
import java.util.List;

public class WorldCup {
    public List<Team> teams;
    public List<Group> groups;
    public List<Team> knockoutStageTeams;
    public Team champion;
    public Match finalMatch;
    public List<Region> region;
    public List<Match> roundOf16Matches;

    public WorldCup() {
        this.teams = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.knockoutStageTeams = new ArrayList<>();
        this.region = new ArrayList<>();
        this.roundOf16Matches = new ArrayList<>();
    }

    public void initializeTeams() {
        for (int i = 0; i < 32; i++) {
            teams.add(new Team("Team " + (i + 1)));
        }
        // // Chia đội thành 8 bảng
        // for (int i = 0; i < 8; i++) {
        // groups.add(new Group("Group " + (char) ('A' + i), teams.subList(i * 4, (i +
        // 1) * 4)));
        // Group.numTeamsOfGroup++;
        // }
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Team> getKnockoutStageTeams() {
        return knockoutStageTeams;
    }

    public void playGroupStage() {
        for (Group group : groups) {
            group.playMatches();
        }
    }

    public void advanceToKnockoutStage() { 
        for (Group group : groups) {
            List<Team> rankedTeams = group.getRankedTeams();
            knockoutStageTeams.add(rankedTeams.get(0)); // đội đứng đầu
            knockoutStageTeams.add(rankedTeams.get(1)); // đội đứng thứ hai
        }
    }

    // chơi vòng loại 1/16
    public void playRoundOf16() {
        List<Team> roundOf16Teams = getKnockoutStageTeams(); // Lấy danh sách đội tham gia vòng 1/16
    
        // Mô phỏng các trận đấu trong vòng 1/16
        for (int i = 0; i < roundOf16Teams.size(); i += 2) {
            Team team1 = roundOf16Teams.get(i);
            Team team2 = roundOf16Teams.get(i + 1);
            Match match = new Match(team1, team2);
            match.play(); // Chơi trận đấu
            roundOf16Matches.add(match); // Thêm trận đấu vào danh sách các trận đấu vòng 1/16
        }
    }
    
    public List<Match> getRoundOf16Matches() {
        return roundOf16Matches;
    }

    public List<Team> getAllTeamsFromGroupStage() {
        List<Team> allGroupStageTeams = new ArrayList<>();
        for (Group group : groups) {
            allGroupStageTeams.addAll(group.getTeams());
        }
        return allGroupStageTeams;
    }
    
    // Chơi tứ kết
    public List<Team> playQuarterFinals() {
        List<Team> quarterFinalWinners = new ArrayList<>(); // Danh sách các đội thắng ở tứ kết
        for (int i = 0; i < 8; i += 2) { // i+=2 để chọn ra 2 đội đầu tiên trong danh sách các đội tham gia tứ kết
            Match match = new Match(knockoutStageTeams.get(i), knockoutStageTeams.get(i + 1)); // Trận đấu giữa đội 1 và
                                                                                               // đội 2
            match.play(); // Chơi trận đấu
            quarterFinalWinners.add(match.getWinner()); // Thêm đội thắng vào danh sách các đội thắng ở tứ kết
        }
        return quarterFinalWinners; // Trả về danh sách các đội thắng ở tứ kết
    }

    // Chơi bán kết
    public List<Team> playSemiFinals(List<Team> quarterFinalWinners) {
        List<Team> semiFinalWinners = new ArrayList<>();    // Danh sách các đội thắng ở bán kết
        // Trận S1: Thắng trận Q1 – Thắng trận Q2
        Match semiFinal1 = new Match(quarterFinalWinners.get(0), quarterFinalWinners.get(1));   // Trận S1 giữa đội thắng Q1 và đội thắng Q2
        semiFinal1.play();
        semiFinalWinners.add(semiFinal1.getWinner());   // Thêm đội thắng vào danh sách các đội thắng ở bán kết

        // Trận S2: Thắng trận Q3 – Thắng trận Q4
        Match semiFinal2 = new Match(quarterFinalWinners.get(2), quarterFinalWinners.get(3));   // Trận S2 giữa đội thắng Q3 và đội thắng Q4
        semiFinal2.play();
        semiFinalWinners.add(semiFinal2.getWinner());   // Thêm đội thắng vào danh sách các đội thắng ở bán kết

        return semiFinalWinners; // Trả về danh sách các đội thắng ở bán kết
    }

    // Choi chung ket
    public void playFinal(List<Team> semiFinalWinners) {
        // Trận chung kết: Thắng S1 – Thắng S2
        finalMatch = new Match(semiFinalWinners.get(0), semiFinalWinners.get(1)); // Trận chung kết giữa đội thắng S1 và
                                                                                              // đội thắng S2
        finalMatch.play(); // Chơi trận chung kết
        champion = finalMatch.getWinner(); // Lấy đội thắng chung kết
    }

    public void playKnockoutStage() {
        // Mô phỏng các trận đấu vòng loại trực tiếp
        List<Team> currentStageTeams = new ArrayList<>(knockoutStageTeams); // Danh sách các đội tham gia vòng loại trực tiếp
        while (currentStageTeams.size() > 1) { // Trong khi còn hơn 1 đội tham gia
            List<Team> nextStageTeams = new ArrayList<Team>(); // Danh sách các đội tham gia vòng tiếp theo
            for (int i = 0; i < currentStageTeams.size(); i += 2) { // i+=2 để chọn ra 2 đội đầu tiên trong danh sách các đội tham gia
                Match match = new Match(currentStageTeams.get(i), currentStageTeams.get(i + 1)); // Trận đấu giữa đội 1 và đội 2
                match.play(); // Chơi trận đấu
                nextStageTeams.add(match.getWinner()); // Thêm đội thắng vào danh sách các đội thắng ở vòng tiếp theo
            }
            currentStageTeams = nextStageTeams; // Cập nhật danh sách các đội tham gia vòng loại trực tiếp
        }
        champion = currentStageTeams.get(0); // Đội thắng cuối cùng là đội vô địch
        // finalMatch = new Match(currentStageTeams.get(0), currentStageTeams.get(1));
    }

    public Team getChampion() {
        return champion;
    }

    public Match getFinalMatch() {
        return finalMatch;
    }

    public static void main(String[] args) {
        // Team team1 = new Team("Đội 1", "1");
        // team1.points = 6;
        // Team team2 = new Team("Đội 2", "2");
        // team2.points = 3;
        // Team team3 = new Team("Đội 3", "3");
        // team3.points = 0;
        // Team team4 = new Team("Đội 4", "4");
        // team4.points = 9;

        // ArrayList<Team> teams = new ArrayList<>();
        // teams.add(team1);
        // teams.add(team2);
        // teams.add(team3);
        // teams.add(team4);
        // Group group = new Group("Group A",teams);

        // System.out.println(group.getRankedTeams().get(0).name);

        // group.printTeamsinGroup();
        WorldCup wc = new WorldCup();
        Region region = new Region();
        region.addTeamsInAsia();
        region.addTeamsInAfrica();
        region.addTeamsInEurope();
        region.addTeamsInNorthCentralAmericaAndCaribbean();
        region.addTeamsInOceania();
        region.addTeamsInSouthAmerica();
        wc.region.add(region);

        // Playoff matches
        Team asiaPlayoffWinner = region.playAsiaVsNorthCentralAmericaPlayoff();
        System.out.println("Winner of Asia vs North Central America playoff: " + asiaPlayoffWinner.getName());

        Team southAmericaPlayoffWinner = region.playSouthAmericaVsOceaniaPlayoff();
        System.out.println("Winner of South America vs Oceania playoff: " + southAmericaPlayoffWinner.getName());

        List<Group> groups = region.distributeTeamsIntoGroups();

        for (Group group : groups) {
            System.out.println(group.getName() + ":");
            group.printTeamsinGroup();
            wc.groups.add(group);
            System.out.println();
        }
        System.out.println(wc.groups.get(0).getTeams().get(0).getName());
        // System.out.println(wc.region);
        // wc.initializeTeams();.

        // for (int i = 0; i < wc.teams.size(); i++) {
        // System.out.println(wc.teams.get(i).name);
        // System.out.println(wc.teams.get(i).coach);
        // }
        // for (int i = 0; i < wc.groups.size(); i++) {
        // System.out.println(wc.groups.get(i).name);
        // }
        // for (int i = 0; i < wc.groups.size(); i++) {
        // System.out.println(wc.groups.get(i).name);

        // for (int j = 0; j < wc.groups.get(i).teams.size(); j++) {
        // System.out.println(wc.groups.get(i).teams.get(j).name);
        // }
        // // System.out.println(wc.groups.get(i).name);
        // }

        // for (int i = 0; i < wc.groups.size(); i++) {
        // wc.groups.get(i).playMatches();

        // for(int j =0; j<wc.groups.get(i).teams.size();j++){
        // wc.groups.get(i).teams.get(j)
        // }
        // }
        // System.out.println(wc.teams.get(0).name);
        // System.out.println(wc.teams.get(0).points);
        // System.out.println(wc.teams.get(0).coach);
        // System.out.println(wc.teams.get(0).matchesPlayed);

        // System.out.println(wc.teams.get(1).name);
        // System.out.println(wc.teams.get(1).points);
        // System.out.println(wc.teams.get(1).coach);
        // System.out.println(wc.teams.get(1).matchesPlayed);
        // System.out.println(wc.teams.get(0).name);
        // for (int i = 0; i < wc.region.size(); i++) {
        // // Region region_ = wc.region.get(i);
        // for (int j = 0; j < wc.region.get(i).asia.size(); j++) {
        // System.out.println(wc.region.get(i).asia.get(j).name);
        // }
        // }
    }
}

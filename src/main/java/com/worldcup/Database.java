package com.worldcup;

import java.util.*;

public class Database {
    public Map<String, Team> teams;
    public Map<UUID, Match> matches;

    public Database() {
        teams = new HashMap<>();
        matches = new HashMap<>();
    }

    public boolean connect() {
        return true; // Mô phỏng kết nối cơ sở dữ liệu thành công
    }

    public void saveTeam(Team team) {
        teams.put(team.getName(), team);
    }

    public Team loadTeam(String name) {
        return teams.get(name);
    }

    public void saveMatchResult(Match match) {
        matches.put(match.getId(), match);
    }

    public Match loadMatchResult(UUID id) {
        return matches.get(id);
    }
}

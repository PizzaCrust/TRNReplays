package com.fortnitetracker.replays;

import java.util.List;

public class ReplayData {

    public static class ReplayInfo {
        public String replayId;
        public String sessionId;
        public String replayFileId;
        public String logFileId;
        public String versionUE;
        public String versionBranch;
        public int versionChangelist;
        public String playedAt;
        public double duration;
        public String playlistType;
        public String playlistName;
        public String playlist;
        public String uploaderAccountId;
        public String uploaderName;
        public int uploaderPlatformId;
        public int uploaderEliminations;
        public int uploaderScore;
        public int uploaderKilledById;
        public int totalPlayerStructures;
    }

    public static class Player {
        public int playerId;
        public String accountId;
        public int platformId;
        public double timePlayed;
        public String playerName;
        public String streamerModeName;
        public boolean isUploader;
        public boolean hasThankedBusDriver;
        public boolean hasWon;
        public int placement;
        public int teamId;
        public int squadId;
        public int partyOwnerId;
        // down but not out count
        public int dbnOs;
        public int kills;
    }

    public ReplayInfo info;

    public List<Player> players;

}

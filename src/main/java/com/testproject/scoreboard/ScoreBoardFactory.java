package com.testproject.scoreboard;

public class ScoreBoardFactory {

    public static ScoreBoard create(MatchRepository matchRepository) {
        return new ScoreBoardImpl(matchRepository);
    }
}

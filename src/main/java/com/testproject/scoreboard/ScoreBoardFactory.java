package com.testproject.scoreboard;

public class ScoreBoardFactory {

    public static ScoreBoard create() {
        return new ScoreBoardImpl(new MatchRepository());
    }
}

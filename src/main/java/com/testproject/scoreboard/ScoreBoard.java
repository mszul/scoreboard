package com.testproject.scoreboard;

import java.util.List;

public interface ScoreBoard {

    Match start(Teams teams);

    void finish(Teams teams);

    Match updateScore(Teams teams, Score score);

    List<String> getSummary();
}

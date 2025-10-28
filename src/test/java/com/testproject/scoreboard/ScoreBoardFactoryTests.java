package com.testproject.scoreboard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreBoardFactoryTests {

    @Test
    public void create_shouldCreateScoreBoard() {
        var scoreboard = ScoreBoardFactory.create(new MatchRepository());

        assertThat(scoreboard).isInstanceOf(ScoreBoardImpl.class);
    }

}
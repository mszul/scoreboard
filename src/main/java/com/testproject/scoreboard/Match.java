package com.testproject.scoreboard;

import com.testproject.scoreboard.storage.Storable;
import org.jetbrains.annotations.NotNull;

public record Match(Teams teams, Score score) implements Storable<Teams> {

    public static final String MATCH_FORMAT = "%s %d - %s %d";

    @Override
    public Teams getId() {
        return teams;
    }

    @Override
    @NotNull
    public String toString() {
        return String.format(MATCH_FORMAT,
                teams.host().locale().getDisplayCountry(),
                score.host(),
                teams.away().locale().getDisplayCountry(),
                score.away()
        );
    }
}

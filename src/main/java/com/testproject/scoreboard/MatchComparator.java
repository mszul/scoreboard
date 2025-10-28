package com.testproject.scoreboard;

import java.util.Comparator;

class MatchComparator implements Comparator<Match> {

    @Override
    public int compare(Match o1, Match o2) {
        return (o2.score().host() + o2.score().away()) - (o1.score().host() + o1.score().away());
    }
}

package com.testproject.scoreboard;

import java.util.ArrayList;
import java.util.List;

class ScoreBoardImpl implements ScoreBoard {

    public static final Score DEFAULT_SCORE = new Score(0, 0);
    private final MatchRepository matchRepository;

    public ScoreBoardImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match start(Teams teams) {
        validateTeamsAreNotPlaying(teams);
        var match = new Match(teams, DEFAULT_SCORE);
        matchRepository.save(match);
        return match;
    }

    private void validateTeamsAreNotPlaying(Teams teams) {
        var duplicatedTeams = matchRepository.findByPredicate(
                t -> t.teams().host().equals(teams.host()) ||
                        t.teams().host().equals(teams.away()) ||
                        t.teams().away().equals(teams.host()) ||
                        t.teams().away().equals(teams.away()));
        if (!duplicatedTeams.isEmpty()) {
            var firstDuplicated = duplicatedTeams.getFirst();
            var duplicatedTeam = teams.host().equals(firstDuplicated.teams().host()) ? teams.host() : teams.away();
            throw new DuplicatedMatchException(String.format("%s already play a match",
                    duplicatedTeam.locale().getDisplayCountry()));
        }
    }

    @Override
    public void finish(Teams teams) {
        validateMatchExists(teams);
        matchRepository.removeById(teams);
    }

    @Override
    public Match updateScore(Teams teams, Score score) {
        validateMatchExists(teams);
        var updated = new Match(teams, score);
        matchRepository.save(updated);
        return updated;
    }

    @Override
    public List<String> getSummary() {
        var matches = new ArrayList<>(matchRepository.getAll());
        matches.sort(new MatchComparator());
        return matches.stream().map(Match::toString).toList();
    }

    private void validateMatchExists(Teams teams) {
        if (matchRepository.findById(teams).isEmpty()) {
            throw new MatchNotFoundException(String.format("Match between %s and %s not found",
                    teams.host().locale().getDisplayCountry(), teams.away().locale().getDisplayCountry()));
        }
    }
}

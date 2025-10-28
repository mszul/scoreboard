package com.testproject.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreBoardImplTests {

    public static final Score DEFAULT_SCORE = new Score(0, 0);
    public static final Score NEW_SCORE = new Score(2, 1);
    public static final Score NEW_SCORE_2 = new Score(10, 1);
    public static final Score NEW_SCORE_3 = new Score(0, 4);
    public static final Locale HOST_TEAM = Locale.GERMANY;
    public static final Locale AWAY_TEAM = Locale.FRANCE;
    public static final Locale HOST_TEAM_2 = Locale.CHINA;
    public static final Locale AWAY_TEAM_2 = Locale.ITALY;
    public static final Locale HOST_TEAM_3 = Locale.of("cz", "CZ");
    public static final Locale AWAY_TEAM_3 = Locale.of("pl", "PL");
    public static final Locale HOST_TEAM_4 = Locale.US;
    public static final Locale AWAY_TEAM_4 = Locale.UK;

    private MatchRepository matchRepository;
    private ScoreBoard scoreBoard;

    @BeforeEach
    public void setup() {
        matchRepository = new MatchRepository();
        scoreBoard = new ScoreBoardImpl(matchRepository);
    }

    @Test
    public void start_singleMatch_shouldAdd() {
        var host = new Team(HOST_TEAM);
        var guest = new Team(AWAY_TEAM);
        var teams = new Teams(host, guest);

        var match = scoreBoard.start(teams);

        assertThat(match).isNotNull();
        assertThat(match.teams().host()).isEqualTo(host);
        assertThat(match.teams().away()).isEqualTo(guest);
        assertThat(match.score()).isEqualTo(DEFAULT_SCORE);
        assertThat(matchRepository.getAll()).containsExactly(match);
    }

    @Test
    public void start_hostAlreadyPlay_shouldThrowsException() {
        var teams = createTeams(HOST_TEAM, AWAY_TEAM);
        var teams2 = createTeams(HOST_TEAM, AWAY_TEAM_2);
        scoreBoard.start(teams);

        var ex = assertThrows(DuplicatedMatchException.class, () -> scoreBoard.start(teams2));

        var msg = String.format("%s already play a match", HOST_TEAM.getDisplayCountry());
        assertThat(ex.getMessage()).contains(msg);
    }

    @Test
    public void start_awayAlreadyPlay_shouldThrowsException() {
        var teams = createTeams(HOST_TEAM, AWAY_TEAM);
        var teams2 = createTeams(HOST_TEAM_2, AWAY_TEAM);
        scoreBoard.start(teams);

        var ex = assertThrows(DuplicatedMatchException.class, () -> scoreBoard.start(teams2));

        var msg = String.format("%s already play a match", AWAY_TEAM.getDisplayCountry());
        assertThat(ex.getMessage()).contains(msg);
    }

    @Test
    public void finish_singleMatch_shouldReturnNoMatches() {
        Teams teams = createTeams(HOST_TEAM, AWAY_TEAM);
        scoreBoard.start(teams);

        scoreBoard.finish(teams);

        var matches = matchRepository.getAll();
        assertThat(matches).isNotNull();
        assertThat(matches).isEmpty();
    }

    @Test
    public void finish_notExistingMatch_shouldThrowException() {
        var teams = createTeams(HOST_TEAM, AWAY_TEAM);

        var ex = assertThrows(MatchNotFoundException.class, () -> scoreBoard.finish(teams));

        var msg = String.format("Match between %s and %s not found", HOST_TEAM.getDisplayCountry(), AWAY_TEAM.getDisplayCountry());
        assertThat(ex.getMessage()).contains(msg);
    }

    @Test
    public void update_simpleUpdate_shouldUpdateValues() {
        var host = new Team(HOST_TEAM);
        var guest = new Team(AWAY_TEAM);
        var teams = new Teams(host, guest);
        var match = scoreBoard.start(teams);

        match = scoreBoard.updateScore(teams, NEW_SCORE);

        assertThat(match).isNotNull();
        assertThat(match.teams().host()).isEqualTo(host);
        assertThat(match.teams().away()).isEqualTo(guest);
        assertThat(match.score()).isEqualTo(NEW_SCORE);
        assertThat(matchRepository.getAll()).containsExactly(match);
    }

    @Test
    public void updateScore_notExistingMatch_shouldThrowException() {
        var teams = createTeams(HOST_TEAM, AWAY_TEAM);

        var ex = assertThrows(MatchNotFoundException.class, () -> scoreBoard.updateScore(teams, NEW_SCORE));

        var msg = String.format("Match between %s and %s not found", HOST_TEAM.getDisplayCountry(), AWAY_TEAM.getDisplayCountry());
        assertThat(ex.getMessage()).contains(msg);
    }

    @Test
    public void getSummary_multipleCountries_shouldGetOrderedList() {
        var teams1 = createTeams(HOST_TEAM, AWAY_TEAM);
        var teams2 = createTeams(HOST_TEAM_2, AWAY_TEAM_2);
        var teams3 = createTeams(HOST_TEAM_3, AWAY_TEAM_3);
        var teams4 = createTeams(HOST_TEAM_4, AWAY_TEAM_4);
        scoreBoard.start(teams1);
        scoreBoard.start(teams2);
        scoreBoard.start(teams3);
        scoreBoard.start(teams4);
        var match1 = scoreBoard.updateScore(teams1, NEW_SCORE);
        var match2 = scoreBoard.updateScore(teams2, NEW_SCORE_2);
        var match3 = scoreBoard.updateScore(teams3, NEW_SCORE_3);
        var match4 = scoreBoard.updateScore(teams4, NEW_SCORE_2);

        var summary = scoreBoard.getSummary();

        assertThat(summary).containsExactly(createRecord(match2),
                createRecord(match4),
                createRecord(match3),
                createRecord(match1));
    }

    private static Teams createTeams(Locale hostTeam, Locale awayTeam) {
        var host = new Team(hostTeam);
        var guest = new Team(awayTeam);
        return new Teams(host, guest);
    }

    private static String createRecord(Match match) {
        return String.format("%s %d - %s %d", match.teams().host(), match.score().host(), match.teams().away(), match.score().away());
    }
}

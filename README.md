# Scoreboard

a simple library handling a basic live scoreboard functionality.

## Requirements

* Java Sdk 25
* Maven: 3.8+

## Running

test and build with maven

```mvn clean package```

### Sample usage

```java
import com.testproject.scoreboard.Score;
import com.testproject.scoreboard.ScoreBoardFactory;
import com.testproject.scoreboard.Team;
import com.testproject.scoreboard.Teams;

import java.util.Locale;

public class Demo {

    static void main(String[] args) {
        var scoreboard = ScoreBoardFactory.create();
        var teams = new Teams(new Team(Locale.ITALY), new Team(Locale.FRANCE));
        var teams2 = new Teams(new Team(Locale.US), new Team(Locale.KOREA));
        var teams3 = new Teams(new Team(Locale.GERMANY), new Team(Locale.of("pl", "PL")));

        scoreboard.start(teams);
        scoreboard.start(teams2);
        scoreboard.start(teams3);

        scoreboard.updateScore(teams, new Score(1, 0));
        scoreboard.updateScore(teams3, new Score(4, 2));

        scoreboard.finish(teams2);

        System.out.println(scoreboard.getSummary());
    }
}


```





package org.danil;


@FunctionalInterface
public interface RockPaperScissorsPlayer {
    enum RockPaperScissorsEnum {
        ROCK, PAPER, SCISSORS
    }
    RockPaperScissorsEnum play();
}

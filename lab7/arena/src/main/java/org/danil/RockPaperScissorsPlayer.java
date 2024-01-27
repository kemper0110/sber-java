package org.danil;


@FunctionalInterface
public interface RockPaperScissorsPlayer {
    enum RockPaperScissorsEnum {
        ROCK, PAPER, SCISSORS;

        public boolean beats(RockPaperScissorsEnum rhs) {
            return switch (this) {
                // rock beats scissors
                case ROCK -> rhs == SCISSORS;
                // paper beats rock
                case PAPER -> rhs == ROCK;
                // scissors beats paper
                case SCISSORS -> rhs == PAPER;
            };
        }
    }
    RockPaperScissorsEnum play();
}

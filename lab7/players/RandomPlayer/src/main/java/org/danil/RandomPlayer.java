package org.danil;

import java.util.Random;

public class RandomPlayer implements RockPaperScissorsPlayer {
    @Override
    public RockPaperScissorsEnum play() {
        int id = new Random().nextInt(RockPaperScissorsEnum.values().length);
        return RockPaperScissorsEnum.values()[id];
    }
}

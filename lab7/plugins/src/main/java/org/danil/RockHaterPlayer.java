package org.danil;

import java.util.Random;

public class RockHaterPlayer implements RockPaperScissorsPlayer{
    @Override
    public RockPaperScissorsEnum play() {
        int id = new Random().nextInt(RockPaperScissorsEnum.values().length - 1);
        return RockPaperScissorsEnum.values()[1 + id];
    }
}

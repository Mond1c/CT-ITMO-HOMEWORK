package game;

import java.util.Random;

public class RandomPlayer implements Player {
    private final int m;
    private final int n;
    private final Random random;

    public RandomPlayer(final int m, final int n) {
        this.m = m;
        this.n = n;
        this.random = new Random();
    }

    @Override
    public Move makeMove(Cell turn) {
        return new Move(random.nextInt(m), random.nextInt(n), turn);
    }
}

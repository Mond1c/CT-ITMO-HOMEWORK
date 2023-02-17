package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final List<Player> players = new ArrayList<>();
    private static void choosePlayer(final Scanner scanner, int m, int n) {
        System.out.println("Choose first player:");
        System.out.println("1. Human");
        System.out.println("2. Random");
        final int plyaerType = scanner.nextInt();
        if (plyaerType == 1) {
            players.add(new Human(scanner, System.out));
        } else {
            players.add(new RandomPlayer(m, n));
        }
    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Choose game mode:");
        System.out.println("1. One game");
        System.out.println("2. Tournament");
        final int gameMode = scanner.nextInt();
        System.out.println("Write board row's count");
        final int m = scanner.nextInt();
        System.out.println("Write board column's count");
        final int n = scanner.nextInt();
        System.out.println("Write length of winner combination");
        final int k = scanner.nextInt();
        System.out.println("Write count of blocked cells");
        int countOfBlockedCells = scanner.nextInt();
        while (countOfBlockedCells > m * n) {
            System.out.println("You entered incorrect count of blocked cells. Try again");
            countOfBlockedCells = scanner.nextInt();
        }
        final List<MnkBoard.BlockedCell> blockedCells = new ArrayList<>();
        for (int i = 0; i < countOfBlockedCells; i++) {
             int row = scanner.nextInt();
             int column = scanner.nextInt();
             while (0 > row || row >= m
                || 0 > column || column >= n) {
                 System.out.println("You entered incorrect position of blocked cell. Try again");
                 row = scanner.nextInt();
                 column = scanner.nextInt();
             }
        }
        if (gameMode == 1) {
            for (int i = 0; i < 2; i++) {
                choosePlayer(scanner, m, n);
            }
            final int result =
                    new MnkGame(new MnkBoard(m, n, k, blockedCells), players.get(0), players.get(1), System.out).play(false);
            switch (result) {
                case 1 -> System.out.println("First player won");
                case 2 -> System.out.println("Second player won");
                case 0 -> System.out.println("Draw");
                default -> throw new AssertionError("Unknown result " + result);
            }
        }
        else if (gameMode == 2) {
            System.out.println("Enter count of players");
            int countOfPlayers = scanner.nextInt();
            while (countOfPlayers < 2) {
                System.out.println("Incorrect players count (< 2). Try again");
                countOfPlayers = scanner.nextInt();
            }
            for (int i = 0; i < countOfPlayers; i++) {
                choosePlayer(scanner, m, n);
            }
            new Tournament(m, n, k, players,
                    blockedCells, System.out).play();
        }
        // :NOTE: хочу чтобы турнир игрался
    }
}

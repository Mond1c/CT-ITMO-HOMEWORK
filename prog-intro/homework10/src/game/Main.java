package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Write board row's count");
        final int m = scanner.nextInt();
        System.out.println("Write board column's count");
        final int n = scanner.nextInt();
        System.out.println("Write length of winner combination");
        final int k = scanner.nextInt();
        List<MnkBoard.BlockedCell> blockedCells = new ArrayList<>();
        for (int i = 0; i < Math.min(m, n); i++) {
            blockedCells.add(new MnkBoard.BlockedCell(i, i));
        }
        new Tournament(m, n, k, List.of(new Human(scanner), new Human(scanner), new Human(scanner)), blockedCells).play();
    }
}

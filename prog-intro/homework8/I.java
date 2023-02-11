import java.util.Scanner;

public class I {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int xl = Integer.MAX_VALUE, yl = Integer.MAX_VALUE;
        int xr = Integer.MIN_VALUE, yr = Integer.MIN_VALUE;
        while (n-- > 0) {
            int x = scanner.nextInt(), y = scanner.nextInt(), h = scanner.nextInt();
            xl = Math.min(xl, x - h);
            xr = Math.max(xr, x + h);
            yl = Math.min(yl, y - h);
            yr = Math.max(yr, y + h);
        }
        int x = (xl + xr) / 2;
        int y = (yl + yr) / 2;
        int max = Math.max(xr - xl, yr - yl);
        int h = max / 2;
        if (max % 2 != 0) {
            h++;
        }
        System.out.println(x + " " + y + " " + h);
    }
}
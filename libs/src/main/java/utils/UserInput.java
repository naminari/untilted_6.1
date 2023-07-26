package utils;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserInput {
    private static final Scanner scanner = new Scanner(System.in);
    public static String input() {
        System.out.print("> ");
        String line = null;
        try {
            line = scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.exit(0);
        }
        return line;
    }
}

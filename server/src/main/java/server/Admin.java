package server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
public class Admin implements Runnable {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            try {
                if (System.in.available() > 0) {
                    String input = null;
                    try {
                        input = scanner.nextLine().trim();
                    } catch (NoSuchElementException e) {
                        exit();
                    }
                    if (Objects.requireNonNull(input).equals("exit")) {
                        exit();
                    } else {
                        System.out.println("Чтобы выйти из серверного приложения введите: exit");
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void exit() {
        log.info("Server is ended");
        System.exit(0);
    }

}
package Introduce;

import client.Client;
import utils.DataHash;
import utils.Message;
import utils.TypeMessage;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Authorization {
    private final Scanner scanner = new Scanner(System.in);

    public void run(Client client) throws NoSuchElementException {

        while (true) {
            System.out.print("If you want to register enter \"r\" , if log in - \"l\": \n > ");
            String answer = scanner.nextLine().trim();

            if (!(answer.matches("r") || answer.matches("l"))) {
                continue;
            }

            System.out.print("Enter login: \n > ");
            String login = scanner.nextLine().trim();

            if (login.isEmpty()) {
                System.out.println("Empty login");
                continue;
            }

            System.out.print("Enter password: \n > ");
            String password = scanner.nextLine().trim();

            if (password.isEmpty()) {
                System.out.println("Empty password");
                continue;
            }

            setData(client, login, DataHash.encodeWord(password));

            if (answer.equalsIgnoreCase("r")) {
                if (this.doRegisterLogic(client)) {
                    return;
                }
            } else if (answer.equalsIgnoreCase("l")) {
                if (this.doLogInLogic(client)) {
                    return;
                }
            }

        }
    }

    private boolean doRegisterLogic(Client client) {
        Message message = client.register();
        TypeMessage type = message.getTypeMessage();
        System.out.println(message.getMessage());
        return type == TypeMessage.REGISTER;
    }

    private boolean doLogInLogic(Client client) {
        Message message = client.logIn();
        TypeMessage type = message.getTypeMessage();
        System.out.println(message.getMessage());
        return type == TypeMessage.LOG_IN;
    }

    private void setData(Client client, String login, String password) {
        client.setLogin(login);
        client.setPassword(password);
    }
}
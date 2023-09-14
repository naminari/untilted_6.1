package Introduce;

import client.Client;
import utils.Message;
import utils.TypeCommand;

import java.util.*;

public class Introduce {
    private final Client client;
    private final Scanner scanner = new Scanner(System.in);

    public Introduce( Client client) {
        this.client = client;
    }

    private String[] getUserInput() {
        String[] input = null;
        while (Objects.isNull(input)) {
            System.out.print("Ведите команду: \n > ");
            String line = null;
            try {
                line = scanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.exit(0);
            }
            if (!line.isEmpty()) {
                input = line.split(" ", 2);
            } else {
                System.out.println("Uncorrected input");
            }
        }
        return input;
    }

    public void run() throws NullPointerException {
        while (true) {
            String[] request = getUserInput();
            if (Objects.equals(request[0], "exit")){
                System.out.println("Good bye");
                System.exit(0);
            }

            Message message = client.send(getCmdType(request[0]), getArgs(request));
            System.out.println(message.getMessage());
            }
    }

    public TypeCommand getCmdType(String cmd) {
        if (TypeCommand.geTypeByName(cmd) == null){
            System.out.println("No such command");
            return null;
        }
        return TypeCommand.geTypeByName(cmd);
    }

    public String[] getArgs(String[] array) {
        String[] newArray = new String[array.length - 1];
        System.arraycopy(array, 1, newArray, 0, array.length - 1);
        return newArray;
    }
}
package Introduce;

import client.Client;
import exceptions.CmdArgsAmountException;
import utils.TypeCommand;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
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

    public void run() throws CmdArgsAmountException, FileNotFoundException, InvocationTargetException, IllegalAccessException, NullPointerException {
        while (true) {
            String[] request = getUserInput();
            if (Objects.equals(request[0], "exit")){
                System.out.println("Good bye");
                System.exit(0);
            }
            client.send(getCmdType(request[0]), getArgs(request));  /// программа работает

        }
    }

    public TypeCommand getCmdType(String cmd) { ///  тут уже не работает
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
//}    public CmdRequest getRequest(String[] args){
//    if (!cmdHandler.checkingTheList(args[0])){
//        System.out.println("No such command");
//    } else {
//        List<String> list = new ArrayList<>(Arrays.asList(args));
//        list.remove(0);
//        Command command = cmdHandler.getCmds().get(args[0]);
//        CmdArgs cmdArgs= new CmdArgs(list.toArray(new String[0]));
//        CmdRequest request = new CmdRequest(command, cmdArgs);
//        return request;
//    }
//}
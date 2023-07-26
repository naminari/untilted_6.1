package org.example;

import Introduce.Introduce;
import builders.HumanDirector;
import client.Client;
import collection.HumanValidator;
import exceptions.CmdArgsAmountException;


import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) throws CmdArgsAmountException, FileNotFoundException, InvocationTargetException, IllegalAccessException {
        HumanDirector humanDirector = new HumanDirector(new HumanValidator());
//        ResourceBundle bundle = ResourceBundle.getBundle("resources.myClient");
//        String host = bundle.getString("hostName");
//        int port = Integer.parseInt(bundle.getString("port"));
        String host = "localhost";
        int port = 8111;
        Client client = new Client(humanDirector, host, port);
        Introduce introduce = new Introduce(client);
        introduce.run();
    }
}
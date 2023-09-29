package org.example;

import Introduce.Introduce;
import builders.HumanDirector;
import client.Client;
import collection.HumanValidator;
import exceptions.CmdArgsAmountException;
import Introduce.Authorization;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) throws CmdArgsAmountException, FileNotFoundException, InvocationTargetException, IllegalAccessException {
        HumanDirector humanDirector = new HumanDirector(new HumanValidator());
        Authorization authorization = new Authorization();
        ResourceBundle bundle = ResourceBundle.getBundle("clientProperties");
        String host = bundle.getString("hostName");
        int port = Integer.parseInt(bundle.getString("port"));
        Client client = new Client(humanDirector, host, port);
        try {
            authorization.run(client);
        } catch (NoSuchElementException e) {
            System.exit(0);
        }

        Introduce introduce = new Introduce(client);
        introduce.run();
    }
}
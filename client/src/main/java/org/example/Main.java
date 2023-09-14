package org.example;

import Introduce.Introduce;
import builders.HumanDirector;
import client.Client;
import collection.HumanValidator;
import exceptions.CmdArgsAmountException;


import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws CmdArgsAmountException, FileNotFoundException, InvocationTargetException, IllegalAccessException {
        HumanDirector humanDirector = new HumanDirector(new HumanValidator());
        String host = "localhost";
        int port = 7211;
        Client client = new Client(humanDirector, host, port);
        Introduce introduce = new Introduce(client);
        introduce.run();
    }
}
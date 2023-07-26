package org.example;

import builders.HumanDirector;
import cmd.CmdHandler;
import collection.HumanSet;
import collection.HumanValidator;
import commands.*;
import humans.HumanBeing;
import io.XMLFileWriter;
import server.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) throws IOException {
//        ResourceBundle bundle = ResourceBundle.getBundle("myServer", new Locale("UA"));
//        int port = Integer.parseInt(bundle.getString("port"));
//        String host = bundle.getString("hostName");
        String host = "localhost";
        int port = 8111;
        File file = new File("src/main/resources/redound.xml");
        XMLFileWriter<HumanBeing> writer = new XMLFileWriter<>();
        LinkedHashSet<HumanBeing> collection = new LinkedHashSet<>();
        HumanValidator humanValidator = new HumanValidator();
        HumanDirector humanDirector = new HumanDirector(humanValidator);
        HumanSet humanSet = new HumanSet(collection, humanDirector, file, humanValidator, writer);
        CmdHandler cmdHandler = new CmdHandler();{{
            new Add(humanSet);
            new AddIfMin(humanSet);
            new Clear(humanSet);
            new CountLessWeapon(humanSet);
            new ExecuteScript(humanSet, cmdHandler);
            new FilterByImpactSpeed(humanSet);
            new FilterGreaterThanMood(humanSet);
            new Help(cmdHandler);
            new History(cmdHandler);
            new Info(humanSet);
            new RemoveById(humanSet);
            new RemoveLower(humanSet);
            new Save(humanSet);
            new Show(humanSet);
            new Update(humanSet);
        }}

        new Server(port, cmdHandler).run();


    }
}
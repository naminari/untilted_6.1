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
import java.io.IOException;
import java.util.LinkedHashSet;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 7211;
        File file = new File("C:\\Users\\naminari\\Downloads\\лабы\\Прога\\redound.xml");
        XMLFileWriter<HumanBeing> writer = new XMLFileWriter<>();
        LinkedHashSet<HumanBeing> collection = new LinkedHashSet<>();
        HumanValidator humanValidator = new HumanValidator();
        HumanDirector humanDirector = new HumanDirector(humanValidator);
        HumanSet humanSet = new HumanSet(collection, humanDirector, file, humanValidator, writer);
        CmdHandler cmdHandler = new CmdHandler() {{
            addComm(new Add(humanSet));
            addComm(new AddIfMin(humanSet));
            addComm(new Clear(humanSet));
            addComm(new CountLessWeapon(humanSet));
            addComm(new ExecuteScript(humanSet, this));
            addComm(new FilterByImpactSpeed(humanSet));
            addComm(new FilterGreaterThanMood(humanSet));
            addComm(new Help(this));
            addComm(new History(this));
            addComm(new Info(humanSet));
            addComm(new RemoveById(humanSet));
            addComm(new RemoveLower(humanSet));
            addComm(new Save(humanSet));
            addComm(new Show(humanSet));
            addComm(new Update(humanSet));
        }};

        new Server(port, cmdHandler).run();


    }
}
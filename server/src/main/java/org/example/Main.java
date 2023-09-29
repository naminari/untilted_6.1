package org.example;

import builders.HumanDirector;
import cmd.CmdHandler;
import collection.*;
import commands.*;
import database.DatabaseHandler;
import database.PostgresHumanReader;
import humans.HumanBeing;
import io.XMLFileWriter;
import server.CustomerRegistrar;
import server.Server;
import server.ServerEventQueue;
import server.WorkerParameters;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println((e.getMessage()));
            System.exit(-1);
        }
        List<HumanBeing> humans = null;
        String databseInfo = null;
        DatabaseHandler dbHandler = null;
        ResourceBundle bundle = ResourceBundle.getBundle("serverResources");
        String url = bundle.getString("url");
        String userName = bundle.getString("userName");
        String password = bundle.getString("password");
        String hostName = bundle.getString("hostName");
        int port = Integer.parseInt(bundle.getString("port"));
        try{
            dbHandler = new DatabaseHandler(url, userName, password);
            humans = new PostgresHumanReader().readObjects(dbHandler.getConnection());
            databseInfo = dbHandler.getConnection().getCatalog();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        Validator<HumanBeing> validator = new SimpleHumanValidator();
        HumanSet humanSet = new HumanSet(humans, databseInfo, validator);
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
//            addComm(new RemoveLower(humanSet));
            addComm(new Show(humanSet));
            addComm(new Update(humanSet));
        }};

        CustomerRegistrar registrar = new CustomerRegistrar();
        ServerEventQueue queue = new ServerEventQueue();
        WorkerParameters parameters = new WorkerParameters(cmdHandler, dbHandler, registrar, queue);
        new Server(hostName, port, parameters).run();


    }
}
//        File file = new File("C:\\Users\\naminari\\Downloads\\лабы\\Прога\\redound.xml");
//        XMLFileWriter<HumanBeing> writer = new XMLFileWriter<>();
//        LinkedHashSet<HumanBeing> collection = new LinkedHashSet<>();
//        HumanValidator humanValidator = new HumanValidator();
//        HumanDirector humanDirector = new HumanDirector(humanValidator);
//        HumanSet humanSet = new HumanSet(collection, humanDirector, file, humanValidator, writer);
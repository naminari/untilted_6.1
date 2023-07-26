package commands;

import cmd.*;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ExecuteScriptException;
import exceptions.ValidException;
import utils.FileChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ExecuteScript extends AbstractCommand {
    private CmdHandler handler;
    private final Controller controller = new Controller();
    private final ArrayList<String> scriptResult = new ArrayList<>();

    public ExecuteScript(HumanSet set, CmdHandler handler) {
        super("execute_script", "file_name : считать и исполнить скрипт из указанного файла.", CmdType.SIMPLE_ARG);
        this.handler = handler;
    }
    private File getCurrentFileOrThrowValidException(String[] args) throws ValidException, IOException, ExecuteScriptException {
        if (args.length == 1) {
            File file = new File(args[0]);
            if (FileChecker.checkFileToRead(file)) {
                controller.addExc(file.getAbsolutePath());
                return file;
            }
        }
        throw new ValidException("Uncorrect input");
    }

    private List<String[]> readFileLinesOrReturnNull(File file) throws NoSuchElementException {
        if (Optional.ofNullable(file).isEmpty()) {
            return null;
        } else {
            List<String[]> list;
            try (Scanner scanner = new Scanner(file)) {
                list = new ArrayList<String[]>();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] cmdArgs = line.split(" ");
                        if (cmdArgs[0].equals(this.name)) {
                            String[] cmdArg = line.split(" ", 2);
                            list.add(cmdArg);
                        } else {
                            list.add(cmdArgs);
                        }
                    }
                }
                return list;
            } catch (IOException e) {
                return null;
            }
        }
    }

    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        try {
            File file = getCurrentFileOrThrowValidException((String[]) args);
            Optional<List<String[]>> list = Optional.ofNullable(readFileLinesOrReturnNull(file));
            for (String[] cmdArgs : list.orElseThrow(() -> new ExecuteException("Failed to read file"))) {
                Command command = handler.getCmds().get(cmdArgs[0]);
                if (!Objects.isNull(command)) {
                    if (cmdArgs.length == 1) {
                        scriptResult.add(command.action(new Serializable[]{}));
                    } else {
                        List<String> argsList = new ArrayList<>(Arrays.asList(cmdArgs));
                        argsList.remove(0);
                        String[] argsArray = argsList.toArray(new String[0]);
                        scriptResult.add(command.action(argsArray));
                    }
                }
            }
        } catch (ValidException | IOException e) {
            throw new ExecuteException(e);
        } catch (ExecuteScriptException e) {
            System.out.println(e.getMessage());
        } finally {
            controller.getExcHistory().clear();
            controller.setZeroSize();
        }
        return scriptResult.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    private static class Controller {
        private final HashSet<String> excHistory;
        private int controlSize;

        private Controller() {
            excHistory = new HashSet<>();
        }

        private void addExc(String exc) throws ExecuteScriptException {
            excHistory.add(exc);
            controlSize = controlSize + 1;
            checkRecursion();
        }

        private HashSet<String> getExcHistory() {
            return excHistory;
        }

        private void setZeroSize() {
            this.controlSize = 0;
        }

        private void checkRecursion() throws ExecuteScriptException {
            if (excHistory.size() != controlSize) {
                throw new ExecuteScriptException("Рекурсия!");
            }
        }
    }
}

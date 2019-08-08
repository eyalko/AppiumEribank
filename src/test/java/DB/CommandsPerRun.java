package DB;

import java.util.*;

public class CommandsPerRun {

    // runID(generate in appium driver) --> Linked list containing command details
    private static Map<Integer, List<CommandDetails>> commandPerRunMap;
    private static CommandsPerRun commandsPerRun = null;
    private static Integer lastRunId = 0;

    private CommandsPerRun(){
        commandPerRunMap = new HashMap<>();
    }

    public synchronized static CommandsPerRun getInstance(){
        if(commandsPerRun == null){
            commandsPerRun = new CommandsPerRun();
        }
        return commandsPerRun;
    }

    public List getCommandsForRun(Integer runIdentifier){
        return commandPerRunMap.get(runIdentifier);
    }

    public void setCommandPerRun(Integer runIdentifier, CommandDetails commandDetails){
//        System.out.println("** set command of: " + commandDetails.command + " toLog: "+ commandDetails.toLog);
        commandPerRunMap
                .computeIfAbsent(runIdentifier, k -> new ArrayList<>())
                .add(commandDetails);
//        System.out.println("size after: " + getInstance().getCommandsForRun(runIdentifier).size());
    }

    public void clearRun(Integer runIdentifier){
        commandPerRunMap.remove(runIdentifier);
    }

    public synchronized Integer getNextRunID(){
        lastRunId++;
        return lastRunId;
    }
}

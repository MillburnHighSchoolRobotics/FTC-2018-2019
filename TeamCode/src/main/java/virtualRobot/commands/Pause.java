package virtualRobot.commands;

import virtualRobot.utils.BetterLog;

import virtualRobot.commands.Command;

/**
 * Created by Yanjun on 11/12/2015.
 * Pauses the Robot
 */
public class Pause extends Command {

    private int nMillis;

    public Pause(int time) {
        nMillis = time;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        boolean isInterrupted = false;

        try {
            Thread.sleep(nMillis);
        } catch (InterruptedException e) {
            isInterrupted = true;
        }

        return isInterrupted;
    }
}

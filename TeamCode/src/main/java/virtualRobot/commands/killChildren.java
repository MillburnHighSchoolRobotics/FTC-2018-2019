package virtualRobot.commands;

import virtualRobot.LogicThread;

/**
 * Created by 17osullivand on 1/31/17.
 * KIlls all the babies of the thread (aka threads spanwed with spawn new thread)
 */

public class killChildren extends Command {
    private LogicThread logicThread;
    public killChildren(LogicThread LogicThread) {
        this.logicThread = logicThread;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        logicThread.killChildren();
        return false;
    }
}

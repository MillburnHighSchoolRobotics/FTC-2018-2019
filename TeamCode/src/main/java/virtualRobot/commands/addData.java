package virtualRobot.commands;

import virtualRobot.LogicThread;

/**
 * Created by 17osullivand on 11/3/16.
 */

public class addData extends Command {
    private LogicThread LogicThread;
   private Object[] myData;
    public addData(LogicThread LogicThread, Object... data) {
        this.LogicThread = LogicThread;
        this.myData = data;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        return false;
    }
    public LogicThread getLogicThread() {
        return LogicThread;
    }
    public Object[] getMyData() {
        return myData;
    }
}

package virtualRobot.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.LogicThread;
import virtualRobot.SallyJoeBot;
import virtualRobot.Condition;

/**
 * Created by ethachu19 on 3/31/17
 *
 * Command is the part that accesses the robot itself
 */
public abstract class Command  {

    final int BREAK = 2;
    final int END = 1;
    final int NO_CHANGE = 0;
    private final HashMap<Condition, String> conditionals = new HashMap<>();
    private final AtomicBoolean stopCommand = new AtomicBoolean(false);
    protected LogicThread parentThread = null;
    protected SallyJoeBot robot;

    protected Command() {
        robot = Command.ROBOT;
    }

    /**
     * To evaluate the action of the string and act accordingly
     *
     * @param s Action string
     * @return An int associated with the action needed in the method changeRobotState
     */
    protected int activate(String s) {
        switch(s) {
            case "BREAK":
                return BREAK;
            case "END":
                return END;
        }
        return NO_CHANGE;
    }

    /**
     * Adds a condition to HashMap of condiiton and associates it with an action
     *
     * @param condition
     * @param action
     */
    public synchronized void addCondition(Condition condition, String action) {
        conditionals.put(condition, action);
    }

    public synchronized Command addConditionThis(Condition condition, String action) {
        conditionals.put(condition, action);
        return this;
    }

    /**
     * For use of LogicThread in runCommand to set the parent thread for the command to access the monitor threads
     *
     * @see LogicThread
     * @param parentThread
     */
    public synchronized void setParentThread(LogicThread parentThread) {
        this.parentThread = parentThread;
    }
    public synchronized Command setParentThreadThis(LogicThread parentThread) {
        this.parentThread = parentThread;
        return this;
    }

    /**
     * Changes the SallyJoeBot itself
     * Calls to checkCondition will check all conditionals and return based on predfined action
     *
     * @return If it ended due to interrupt
     * @throws InterruptedException
     */
    public abstract boolean changeRobotState() throws InterruptedException;

    /**
     * Checks each conditional in HashMap and activates corresponding action
     *
     * @return Action to do in changeRobotState
     */
    protected final int checkConditionals() {
        if (stopCommand.get())
            return END;
        for (Map.Entry<Condition, String> entry : conditionals.entrySet()) {
            if (entry.getKey() == null)
                continue;
            if (entry.getKey().isConditionMet())
                return activate(entry.getValue());
        }
        return NO_CHANGE;
    }

    public void stopCommand() {
        stopCommand.set(true);
    }

    public void resetStopCommand() {
        stopCommand.set(false);
    }

    public final static SallyJoeBot ROBOT = new SallyJoeBot();
}

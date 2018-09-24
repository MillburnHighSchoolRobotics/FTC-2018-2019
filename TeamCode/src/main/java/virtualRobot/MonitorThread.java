package virtualRobot;

import virtualRobot.utils.BetterLog;

import virtualRobot.commands.Command;

/**
 * Created by shant on 1/10/2016.
 * Monitor threads are used by delegateMonitor in LogicThread.
 * They have the abstract method setStatus, where a particular monitor thread is told how to determine whether
 * the monitor thread should be triggered or not.
 */
public abstract class MonitorThread extends Thread {
    private volatile boolean status; //usually should be TRUE, if stuff goes wrong, make it FALSE
    public static boolean NORMAL;
    public boolean isThread = false;
    protected SallyJoeBot robot;

    public MonitorThread () {
        robot = Command.ROBOT;
        NORMAL = true;
        status = true;
    }

    /**
     * Sets isThread
     *
     * @param isThread
     */
    public void setThread(boolean isThread) {
        this.isThread = isThread;
    }

    @Override
    public void run() { //Constantly Runs as long as the status is normal. As soon as it isn't, it stops running. The god Thread in the meantime will have detected that the status is not normal.
        while (!Thread.currentThread().isInterrupted()) {
            if (setStatus() != NORMAL) {
                status = !NORMAL;
            }
            //BetterLog.d("Monitor", Boolean.toString(status));
        }

    }

    /**
     * Gets the status bast
     *
     * @return Current Status
     */
    public synchronized boolean getStatus() {
        if (!isThread)
            return setStatus();
        boolean temp = status;
        resetStatus();
        return temp;
    }

    /**
     * Puts status back to normal
     */
    private void resetStatus() {
        status = NORMAL;
    }

    public abstract boolean setStatus();
}

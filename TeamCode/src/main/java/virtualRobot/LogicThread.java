package virtualRobot;


import android.provider.Settings;
import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.commands.Command;
import virtualRobot.commands.SpawnNewThread;
import virtualRobot.commands.Translate;
import virtualRobot.utils.BetterLog;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by ethachu19 on 3/31/17
 *
 * LogicThread is the main working thread that runs procedurally
 * In realRun use runCommand to run commands
 * Do not use Command.changeRobotState
 *
 * HAHA TAKE THAT SUCKERS
 */
public abstract class LogicThread extends Thread {
    protected List<Thread> children = new ArrayList<>(); //contains a list of threads created under this logic Thread using spawn new thread
    protected SallyJoeBot robot = Command.ROBOT;
    private ConcurrentHashMap<String, Boolean> monitorData = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Condition, LogicThread> interrupts = new ConcurrentHashMap<>();
    protected volatile Command lastRunCommand = null;
    private List<MonitorThread> monitorThreads = Collections.synchronizedList(new ArrayList<MonitorThread>());
    private volatile long startTime, elapsedTime = 0;

    protected AtomicBoolean isPaused = new AtomicBoolean(false);
    protected boolean shouldStartISR = true;

    public ReflectionUpdateThread currentUpdateThread = null;

    //The thread to check all monitorThreads and put data in HashMap and check for interrupts
    private Thread interruptHandler = new Thread() {

        public LogicThread parent = null;

        public Thread setParent(LogicThread logicThread) {
            parent = logicThread;
            return this;
        }

        public void run() {
            parent.startTime = System.currentTimeMillis();
            boolean isInterrupted = false;
            while (!isInterrupted) {
                for (MonitorThread m : parent.monitorThreads) {
                    parent.monitorData.put(m.getClass().getName(), !m.getStatus() || parent.monitorData.get(m.getClass().getName()));
                }

                for (Map.Entry<Condition, LogicThread> entry : parent.interrupts.entrySet()) {
                    if (entry.getKey().isConditionMet()) {
                        parent.isPaused.set(true);
                        if (parent.lastRunCommand != null)
                            parent.lastRunCommand.stopCommand(); //stops currently running command
                        robot.stopMotors();
                        try {
                            entry.getValue().realRun();
                        } catch (InterruptedException e) {
                            isInterrupted = true;
                        }
                        entry.getValue().killChildren();
                        parent.lastRunCommand.resetStopCommand();
                        parent.isPaused.set(false);
                    }
                }
                parent.elapsedTime = System.currentTimeMillis() - startTime;
                isInterrupted = Thread.currentThread().isInterrupted() || isInterrupted;
                if (isInterrupted)
                    break;

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    isInterrupted = true;
                    break;
                }
            }
        }
    }.setParent(this);

    public LogicThread() {
        super(GlobalUtils.updateThreadGroup, "Logic Thread");
    }

    @Override
    public void run(){
        isPaused.set(false);
        addPresets();
        if(shouldStartISR) {
            interruptHandler.start();
        }
        try {
            realRun();
        } catch (InterruptedException e) {
            BetterLog.d("INTERRUPTS", this.getClass().getName() + " was interrupted");
        }finally {
            killChildren();
            killMonitorThreads();
            if (shouldStartISR)
                interruptHandler.interrupt();
            isPaused.set(false);
        }
    }

    /**
     * Runs command with certain conditions to check type of command
     *
     * @param c Command to run
     * @return Whether command stopped by interrupt
     */
    protected synchronized void runCommand(@NonNull Command c) throws InterruptedException{
        boolean isInterrupted = false;
        boolean stopByIH = false;
        lastRunCommand = c;

        c.setParentThread(this);

        if (isPaused.get()){
            if (waitForNotify())
                throw new InterruptedException("waitForNotify was interrupted");
        }

        if (c instanceof SpawnNewThread) { //Add all children thread to list to kill later
            children.addAll(((SpawnNewThread)c).getThreads());
        }

        try {
            isInterrupted  = c.changeRobotState(); //Actually run the command
        } catch (InterruptedException e) {
            isInterrupted = true;
        }

        if (isInterrupted)
            throw new InterruptedException(c.getClass().getName() + " was interrupted");

        if (isPaused.get()) {
            stopByIH = true;
            if (c instanceof Translate) {
                Translate t = (Translate) c;
                if (t.getDirection().getCode() % 4 == 0) { // Went forward or backward
                    t.setTarget(t.getTarget() - Math.abs(robot.getLFMotor().getPosition() + robot.getRFMotor().getPosition() + robot.getLBMotor().getPosition() + robot.getRBMotor().getPosition()) / 4);
                } else if (t.getDirection().getCode() % 2 == 0) { //Went left or right
                    t.setTarget(t.getTarget() - Math.abs(robot.getLFMotor().getPosition() + robot.getLBMotor().getPosition()) / 2);
                } else {
                    int count = 0;
                    double avg = 0;
                    for (int i = 0; i < 4; i++) { //add distance based off only the motors that moved
                        if (t.getMultiplier()[i] != 0) {
                            count++;
                            switch (i) {
                                case 0:
                                    avg += robot.getLFMotor().getPosition();
                                    break;
                                case 1:
                                    avg += robot.getRFMotor().getPosition();
                                    break;
                                case 2:
                                    avg += robot.getLBMotor().getPosition();
                                    break;
                                case 3:
                                    avg += robot.getRBMotor().getPosition();
                                    break;
                            }
                        }
                    }
                    avg = Math.abs(avg) / count; //average the count
                    t.setTarget(avg / Math.sqrt(2)); //reduce it by multiplier
                }
            }
        }

        if (isPaused.get()){
            if (waitForNotify())
                throw new InterruptedException("waitForNotify was interrupted");
        }

        if (stopByIH) //Resuming after stopped by Interrupt Handler, call command again and resume Translate where it left off
            try {
                if (c instanceof Translate) {
                    Translate t = (Translate) c;
                    isInterrupted = t.changeRobotState();
                } else {
                    isInterrupted = c.changeRobotState();
                }
            } catch (InterruptedException e) {
                isInterrupted = true;
            }
        if (isInterrupted)
            throw new InterruptedException(c.getClass().getName() + " was interrupted");
    }

    /**
     * Gets the data related to the monitorThread given
     *
     * @param obj Class to look for
     * @return Data
     */
    protected boolean getMonitorData(Class<? extends MonitorThread> obj) {
        boolean temp = monitorData.get(obj.getName());
        monitorData.put(obj.getName(), false);
        return temp;
    }

    private boolean waitForNotify() {
        synchronized (this) {
            while (isPaused.get()) {
                try {
//                    this.wait();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes all monitorThreads of class type object
     *
     * @param object class to remove
     */
    public synchronized void removeMonitor(Class<? extends MonitorThread> object) {
        List<MonitorThread> remove = new ArrayList<>();
        for (MonitorThread mt : monitorThreads) {
            if (object.isInstance(mt))
                remove.add(mt);
        }
        monitorData.remove(object.getName());
        for (MonitorThread mt : remove) {
            mt.interrupt();
            monitorThreads.remove(mt);
        }
    }

    /**
     * Determines whether this thread or ISR is alive
     *
     * @return isAlive
     */
    public boolean allIsAlive() {
        return this.isAlive() || interruptHandler.isAlive();
    }

    /**
     * Method with real commands and running commands
     */
    protected abstract void realRun() throws InterruptedException;

    /**
     * Used to delegate monitorThreads and attach interrupts
     */
    protected void addPresets() {}

    /**
     * Attaches intterupt to the ISR with a certain procedure
     *
     * @param condition
     * @param action
     */
    public void attachInterrupt(Condition condition, LogicThread action) {
        interrupts.put(condition, action);
    }

    /**
     * Kills all children spawned by SpawnNewThread
     */
    public synchronized void killChildren() {
        for (Thread x : children) //Kill any threads made using spawn new thread
            if (x.isAlive())
                x.interrupt();
    }

    /**
     * Kills all monitorThreads associated with this LogicThread
     */
    private synchronized void killMonitorThreads() {
        for (MonitorThread x : monitorThreads)
            if (x.isAlive())
                x.interrupt();
    }

    /**
     * Binds monitorThread to LogicThread so that it can reference it in code
     *
     * @param monitorThread monitorThread to be bound to LogicThread
     * @param runAsThread Whether or not monitorThread should be start as new Thread
     */
    public void delegateMonitor(MonitorThread monitorThread, boolean runAsThread) {
        monitorThreads.add(monitorThread);
        monitorData.put(monitorThread.getClass().getName(), false);
        if (runAsThread) {
            monitorThread.setThread(true);
            monitorThread.start();
        }
    }

    /**
     * Binds monitorThread to LogicThread so that it can reference it in code without starting it as a Thread
     *
     * @param monitorThread monitorThread to be bound to LogicThread
     */
    public void delegateMonitor(MonitorThread monitorThread) {
        delegateMonitor(monitorThread,false);
    }
}

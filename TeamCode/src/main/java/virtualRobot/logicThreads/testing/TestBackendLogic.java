package virtualRobot.logicThreads.testing;

import virtualRobot.utils.BetterLog;

import virtualRobot.Condition;
import virtualRobot.LogicThread;
import virtualRobot.commands.Command;
import virtualRobot.commands.MoveMotor;
import virtualRobot.commands.Pause;
import virtualRobot.monitorThreads.TimeMonitor;

/**
 * Created by Ethan Mak on 8/28/2017.
 */

public class TestBackendLogic extends LogicThread {

    protected void addPresets() {
        delegateMonitor(new TimeMonitor(3000));
        attachInterrupt(new Condition() {
            boolean triggered = false;
            @Override
            public boolean isConditionMet() {
                if (getMonitorData(TimeMonitor.class) && triggered == false) {
                    triggered = true;
                    return true;
                }
                return false;
//                return true;
            }
        }, new LogicThread() {
            @Override
            protected void realRun() throws InterruptedException {
                runCommand(new Pause(1000));
            }
        });
    }

    @Override
    protected void realRun() throws InterruptedException{

        runCommand(new MoveMotor(robot.getLFMotor(),1,5000));
//        runCommand(new Command() {
//            @Override
//            protected int activate(String s) {
//                return 0;
//            }
//
//            @Override
//            public boolean changeRobotState() throws InterruptedException {
//                return false;
//            }
//        });
    }
}

package virtualRobot.logicThreads.testing;

import virtualRobot.utils.BetterLog;

import virtualRobot.Condition;
import virtualRobot.LogicThread;
import virtualRobot.commands.Rotate;
import virtualRobot.utils.MathUtils;

/**
 * Created by Ethan Mak on 8/29/2017.
 *
 * Used for automated tuning of PID
 */

public class RotateAutoPIDLogic extends LogicThread {
    double kP = 0.001;
    double increment = 0.001;
    long iteration = 1;
    long startTime, endTime;
    boolean lastKPTooSmall, currentKPTooBig;

    @Override
    protected void realRun() throws InterruptedException {
        while (true) {
            startTime = System.currentTimeMillis();
            runCommand(new Rotate(kP, 90, 35000).addConditionThis(new Condition() {
                double lastYaw = -400;

                @Override
                public boolean isConditionMet() {
                    boolean temp = MathUtils.equals(robot.getImu().getHeading(), lastYaw);
                    lastYaw = robot.getImu().getHeading();
                    return temp;
                }
            }, "BREAK"));
            endTime = System.currentTimeMillis();
            currentKPTooBig = (endTime - startTime >= 35000);
            BetterLog.d("AutoPID", "Iteration: " + iteration + " KU: " + kP + " Increment: " + increment);
            robot.addToTelemetry("KU: ", kP + " Increment: " + increment);
            robot.addToTelemetry("Iteration #", iteration);

            if (iteration != 1) {
                if (lastKPTooSmall && currentKPTooBig) {
                    BetterLog.d("AutoPID", "\n----------------------------------------------------------------------------\n");
                    kP -= increment;
                    increment /= 10;
                    kP += increment;
                }
                if (!lastKPTooSmall && !currentKPTooBig) {
                    BetterLog.d("AutoPID", "\n-----------------------------------------------------------------------\n");
                    increment /= 10;
                    kP += increment;
                }
            }
            if (!lastKPTooSmall && currentKPTooBig) {
                kP -= increment;
            }
            if (lastKPTooSmall && !currentKPTooBig) {
                kP += increment;
            }

            iteration++;

            lastKPTooSmall = !currentKPTooBig;

            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}

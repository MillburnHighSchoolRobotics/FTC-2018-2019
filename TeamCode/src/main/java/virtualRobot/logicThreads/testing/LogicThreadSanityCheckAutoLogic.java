package virtualRobot.logicThreads.testing;

import virtualRobot.Condition;
import virtualRobot.LogicThread;
import virtualRobot.commands.MoveMotor;
import virtualRobot.utils.MathUtils;

/**
 * Created by david on 10/6/17.
 *
 * This class was created because we weren't sure whether LogicThreads or the JoystickControllers
 * were causing our code to be jittery.
 */

public class LogicThreadSanityCheckAutoLogic extends LogicThread {
    @Override
    protected void addPresets() {
        shouldStartISR = false;
    }

    @Override
    protected void realRun() throws InterruptedException {
        double pow = 1;
        int n = 0;
        while (!Thread.currentThread().isInterrupted()) {
            runCommand(new MoveMotor(robot.getLBMotor(), pow, 1000));
            robot.addToTelemetry("Iteration: ", n++);
            if (MathUtils.equals(pow % 1, 0)) {
                pow *= 0.5;
            } else {
                pow *= -2;
            }
        }
    }
}

